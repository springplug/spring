package com.springplug.user.mvc.service.impl;

import com.springplug.user.mvc.feign.UserFeign;
import com.springplug.user.mvc.service.IUserService;
import com.springplug.web.mvc.domain.AjaxResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class UserServiceImpl implements IUserService {

    @Autowired
    UserFeign userFeign;

    @Override
    public AjaxResult getRedEnvelopePermission(Map<String, Object> paramsMap) {
        return userFeign.getRedEnvelopePermission(paramsMap);
    }
    @Override
    public AjaxResult addRedEnvelopePermission(String userId, String sysCode) {
        return userFeign.addPowerList(userId, sysCode);
    }
    @Override
    public AjaxResult updateRedEnvelopePermission(String userId, Integer permission, String sysCode) {
        return userFeign.updatePower(userId, permission, sysCode);
    }
    @Override
    public AjaxResult deleteRedEnvelopePermission(String userId, String sysCode) {
        return userFeign.deletePower(userId, sysCode);
    }
    @Override
    public AjaxResult getUserList(String searchKey) {
        return userFeign.getUserList(searchKey);
    }
}
