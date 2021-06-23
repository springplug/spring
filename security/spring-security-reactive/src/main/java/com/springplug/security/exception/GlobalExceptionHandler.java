package com.springplug.security.exception;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(value = {AccessDeniedException.class})
    public Map<String,Object> accessDeniedExceptionHandle(AccessDeniedException e) {
        HashMap<String, Object> resultMap = new HashMap<>();
        resultMap.put("code",401);
        resultMap.put("msg","权限不足");
        return resultMap;
    }
}
