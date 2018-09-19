package com.ssm.common.jpush;


import cn.jpush.api.push.model.Message;
import cn.jpush.api.push.model.Options;
import cn.jpush.api.push.model.Platform;
import cn.jpush.api.push.model.PushPayload;
import cn.jpush.api.push.model.audience.Audience;
import cn.jpush.api.push.model.notification.AndroidNotification;
import cn.jpush.api.push.model.notification.IosNotification;
import cn.jpush.api.push.model.notification.Notification;

import java.util.Arrays;
import java.util.Map;

/**
 * 构建极光推送，builder ，方便推送
 */
public final class PushPayloadBuilder {
    /**
     * 发送通知与消息，IOS ，Android 都通用
     *
     * @param extras         消息
     * @param content        通知内容
     * @param registrationId 极光的 registrationId
     * @return
     */
    public static PushPayload bulidPushPayloadBuilderIosAndAndroidMessageAndNotify(Map extras, String content, String registrationId) {
        return PushPayload.newBuilder()
                .setPlatform(Platform.all())
                .setAudience(Audience.registrationId(Arrays.asList(registrationId.split(","))))
                //通知
                .setNotification(Notification.newBuilder()
                        .addPlatformNotification(IosNotification.newBuilder()
                                .setAlert(content)
                                //  图标显示 +1
                                .incrBadge(1)
                                .setSound("happy")
                                .addExtras(extras)
                                .build())
                        .addPlatformNotification(AndroidNotification.newBuilder()
                                .setAlert(content)
                                .setTitle("title")
                                .addExtras(extras)
                                .build())
                        .build())
                //消息
                .setMessage(Message.newBuilder()
                        .setMsgContent(content)
                        .addExtras(extras)
                        .build())
                .setOptions(Options.newBuilder()
                        .setApnsProduction(false)
                        .setTimeToLive(864000)
                        .build())
                .build();
    }
}
