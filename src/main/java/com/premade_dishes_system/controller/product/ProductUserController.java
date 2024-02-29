package com.premade_dishes_system.controller.product;

import cn.dev33.satoken.stp.StpUtil;
import cn.dev33.satoken.util.SaResult;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.premade_dishes_system.pojo.product.ProductInfo;
import com.premade_dishes_system.pojo.product.ProductSpecs;
import com.premade_dishes_system.pojo.user.Address;
import com.premade_dishes_system.pojo.user.UserOrg;
import com.premade_dishes_system.service.product.ProductInfoService;
import com.premade_dishes_system.service.product.ProductSpecsService;
import com.premade_dishes_system.service.user.AddressesService;
import com.premade_dishes_system.service.user.UserDetailsService;
import com.premade_dishes_system.service.user.UserOrgService;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
@RequestMapping("/api/prodUser")
public class ProductUserController {
    /**
     * 服务对象
     */
    @Resource
    private ProductInfoService infoService;

    @Resource
    private ProductSpecsService psService;

    @Resource
    private AddressesService addService;

    @Resource
    private UserOrgService uService;

    /**
     * 首页推荐
     *
     * @return 推荐货物列表
     */
    @GetMapping("/recommend")
    public SaResult recommend(int num) {
        int uid = StpUtil.getLoginIdAsInt();
        List<ProductInfo> prods = infoService.recommend(uid, num);
        return SaResult.data(prods);
    }

    /**
     * 主页模糊搜索
     *
     * @param content  搜索内容
     * @param pageId   当前页面
     * @param pageSize 页面大小
     * @return 结果
     */
    @GetMapping("/search")
    public SaResult searchInfo(String content,
                               @RequestParam("pid") int pageId,
                               @RequestParam("pNum") int pageSize) {
        Page<ProductInfo> page = new Page<>(pageId, pageSize);
        QueryWrapper<ProductInfo> queryWrapper = new QueryWrapper<>();

        if (!content.equals("")) queryWrapper.like("pname", content);

        Page<ProductInfo> list = infoService.page(page, queryWrapper);

        SaResult saResult = new SaResult();
        saResult.set("list", list.getRecords())
                .set("currentPage", pageId)
                .set("pageSize", pageSize)
                .set("total", infoService.count(queryWrapper));
        return saResult;
    }

    /**
     * 查看某一货物
     *
     * @param pid 货物id
     * @return 货物对象
     */
    @GetMapping("/view")
    public SaResult viewProd(int pid) {
        if(infoService.count(new QueryWrapper<ProductInfo>().eq("pid",pid))==0)
            return SaResult.ok().setMsg("404");

        SaResult saResult = new SaResult();

//        查询发货地
        Integer sellerId = infoService.getOne(new QueryWrapper<ProductInfo>().eq("pid", pid)).getSellerId();
        System.out.println(sellerId);
        List<Address> addresses = addService.defaultFirst(sellerId);

        saResult.set("detail", infoService.getById(pid))
                .set("specs", psService.list(new QueryWrapper<ProductSpecs>().eq("pid", pid)))
                .set("address", addresses.size() == 0 ? addresses : addresses.get(0))
                .set("provider",uService.getById(sellerId));
        return saResult;
    }
}
