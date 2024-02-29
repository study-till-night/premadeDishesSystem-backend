package com.premade_dishes_system.service.impl.user;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.premade_dishes_system.mapper.user.UserDetailsMapper;
import com.premade_dishes_system.pojo.user.UserDetails;
import com.premade_dishes_system.service.user.UserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author Shu-King
 * @description 针对表【user_details】的数据库操作Service实现
 * @createDate 2023-02-01 20:01:44
 */
@Service
public class UserDetailsServiceImpl extends ServiceImpl<UserDetailsMapper, UserDetails>
        implements UserDetailsService {
    @Autowired
    private UserDetailsMapper userDetailsMapper;


    @Override
    public boolean checkPhoneUnique(UserDetails user) {
        QueryWrapper<UserDetails> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("mobile", user.getMobile())
                .ne("uid",user.getUid());
        return userDetailsMapper.selectCount(queryWrapper) == 0;
    }

    @Override
    public boolean checkEmailUnique(UserDetails user) {
        QueryWrapper<UserDetails> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("email", user.getEmail())
                .ne("uid",user.getUid());
        return userDetailsMapper.selectCount(queryWrapper) == 0;
    }
}




