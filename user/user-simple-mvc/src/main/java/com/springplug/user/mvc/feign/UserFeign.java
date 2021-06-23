package com.springplug.user.mvc.feign;

import com.springplug.web.mvc.domain.AjaxResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@FeignClient("zzmg-user-service")
public interface UserFeign {

    /**
     * 修改权限
     * @param userId 用户编号
     * @param visitPower 权限 0启用 1禁用
     * @return
     */
    @PutMapping("v1/sys/powerPage")
    AjaxResult updatePower(@RequestParam("userId") String userId, @RequestParam("visitPower") Integer visitPower, @RequestParam("sysCode") String sysCode);

    /**
     * 给用户新增用户管理系统权限
     * @param userId
     * @return
     */
    @PostMapping("v1/sys/powerList")
    AjaxResult addPowerList(@RequestParam("userId") String userId, @RequestParam("sysCode") String sysCode);

    /**
     * 查询系统下  用户权限列表
     * @param paramMap
     * @return
     */
    @GetMapping("v1/sys/redEnvelopePermission")
    AjaxResult getRedEnvelopePermission(@SpringQueryMap Map<String, Object> paramMap);

    /**
     * 删除权限
     * @param userId
     * @return
     */
    @DeleteMapping("v1/sys/powerPage")
    AjaxResult deletePower(@RequestParam("userId") String userId, @RequestParam("sysCode") String sysCode);

    /**
     * 新增权限的时候查询用户列表
     * @param searchKey
     * @return
     */
    @GetMapping("v1/sys/powerList")
    AjaxResult getUserList(@RequestParam("searchKey") String searchKey);

}
