package com.premade_dishes_system.controller.product;

import cn.dev33.satoken.annotation.SaCheckRole;
import cn.dev33.satoken.annotation.SaMode;
import cn.dev33.satoken.util.SaResult;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.premade_dishes_system.pojo.product.ProductInfo;
import com.premade_dishes_system.pojo.product.ProductRecipe;
import com.premade_dishes_system.pojo.user.ProviderGrade;
import com.premade_dishes_system.pojo.user.UserOrg;
import com.premade_dishes_system.service.product.ProductInfoService;
import com.premade_dishes_system.service.product.ProductRecipeService;
import com.premade_dishes_system.service.user.ProviderGradeService;
import com.premade_dishes_system.service.user.UserOrgService;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * 公开制作配方(ProductRecipe)表控制层
 *
 * @author makejava
 * @since 2023-04-17 21:35:03
 */
@RestController
@RequestMapping("/api/recipe")
public class ProductRecipeController {
    /**
     * 服务对象
     */
    @Resource
    private ProductRecipeService recipeService;
    @Resource
    private ProductInfoService infoService;
    @Resource
    private UserOrgService orgService;
    @Resource
    private ProviderGradeService gradeService;

    private List<Integer> loveList = new ArrayList<>();

    @GetMapping("/get")
    public SaResult queryList(@RequestParam("pid") int pageId,
                              @RequestParam("pNum") int pageSize,
                              Integer uid) {
        Page<ProductRecipe> page = new Page<>(pageId, pageSize);
        QueryWrapper<ProductRecipe> queryWrapper = new QueryWrapper<ProductRecipe>().orderByDesc("love");
//        查询某一供应商所有食谱
        if (uid != null) queryWrapper.eq("seller_id", uid);
        Page<ProductRecipe> list = recipeService.page(page, queryWrapper);

        ArrayList<ProductInfo> prods = new ArrayList<>();
        ArrayList<UserOrg> orgs = new ArrayList<>();
        ArrayList<Boolean> isLoved = new ArrayList<>();

        for (ProductRecipe item : list.getRecords()) {
            Integer sellerId = item.getSellerId();
            Integer pid = item.getPid();
            prods.add(infoService.getById(pid));
            orgs.add(orgService.getById(sellerId));
//            是否已经点过赞
            isLoved.add(loveList.contains(item.getRid()));
        }

        SaResult saResult = new SaResult();
        saResult.set("recipes", list.getRecords())
                .set("prods", prods)
                .set("orgs", orgs)
                .set("isLoved", isLoved)
                .set("currentPage", pageId)
                .set("pageSize", pageSize)
                .set("total", recipeService.count());
        return saResult;
    }

    @PostMapping("/add")
    @SaCheckRole("provider")
    @Transactional
    public SaResult add(@RequestBody ProductRecipe recipe) {
        recipeService.save(recipe);

        Integer sellerId = recipe.getSellerId();
        Integer pid = recipe.getPid();

//        设置为已公开
        infoService.update(new UpdateWrapper<ProductInfo>()
                .set("is_opened",true).eq("pid",pid));

        ProviderGrade grade = gradeService.getOne(new QueryWrapper<ProviderGrade>()
                .eq("uid",sellerId));
//        当前供应商透明度加一
        if (gradeService.update(new UpdateWrapper<ProviderGrade>()
                .set("transparency", grade.getTransparency() + 1)
                .eq("uid", sellerId)))
            return SaResult.ok("发布成功!感谢您做出的分享！");
        return SaResult.error("发布失败");
    }

    @PostMapping("/update")
    @SaCheckRole("provider")
    public SaResult update(@RequestBody ProductRecipe recipe) {
        if (recipeService.update(recipe, new QueryWrapper<ProductRecipe>().eq("rid", recipe.getRid())))
            return SaResult.ok("修改成功");
        return SaResult.error("修改失败");
    }

    @PostMapping("/love")
    public SaResult addLove(int rid) {
        if (loveList.contains(rid)) return SaResult.error("已经点赞");

        ProductRecipe recipe = recipeService.getById(rid);
        if (recipeService.update(new UpdateWrapper<ProductRecipe>()
                .set("love", recipe.getLove() + 1).eq("rid", rid))) {
            loveList.add(rid);
            return SaResult.ok("点赞成功");
        }
        return SaResult.error("点赞失败");
    }
}