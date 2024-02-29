package com.premade_dishes_system.service.impl.user;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.premade_dishes_system.mapper.user.ShopCarMapper;
import com.premade_dishes_system.pojo.user.ShopCar;
import com.premade_dishes_system.service.user.ShopCarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author Shu-King
 * @description 针对表【shop_car】的数据库操作Service实现
 * @createDate 2023-02-01 20:01:44
 */
@Service
public class ShopCarServiceImpl extends ServiceImpl<ShopCarMapper, ShopCar>
        implements ShopCarService {
    @Autowired
    private ShopCarMapper shopCarMapper;

    @Override
    public boolean updateStatus(int cid) {
        return shopCarMapper.updateById(cid) != 0;
    }

    @Override
    public boolean updateAllStatus(int uid) {
        QueryWrapper<ShopCar> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("uid", uid);
        int cntAll = shopCarMapper.selectCount(queryWrapper);

        queryWrapper.eq("is_chosen", 1);
        int cntChosen = shopCarMapper.selectCount(queryWrapper);

        int res = 0;
        if (cntAll != cntChosen)
            res = shopCarMapper.updateByChosen1(uid);
        else res = shopCarMapper.updateByChosen2(uid);

        return res != 0;
    }
}




