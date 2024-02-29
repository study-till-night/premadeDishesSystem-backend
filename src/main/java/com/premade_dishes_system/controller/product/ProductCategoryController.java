package com.premade_dishes_system.controller.product;


import cn.dev33.satoken.util.SaResult;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.premade_dishes_system.pojo.product.ProductCategory;
import com.premade_dishes_system.pojo.product.ProductInfo;
import com.premade_dishes_system.service.product.ProductCategoryService;
import com.premade_dishes_system.service.product.ProductInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * (ProductCategory)表控制层
 *
 * @author Shu-King
 * @since 2023-02-01 20:39:43
 */
@RestController
@RequestMapping("/api/category")
public class ProductCategoryController {
    /**
     * 服务对象
     */
    @Autowired
    private ProductCategoryService cService;
    @Autowired
    private ProductInfoService pService;

    /**
     * 获取商品分类列表
     *
     * @return 结果
     */
    @GetMapping("/get")
    public SaResult queryList() {
        List<ProductCategory> list = cService.list();
        if (list != null) return SaResult.data(list);
        return SaResult.error("获取商品分类失败");
    }

    /**
     * 获取分类货物列表
     * @param sort 分类id
     * @param pageId 当前页面
     * @param pageSize 页面大小
     * @return 结果
     */
    @GetMapping("/sort/{sid}")
    public SaResult queryBySort(@PathVariable("sid") int sort,
                                @RequestParam("pid") int pageId,
                                @RequestParam("pNum") int pageSize) {
        if(cService.count(new QueryWrapper<ProductCategory>().eq("category_id",sort))==0)
            return SaResult.ok().setMsg("404");

        Page<ProductInfo> page = new Page<>(pageId, pageSize);

        Page<ProductInfo> list = pService.page(page,new QueryWrapper<ProductInfo>()
                .eq("category_id", sort));

        SaResult saResult = new SaResult();
        saResult.set("list", list.getRecords())
                .set("currentPage", pageId)
                .set("pageSize", pageSize)
                .set("total", pService.count(new QueryWrapper<ProductInfo>()
                        .eq("category_id",sort)));
        return saResult;
    }
}

