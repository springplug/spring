package com.springplug.push.gt.client.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.getui.push.v2.sdk.api.PushApi;
import com.getui.push.v2.sdk.common.ApiResult;
import com.springplug.push.gt.client.Mapper.PushResultMapper;
import com.springplug.push.gt.client.cofig.GtConfig;
import com.springplug.push.gt.client.entity.PushEntity;
import com.springplug.push.gt.client.entity.PushResult;
import com.springplug.push.gt.client.enums.GtTypeEm;
import com.springplug.push.gt.client.service.GtPushService;
import com.springplug.push.gt.client.service.PushService;
import com.springplug.common.util.json.JsonUtil;
import com.springplug.push.gt.client.strategy.GtPushStrategyFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;

/**
 * @author jwt
 * @version 1.0
 * @date 2021-5-29 11:52
 */
@Slf4j
@Service
public class GtPushServiceImpl implements GtPushService {

    @Resource
    PushResultMapper pushResultMapper;
    @Autowired
    GtConfig gtConfig;
    @Resource
    GtPushStrategyFactory strategyFactory;

    @Override
    public boolean push(PushEntity entity) {
        boolean pushSuccess = false;
        PushResult pushResult=new PushResult();
        pushResult.setAction("push");
        pushResult.setCreateTime(new Date());
        pushResult.setParameter(JSONObject.toJSONString(entity));
        pushResult.setResultCode(-1);
        try {
            // 获取对应项目的pushApi
            PushApi pushApi = gtConfig.getPushBeanName(entity.getAppClass());
            // 获取对应项目包名
            String packageName = gtConfig.getPackageName(entity.getAppClass());
            ApiResult result = new ApiResult();
            // 获取对应类型api
            PushService bean = strategyFactory.getBean(entity.getCastTypeEm());
            // 通知消息发送
            if (entity.getGtType() == GtTypeEm.NOTIFICATION) {
                result = bean.pushNotification(entity, pushApi, packageName);
            }else
                // 透传消息发送
                if (entity.getGtType() == GtTypeEm.MESSAGE) {
                    result = bean.pushMessage(entity, pushApi);
                }
            if (result.getMsg().equals("success")){
                pushSuccess = true;
                pushResult.setResultCode(0);
                pushResult.setResult(JSONObject.toJSONString(result));
            }else {
                pushResult.setResult(JSONObject.toJSONString(result));
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.error("发送推送失败:"+ JsonUtil.toJson(pushResult)+","+e.getMessage());
        }

        pushResultMapper.addPushResult(pushResult);
        return pushSuccess;
    }
}
