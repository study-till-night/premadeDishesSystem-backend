package com.premade_dishes_system.controller.user;

import cn.dev33.satoken.util.SaResult;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.premade_dishes_system.pojo.user.ProviderGrade;
import com.premade_dishes_system.pojo.user.UserRole;
import com.premade_dishes_system.service.user.ProviderGradeService;
import com.premade_dishes_system.service.user.UserAccountService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 供应商数值评估(ProviderGrade)表控制层
 *
 * @author makejava
 * @since 2023-04-17 21:34:23
 */
@RestController
@RequestMapping("/api/grade")
public class ProviderGradeController {
    /**
     * 服务对象
     */
    @Resource
    private ProviderGradeService gradeService;

    @Resource
    private UserAccountService userService;

    /**
     * 获取供应商份数
     * @param uid 供应商id
     * @return 分数
     */
    @GetMapping("/get")
    public SaResult getGrade(int uid) {
        UserRole user = userService.getById(uid);
        if (user == null) return SaResult.data(new ProviderGrade());
        if (user.getUserRole() != 2)
            return SaResult.error("查询角色非供应商");
        System.out.println(uid);
        return SaResult.data(gradeService.getOne(new QueryWrapper<ProviderGrade>().eq("uid", uid)));
    }
}

