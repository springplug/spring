package com.springplug.web.flux.base.controller;

import com.springplug.web.flux.domain.AjaxResult;
import com.springplug.web.flux.holder.ReactiveRequestContextHolder;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public class BaseController {

    //获取ServerHttpRequest
    public static Mono<ServerHttpRequest> getServerHttpRequest() {
        return ReactiveRequestContextHolder.getRequest();
    }

    //获取HttpHeaders
    public static Mono<HttpHeaders> getHeader() {
        return getServerHttpRequest().flatMap(request -> Mono.just(request.getHeaders()));
    }

    /**
     * 返回成功消息
     */
    public <T> Mono<AjaxResult> success(Mono<T> obj) {
        return obj.flatMap(o ->ajaxResult(o)).switchIfEmpty(AjaxResult.success());
    }

    /**
     * 返回成功消息
     */
    private <T> Mono<AjaxResult> ajaxResult(Object obj) {
        if (obj instanceof Integer) {
            return ((Integer) obj) > 0 ? AjaxResult.success() : error();
        }
        if (obj instanceof Boolean) {
            return ((Boolean) obj) ? AjaxResult.success() : error();
        }
        return AjaxResult.success(obj);
    }

    /**
     * 返回成功消息
     */
    public <T> Mono<AjaxResult> success() {
        return (AjaxResult.success());
    }

    /**
     * 返回成功消息
     */
    public <T> Mono<AjaxResult> success(Flux<T> obj) {
        return obj.collectList().flatMap(AjaxResult::success);
    }

    /**
     * 返回成功消息
     */
    public <T> Mono<AjaxResult> error() {
        return AjaxResult.error();
    }

    /**
     * 返回成功消息
     */
    public <T> Mono<AjaxResult> error(String message) {
        return Mono.just(AjaxResult.error(message));
    }

}
