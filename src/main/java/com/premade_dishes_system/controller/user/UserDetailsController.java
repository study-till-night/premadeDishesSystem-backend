package com.premade_dishes_system.controller.user;


import cn.dev33.satoken.annotation.SaCheckRole;
import cn.dev33.satoken.stp.StpUtil;
import cn.dev33.satoken.util.SaResult;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.premade_dishes_system.mapper.product.OrderProductMapper;
import com.premade_dishes_system.pojo.product.OrderProduct;
import com.premade_dishes_system.pojo.trade.Contract;
import com.premade_dishes_system.pojo.user.ProviderGrade;
import com.premade_dishes_system.pojo.user.UserDetails;
import com.premade_dishes_system.pojo.user.UserOrg;
import com.premade_dishes_system.service.product.OrderProductService;
import com.premade_dishes_system.service.trade.ContractService;
import com.premade_dishes_system.service.user.ProviderGradeService;
import com.premade_dishes_system.service.user.UserDetailsService;
import com.premade_dishes_system.service.user.UserOrgService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * (UserDetails)表控制层
 *
 * @author Shu-King
 * @since 2023-02-01 20:39:44
 */
@RestController
@CrossOrigin("*")
@RequestMapping("/api/userDetails")
public class UserDetailsController {
    /**
     * 服务对象
     */
    @Resource
    private UserDetailsService detailService;

    @Resource
    private UserOrgService orgService;

    @Resource
    private OrderProductService orderService;

    @Resource
    private OrderProductMapper orderMapper;

    @Resource
    private ProviderGradeService gradeService;

    @Resource
    private ContractService cService;

    /**
     * 查询用户详细信息及校企信息
     *
     * @return 结果
     */
    @GetMapping("/get")
    public SaResult getDetails() {
        int uid = StpUtil.getLoginIdAsInt();
        UserDetails details = detailService.getById(uid);
        UserOrg org = orgService.getById(uid);

        SaResult saResult = new SaResult();
        saResult.set("details", details)
                .set("org", org);
        return saResult;
    }

    /**
     * 供应商信息
     *
     * @return 数据
     */
    @GetMapping("/getData")
    @SaCheckRole("provider")
    public SaResult getData(int days) {
        int uid = StpUtil.getLoginIdAsInt();
        ProviderGrade grade = gradeService.getById(uid);
//        成交的长期订单总数
        int contractCount = cService.count(new QueryWrapper<Contract>()
                .eq("uid2", uid).eq("status", 2));
//        订单总数和收益
        int orderCount, income;
        if (days == -1)
            orderCount = orderService.count(new QueryWrapper<OrderProduct>().eq("seller_id", uid));
        else orderCount = orderMapper.selectCountByDay(days,uid);

        income = orderService.queryMyData(days, uid);
        return new SaResult().set("grade", grade)
                .set("contractCount", contractCount)
                .set("orderCount", orderCount)
                .set("income", income);
    }

    /**
     * 更新信息
     *
     * @param userDetails 用户信息
     * @return 结果
     */
    @PostMapping("/update")
    public SaResult updateDetails(@RequestBody UserDetails userDetails) {
        if (StpUtil.getLoginIdAsInt() != userDetails.getUid())
            return SaResult.error("无权限修改该用户信息");

        if (!detailService.checkEmailUnique(userDetails))
            return SaResult.error("邮箱重复");
        if (!detailService.checkPhoneUnique(userDetails))
            return SaResult.error("手机号重复");

        userDetails.setUid(StpUtil.getLoginIdAsInt());
        if (detailService.updateById(userDetails))
            return SaResult.ok("修改成功");
        return SaResult.error("修改失败");
    }
}

