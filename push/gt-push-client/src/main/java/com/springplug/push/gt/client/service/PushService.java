package com.springplug.push.gt.client.service;

import com.getui.push.v2.sdk.api.PushApi;
import com.getui.push.v2.sdk.common.ApiResult;
import com.springplug.push.gt.client.entity.PushEntity;

/**
 * @author jwt
 * @version 1.0
 * @date 2021/3/15 9:35
 */
public interface PushService {

    ApiResult pushNotification(PushEntity entity, PushApi pushApi, String packageName);

    ApiResult pushMessage(PushEntity entity, PushApi pushApi);
}
