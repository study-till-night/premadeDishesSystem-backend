package com.premade_dishes_system.service.user;

import com.baomidou.mybatisplus.extension.service.IService;
import com.premade_dishes_system.pojo.user.Address;

import java.util.List;

/**
 * @author Shu-King
 * @description 针对表【addresses】的数据库操作Service
 * @createDate 2023-02-01 19:48:50
 */
public interface AddressesService extends IService<Address> {
    /**
     * 修改默认地址
     * @return 是否成功
     */
    public boolean changeDefault(int aid,int uid);

    /**
     * 获取默认地址 无默认则返回第一个
     * @param uid 用户id
     * @return 地址列表
     */
    public List<Address> defaultFirst(int uid);
}
