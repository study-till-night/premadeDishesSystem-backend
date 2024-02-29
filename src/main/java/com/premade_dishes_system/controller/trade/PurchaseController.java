package com.premade_dishes_system.controller.trade;

import cn.dev33.satoken.annotation.SaCheckRole;
import cn.dev33.satoken.annotation.SaMode;
import cn.dev33.satoken.stp.StpUtil;
import cn.dev33.satoken.util.SaResult;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.premade_dishes_system.pojo.product.OrderProduct;
import com.premade_dishes_system.pojo.product.ProductInfo;
import com.premade_dishes_system.pojo.product.ProductSpecs;
import com.premade_dishes_system.pojo.user.ShopCar;
import com.premade_dishes_system.service.product.OrderProductService;
import com.premade_dishes_system.service.product.ProductInfoService;
import com.premade_dishes_system.service.product.ProductSpecsService;
import com.premade_dishes_system.service.user.ShopCarService;
import com.premade_dishes_system.utils.Arith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/purchase")
@SaCheckRole(mode = SaMode.OR, value = {"common_user", "organization"})
@Transactional(rollbackFor = Exception.class)
public class PurchaseController {
    @Autowired
    private ShopCarService shopCarService;
    @Autowired
    private ProductInfoService productService;
    @Autowired
    private ProductSpecsService specsService;
    @Autowired
    private OrderProductService orderService;

    /**
     * 提交订单
     *
     * @return 结果
     */
    @PostMapping("/do")
    public SaResult purchase(@RequestParam("aid") Integer aid) {
        int uid = StpUtil.getLoginIdAsInt();
        List<ShopCar> shopCars = shopCarService.list(new QueryWrapper<ShopCar>().eq("uid", uid)
                .eq("is_chosen", true));

        //为每个购物车条目生成对应订单
        for (ShopCar item : shopCars) {
            OrderProduct order = new OrderProduct();
            //设置买家id
            order.setUid(uid);

            int pid = item.getPid();
            int specId = item.getSpecId();
            ProductInfo prod = productService.getById(pid);
            ProductSpecs spec = specsService.getById(specId);

            //设置供应商id
            order.setSellerId(prod.getSellerId());
            //设置总金额
            double price = spec.getPrice();
            int count = item.getCount();
            double totalPrice = Arith.mulWithTwoBit(price, count);
            order.setTotalPrice(totalPrice);
            //设置货物id
            order.setPid(pid);
            //设置规格id
            order.setSpecId(specId);
            //设置数量
            order.setCount(count);
            //设置地址
            order.setAid(aid);
            //保存订单
            orderService.save(order);

            //更新库存
            if (!specsService.updateStocks(specId, item.getCount())) {
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                return SaResult.error("商品: " + prod.getProductName() + "库存不足");
            }
            //更新销量
            if (!productService.updateSales(pid, item.getCount()))
                return SaResult.error("failed");
            //删除购物车对应条目
            shopCarService.removeById(item.getCid());
        }

        return SaResult.ok("购买成功");
    }
}
