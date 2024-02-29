package com.premade_dishes_system.config;

import cn.dev33.satoken.context.SaHolder;
import cn.dev33.satoken.filter.SaServletFilter;
import cn.dev33.satoken.interceptor.SaInterceptor;
import cn.dev33.satoken.router.SaHttpMethod;
import cn.dev33.satoken.router.SaRouter;
import cn.dev33.satoken.stp.StpUtil;
import com.premade_dishes_system.utils.AjaxJson;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class SaTokenConfig implements WebMvcConfigurer {
    // 注册 Sa-Token 拦截器，打开注解式鉴权功能
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
//        registry
//                .addInterceptor(new SaInterceptor(handle -> StpUtil.checkLogin()))
//                .addPathPatterns("/**")
//                .excludePathPatterns("/system/login", "/system/register");

        registry.addInterceptor(new SaInterceptor(handle -> {
            SaRouter.match("/**")
                    .notMatch("/system/login", "/system/register")
                    .check(r -> StpUtil.checkLogin());
            // 根据路由划分模块，不同模块不同鉴权
            SaRouter.match("/api/contract/**", r -> StpUtil.checkRoleOr("organization", "provider"));
            SaRouter.match("/api/shopCar/**", r -> StpUtil.checkRoleOr("common_user", "organization"));
//            SaRouter.match("/api/prodPro/**", r -> StpUtil.checkRole("provider")).notMatch("/api/prodPro/get");
            SaRouter.match("/api/prodSpec/**", r -> StpUtil.checkRole("provider"));
        })).addPathPatterns("/**");


    }

    @Bean
    public SaServletFilter getSaServletFilter() {
        return new SaServletFilter()
                // 拦截与排除 path
                .addInclude("/**").addExclude("/favicon.ico")

                // 全局认证函数
                .setAuth(obj -> {
                    // ...
                })

                // 异常处理函数
                .setError(e -> AjaxJson.getError(e.getMessage()))

                // 前置函数：在每次认证函数之前执行
                .setBeforeAuth(obj -> {
                    // ---------- 设置跨域响应头 ----------
                    SaHolder.getResponse()
                            // 允许指定域访问跨域资源
                            .setHeader("Access-Control-Allow-Origin", "*")
                            // 允许所有请求方式
                            .setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, DELETE")
                            // 有效时间
                            .setHeader("Access-Control-Max-Age", "3600")
                            // 允许的header参数
                            .setHeader("Access-Control-Allow-Headers", "*");

                    // 如果是预检请求，则立即返回到前端
                    SaRouter.match(SaHttpMethod.OPTIONS)
                            .free(r -> System.out.println("--------OPTIONS预检请求，不做处理"))
                            .back();
                })
                ;
    }
}
