package com.premade_dishes_system.controller.user;


import cn.dev33.satoken.secure.SaSecureUtil;
import cn.dev33.satoken.stp.StpUtil;
import cn.dev33.satoken.util.SaResult;
import com.premade_dishes_system.pojo.system.LoginBody;
import com.premade_dishes_system.pojo.user.UserOrg;
import com.premade_dishes_system.service.user.UserAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

/**
 * (UserAccount)表控制层
 *
 * @author Shu-King
 * @since 2023-02-01 20:39:44
 */
@RestController
@CrossOrigin("*")
@RequestMapping("/api/userAccount")
@Transactional(rollbackFor = Exception.class)
public class UserAccountController {
    /**
     * 服务对象
     */
    @Autowired
    private UserAccountService userAccountService;

    /**
     * 更新密码
     *
     * @param loginBody 登录对象 用户名+密码
     * @return 结果
     */
    @PostMapping("/update")
    public SaResult UpdatePassword(@RequestBody LoginBody loginBody) {
        String userName = loginBody.getUserName();
        String password = loginBody.getPassword();

        int uid = StpUtil.getLoginIdAsInt();
        String name = userAccountService.getById(uid).getUserName();
        if(!name.equals(userName)) return SaResult.error("无权限修改该用户信息");

        int res = userAccountService.resetUserPwd(userName, password);
        if (res == 0)
            return SaResult.error("密码与原密码相同");
        else if (res == 1)
            return SaResult.ok("修改成功");
        return SaResult.error("修改失败");
    }

    /**
     * 校企认证
     *
     * @param userOrg 校企对象
     * @return 结果
     */
    @PostMapping("/certificate")
    public SaResult certificate(@RequestBody UserOrg userOrg) {
        if(userOrg.getUid()!=StpUtil.getLoginIdAsInt() )
            return SaResult.error("无权限修改该用户信息");

        if (userAccountService.certificate(userOrg.getUid(), userOrg))
            return SaResult.ok("认证成功");
        return SaResult.error("认证失败");
    }


}

