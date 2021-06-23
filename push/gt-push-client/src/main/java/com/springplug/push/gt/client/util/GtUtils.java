package com.springplug.push.gt.client.util;

import com.alibaba.fastjson.JSONObject;
import com.getui.push.v2.sdk.dto.req.*;
import com.getui.push.v2.sdk.dto.req.message.PushChannel;
import com.getui.push.v2.sdk.dto.req.message.PushDTO;
import com.getui.push.v2.sdk.dto.req.message.PushMessage;
import com.getui.push.v2.sdk.dto.req.message.android.AndroidDTO;
import com.getui.push.v2.sdk.dto.req.message.android.GTNotification;
import com.getui.push.v2.sdk.dto.req.message.android.ThirdNotification;
import com.getui.push.v2.sdk.dto.req.message.android.Ups;
import com.getui.push.v2.sdk.dto.req.message.ios.Alert;
import com.getui.push.v2.sdk.dto.req.message.ios.Aps;
import com.getui.push.v2.sdk.dto.req.message.ios.IosDTO;
import com.springplug.common.util.string.StringUtils;
import com.springplug.push.gt.client.entity.PushEntity;
import com.springplug.push.gt.client.enums.GtClickTypeEm;

import java.net.URLEncoder;
import java.util.HashSet;
import java.util.List;
import java.util.UUID;

import static com.springplug.common.util.string.StringUtils.isNotEmpty;

/**
 * @author jwt
 * @version 1.0
 * @date 2021/3/13 9:56
 */
public class GtUtils {
//    --------------------------- 通知 -----------------------------------

    // 单推数据
    public static PushDTO<Audience> pushDTO(PushEntity entity, String packageName) {
        PushDTO<Audience> pushDTO = new PushDTO<Audience>();
        pushDTO.setRequestId(System.currentTimeMillis() + ""); // 请求唯一标识号，10-32位之间；如果request_id重复，会导致消息丢失，推荐时间戳
        pushDTO.setGroupName("groupName-1"); // 组名

// ------------------------------------------------  推送条件设置 ---------------------------------------------------------

        Settings settings = new Settings();
        // 消息离线时间设置，1表示不设离线，-1 ～ 3 * 24 * 3600 * 1000(3天)之间
        settings.setTtl(3600000);
        // 厂商通道策略
        Strategy strategy = new Strategy();
        // 表示该消息优先从厂商通道下发
        strategy.setIos(4);
        // 表示该消息在用户在线时推送个推通道
        strategy.setDef(1);
        settings.setStrategy(strategy);
        // 定时发送,需联系商务开通
        if (StringUtils.isNotNull(entity.getScheduleTime())) {
            settings.setScheduleTime(entity.getScheduleTime());
        }
        pushDTO.setSettings(settings);

// ------------------------------------------------  个推推送消息参数 ---------------------------------------------------------

        PushMessage pushMessage = new PushMessage();
        GTNotification notification = new GTNotification();
        // 设置大文本,与bigImage二选一，都填写报错
        if (isNotEmpty(entity.getBigText())) {
            notification.setBigText(entity.getBigText());
        } else
            // 设置图片，与bigText二选一，都填写报错
            if (isNotEmpty(entity.getBigImage())) {
            notification.setBigImage(entity.getBigImage());
        }
        // 设置logo，需客户端内嵌
        if (isNotEmpty(entity.getLogo())) {
            notification.setLogo(entity.getLogo());
        }
        // 设置logoUrl
        if (isNotEmpty(entity.getLogoUrl())) {
            notification.setLogoUrl(entity.getLogoUrl());
        }
        // 设置标题
        notification.setTitle(entity.getTitle());
        // 设置文本内容
        notification.setBody(entity.getBody());
        // 设置点击后续事件
        notification.setClickType(entity.getGtClickTypeEm().getValue());
        // 自定义内容数据填写
        if (entity.getGtClickTypeEm() == GtClickTypeEm.PAYLOAD) {
            notification.setPayload(entity.getUrl());
        }
        // 填写打开url地址
        if (entity.getGtClickTypeEm() == GtClickTypeEm.URL) {
            notification.setUrl(entity.getUrl());
        }
        // 打开应用内页面
        if (entity.getGtClickTypeEm() == GtClickTypeEm.INTENT) {
            JSONObject param = new JSONObject();
            param.put("type", entity.getMessageType());
            param.put("param", entity.getParam());
            notification.setIntent("intent:#Intent;launchFlags=0x04000000;action=android.intent.action.oppopush;component="+packageName+"/io.dcloud.PandoraEntry;S.UP-OL-SU=true;S.title="+entity.getTitle()+";S.content="+entity.getBody()+";S.payload="+ URLEncoder.encode(param.toJSONString())+";end");
        }
        notification.setBadgeAddNum("1"); // 角标设置+1
        pushMessage.setNotification(notification);
        pushDTO.setPushMessage(pushMessage);

// ------------------------------------------------  android厂商推送推送消息参数 ---------------------------------------------------------

        PushChannel pushChannel = new PushChannel(); // 厂商推送消息参数
        // android配置实体类
        AndroidDTO androidDTO = new AndroidDTO();
        // android消息
        Ups ups = new Ups();
        ThirdNotification thirdNotification = new ThirdNotification();
        thirdNotification.setClickType(entity.getGtClickTypeEm().getValue());
        // 自定义内容数据填写
        if (entity.getGtClickTypeEm() == GtClickTypeEm.PAYLOAD) {
            thirdNotification.setPayload(entity.getUrl());
        }
        // 填写打开url地址
        if (entity.getGtClickTypeEm() == GtClickTypeEm.URL) {
            thirdNotification.setUrl(entity.getUrl());
        }
        // 打开应用内页面
        if (entity.getGtClickTypeEm() == GtClickTypeEm.INTENT) {
            JSONObject param = new JSONObject();
            param.put("type", entity.getMessageType());
            param.put("param", entity.getParam());
            thirdNotification.setIntent("intent:#Intent;launchFlags=0x04000000;action=android.intent.action.oppopush;component="+packageName+"/io.dcloud.PandoraEntry;S.UP-OL-SU=true;S.title="+entity.getTitle()+";S.content="+entity.getBody()+";S.payload="+ URLEncoder.encode(param.toJSONString())+";end");
        }
        // 设置标题
        thirdNotification.setTitle(entity.getTitle());
        // 设置文本内容
        thirdNotification.setBody(entity.getBody());
        ups.setNotification(thirdNotification);
        // 设置options 方式一，具体设置查看官网
        ups.addOption("HW","/message/android/notification/badge/add_num",1); // 设置离线发送华为角标+1
        // 设置华为点击打开应用
        ups.addOption("HW","/message/android/notification/badge/class","io.dcloud.PandoraEntry");
        // 设置华为推送等级为HIGH
        ups.addOption("HW","/message/android/notification/badge/importance","HIGH");
        androidDTO.setUps(ups);
        pushChannel.setAndroid(androidDTO);

// ------------------------------------------------  ios推送消息参数 ---------------------------------------------------------

        IosDTO iosDTO = new IosDTO(); // ios配置实体类
        // 自定义内容数据填写
        if (entity.getGtClickTypeEm() == GtClickTypeEm.PAYLOAD) {
            iosDTO.setPayload(entity.getUrl());
        }
        // 填写打开url地址
        if (entity.getGtClickTypeEm() == GtClickTypeEm.URL) {
            JSONObject object = new JSONObject();
            object.put("url", entity.getUrl());
            iosDTO.setPayload(object.toJSONString());
        }
        // 打开应用内页面
        if (entity.getGtClickTypeEm() == GtClickTypeEm.INTENT) {
            JSONObject param = new JSONObject();
            param.put("type", entity.getMessageType());
            param.put("param", entity.getParam());
            iosDTO.setPayload(param.toJSONString());
        }
        // 设置角标+1
        iosDTO.setAutoBadge("+1");
        Aps aps = new Aps();
        // 通知消息
        Alert alert = new Alert();
        // 设置标题
        alert.setTitle(entity.getTitle());
        // 设置文本内容
        alert.setBody(entity.getBody());
        aps.setAlert(alert);
        iosDTO.setAps(aps);
        pushChannel.setIos(iosDTO);
        pushDTO.setPushChannel(pushChannel);

        return pushDTO;
    }

    // 创建消息
    public static PushDTO<Audience> createMsg(PushEntity entity, String packageName) {
        PushDTO<Audience> pushDTO = pushDTO(entity, packageName);
        return pushDTO;
    }

    // 群推
    public static PushDTO<String> pushDTOAll(PushEntity entity, String packageName) {
        PushDTO<String> pushDTO = new PushDTO<String>();
        pushDTO.setRequestId(UUID.randomUUID().toString().substring(0, 16)); // 请求唯一标识号，10-32位之间；如果request_id重复，会导致消息丢失
        pushDTO.setGroupName("g-name");// 组名

// ------------------------------------------------  推送条件设置 ---------------------------------------------------------

        Settings settings = new Settings();
        // 消息离线时间设置，1表示不设离线，-1 ～ 3 * 24 * 3600 * 1000(3天)之间
        settings.setTtl(3600000);
        // 厂商通道策略
        Strategy strategy = new Strategy();
        // 表示该消息优先从厂商通道下发
        strategy.setIos(4);
        // 表示该消息在用户在线时推送个推通道
        strategy.setDef(1);
        settings.setStrategy(strategy);
        // 定时发送,需联系商务开通
        if (StringUtils.isNotNull(entity.getScheduleTime())) {
            settings.setScheduleTime(entity.getScheduleTime());
        }
        pushDTO.setSettings(settings);
        // 推送所有
        pushDTO.setAudience("all");

// ------------------------------------------------  个推推送消息参数 ---------------------------------------------------------

        PushMessage pushMessage = new PushMessage();
        GTNotification notification = new GTNotification();
        // 设置大文本,与bigImage二选一，都填写报错
        if (isNotEmpty(entity.getBigText())) {
            notification.setBigText(entity.getBigText());
        } else
            // 设置图片，与bigText二选一，都填写报错
            if (isNotEmpty(entity.getBigImage())) {
            notification.setBigImage(entity.getBigImage());
        }
        // 设置logo，需客户端内嵌
        if (isNotEmpty(entity.getLogo())) {
            notification.setLogo(entity.getLogo());
        }
        // 设置logoUrl
        if (isNotEmpty(entity.getLogoUrl())) {
            notification.setLogoUrl(entity.getLogoUrl());
        }
        // 设置标题
        notification.setTitle(entity.getTitle());
        // 设置文本内容
        notification.setBody(entity.getBody());
        // 设置点击后续事件
        notification.setClickType(entity.getGtClickTypeEm().getValue());
        // 自定义内容数据填写
        if (entity.getGtClickTypeEm() == GtClickTypeEm.PAYLOAD) {
            notification.setPayload(entity.getUrl());
        }
        // 填写打开url地址
        if (entity.getGtClickTypeEm() == GtClickTypeEm.URL) {
            notification.setUrl(entity.getUrl());
        }
        // 打开应用内页面
        if (entity.getGtClickTypeEm() == GtClickTypeEm.INTENT) {
            JSONObject param = new JSONObject();
            param.put("type", entity.getMessageType());
            param.put("param", entity.getParam());
            notification.setIntent("intent:#Intent;launchFlags=0x04000000;action=android.intent.action.oppopush;component="+packageName+"/io.dcloud.PandoraEntry;S.UP-OL-SU=true;S.title="+entity.getTitle()+";S.content="+entity.getBody()+";S.payload="+ URLEncoder.encode(param.toJSONString())+";end");
        }
        notification.setBadgeAddNum("1"); // 角标设置+1
        pushMessage.setNotification(notification);
        pushDTO.setPushMessage(pushMessage);

// ------------------------------------------------  android厂商推送推送消息参数 ---------------------------------------------------------

        PushChannel pushChannel = new PushChannel(); // 厂商推送消息参数
        // android配置实体类
        AndroidDTO androidDTO = new AndroidDTO();
        // android消息
        Ups ups = new Ups();
        ThirdNotification thirdNotification = new ThirdNotification();
        thirdNotification.setClickType(entity.getGtClickTypeEm().getValue());
        // 自定义内容数据填写
        if (entity.getGtClickTypeEm() == GtClickTypeEm.PAYLOAD) {
            thirdNotification.setPayload(entity.getUrl());
        }
        // 填写打开url地址
        if (entity.getGtClickTypeEm() == GtClickTypeEm.URL) {
            thirdNotification.setUrl(entity.getUrl());
        }
        // 打开应用内页面
        if (entity.getGtClickTypeEm() == GtClickTypeEm.INTENT) {
            JSONObject param = new JSONObject();
            param.put("type", entity.getMessageType());
            param.put("param", entity.getParam());
            thirdNotification.setIntent("intent:#Intent;launchFlags=0x04000000;action=android.intent.action.oppopush;component="+packageName+"/io.dcloud.PandoraEntry;S.UP-OL-SU=true;S.title="+entity.getTitle()+";S.content="+entity.getBody()+";S.payload="+ URLEncoder.encode(param.toJSONString())+";end");
        }
        // 设置标题
        thirdNotification.setTitle(entity.getTitle());
        // 设置文本内容
        thirdNotification.setBody(entity.getBody());
        ups.setNotification(thirdNotification);
//        设置options 方式一，具体设置查看官网
        ups.addOption("HW","/message/android/notification/badge/add_num",1); // 设置离线发送华为角标+1
        // 设置华为点击打开应用
        ups.addOption("HW","/message/android/notification/badge/class","io.dcloud.PandoraEntry");
        // 设置华为推送等级为HIGH
        ups.addOption("HW","/message/android/notification/badge/importance","HIGH");
        androidDTO.setUps(ups);
        pushChannel.setAndroid(androidDTO);

// ------------------------------------------------  ios推送消息参数 ---------------------------------------------------------

        IosDTO iosDTO = new IosDTO(); // ios配置实体类
        // 自定义内容数据填写
        if (entity.getGtClickTypeEm() == GtClickTypeEm.PAYLOAD) {
            iosDTO.setPayload(entity.getUrl());
        }
        // 填写打开url地址
        if (entity.getGtClickTypeEm() == GtClickTypeEm.URL) {
            JSONObject object = new JSONObject();
            object.put("url", entity.getUrl());
            iosDTO.setPayload(object.toJSONString());
        }
        // 打开应用内页面
        if (entity.getGtClickTypeEm() == GtClickTypeEm.INTENT) {
            JSONObject param = new JSONObject();
            param.put("type", entity.getMessageType());
            param.put("param", entity.getParam());
            iosDTO.setPayload(param.toJSONString());
        }
        // 设置角标+1
        iosDTO.setAutoBadge("+1");
        Aps aps = new Aps();
        // 通知消息
        Alert alert = new Alert();
        // 设置标题
        alert.setTitle(entity.getTitle());
        // 设置文本内容
        alert.setBody(entity.getBody());
        aps.setAlert(alert);
        iosDTO.setAps(aps);
        pushChannel.setIos(iosDTO);
        pushDTO.setPushChannel(pushChannel);
        return pushDTO;
    }

    // 设置条件
    public static PushDTO<Audience> pushByTag(PushEntity entity, String packageName) {
        PushDTO<Audience> pushDTO = pushDTO(entity, packageName);
        Audience audience = new Audience();
        List<JSONObject> conditions = entity.getCondition();
        // 设置条件发送集合
        for (JSONObject condition : conditions) {
            Condition condition1 = new Condition();
            condition1.setKey(condition.getString("key"));
            condition1.setValues(new HashSet<>(condition.getJSONArray("value").toJavaList(String.class)));
            condition1.setOptType(condition.getString("optType"));
            audience.addCondition(condition1);
        }
        pushDTO.setAudience(audience);
        return pushDTO;
    }

    // 标签推送
    public static PushDTO<Audience> pushByFastCustomTag(PushEntity entity, String packageName) {
        PushDTO<Audience> pushDTO = pushDTO(entity, packageName);
        // 设置标签
        fullTag(pushDTO,entity);
        return pushDTO;
    }

//    --------------------------- 透传 -----------------------------------

    // 单推数据
    public static PushDTO<Audience> pushDTOMessage(PushEntity entity) {
        PushDTO<Audience> pushDTO = new PushDTO<>();
        // 组名
        pushDTO.setGroupName("groupName-1");
        // 请求唯一标识号，10-32位之间；如果request_id重复，会导致消息丢失
        pushDTO.setRequestId(System.currentTimeMillis() + "");

// ------------------------------------------------  个推透传消息参数 ---------------------------------------------------------

        Settings settings = new Settings();
        // 消息离线时间设置，1表示不设离线，-1 ～ 3 * 24 * 3600 * 1000(3天)之间
        settings.setTtl(3600000);
        // 厂商通道策略
        Strategy strategy = new Strategy();
        // 表示该消息优先从厂商通道下发
        strategy.setIos(4);
        // 表示该消息在用户在线时推送个推通道
        strategy.setDef(1);
        settings.setStrategy(strategy);
        // 定时发送,需联系商务开通
        if (StringUtils.isNotNull(entity.getScheduleTime())) {
            settings.setScheduleTime(entity.getScheduleTime());
        }
        pushDTO.setSettings(settings);

        PushMessage pushMessage = new PushMessage();
        // 设置透传消息
        pushMessage.setTransmission(entity.getBody());

// ------------------------------------------------  android厂商推送推送消息参数 ---------------------------------------------------------

        PushChannel pushChannel = new PushChannel();
        AndroidDTO androidDTO = new AndroidDTO();
        Ups ups = new Ups();
        // 设置透传消息
        ups.setTransmission(entity.getBody());
        androidDTO.setUps(ups);
        pushChannel.setAndroid(androidDTO);

// ------------------------------------------------  ios厂商推送推送消息参数 ---------------------------------------------------------

        IosDTO iosDTO = new IosDTO();
        // 设置ios透传消息
        iosDTO.setPayload(entity.getBody());
        pushChannel.setIos(iosDTO);
        return pushDTO;
    }

    // 创建消息
    public static PushDTO<Audience> createMessage(PushEntity entity){
        PushDTO<Audience> pushDTO = pushDTOMessage(entity);
        return pushDTO;
    }

    // 群推
    public static PushDTO<String> pushDTOAllMessage(PushEntity entity) {
        PushDTO<String> pushDTO = new PushDTO<String>();
        // 请求唯一标识号，10-32位之间；如果request_id重复，会导致消息丢失
        pushDTO.setRequestId(UUID.randomUUID().toString().substring(0, 16));
        // 组名
        pushDTO.setGroupName("g-name");

// ------------------------------------------------  个推透传消息参数 ---------------------------------------------------------

        Settings settings = new Settings();
        // 消息离线时间设置，1表示不设离线，-1 ～ 3 * 24 * 3600 * 1000(3天)之间
        settings.setTtl(3600000);
        // 厂商通道策略
        Strategy strategy = new Strategy();
        // 表示该消息优先从厂商通道下发
        strategy.setIos(4);
        // 表示该消息在用户在线时推送个推通道
        strategy.setDef(1);
        settings.setStrategy(strategy);
        // 定时发送,需联系商务开通
        if (StringUtils.isNotNull(entity.getScheduleTime())) {
            settings.setScheduleTime(entity.getScheduleTime());
        }
        pushDTO.setSettings(settings);
        // 发送所有
        pushDTO.setAudience("all");
        PushMessage pushMessage = new PushMessage();
        // 设置透传消息
        pushMessage.setTransmission(entity.getBody());

// ------------------------------------------------  android厂商推送推送消息参数 ---------------------------------------------------------

        PushChannel pushChannel = new PushChannel();

        AndroidDTO androidDTO = new AndroidDTO();
        Ups ups = new Ups();
        // 设置透传消息
        ups.setTransmission(entity.getBody());
        androidDTO.setUps(ups);
        pushChannel.setAndroid(androidDTO);

// ------------------------------------------------  ios厂商推送推送消息参数 ---------------------------------------------------------

        IosDTO iosDTO = new IosDTO();
        // 设置ios透传消息
        iosDTO.setPayload(entity.getBody());
        pushChannel.setIos(iosDTO);
        return pushDTO;
    }

    // 设置条件
    public static PushDTO<Audience> pushByTagMessage(PushEntity entity) {
        PushDTO<Audience> pushDTO = pushDTOMessage(entity);
        Audience audience = new Audience();
        List<JSONObject> conditions = entity.getCondition();
        // 设置条件集合
        for (JSONObject condition : conditions) {
            Condition condition1 = new Condition();
            condition1.setKey(condition.getString("key"));
            condition1.setValues(new HashSet<>(condition.getJSONArray("value").toJavaList(String.class)));
            condition1.setOptType(condition.getString("optType"));
            audience.addCondition(condition1);
        }
        pushDTO.setAudience(audience);
        return pushDTO;
    }

    // 标签推送
    public static PushDTO<Audience> pushByFastCustomTagMessage(PushEntity entity) {
        PushDTO<Audience> pushDTO = pushDTOMessage(entity);
        // 设置标签
        fullTag(pushDTO,entity);
        return pushDTO;
    }

//    -------------------------------------------------------------------------

    // 批量推送,默认同步
    public static AudienceDTO pushListByCid(String taskId, List<String> cidList) {
        AudienceDTO audienceDTO = new AudienceDTO();
        // 批量发送设置模板taskId
        audienceDTO.setTaskid(taskId);
        Audience audience = new Audience();
        audience.setCid(cidList);
        audienceDTO.setAudience(audience);
        return audienceDTO;
    }

    public static void fullCid(PushDTO<Audience> pushDTO, PushEntity entity) {
        Audience audience = new Audience();
        audience.setCid(entity.getCidList());
        pushDTO.setAudience(audience);
    }

    public static void fullTag(PushDTO pushDTO, PushEntity entity) {
        Audience audience = new Audience();
        audience.setFastCustomTag(entity.getTag());
        pushDTO.setAudience(audience);
    }

}
