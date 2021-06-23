package com.springplug.web.flux.argument.resolver;

import com.springplug.common.util.json.JsonUtil;
import com.springplug.common.util.string.StringUtils;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMapAdapter;
import org.springframework.web.reactive.BindingContext;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;
import sun.reflect.generics.reflectiveObjects.ParameterizedTypeImpl;
import java.lang.reflect.Type;
import java.util.*;

@Component
public class CollectionArgumentResolver extends BaseArgumentResolver{

    @Override
    public boolean supportsParameter(MethodParameter methodParameter) {
        return methodParameter.getParameterType().isAssignableFrom(List.class)
                || methodParameter.getParameterType().isAssignableFrom(Set.class);
    }

    @Override
    public Mono<Object> resolveArgument(MethodParameter methodParameter, BindingContext bindingContext, ServerWebExchange exchange) {
        return  exchange.getFormData().map(a->{
            LinkedMultiValueMap<String,List<String>> map = new LinkedMultiValueMap(a);
            LinkedMultiValueMap<String,List<String>> queryMap = new LinkedMultiValueMap(exchange.getRequest().getQueryParams());
            map.addAll(queryMap);
            return new MultiValueMapAdapter<String,String>(new HashMap(map));
        }).flatMap(f -> {
            Type actualTypeArguments = ((ParameterizedTypeImpl) methodParameter.getGenericParameterType()).getActualTypeArguments()[0];
            String str = f.getFirst(methodParameter.getParameterName());
            return StringUtils.isEmpty(str)?Mono.just(checkClass(methodParameter, List.class)?new ArrayList():new HashSet<>()):Optional.ofNullable(JsonUtil.toList(str, (Class<?>) actualTypeArguments)).map(a -> checkClass(methodParameter, List.class) ? Mono.just(a) : Mono.just(new HashSet(a))).orElseGet(() -> {
                Collection o = checkClass(methodParameter, List.class) ? new ArrayList() : new HashSet();
                CollectionUtils.addAll(o, strArrayToAppointArray(str.split(SPLIT), (Class<?>) actualTypeArguments));
                return Mono.just(o);
            });
        });
    }

    private Object[] strArrayToAppointArray(String[] str, Class<?> c) {
        try {
            return Optional.ofNullable(c.getMethod(METHOD, String.class)).map(a -> Arrays.stream(str).map(s -> valueOf(a, s)).toArray()).get();
        } catch (NoSuchMethodException e) {
            try {
                return Optional.ofNullable(c.getDeclaredConstructor(String.class)).map(a -> Arrays.stream(str).map(s -> init(a, s)).toArray()).get();
            } catch (NoSuchMethodException ex) {
                throw new IllegalArgumentException(MESSGAE);
            }
        }
    }

}
