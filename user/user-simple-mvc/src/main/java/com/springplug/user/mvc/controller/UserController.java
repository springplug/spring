package com.springplug.user.mvc.controller;

import com.springplug.user.mvc.service.IUserService;
import com.springplug.web.mvc.annotation.Auth;
import com.springplug.web.mvc.domain.AjaxResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("user")
public class UserController {

    @Autowired
    IUserService userService;

    @Value("#{'${spring.sys.info.name:}'.empty ? '${spring.application.name}' : '${spring.sys.info.name:}'}")
    private String sysName;


    /**
     * 角色添加系统权限
     *
     * @param userId 用户编号
     * @Param sysCode 系统编码
     */
    @PostMapping("permission")
    @Auth
    public AjaxResult addRedEnvelopePermission(String userId) {
        Assert.notNull(userId,"用户id不能为空");
        return userService.addRedEnvelopePermission(userId,sysName);
    }

    /**
     * 修改系统权限
     *
     * @param userId     用户编号
     * @param permission 权限 0启用 1禁用
     */
    @PutMapping("permission")
    @Auth
    public AjaxResult updateRedEnvelopePermission(String userId,Integer permission) {
        Assert.notNull(userId,"用户id不能为空");
        Assert.notNull(permission,"权限不能为空");
        Assert.isTrue(permission==1||permission==0,()->"权限状态错误");
        return userService.updateRedEnvelopePermission(userId, permission, sysName);
    }

    /**
     * 删除系统权限
     *
     * @param userId 用户编号
     */
    @DeleteMapping("permission")
    @Auth
    public AjaxResult deleteRedEnvelopePermission(String userId) {
        Assert.notNull(userId,"用户id不能为空");
        return userService.deleteRedEnvelopePermission(userId, sysName);
    }

    /**
     * 新增权限的时候查询用户列表
     * @param searchKey
     * @return
     */
    @GetMapping("list")
    @Auth
    public AjaxResult getUserList(String searchKey){
        Assert.notNull(searchKey,"搜索条件不能为空");
        return userService.getUserList(searchKey);
    }

    /**
     * 查询该系统的用户列表
     * @param map
     * @return
     */
    @GetMapping("")
    @Auth
    public AjaxResult getUserList(@RequestParam Map<String, Object> map){
        map.put("sysCode", sysName);
        return userService.getRedEnvelopePermission(map);
    }

}