package com.premade_dishes_system.controller.product;


import cn.dev33.satoken.annotation.SaCheckRole;
import cn.dev33.satoken.annotation.SaMode;
import cn.dev33.satoken.stp.StpUtil;
import cn.dev33.satoken.util.SaResult;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.premade_dishes_system.pojo.product.OrderProduct;
import com.premade_dishes_system.pojo.product.ProductInfo;
import com.premade_dishes_system.pojo.trade.Contract;
import com.premade_dishes_system.service.product.OrderProductService;
import com.premade_dishes_system.service.product.ProductInfoService;
import com.premade_dishes_system.service.trade.ContractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * (ProductInfo)表控制层
 *
 * @author Shu-King
 * @since 2023-02-01 20:39:44
 */
@RestController
@Transactional(rollbackFor = Exception.class)
@RequestMapping("/api/prodPro")
public class ProductProviderController {
    /**
     * 服务对象
     */
    @Resource
    private ProductInfoService pService;

    @Autowired
    private ContractService contractService;

    @Autowired
    private OrderProductService orderService;

    /**
     * 获取供应商货物总数
     *
     * @param uid 供应商id
     * @return 结果
     */
    @GetMapping("/count")
    public SaResult queryCount(int uid) {
        int count = pService.count(new QueryWrapper<ProductInfo>()
                .eq("uid", uid));
        return SaResult.ok().set("total", count);
    }

    /**
     * 获取货物管理列表
     *
     * @param uid      供应商id
     * @param pageId   当前页面
     * @param pageSize 页面大小
     * @return 结果
     */
    @GetMapping("/get")
    public SaResult queryList(int uid,
                              @RequestParam("pid") int pageId,
                              @RequestParam("pNum") int pageSize) {
        Page<ProductInfo> page = new Page<>(pageId, pageSize);
        Page<ProductInfo> list = pService.page(page, new QueryWrapper<ProductInfo>()
                .eq("seller_id", uid));

        SaResult saResult = new SaResult();
        saResult.set("list", list.getRecords())
                .set("currentPage", pageId)
                .set("pageSize", pageSize)
                .set("total", pService.count(new QueryWrapper<ProductInfo>()
                        .eq("seller_id", uid)));
        return saResult;
    }

    /**
     * 按类别和名字搜索
     *
     * @param content  名字
     * @param category 类别
     * @return 列表
     */
    @GetMapping("/search")
    @SaCheckRole("provider")
    public SaResult search(String content, Integer category, @RequestParam(required = true) int uid) {
        if (uid != StpUtil.getLoginIdAsInt())
            return SaResult.error("无权查询该用户信息");

        QueryWrapper<ProductInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("seller_id", uid);

        if (content != null) queryWrapper.like("pname", content);
        if (category != null) queryWrapper.eq("category_id", category);

        List<ProductInfo> list = pService.list(queryWrapper);

        return SaResult.data(list);
    }

    /**
     * 上架货物
     *
     * @param prod 货物对象
     * @return 结果
     */
    @PostMapping("/add")
    @SaCheckRole("provider")
    public SaResult addProduct(@RequestBody ProductInfo prod) {
        if (prod.getSellerId() != StpUtil.getLoginIdAsInt())
            return SaResult.error("无权修改该用户信息");

        if (!pService.checkNameUnique(prod))
            return SaResult.error("已有同名商品");

        if (pService.save(prod))
            return SaResult.ok("上架成功");
        return SaResult.error("上架失败");
    }


    /**
     * 修改
     *
     * @param prod 货物对象
     * @return 结果
     */
    @PostMapping("/update")
    @SaCheckRole("provider")
    public SaResult updateProductInfo(@RequestBody ProductInfo prod) {
        if (prod.getSellerId() != StpUtil.getLoginIdAsInt())
            return SaResult.error("无权修改该用户信息");

        String oldName = pService.getById(prod.getPid()).getProductName();
        if (!pService.checkNameUnique(prod) && !prod.getProductName().equals(oldName))
            return SaResult.error("已有同名商品");

        pService.updateById(prod);
        return SaResult.ok("修改成功");
    }

    /**
     * 修改货物个性化配比许可
     *
     * @param pid 货物id
     * @return 结果
     */
    @PostMapping("/updateCustomizable")
    @SaCheckRole("provider")
    public SaResult updateCustomizable(int pid) {
        ProductInfo prod = pService.getById(pid);
        if (prod.getSellerId() != StpUtil.getLoginIdAsInt())
            return SaResult.error("无权修改该用户信息");

        if (pService.update(new UpdateWrapper<ProductInfo>()
                .set("customizable", !prod.getCustomizable())
                .eq("pid", pid)))
            return SaResult.ok("修改成功");
        return SaResult.error("修改失败");
    }

    /**
     * 下架货物
     *
     * @param pid 货物id
     * @return 结果
     */
    @PostMapping("/delete")
    @SaCheckRole("provider")
    public SaResult deleteProduct(int pid) {
        ProductInfo prod = pService.getById(pid);
        if (prod.getSellerId() != StpUtil.getLoginIdAsInt())
            return SaResult.error("无权修改该用户信息");
        if (prod.getIsDeleted())
            return SaResult.error("该货物已下架");

        //检查是否有相关未完成订单或批发单
        int count1 = orderService.count(new QueryWrapper<OrderProduct>()
                .eq("pid", pid)
                .lt("status", 3));

        int count2 = contractService.count(new QueryWrapper<Contract>()
                .eq("product_id", pid)
                .lt("status", 3));

        if (count1 != 0 || count2 != 0)
            return SaResult.error("有该商品相关未完成订单，无法删除");

        if (pService.update(new UpdateWrapper<ProductInfo>()
                .set("is_deleted", true)
                .eq("pid", pid)))
            return SaResult.ok("下架成功");
        return SaResult.error("下架失败");
    }
}

