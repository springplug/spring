package com.springplug.push.gt.client.service.impl;

import com.getui.push.v2.sdk.api.PushApi;
import com.getui.push.v2.sdk.common.ApiResult;
import com.getui.push.v2.sdk.dto.req.Audience;
import com.getui.push.v2.sdk.dto.req.AudienceDTO;
import com.getui.push.v2.sdk.dto.req.message.PushDTO;
import com.getui.push.v2.sdk.dto.res.TaskIdDTO;
import com.springplug.push.gt.client.entity.PushEntity;
import com.springplug.push.gt.client.service.PushService;
import com.springplug.push.gt.client.util.GtUtils;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 *  批量推
 * @author jwt
 * @version 1.0
 * @date 2021/3/15 9:55
 */
@Service
public class GtListPushServiceImpl implements PushService {

    @Override
    public ApiResult pushNotification(PushEntity entity, PushApi pushApi, String packageName) {
        ApiResult<Map<String, Map<String, String>>> result = new ApiResult<>();
        PushDTO<Audience> pushDTO = GtUtils.createMsg(entity, packageName);
        ApiResult<TaskIdDTO> msg = pushApi.createMsg(pushDTO);
        String taskId = msg.getData().getTaskId();
        AudienceDTO audienceDTO = GtUtils.pushListByCid(taskId, entity.getCidList());
        result = pushApi.pushListByCid(audienceDTO);
        return result;
    }

    @Override
    public ApiResult pushMessage(PushEntity entity, PushApi pushApi) {
        ApiResult<Map<String, Map<String, String>>> result = new ApiResult<>();
        PushDTO<Audience> pushDTO = GtUtils.createMessage(entity);
        ApiResult<TaskIdDTO> msg = pushApi.createMsg(pushDTO);
        String taskId = msg.getData().getTaskId();
        AudienceDTO audienceDTO = GtUtils.pushListByCid(taskId, entity.getCidList());
        result = pushApi.pushListByCid(audienceDTO);
        return result;
    }
}
