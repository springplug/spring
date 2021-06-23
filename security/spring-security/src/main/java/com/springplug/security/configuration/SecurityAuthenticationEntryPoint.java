package com.springplug.security.configuration;

import com.springplug.common.util.json.JsonUtil;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;

public class SecurityAuthenticationEntryPoint implements AuthenticationEntryPoint {


    @Override
    public void commence(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AuthenticationException e) throws IOException {
        httpServletResponse.setCharacterEncoding("UTF-8");
        httpServletResponse.setContentType("application/json; charset=utf-8");
        PrintWriter out = httpServletResponse.getWriter();
        HashMap<String, Object> resultMap = new HashMap<>();
        resultMap.put("code",401);
        resultMap.put("msg","权限不足");
        out.print(JsonUtil.toJson(resultMap));
    }
}
