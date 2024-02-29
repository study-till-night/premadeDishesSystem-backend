package com.premade_dishes_system.controller.product;


import cn.dev33.satoken.stp.StpUtil;
import cn.dev33.satoken.util.SaResult;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.premade_dishes_system.pojo.product.OrderProduct;
import com.premade_dishes_system.pojo.product.ProductInfo;
import com.premade_dishes_system.pojo.product.ProductSpecs;
import com.premade_dishes_system.service.product.OrderProductService;
import com.premade_dishes_system.service.product.ProductInfoService;
import com.premade_dishes_system.service.product.ProductSpecsService;
import com.premade_dishes_system.service.trade.ContractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * (ProductSpecs)表控制层
 *
 * @author Shu-King
 * @since 2023-02-01 20:39:44
 */
@RestController
@RequestMapping("/api/prodSpec")
public class ProductSpecsController {
    /**
     * 服务对象
     */
    @Resource
    private ProductSpecsService psService;

    @Autowired
    private ProductInfoService pService;

    @Autowired
    private OrderProductService orderService;

    /**
     * 获取货物规格列表
     *
     * @param pageId 当前页面
     * @param pageSize 页面大小
     * @return 结果
     */
    @GetMapping("/get")
    public SaResult queryList(int pid,
                              int pageId,
                              @RequestParam("pNum") int pageSize) {
        int sellerId = pService.getById(pid).getSellerId();
        if (StpUtil.getLoginIdAsInt() != sellerId)
            return SaResult.error("无权限获取该用户信息");

        Page<ProductSpecs> page = new Page<>(pageId, pageSize);
        QueryWrapper<ProductSpecs> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("pid", pid);

        Page<ProductSpecs> list = psService.page(page, queryWrapper);
        SaResult saResult = new SaResult();
        saResult.set("list", list.getRecords())
                .set("currentPage", pageId)
                .set("pageSize", pageSize)
                .set("total", psService.count(queryWrapper));
        return saResult;
    }


    /**
     * 新增
     *
     * @param spec 规格对象
     * @return 结果
     */
    @PostMapping("/add")
    private SaResult addSpec(@RequestBody ProductSpecs spec) {
        ProductInfo prod = pService.getById(spec.getPid());
        if (StpUtil.getLoginIdAsInt() != prod.getSellerId())
            return SaResult.error("无权限修改该用户信息");

        if (!psService.checkNameUnique(spec)) return SaResult.error("已有同名规格");

        if (psService.save(spec))
            return SaResult.ok("添加成功");
        else return SaResult.error("添加失败");
    }

    /**
     * 修改规格信息
     *
     * @param spec 规格对象
     * @return 结果
     */
    @PostMapping("/update")
    public SaResult updateSpec(@RequestBody ProductSpecs spec) {
        ProductInfo prod = pService.getById(spec.getPid());
        if (StpUtil.getLoginIdAsInt() != prod.getSellerId())
            return SaResult.error("无权限修改该用户信息");

        String oldName = psService.getById(spec.getProductSpecId()).getSpecName();
        if (!psService.checkNameUnique(spec) && !oldName.equals(spec.getSpecName()))
            return SaResult.error("已有同名规格");

        if (psService.updateById(spec))
            return SaResult.ok("修改成功");
        return SaResult.error("修改失败");
    }

    /**
     * 修改启用状态
     *
     * @param specId 规格id
     * @return 结果
     */
    @PostMapping("/enable")
    public SaResult updateEnabled(@RequestParam("sid") int specId) {
        int pid = psService.getById(specId).getPid();
        Integer sellerId = pService.getById(pid).getSellerId();
        if (StpUtil.getLoginIdAsInt() != sellerId)
            return SaResult.error("无权限修改该用户信息");

        if (psService.UpdateEnabled(specId))
            return SaResult.ok("修改成功");
        return SaResult.error("修改失败");
    }

    /**
     * 删除规格
     *
     * @param sid 货物id
     * @return 结果
     */
    @PostMapping("/delete")
    public SaResult deleteSpec(int sid) {
        ProductInfo prod = pService.getById(sid);
        if (prod.getSellerId() != StpUtil.getLoginIdAsInt())
            return SaResult.error("无权修改该用户信息");

        //检查是否有相关未完成订单
        int count = orderService.count(new QueryWrapper<OrderProduct>()
                .eq("pid", sid)
                .le("status", 3));

        if (count > 0)
            return SaResult.error("有该商品相关未完成订单，无法删除");

        if (psService.removeById(sid))
            return SaResult.ok("删除成功");
        return SaResult.error("删除失败");
    }
}

