package com.springplug.data.core.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.METHOD, ElementType.ANNOTATION_TYPE})
public @interface Operator {


    Type value() default Type.EQ;

    String column() default "";

    enum Type {
        EQ,LIKE, LIKELEFT, LIKERIGHT,GT,GTE,LT,LTE;
    }

}

