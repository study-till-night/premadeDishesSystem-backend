package com.premade_dishes_system.extension;

import cn.dev33.satoken.stp.StpInterface;
import cn.dev33.satoken.stp.StpUtil;
import com.premade_dishes_system.mapper.system.SysRoleMenuMapper;
import com.premade_dishes_system.mapper.user.UserRoleMapper;
import com.premade_dishes_system.pojo.user.UserRole;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Configuration
public class StpInterfaceImpl implements StpInterface {
    @Autowired
    private SysRoleMenuMapper sysRoleMenuMapper;
    @Autowired
    private UserRoleMapper userRoleMapper;

    @Override
    /*
      获取当前用户权限
     */
    public List<String> getPermissionList(Object loginId, String loginType) {
        Integer userRole = userRoleMapper.selectById((Integer) loginId).getUserRole();
        return sysRoleMenuMapper.selectByRole(userRole);
    }

    /**
     * 获取当前用户角色
     *
     * @param loginId 登录id
     * @param loginType 登录类型
     * @return 角色列表
     */
    @Override
    public List<String> getRoleList(Object loginId, String loginType) {
        int uid = Integer.parseInt(loginId.toString());
        int role = userRoleMapper.selectById(uid).getUserRole();
        if (role == 2) return Collections.singletonList("provider");
        if (role == 1) return Collections.singletonList("organization");
        return Collections.singletonList("common_user");
    }
}
