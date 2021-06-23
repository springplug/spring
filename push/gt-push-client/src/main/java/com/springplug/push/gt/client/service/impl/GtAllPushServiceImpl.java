package com.springplug.push.gt.client.service.impl;

import com.getui.push.v2.sdk.api.PushApi;
import com.getui.push.v2.sdk.common.ApiResult;
import com.getui.push.v2.sdk.dto.req.message.PushDTO;
import com.getui.push.v2.sdk.dto.res.TaskIdDTO;
import com.springplug.push.gt.client.entity.PushEntity;
import com.springplug.push.gt.client.util.GtUtils;
import com.springplug.push.gt.client.service.PushService;
import org.springframework.stereotype.Service;

/**
 * 群推
 * @author jwt
 * @version 1.0
 * @date 2021/3/15 9:56
 */
@Service
public class GtAllPushServiceImpl implements PushService {

    @Override
    public ApiResult pushNotification(PushEntity entity, PushApi pushApi, String packageName) {
        ApiResult<TaskIdDTO> result = new ApiResult<>();
        PushDTO<String> pushDTOAll = GtUtils.pushDTOAll(entity, packageName);
        result = pushApi.pushAll(pushDTOAll);
        return result;
    }

    @Override
    public ApiResult pushMessage(PushEntity entity, PushApi pushApi) {
        ApiResult<TaskIdDTO> result = new ApiResult<>();
        PushDTO<String> pushDTOAll = GtUtils.pushDTOAllMessage(entity);
        result = pushApi.pushAll(pushDTOAll);
        return result;
    }
}
