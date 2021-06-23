package com.springplug.user.reactive.service.impl;

import com.springplug.user.reactive.feign.UserFeign;
import com.springplug.user.reactive.service.IUserService;
import com.springplug.web.flux.domain.AjaxResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import java.util.Map;

@Service
public class UserServiceImpl implements IUserService {


    @Autowired
    UserFeign userFeign;


    @Override
    public Mono<AjaxResult> getRedEnvelopePermission(Map<String, Object> paramsMap) {
        return userFeign.getRedEnvelopePermission(paramsMap);
    }
    @Override
    public Mono<AjaxResult> addRedEnvelopePermission(String userId, String sysCode) {
        return userFeign.addPowerList(userId, sysCode);
    }
    @Override
    public Mono<AjaxResult> updateRedEnvelopePermission(String userId, Integer permission, String sysCode) {
        return userFeign.updatePower(userId, permission, sysCode);
    }
    @Override
    public Mono<AjaxResult> deleteRedEnvelopePermission(String userId, String sysCode) {
        return userFeign.deletePower(userId, sysCode);
    }
    @Override
    public Mono<AjaxResult> getUserList(String searchKey) {
        return userFeign.getUserList(searchKey);
    }
}
