package com.premade_dishes_system.service.impl.user;

import cn.dev33.satoken.secure.SaSecureUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.premade_dishes_system.mapper.user.ProviderGradeMapper;
import com.premade_dishes_system.mapper.user.UserDetailsMapper;
import com.premade_dishes_system.mapper.user.UserOrgMapper;
import com.premade_dishes_system.mapper.user.UserRoleMapper;
import com.premade_dishes_system.pojo.user.ProviderGrade;
import com.premade_dishes_system.pojo.user.UserDetails;
import com.premade_dishes_system.pojo.user.UserOrg;
import com.premade_dishes_system.pojo.user.UserRole;
import com.premade_dishes_system.service.user.UserAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Shu-King
 * @description 针对表【user_account】的数据库操作Service实现
 * @createDate 2023-02-01 20:01:44
 */
@Service
public class UserAccountServiceImpl extends ServiceImpl<UserRoleMapper, UserRole>
        implements UserAccountService {
    @Autowired
    private UserRoleMapper userRoleMapper;

    @Autowired
    private UserDetailsMapper userDetailsMapper;

    @Autowired
    private UserOrgMapper userOrgMapper;

    @Autowired
    private ProviderGradeMapper gradeMapper;

    @Override
    public UserRole selectUserByUserName(String userName) {
        QueryWrapper<UserRole> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_name", userName);
        return userRoleMapper.selectOne(queryWrapper);
    }

    @Override
    public UserRole selectUserById(int userId) {
        QueryWrapper<UserRole> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("uid", userId);
        return userRoleMapper.selectOne(queryWrapper);
    }

    @Override
    public boolean checkUserNameUnique(String userName) {
        QueryWrapper<UserRole> roleQueryWrapper = new QueryWrapper<>();
        roleQueryWrapper.eq("user_name", userName);
        return userRoleMapper.selectCount(roleQueryWrapper) != 0;
    }

    @Override
    @Transactional
    public boolean registerUser(UserRole user, UserOrg org) {
        user.setPassword(SaSecureUtil.md5(user.getPassword()));

        UserDetails userDetails = new UserDetails();
        ProviderGrade grade = new ProviderGrade();

        int curUid = userRoleMapper.selectCount(null) + 1;
        System.out.println(curUid);
        userDetails.setUid(curUid);
//        供应商企业对象
        userRoleMapper.insert(user);
        userDetailsMapper.insert(userDetails);

        if (org != null) {
            org.setUid(curUid);
            grade.setUid(curUid);
            return userOrgMapper.insert(org) != 0 && gradeMapper.insert(grade) != 0;
        }
        return true;
    }

    @Override
    public int resetUserPwd(String userName, String password) {
        UpdateWrapper<UserRole> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("user_name", userName);
        UserRole userRole = userRoleMapper.selectOne(updateWrapper);

        String encryptedPwd = SaSecureUtil.md5(password);
        if (userRole.getPassword().equals(encryptedPwd))
            return 0;

        userRole.setPassword(encryptedPwd);
        if (userRoleMapper.update(userRole, updateWrapper) != 0)
            return 1;

        return 2;
    }

    @Override
    public boolean certificate(int uid, UserOrg userOrg) {
        return userRoleMapper.updateRoleById(uid) != 0 && userOrgMapper.insert(userOrg) != 0;
    }
}




