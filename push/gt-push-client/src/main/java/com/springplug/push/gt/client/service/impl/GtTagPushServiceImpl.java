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
 * 标签推
 * @author jwt
 * @version 1.0
 * @date 2021/3/15 10:00
 */
@Service
public class GtTagPushServiceImpl implements PushService {

    @Override
    public ApiResult pushNotification(PushEntity entity, PushApi pushApi, String packageName) {
        ApiResult<TaskIdDTO> result = new ApiResult<>();
        PushDTO<Audience> pushDTO = GtUtils.pushByFastCustomTag(entity, packageName);
        result = pushApi.pushByFastCustomTag(pushDTO);
        return result;
    }

    @Override
    public ApiResult pushMessage(PushEntity entity, PushApi pushApi) {
        ApiResult<TaskIdDTO> result = new ApiResult<>();
        PushDTO<Audience> pushDTO = GtUtils.pushByFastCustomTagMessage(entity);
        result = pushApi.pushByFastCustomTag(pushDTO);
        return result;
    }
}
