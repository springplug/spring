package com.springplug.push.gt.client.strategy;

import com.springplug.push.gt.client.enums.GtCastTypeEm;
import com.springplug.push.gt.client.service.PushService;
import com.springplug.push.gt.client.service.impl.*;
import com.zpx.push.gt.client.service.impl.*;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author jwt
 * @version 1.0
 * @date 2021/3/15 10:03
 */
@Component
public class GtPushStrategyFactory implements BeanPostProcessor {

    /**
     * 模块-监听者映射
     */
    private static Map<GtCastTypeEm, PushService> services = new ConcurrentHashMap<GtCastTypeEm, PushService>();

    public PushService getBean(GtCastTypeEm type){
        return services.get(type);
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        if (bean instanceof GtUniPushServiceImpl) {
            services.put(GtCastTypeEm.UNI_CAST, (PushService) bean); // 单推
        }if (bean instanceof GtListPushServiceImpl) {
            services.put(GtCastTypeEm.LIST_CAST, (PushService) bean); // 批量推
        }if (bean instanceof GtAllPushServiceImpl) {
            services.put(GtCastTypeEm.ALL_CAST, (PushService) bean); // 群推
        }if (bean instanceof GtConditionPushServiceImpl) {
            services.put(GtCastTypeEm.CONDITION_CAST, (PushService) bean); // 条件推
        }if (bean instanceof GtTagPushServiceImpl) {
            services.put(GtCastTypeEm.TAG_CAST, (PushService) bean); // 标签推
        }
        return bean;
    }
}
