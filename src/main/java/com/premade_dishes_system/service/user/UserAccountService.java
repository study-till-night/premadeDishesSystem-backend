package com.premade_dishes_system.service.user;

import com.baomidou.mybatisplus.extension.service.IService;
import com.premade_dishes_system.pojo.user.UserOrg;
import com.premade_dishes_system.pojo.user.UserRole;
import org.springframework.stereotype.Service;

/**
 * @author Shu-King
 * @description 针对表【user_account】的数据库操作Service
 * @createDate 2023-02-01 20:01:44
 */
@Service
public interface UserAccountService extends IService<UserRole> {
    /**
     * 通过用户名查询用户
     *
     * @param userName 用户名
     * @return 用户对象信息
     */
    UserRole selectUserByUserName(String userName);

    /**
     * 通过用户ID查询用户
     *
     * @param userId 用户ID
     * @return 用户对象信息
     */
    public UserRole selectUserById(int userId);

    /**
     * 校验用户名称是否唯一
     *
     * @param userName 用户名称
     * @return 结果
     */
    public boolean checkUserNameUnique(String userName);

    /**
     * 注册用户信息
     *
     * @param user 用户信息
     * @return 结果
     */
    public boolean registerUser(UserRole user,UserOrg name);


    /**
     * 重置用户密码
     *
     * @param userName 用户名
     * @param password 密码
     * @return 0 密码重复 1 修改成功 2修改失败
     */
    public int resetUserPwd(String userName, String password);

    /**
     * 校企认证
     * @param uid 用户
     * @return 结果
     */
    public boolean certificate(int uid, UserOrg userOrg);
}
