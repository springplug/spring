package com.springplug.web.flux.argument.resolver;

import com.springplug.web.flux.annotation.RequestMap;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMapAdapter;
import org.springframework.web.reactive.BindingContext;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class MapArgumentResolver extends BaseArgumentResolver{
    @Override
    public boolean supportsParameter(MethodParameter methodParameter) {
        return methodParameter.hasParameterAnnotation(RequestMap.class)&&methodParameter.getParameterType().isAssignableFrom(Map.class);
    }

    @Override
    public Mono<Object> resolveArgument(MethodParameter methodParameter, BindingContext bindingContext, ServerWebExchange exchange) {
        return  exchange.getFormData().map(a->{
            LinkedMultiValueMap<String,List<String>> map = new LinkedMultiValueMap(a);
            LinkedMultiValueMap<String,List<String>> queryMap = new LinkedMultiValueMap(exchange.getRequest().getQueryParams());
            map.addAll(queryMap);
            return new MultiValueMapAdapter<String,String>(new HashMap(map));
        }).flatMap(f-> Mono.defer(()->{

            Map<String,Object> map=new HashMap<>();
            f.forEach((k,v)->map.put(k,v.size()>1?v:v.get(0)));
            return Mono.just(map);
        }));
    }
}
