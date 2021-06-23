package com.springplug.web.flux.aop;

import com.springplug.web.flux.domain.AjaxResult;
import com.springplug.web.flux.holder.ReactiveRequestContextHolder;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.Arrays;

@Aspect
@Component
public class LoginAspect {

    @Value("${spring.application.name}")
    String name;

    @Around("@annotation(com.zzmg.web.flux.annotation.Auth)")
    public Object doAroundAdvice(ProceedingJoinPoint joinPoint) throws Throwable {
        return ReactiveRequestContextHolder.getRequest().flatMap(request -> null==request.getHeaders().getFirst("Authorization-user-sys-roles")||(!Arrays.asList(request.getHeaders().getFirst("Authorization-user-sys-roles").split(",")).contains(name))||(null == request.getHeaders().getFirst("Authorization-id")) || "406".equals(request.getHeaders().getFirst("Authorization-code")) || "401".equals(request.getHeaders().getFirst("Authorization-code")) ? Mono.just(AjaxResult.auth()) : Mono.empty()).switchIfEmpty((Mono<AjaxResult>) joinPoint.proceed());
    }
}

