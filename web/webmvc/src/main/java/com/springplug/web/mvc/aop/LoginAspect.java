package com.springplug.web.mvc.aop;

import com.springplug.web.mvc.domain.AjaxResult;
import com.springplug.web.mvc.util.ServletUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

@Aspect
@Component
public class LoginAspect {

    @Value("${spring.application.name}")
    String name;

    @Around("@annotation(com.springplug.web.mvc.annotation.Auth)")
    public Object doAroundAdvice(ProceedingJoinPoint joinPoint) throws Throwable {
        HttpServletRequest request = ServletUtils.getRequest();
        // 获取用户权限列表
        String sysRoles = request.getHeader("Authorization-user-sys-roles");
        // 获取用户id
        String id = request.getHeader("Authorization-id");
        // 没有权限不允许访问
        if (null == sysRoles || !sysRoles.contains(name) || null == id) {
            return AjaxResult.auth();
        }
        String code = request.getHeader("Authorization-code");
        String blackCode = request.getHeader("Authorization-black-code");
        // 该用户没有token
        if ("401".equals(code)) {
            // todo: this user no have token
            return AjaxResult.auth("该用户没有登陆");
        }

        // 该用户token已失效
        if ("406".equals(code)) {
            // todo: this user token has expired
            return AjaxResult.auth("该用户登陆已过期");
        }

        // 该用户在黑名单中
        if ("1".equals(blackCode)){}

        return joinPoint.proceed();
    }
}