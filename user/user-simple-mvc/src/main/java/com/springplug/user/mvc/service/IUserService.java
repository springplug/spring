package com.springplug.user.mvc.service;

import com.springplug.web.mvc.domain.AjaxResult;

import java.util.Map;

public interface IUserService {

    /**
     * 查询系统权限列表
     *
     * @param paramsMap 查询条件参数
     * @return java.util.List<java.util.Map < java.lang.String, java.lang.Object>>
     */
    AjaxResult getRedEnvelopePermission(Map<String, Object> paramsMap);

    /**
     * 添加系统权限
     *
     * @param userId 用户编号
     * @return com.zzsc.xrhb.api.dto.AjaxResult
     * @Param sysCode 系统编码
     */
    AjaxResult addRedEnvelopePermission(String userId, String sysCode);

    /**
     * 修改系统权限
     *
     * @param userId 用户编号
     * @param permission 权限
     * @param sysCode 系统编码
     * @return com.zzsc.xrhb.api.dto.AjaxResult
     */
    AjaxResult updateRedEnvelopePermission(String userId, Integer permission, String sysCode);

    /**
     * 删除角色系统权限
     *
     * @param userId 用户编号
     * @param sysCode 系统编码
     * @return com.zzsc.xrhb.api.dto.AjaxResult
     */
    AjaxResult deleteRedEnvelopePermission(String userId, String sysCode);

    /**
     * 新增权限的时候查询用户列表
     * @param searchKey
     * @return
     */
    AjaxResult getUserList(String searchKey);

}
