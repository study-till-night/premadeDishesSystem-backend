package com.premade_dishes_system.controller.product;


import cn.dev33.satoken.stp.StpUtil;
import cn.dev33.satoken.util.SaResult;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.premade_dishes_system.pojo.product.OrderProduct;
import com.premade_dishes_system.pojo.product.ProductInfo;
import com.premade_dishes_system.pojo.product.ProductSpecs;
import com.premade_dishes_system.pojo.user.Address;
import com.premade_dishes_system.pojo.user.UserOrg;
import com.premade_dishes_system.service.product.OrderProductService;
import com.premade_dishes_system.service.product.ProductInfoService;
import com.premade_dishes_system.service.product.ProductSpecsService;
import com.premade_dishes_system.service.user.AddressesService;
import com.premade_dishes_system.service.user.UserOrgService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * (OrderProduct)表控制层
 *
 * @author Shu-King
 * @since 2023-02-01 20:39:43
 */
@RestController
@RequestMapping("/api/order")
public class OrderProductController {
    /**
     * 服务对象
     */
    @Resource
    private OrderProductService oService;

    @Resource
    private ProductInfoService pService;

    @Resource
    private ProductSpecsService sService;

    @Resource
    private UserOrgService uService;

    @Resource
    private AddressesService aService;

    /**
     * 分页查询
     *
     * @param status   查询何种状态订单
     * @param pageId   当前页面
     * @param pageSize 页面大小
     * @return 结果
     */
    @GetMapping("/get")
    public SaResult queryList(
            int status,
            String content,
            @RequestParam("pid") int pageId,
            @RequestParam("pNum") int pageSize) {
        int uid = StpUtil.getLoginIdAsInt();
        List<String> roleList = StpUtil.getRoleList(uid);

        Page<OrderProduct> page = new Page<>(pageId, pageSize);
        QueryWrapper<OrderProduct> queryWrapper = new QueryWrapper<OrderProduct>().orderByDesc("create_time");
//        是否查询某一状态
        if (status != -1)
            queryWrapper.eq("status", status);

//        根据货物名查找
        ArrayList<Integer> pids = new ArrayList<>();
        if (!content.equals("")) {
            //            满足搜索内容的货物
            List<ProductInfo> prods = pService.list(new QueryWrapper<ProductInfo>().like("pname", content));
            if (prods.size() != 0) {
                prods.forEach(item -> {
                    pids.add(item.getPid());
                });
                queryWrapper.in("pid", pids);
            } else queryWrapper.eq("pid", -1);
        }
//        判断当前角色
        if (roleList.contains("provider"))
            queryWrapper.eq("seller_id", uid);
        else queryWrapper.eq("uid", uid);

        Page<OrderProduct> orders = oService.page(page, queryWrapper);

        //        货物信息集合
        ArrayList<ProductInfo> prods = new ArrayList<>();
        //        规格集合
        ArrayList<ProductSpecs> specs = new ArrayList<>();
        //        商家名集合
        ArrayList<UserOrg> providers = new ArrayList<>();
        //        地址集合
        ArrayList<Address> addresses = new ArrayList<>();

        orders.getRecords().forEach(item -> {
            Integer pid = item.getPid();
            Integer sid = item.getSpecId();
            Integer sellerId = pService.getById(pid).getSellerId();
            Integer aid = item.getAid();

            prods.add(pService.getById(pid));
            specs.add(sService.getById(sid));
            providers.add(uService.getById(sellerId));
            addresses.add(aService.getById(aid));
        });

        SaResult saResult = new SaResult();
        saResult.set("orders", orders.getRecords())
                .set("prods", prods)
                .set("specs", specs)
                .set("addresses", addresses)
                .set("providers", providers)
                .set("currentPage", pageId)
                .set("pageSize", pageSize)
                .set("total", oService.count(queryWrapper));
        return saResult;
    }

    /**
     * 更新状态
     *
     * @param oid 订单id
     * @return 结果
     */
    @PostMapping("/update")
    public SaResult UpdateStatus(int oid) {
        int uid = StpUtil.getLoginIdAsInt();
        List<String> roleList = StpUtil.getRoleList(uid);
        OrderProduct order = oService.getById(oid);
        int status = order.getStatus();
//        驴头不对马嘴
        if ((uid != order.getSellerId() && roleList.contains("provider"))
                || (uid != order.getUid() && !roleList.contains("provider")))
            return SaResult.error("无权限修改该用户信息");

        if (roleList.contains("provider")) {
            if (status == 3) return SaResult.error("订单已确认收货");
            if (status == 2) return SaResult.error("供应商不能确认收货");
        }

        if (!roleList.contains("provider")) {
            if (status < 2) return SaResult.error("订单不允许确认");
            if (status == 3) return SaResult.error("订单已确认");
        }

        if (status == 0) order.setDeliverTime(new Date());
        if (status == 1) order.setReachTime(new Date());
        if (status == 2) order.setConfirmTime(new Date());
        order.setStatus(status + 1);

        if (oService.update(order, new QueryWrapper<OrderProduct>().eq("oid", oid)))
            return SaResult.ok("修改成功");
        return SaResult.error("修改失败");
    }

}

