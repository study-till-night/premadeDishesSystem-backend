package com.premade_dishes_system.controller.user;


import cn.dev33.satoken.stp.StpUtil;
import cn.dev33.satoken.util.SaResult;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.premade_dishes_system.pojo.product.ProductInfo;
import com.premade_dishes_system.pojo.product.ProductSpecs;
import com.premade_dishes_system.pojo.user.ShopCar;
import com.premade_dishes_system.pojo.user.UserOrg;
import com.premade_dishes_system.service.product.ProductInfoService;
import com.premade_dishes_system.service.product.ProductSpecsService;
import com.premade_dishes_system.service.user.ShopCarService;
import com.premade_dishes_system.service.user.UserDetailsService;
import com.premade_dishes_system.service.user.UserOrgService;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * (ShopCar)表控制层
 *
 * @author Shu-King
 * @since 2023-02-01 20:39:44
 */
@RestController
@RequestMapping("/api/shopCar")
@Transactional(rollbackFor = Exception.class)
public class ShopCarController {
    /**
     * 服务对象
     */
    @Resource
    private ShopCarService shopCarService;
    @Resource
    private ProductInfoService pService;
    @Resource
    private ProductSpecsService psService;
    @Resource
    private UserOrgService uService;

    /**
     * 获取购物车条目总数
     *
     * @return 总量
     */
    @GetMapping("/count")
    public SaResult queryCount() {
        int count = shopCarService.count(new QueryWrapper<ShopCar>()
                .eq("uid", StpUtil.getLoginIdAsInt()));
        return SaResult.data(count);
    }


    /**
     * 获取购物车列表
     *
     * @return 结果
     */
    @GetMapping("/get")
    public SaResult queryList(String content) {
        int uid = StpUtil.getLoginIdAsInt();
        SaResult saResult = new SaResult();

        QueryWrapper<ShopCar> queryWrapper = new QueryWrapper<ShopCar>().eq("uid", uid).orderByDesc("create_time");
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

//        购物车集合
        List<ShopCar> shopCars = shopCarService.list(queryWrapper);
//        货物信息集合
        ArrayList<ProductInfo> productInfos = new ArrayList<>();
//        规格集合
        ArrayList<ProductSpecs> specs = new ArrayList<>();
//        商家名集合
        ArrayList<UserOrg> providers = new ArrayList<>();

        shopCars.forEach(item -> {
            Integer pid = item.getPid();
            Integer sid = item.getSpecId();
            Integer sellerId = pService.getById(pid).getSellerId();

            productInfos.add(pService.getById(pid));
            specs.add(psService.getById(sid));
            providers.add(uService.getById(sellerId));
        });

        return saResult.set("shopCars", shopCars)
                .set("prods", productInfos)
                .set("specs", specs)
                .set("providers", providers);
    }

    /**
     * 添加货物
     *
     * @param item 购物车条目
     * @return 结果
     */
    @PostMapping("/add")
    public SaResult addToShopCar(@RequestBody ShopCar item) {
        int uid = StpUtil.getLoginIdAsInt();
        if (uid != item.getUid())
            return SaResult.error("无权限修改该用户信息");

        ShopCar oldCar = shopCarService.getOne(new QueryWrapper<ShopCar>().eq("uid", uid)
                .eq("spec_id", item.getSpecId()));
        if (oldCar != null) {
            oldCar.setCount(oldCar.getCount()+item.getCount());
            if(shopCarService.update(oldCar,new QueryWrapper<ShopCar>().eq("cid",oldCar.getCid())))
                return SaResult.ok("添加成功");
            return SaResult.error("添加失败");
        }

        if (shopCarService.save(item))
            return SaResult.ok("添加成功");
        return SaResult.error("添加失败");
    }

    /**
     * 修改选中状态
     *
     * @param cid 购物车id
     * @return 结果
     */
    @PostMapping("/updateSingle")
    public SaResult updateSingle(@RequestParam("cid") int cid) {
        int uid = shopCarService.getById(cid).getUid();
        if (uid != StpUtil.getLoginIdAsInt()) return SaResult.error("无权限修改该用户信息");

        if (shopCarService.updateStatus(cid))
            return SaResult.ok("修改选中状态成功");
        return SaResult.error("修改选中状态失败");
    }

    /**
     * 全选/全部取消
     *
     * @return 结果
     */
    @PostMapping("/updateEntire")
    public SaResult updateEntire() {
        int uid = StpUtil.getLoginIdAsInt();
        if (uid != StpUtil.getLoginIdAsInt()) return SaResult.error("无权限修改该用户信息");

        if (shopCarService.updateAllStatus(uid))
            return SaResult.ok("全选/取消成功");
        return SaResult.error("全选/取消失败");

    }

    /**
     * 删除购物车条目
     *
     * @param cid 购物车id
     * @return 结果
     */
    @PostMapping("/delete")
    public SaResult deleteItem(@RequestParam("cid") int cid) {
        int uid = shopCarService.getById(cid).getUid();
        if (uid != StpUtil.getLoginIdAsInt()) return SaResult.error("无权限修改该用户信息");

        if (shopCarService.removeById(cid))
            return SaResult.ok("删除成功");
        return SaResult.error("删除失败");
    }

}

