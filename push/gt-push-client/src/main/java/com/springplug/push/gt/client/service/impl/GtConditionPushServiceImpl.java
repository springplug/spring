package com.springplug.push.gt.client.service.impl;

import com.getui.push.v2.sdk.api.PushApi;
import com.getui.push.v2.sdk.common.ApiResult;
import com.getui.push.v2.sdk.dto.req.Audience;
import com.getui.push.v2.sdk.dto.req.message.PushDTO;
import com.getui.push.v2.sdk.dto.res.TaskIdDTO;
import com.springplug.push.gt.client.entity.PushEntity;
import com.springplug.push.gt.client.util.GtUtils;
import com.springplug.push.gt.client.service.PushService;
import org.springframework.stereotype.Service;

/**
 * 条件推
 * @author jwt
 * @version 1.0
 * @date 2021/3/15 9:58
 */
@Service
public class GtConditionPushServiceImpl implements PushService {
    @Override
    public ApiResult pushNotification(PushEntity entity, PushApi pushApi, String packageName) {
        ApiResult<TaskIdDTO> result = new ApiResult<>();
        PushDTO<Audience> pushDTO = GtUtils.pushByTag(entity, packageName);
        result = pushApi.pushByTag(pushDTO);
        return result;
    }

    @Override
    public ApiResult pushMessage(PushEntity entity, PushApi pushApi) {
        ApiResult<TaskIdDTO> result = new ApiResult<>();
        PushDTO<Audience> pushDTO = GtUtils.pushByTagMessage(entity);
        result = pushApi.pushByFastCustomTag(pushDTO);
        return result;
    }
}
