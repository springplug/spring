package com.springplug.push.gt.client.service.impl;

import com.getui.push.v2.sdk.api.PushApi;
import com.getui.push.v2.sdk.common.ApiResult;
import com.getui.push.v2.sdk.dto.req.Audience;
import com.getui.push.v2.sdk.dto.req.message.PushDTO;
import com.springplug.push.gt.client.entity.PushEntity;
import com.springplug.push.gt.client.util.GtUtils;
import com.springplug.push.gt.client.service.PushService;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * 单推
 * @author jwt
 * @version 1.0
 * @date 2021/3/15 9:49
 */
@Service
public class GtUniPushServiceImpl implements PushService {

    @Override
    public ApiResult pushNotification(PushEntity entity, PushApi pushApi, String packageName) {
        ApiResult<Map<String, Map<String, String>>>  result = new ApiResult();
        PushDTO<Audience> pushDto = GtUtils.pushDTO(entity, packageName);
        GtUtils.fullCid(pushDto, entity);
        result = pushApi.pushToSingleByCid(pushDto);
        return result;
    }

    @Override
    public ApiResult pushMessage(PushEntity entity, PushApi pushApi) {
        ApiResult<Map<String, Map<String, String>>> result = new ApiResult<>();
        PushDTO<Audience> pushDto = GtUtils.pushDTOMessage(entity);
        GtUtils.fullCid(pushDto, entity);
        result = pushApi.pushToSingleByCid(pushDto);
        return result;
    }
}
