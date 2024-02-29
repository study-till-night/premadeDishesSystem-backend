package com.premade_dishes_system.controller.trade;


import cn.dev33.satoken.annotation.SaCheckRole;
import cn.dev33.satoken.annotation.SaMode;
import cn.dev33.satoken.stp.StpUtil;
import cn.dev33.satoken.util.SaResult;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.premade_dishes_system.mapper.trade.DeliveryRecordMapper;
import com.premade_dishes_system.pojo.trade.Contract;
import com.premade_dishes_system.pojo.trade.DeliveryRecord;
import com.premade_dishes_system.pojo.user.ProviderGrade;
import com.premade_dishes_system.pojo.user.UserOrg;
import com.premade_dishes_system.service.product.ProductInfoService;
import com.premade_dishes_system.service.trade.ContractService;
import com.premade_dishes_system.service.trade.DeliveryRecordService;
import com.premade_dishes_system.service.user.ProviderGradeService;
import com.premade_dishes_system.service.user.UserOrgService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 长期批发单(Contract)表控制层
 *
 * @author Shu-King
 * @since 2023-02-02 13:46:46
 */
@RestController
@RequestMapping("/api/contract")
@SaCheckRole(value = {"provider", "organization"}, mode = SaMode.OR)
public class ContractController {
    /**
     * 服务对象
     */
    @Resource
    private ContractService cService;

    @Resource
    private ProductInfoService pService;

    @Resource
    private UserOrgService uoService;

    @Resource
    private ProviderGradeService gService;

    @Resource
    private DeliveryRecordService dService;

    @Autowired
    private DeliveryRecordMapper dMapper;

    /**
     * 获取发货记录总数
     * @param uid 供应商id
     * @return 总数
     */
    @GetMapping("/count")
    public SaResult countRecords(int uid) {
        List<Contract> contracts = cService.list(new QueryWrapper<Contract>().eq("uid2", uid));
        ArrayList<Integer> cids = new ArrayList<>();
        contracts.forEach(item -> {
            cids.add(item.getContractId());
        });
        if (cids.size() == 0)
            return SaResult.data(0);
        return SaResult.data(dService.count(new QueryWrapper<DeliveryRecord>().in("cid", cids)));
    }

    /**
     * 分页查询
     *
     * @param status   查询何种状态订单
     * @param pageId   当前页面
     * @param pageSize 页面大小
     * @return 结果
     */
    @GetMapping("/get")
    public SaResult queryList(Integer status,
                              String content,
                              @RequestParam("pid") int pageId,
                              @RequestParam("pNum") int pageSize) {
        int uid = StpUtil.getLoginIdAsInt();
        List<String> roleList = StpUtil.getRoleList(uid);

        Page<Contract> page = new Page<>(pageId, pageSize);
        QueryWrapper<Contract> queryWrapper = new QueryWrapper<Contract>()

                .and(wrapper ->
                        wrapper.eq("uid1", uid)
                                .or()
                                .eq("uid2", uid)
                ).orderByDesc("create_time");

        if (status != -1)
            queryWrapper.eq("status", status);

        if (!content.equals("")) {
            queryWrapper.like("product_name", content);
        }

        Page<Contract> contracts = cService.page(page, queryWrapper);
        ArrayList<UserOrg> userOrgs = new ArrayList<>();

        contracts.getRecords().forEach(item -> {
            if (roleList.contains("provider"))
                userOrgs.add(uoService.getById(item.getUid1()));
            else userOrgs.add(uoService.getById(item.getUid2()));
        });

        SaResult saResult = new SaResult();
        return saResult.set("contracts", contracts.getRecords())
                .set("userOrgs", userOrgs)
                .set("currentPage", pageId)
                .set("pageSize", pageSize)
                .set("total", cService.count(queryWrapper));
    }

    /**
     * 查询个性化评价
     *
     * @param type     全部 好评还是差评
     * @param pageId   当前页面
     * @param pageSize 页面大小
     * @return 结果
     */
    @GetMapping("/getCom")
    public SaResult queryComments(Integer type,
                                  @RequestParam("pid") int pageId,
                                  @RequestParam("pNum") int pageSize) {
        int uid = StpUtil.getLoginIdAsInt();

        Page<Contract> page = new Page<>(pageId, pageSize);
        QueryWrapper<Contract> queryWrapper = new QueryWrapper<Contract>()
                .isNotNull("custom_grade")
                .eq("uid2", uid)
                .orderByDesc("update_time");
//        好评
        if (type == 1)
            queryWrapper.ge("custom_grade", 4);
//        差评
        if (type == 2)
            queryWrapper.le("custom_grade", 2);

        Page<Contract> contracts = cService.page(page, queryWrapper);
        ArrayList<UserOrg> userOrgs = new ArrayList<>();

        contracts.getRecords().forEach(item -> {
            userOrgs.add(uoService.getById(item.getUid1()));
        });

        return new SaResult().set("contracts", contracts.getRecords())
                .set("userOrgs", userOrgs)
                .set("currentPage", pageId)
                .set("pageSize", pageSize)
                .set("total", cService.count(queryWrapper));
    }


    /**
     * 查询具体批发单及发货记录
     *
     * @param cid 批发单id
     * @return 结果
     */
    @GetMapping("/getOne")
    public SaResult queryOne(int cid) {
        Contract contract = cService.getById(cid);
        List<DeliveryRecord> records = dMapper.selectByCid(cid);
        return SaResult.ok().set("contract", contract)
                .set("records", records);
    }

    /**
     * 提出批发单
     *
     * @param contract 批发单对象
     * @return 结果
     */
    @PostMapping("/propose")
    @SaCheckRole("organization")
    public SaResult propose(@RequestBody Contract contract) {
        int uid = StpUtil.getLoginIdAsInt();

        Integer pid = contract.getPid();

        contract.setUid2(pService.getById(pid).getSellerId());
        contract.setUid1(uid);

        if (cService.save(contract))
            return SaResult.ok("批发单提出成功");
        return SaResult.error("批发单提出失败");
    }

    /**
     * 进行商议
     *
     * @param time  有效期
     * @param num   货物数量
     * @param price 价格
     * @param freq  送货频率
     * @return 结果
     */
    @PostMapping("/discuss")
    public SaResult doDiscussing(int cid,
                                 Integer time,
                                 Integer num,
                                 Double price,
                                 Integer freq
    ) {

        Contract contract = cService.getById(cid);
        int uid = StpUtil.getLoginIdAsInt();
        if (judgeValid(contract, uid) != null)
            return judgeValid(contract, uid);

        contract.setStatus(contract.getStatus() ^ 1);
        if (time != null) contract.setValidityTime(time);
        if (num != null) contract.setProductNum(num);
        if (freq != null) contract.setFrequency(freq);
        if (price != null) contract.setPrice(price);

        if (cService.updateById(contract))
            return SaResult.ok("意愿修改成功");
        return SaResult.error("意愿修改失败");
    }

    /**
     * 盖棺定论
     *
     * @param cid  cid
     * @param type 1 签订批发单 2 拒签批发单
     * @return 结果
     */
    @PostMapping("/decide")
    public SaResult decide(int cid, int type, @RequestParam(value = "reason", required = false) Integer refuseReason) {
        Contract contract = cService.getById(cid);
        int uid = StpUtil.getLoginIdAsInt();
        if (judgeValid(contract, uid) != null)
            return judgeValid(contract, uid);

        contract.setStatus(type + 1);

        if (type == 2)
            if (refuseReason != null) contract.setRefuseReason(refuseReason);
            else return SaResult.error("缺少拒绝原因");
        if (cService.updateById(contract))
            return type == 1 ? SaResult.ok("批发单签订成功") : SaResult.ok("批发单拒绝成功");
        return SaResult.error("签订/拒绝失败");
    }

    /**
     * 对批发单进行发货
     *
     * @param cid 批发单id
     * @return 结果
     */
    @PostMapping("/delivery")
    @Transactional
    @SaCheckRole("provider")
    public SaResult doDelivery(int cid, @RequestParam("date") String dateFormat) {
        Contract contract = cService.getById(cid);
        if (contract.getStatus() != 2)
            return SaResult.error("该订单不处于成交状态");

        if ((contract.getStartTime().getTime() + 1000L * 24 * 60 * 60 * contract.getValidityTime()) < new Date().getTime())
            return SaResult.error("订单已超出有效期");

        contract.setDeliverTimes(contract.getDeliverTimes() + 1);
//        添加本次送货记录
        DeliveryRecord record = new DeliveryRecord();
        record.setCid(contract.getContractId());
        record.setTimes(contract.getDeliverTimes() + 1);

//        判断发货是否按时
        SimpleDateFormat fmt = new SimpleDateFormat("yyyyMMdd");
        if (!fmt.format(new Date()).equals(dateFormat)) {
            int uid = StpUtil.getLoginIdAsInt();
            ProviderGrade grade = gService.getById(uid);
            gService.update(new UpdateWrapper<ProviderGrade>()
                    .set("credit", grade.getCredit() + 1).eq("uid", uid));
            record.setIsLate(true);
        }
        dService.save(record);

        if (cService.update(contract, new QueryWrapper<Contract>().eq("contract_id", cid)))
            return SaResult.ok("发货成功");
        return SaResult.error("发货失败");
    }

    /**
     * @param cid     id
     * @param grade   评分
     * @param comment 评论
     * @return 结果
     */
    @Transactional
    @PostMapping("/comment")
    public SaResult comment(int cid, int grade, String comment) {
        Contract contract = cService.getById(cid);
        contract.setCustomGrade(grade);
        contract.setCustomComment(comment);

        if (grade != 3) {
            Integer sellerId = contract.getUid2();
            ProviderGrade providerGrade = gService.getById(sellerId);
            //        3分以上好评
            if (grade < 3)
                gService.update(new UpdateWrapper<ProviderGrade>()
                        .set("creativity", providerGrade.getCreativity() - 1)
                        .eq("uid", sellerId));
                //        3分以下差评
            else gService.update(new UpdateWrapper<ProviderGrade>()
                    .set("creativity", providerGrade.getCreativity() + 1)
                    .eq("uid", sellerId));
        }
        if (cService.update(contract, new QueryWrapper<Contract>().eq("contract_id", cid)))
            return SaResult.ok("评价成功");
        return SaResult.error("评价失败");
    }


    //    判断批发单操作是否允许
    private SaResult judgeValid(Contract contract, int uid) {
        if (contract.getStatus() == 0) {
            if (uid != contract.getUid2())
                return SaResult.error("无权限修改该用户信息");
        }
        if (contract.getStatus() == 1 && uid != contract.getUid1())
            return SaResult.error("无权限修改该用户信息");

        if (contract.getStatus() >= 2) return SaResult.error("批发单已签订或取消");

        return null;
    }
}

