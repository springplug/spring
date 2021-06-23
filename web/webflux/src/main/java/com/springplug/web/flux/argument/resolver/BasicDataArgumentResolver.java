package com.springplug.web.flux.argument.resolver;

import com.springplug.common.util.string.StringUtils;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMapAdapter;
import org.springframework.web.reactive.BindingContext;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;

/**
 * 自定义参数转换
 */
@Component
public class BasicDataArgumentResolver extends BaseArgumentResolver{

    @Override
    public boolean supportsParameter(MethodParameter methodParameter) {
        return methodParameter.getParameterType().isAssignableFrom(String.class)
                ||methodParameter.getParameterType().isAssignableFrom(Integer.class)
                ||methodParameter.getParameterType().isAssignableFrom(Double.class)
                ||methodParameter.getParameterType().isAssignableFrom(Float.class)
                ||methodParameter.getParameterType().isAssignableFrom(BigDecimal.class)
                ||methodParameter.getParameterType().isAssignableFrom(Boolean.class)
                ||methodParameter.getParameterType().isAssignableFrom(Long.class);
    }

    @Override
    public Mono<Object> resolveArgument(MethodParameter parameter, BindingContext bindingContext, ServerWebExchange exchange) {
        return exchange.getFormData().map(a->{
            LinkedMultiValueMap<String,List<String>> map = new LinkedMultiValueMap(a);
            LinkedMultiValueMap<String,List<String>> queryMap = new LinkedMultiValueMap(exchange.getRequest().getQueryParams());
            map.addAll(queryMap);
            return new MultiValueMapAdapter<String,String>(new HashMap(map));
        }).name(parameter.getParameterName()).flatMap(f->{
            Class<?> parameterType = parameter.getParameterType();
            String str = f.getFirst(parameter.getParameterName());
            List<String> strings = f.get(parameter.getParameterName());
            try {
                return StringUtils.isEmpty(str)?Mono.empty():Mono.just(valueOf(parameterType.getMethod(METHOD, String.class),replaceAll(str)));
            } catch (Exception e) {
                try {
                    return Mono.just(init(parameterType.getDeclaredConstructor(String.class),replaceAll(str)));
                } catch (NoSuchMethodException ex) {
                    throw new IllegalArgumentException(MESSGAE);
                }
            }
        });
    }

}
