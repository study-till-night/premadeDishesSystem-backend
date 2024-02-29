package com.premade_dishes_system.handler;

import cn.dev33.satoken.exception.NotLoginException;
import cn.dev33.satoken.exception.NotPermissionException;
import cn.dev33.satoken.exception.NotRoleException;
import cn.dev33.satoken.util.SaResult;
import com.premade_dishes_system.utils.ExceptionUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 全局异常处理器
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(NotLoginException.class)
    public SaResult handlerException(NotLoginException e) {
        System.out.println(ExceptionUtil.getExceptionMessage(e));
        log.error("当前用户未登录");
        return SaResult.error("请先登录! " + e.getMessage());
    }

    @ExceptionHandler(NotPermissionException.class)
    public SaResult handlerException(NotPermissionException e) {
        System.out.println(ExceptionUtil.getExceptionMessage(e));
        log.error("当前用户缺少权限: " + e.getPermission());
        return SaResult.error("缺少权限: " + e.getPermission());
    }

    // 拦截：缺少角色异常
    @ExceptionHandler(NotRoleException.class)
    public SaResult handlerException(NotRoleException e) {
        System.out.println(ExceptionUtil.getExceptionMessage(e));
        log.error("缺少角色：" + e.getRole());
        return SaResult.error("缺少角色：" + e.getRole());
    }

    // 拦截：其它所有异常
    @ExceptionHandler(Exception.class)
    public SaResult handlerException(Exception e) {
        System.out.println(ExceptionUtil.getExceptionMessage(e));
        return SaResult.error(e.getMessage());
    }
}
