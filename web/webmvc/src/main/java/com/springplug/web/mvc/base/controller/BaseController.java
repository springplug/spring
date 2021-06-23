package com.springplug.web.mvc.base.controller;

import com.springplug.common.util.string.StringUtils;
import com.springplug.web.mvc.domain.AjaxResult;
import com.springplug.web.mvc.util.ServletUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class BaseController {

    /**
     * 获取request
     */
    public HttpServletRequest getRequest() {
        return ServletUtils.getRequest();
    }

    /**
     * 获取response
     */
    public HttpServletResponse getResponse() {
        return ServletUtils.getResponse();
    }

    /**
     * 获取session
     */
    public HttpSession getSession() {
        return getRequest().getSession();
    }

    /**
     * 获取当前登录用户id
     */
    public Long getUserId(){
        String userId=getRequest().getHeader("Authorization-id");
        return StringUtils.isEmpty(userId)?null:Long.parseLong(userId);
    }

    /**
     * 获取当前登录用户登录的系统
     */
    public String getAppCode(){
        return getRequest().getHeader("Authorization-appCode");
    }

    /**
     * 响应返回结果
     *
     * @param rows 影响行数
     * @return 操作结果
     */
    protected AjaxResult toAjax(int rows) {
        return rows > 0 ? AjaxResult.success() : AjaxResult.error();
    }


    /**
     * 响应返回结果
     *
     * @param result 结果
     * @return 操作结果
     */
    protected AjaxResult toAjax(boolean result) {
        return result ? AjaxResult.success() : AjaxResult.error();
    }

    /**
     * 返回成功消息
     */
    public AjaxResult success(String message) {
        return AjaxResult.success(message);
    }

    /**
     * 返回成功消息
     */
    public AjaxResult success(String message,Object data) {
        return AjaxResult.success(message,data);
    }

    /**
     * 返回成功消息
     */
    public AjaxResult success(Object obj) {
        return AjaxResult.success(obj);
    }

    /**
     * 返回失败消息
     */
    public AjaxResult error(String message) {
        return AjaxResult.error(message);
    }

}
