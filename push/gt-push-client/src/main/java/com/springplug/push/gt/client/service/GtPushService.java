package com.springplug.push.gt.client.service;

import com.springplug.push.gt.client.entity.PushEntity;

/**
 * @author jwt
 * @version 1.0
 * @date 2021-5-29 11:50
 */
public interface GtPushService {

    boolean push(PushEntity entity);
}
