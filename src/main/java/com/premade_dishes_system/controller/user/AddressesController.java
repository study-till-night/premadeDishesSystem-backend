package com.premade_dishes_system.controller.user;


import cn.dev33.satoken.stp.StpUtil;
import cn.dev33.satoken.util.SaResult;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.premade_dishes_system.pojo.user.Address;
import com.premade_dishes_system.service.user.AddressesService;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * (Addresses)表控制层
 *
 * @author Shu-King
 * @since 2023-02-01 20:39:41
 */
@RestController
@RequestMapping("/api/address")
@Transactional(rollbackFor = Exception.class)
public class AddressesController {
    /**
     * 服务对象
     */
    @Resource
    private AddressesService addressesService;

    /**
     * 获取地址总数
     *
     * @return 结果 默认地址在首位
     */
    @GetMapping("/getList")
    public SaResult queryTotal() {
        return SaResult.data(addressesService.defaultFirst(StpUtil.getLoginIdAsInt()));
    }

    /**
     * 获取地址列表
     *
     * @param pageId 当前页
     * @param pageSize 页数大小
     * @return 结果
     */
    @GetMapping("/getPage")
    public SaResult queryList(int uid,
                              @RequestParam("pid") int pageId,
                              @RequestParam("pNum") int pageSize) {
        if (StpUtil.getLoginIdAsInt() != uid)
            return SaResult.error("无权限获取该用户信息");

        Page<Address> page = new Page<>(pageId, pageSize);
        QueryWrapper<Address> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("uid", uid);

        Page<Address> addresses = addressesService.page(page, queryWrapper);
        SaResult saResult = new SaResult();
        saResult.set("list", addresses.getRecords())
                .set("currentPage", pageId)
                .set("pageSize", pageSize)
                .set("total", addressesService.count(queryWrapper));
        return saResult;
    }

    /**
     * 添加
     *
     * @param address 地址
     * @return 结果
     */
    @PostMapping("/add")
    public SaResult addAddress(@RequestBody Address address) {
        if (StpUtil.getLoginIdAsInt() != address.getUid())
            return SaResult.error("无权限修改该用户信息");

        if (addressesService.save(address))
            return SaResult.ok("添加成功");
        return SaResult.error("添加失败");
    }

    /**
     * 修改
     *
     * @param address 地址
     * @return 结果
     */
    @PostMapping("/update")
    public SaResult updateAddress(@RequestBody Address address) {
        if (StpUtil.getLoginIdAsInt() != address.getUid())
            return SaResult.error("无权限修改该用户信息");

        if (addressesService.updateById(address))
            return SaResult.ok("修改成功");
        return SaResult.error("修改失败");
    }

    /**
     * 删除
     *
     * @param aid 地址di
     * @return 结果
     */
    @PostMapping("/delete")
    public SaResult deleteAddress(@RequestParam("aid") int aid) {
        int uid = addressesService.getById(aid).getUid();
        if (uid != StpUtil.getLoginIdAsInt())
            return SaResult.error("无权限修改该用户信息");

        if (addressesService.removeById(aid))
            return SaResult.ok("删除成功");
        return SaResult.error("删除失败");
    }

    /**
     * 选择默认
     *
     * @param aid 地址di
     * @return 结果
     */
    @PostMapping("/default")
    public SaResult ChooseDefault(int aid) {
        int uid = addressesService.getById(aid).getUid();
        if (uid != StpUtil.getLoginIdAsInt())
            return SaResult.error("无权限修改该用户信息");

        if (addressesService.changeDefault(aid, uid))
            return SaResult.ok("修改成功");
        return SaResult.error("修改失败");
    }
}

