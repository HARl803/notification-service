package com.haribo.notification_service.application.service;

import com.haribo.notification_service.presentation.request.NotificationRequest;
import com.haribo.notification_service.presentation.response.NotificationResponseForCompterChat;
import com.haribo.notification_service.presentation.response.NotificationResponseForEtc;

import java.util.List;

public interface NotificationService {

    String authorizedProfileId();
    List<NotificationResponseForEtc> getMessageListByReceiverFromMongodbForEtc(String receiver);
    List<NotificationResponseForCompterChat> getMessageListByReceiverFromMongodbForCompterChat(String receiver);
    void saveNotificationWithMatchingId(NotificationRequest request);
    void saveNotificationForCscenter(NotificationRequest request);
    void saveNotificationForCommunity(NotificationRequest request);
    void markAsRead(String notiId);
    String getDescOfTypeId(String typeId);
    String generatePrimaryKeyForNoti();
    String notificationContentDivider(String typeId, String add);
    String findMentorId(String add);
    String findMenteeId(String add);
    String findCompterChatRequestedTime(String add);
}