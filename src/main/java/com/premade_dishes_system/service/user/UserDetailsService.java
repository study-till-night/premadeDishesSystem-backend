package com.premade_dishes_system.service.user;

import com.baomidou.mybatisplus.extension.service.IService;
import com.premade_dishes_system.pojo.user.UserDetails;
import com.premade_dishes_system.pojo.user.UserRole;

/**
 * @author Shu-King
 * @description 针对表【user_details】的数据库操作Service
 * @createDate 2023-02-01 20:01:44
 */
public interface UserDetailsService extends IService<UserDetails> {

    /**
     * 校验手机号码是否唯一
     *
     * @param user 用户信息
     * @return  0 不唯一 / 1唯一
     */
    public boolean checkPhoneUnique(UserDetails user);

    /**
     * 校验email是否唯一
     *
     * @param user 用户信息
     * @return 0 不唯一 / 1唯一
     */
    public boolean checkEmailUnique(UserDetails user);
}
