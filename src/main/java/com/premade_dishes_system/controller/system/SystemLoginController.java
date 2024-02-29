package com.premade_dishes_system.controller.system;

import cn.dev33.satoken.secure.SaSecureUtil;
import cn.dev33.satoken.stp.StpUtil;
import cn.dev33.satoken.util.SaResult;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.premade_dishes_system.pojo.system.LoginBody;
import com.premade_dishes_system.pojo.system.SysRoleMenu;
import com.premade_dishes_system.pojo.user.UserOrg;
import com.premade_dishes_system.pojo.user.UserRole;
import com.premade_dishes_system.service.system.SysRoleMenuService;
import com.premade_dishes_system.service.user.UserAccountService;
import com.premade_dishes_system.service.user.UserDetailsService;
import com.premade_dishes_system.service.user.UserOrgService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@Slf4j
@CrossOrigin("*")
@RestController
@RequestMapping("/system")
@Transactional(rollbackFor = Exception.class)
public class SystemLoginController {
    @Resource
    private UserAccountService userAccountService;

    @Resource
    private SysRoleMenuService menuService;

    /**
     * 登录
     *
     * @param loginBody 登录对象
     * @return 状态
     */
    @PostMapping("/login")
    public SaResult login(@RequestBody LoginBody loginBody) {
        UserRole user = userAccountService.selectUserByUserName(loginBody.getUserName());
        if (user == null) return SaResult.error("用户不存在");
        if (user.getPassword().equals(SaSecureUtil.md5(loginBody.getPassword()))) {
            StpUtil.login(user.getUid());

            return SaResult.data(StpUtil.getTokenInfo());
        } else return SaResult.error("密码错误");
    }

    /**
     * 获取当前登录用户信息
     *
     * @return 用户信息
     */
    @GetMapping("/getInfo")
    public SaResult getInfo() {
        int loginId = StpUtil.getLoginIdAsInt();
        List<String> permissionList = StpUtil.getPermissionList(loginId);
        UserRole userRole = userAccountService.selectUserById(loginId);
        List<String> roleList = StpUtil.getRoleList(loginId);
        log.info(loginId + "用户请求信息----");

        SaResult saResult = new SaResult();
        saResult
                .set("permissions", permissionList)
                .set("uid", userRole.getUid())
                .set("name", userRole.getUserName())
                .set("roles", roleList);

        return saResult;
    }

    /**
     * 获取当前用户路由对象
     *
     * @return 动态路由
     */
    @GetMapping("/getMenu")
    public SaResult getMenu() {
        int uid = StpUtil.getLoginIdAsInt();
        int role = userAccountService.getById(uid).getUserRole();
        List<SysRoleMenu> menus = menuService.list(new QueryWrapper<SysRoleMenu>()
                .eq("role_id", role));
        return SaResult.data(menus);
    }

    /**
     * 注册
     *
     * @param data 注册对象
     * @return 状态
     */
    @PostMapping("/register")
    public SaResult register(@RequestBody Map<String, Object> data) {

        UserRole user = JSON.parseObject(JSON.toJSONString(data.get("user")), UserRole.class);
        UserOrg org = null;

        if (user.getUserRole() == 2)
            org = JSON.parseObject(JSON.toJSONString(data.get("org")), UserOrg.class);

        if (userAccountService.checkUserNameUnique(user.getUserName()))
            return SaResult.error("用户名重复");

        if (userAccountService.registerUser(user, org))
            return SaResult.ok("注册成功");
        return SaResult.error("注册失败");
    }


    /**
     * 退出
     *
     * @return 状态
     */
    @PostMapping("/logout")
    public SaResult logout() {
        StpUtil.logout();
        return SaResult.ok("当前账号退出成功");
    }
}