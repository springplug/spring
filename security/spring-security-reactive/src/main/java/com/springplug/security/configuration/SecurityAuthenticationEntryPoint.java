package com.springplug.security.configuration;

import com.springplug.common.util.json.JsonUtil;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.server.ServerAuthenticationEntryPoint;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.netty.ByteBufFlux;

import java.util.HashMap;

public class SecurityAuthenticationEntryPoint implements ServerAuthenticationEntryPoint {

    @Override
    public Mono<Void> commence(ServerWebExchange exchange, AuthenticationException ex) {
        ServerHttpResponse response = exchange.getResponse();
        response.getHeaders().setContentType(MediaType.APPLICATION_JSON_UTF8);
        HashMap<String, Object> resultMap = new HashMap<>();
        resultMap.put("code",401);
        resultMap.put("msg","权限不足");
        return response.writeAndFlushWith(Flux.just(ByteBufFlux.just(response.bufferFactory().wrap(JsonUtil.toJson(resultMap).getBytes()))));
    }
}
