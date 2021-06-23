package com.springplug.web.flux.argument.resolver;

import org.springframework.core.MethodParameter;
import org.springframework.web.reactive.result.method.HandlerMethodArgumentResolver;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

public abstract class BaseArgumentResolver implements HandlerMethodArgumentResolver {

    protected final String SPLIT=",";

    protected final String MESSGAE="参数转换异常";

    protected final String REPLACE="\"";

    protected final String METHOD="valueOf";

    protected Object valueOf(Method m, String str){
        try {
            return m.invoke(null,replaceAll(str));
        } catch (Exception e) {
            throw new IllegalArgumentException(MESSGAE);
        }
    }

    protected Boolean checkClass(MethodParameter methodParameter, Class c) {
        return methodParameter.getParameterType().isAssignableFrom(c);
    }

    protected Object init(Constructor<?> declaredConstructor, String str){
        try {
            return declaredConstructor.newInstance(replaceAll(str));
        } catch (Exception e) {
            throw new IllegalArgumentException(MESSGAE);
        }
    }


    protected String replaceAll(String str){
        return str.replaceAll(REPLACE,"");
    }
}
