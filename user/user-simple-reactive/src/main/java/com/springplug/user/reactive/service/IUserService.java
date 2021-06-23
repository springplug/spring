package com.springplug.user.reactive.service;

import com.springplug.web.flux.domain.AjaxResult;
import reactor.core.publisher.Mono;
import java.util.Map;

public interface IUserService {

    /**
     * 查询系统权限列表
     *
     * @param paramsMap 查询条件参数
     * @return java.util.List<java.util.Map < java.lang.String, java.lang.Object>>
     */
    Mono<AjaxResult> getRedEnvelopePermission(Map<String, Object> paramsMap);

    /**
     * 添加系统权限
     *
     * @param userId 用户编号
     * @return com.zzsc.xrhb.api.dto.Mono<AjaxResult>
     * @Param sysCode 系统编码
     */
    Mono<AjaxResult> addRedEnvelopePermission(String userId, String sysCode);

    /**
     * 修改系统权限
     *
     * @param userId 用户编号
     * @param permission 权限
     * @param sysCode 系统编码
     * @return com.zzsc.xrhb.api.dto.Mono<AjaxResult>
     */
    Mono<AjaxResult> updateRedEnvelopePermission(String userId, Integer permission, String sysCode);

    /**
     * 删除角色系统权限
     *
     * @param userId 用户编号
     * @param sysCode 系统编码
     * @return com.zzsc.xrhb.api.dto.Mono<AjaxResult>
     */
    Mono<AjaxResult> deleteRedEnvelopePermission(String userId, String sysCode);

    /**
     * 新增权限的时候查询用户列表
     * @param searchKey
     * @return
     */
    Mono<AjaxResult> getUserList(String searchKey);

}
