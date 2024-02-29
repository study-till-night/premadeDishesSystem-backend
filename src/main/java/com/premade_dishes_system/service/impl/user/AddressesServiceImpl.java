package com.premade_dishes_system.service.impl.user;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.premade_dishes_system.mapper.user.AddressesMapper;
import com.premade_dishes_system.pojo.user.Address;
import com.premade_dishes_system.service.user.AddressesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Shu-King
 * @description 针对表【addresses】的数据库操作Service实现
 * @createDate 2023-02-01 19:48:50
 */
@Service
public class AddressesServiceImpl extends ServiceImpl<AddressesMapper, Address>
        implements AddressesService {
    @Autowired
    private AddressesMapper addressesMapper;

    @Override
    public boolean changeDefault(int aid, int uid) {
        addressesMapper.updateByDefault1(uid, aid);
        return addressesMapper.updateByDefault2(aid) != 0;
    }

    @Override
    public List<Address> defaultFirst(int uid) {
        QueryWrapper<Address> queryWrapper = new QueryWrapper<Address>().eq("uid", uid);
        ArrayList<Address> addresses = new ArrayList<>();

//        没有地址直接返回空
        if (addressesMapper.selectCount(queryWrapper) == 0)
            return new ArrayList<>();

        Address defaultAdd = addressesMapper.selectOne(new QueryWrapper<Address>().eq("is_default", true)
                .eq("uid", uid));
//        没有默认则返回全部
        if (defaultAdd == null) {
            return this.list(queryWrapper);
        }
//        默认地址放于首位
        addresses.add(defaultAdd);
        addresses.addAll(this.list(queryWrapper.ne("is_default", true)));
        return addresses;
    }
}




