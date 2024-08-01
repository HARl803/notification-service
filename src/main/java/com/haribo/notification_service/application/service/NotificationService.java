package com.haribo.notification_service.application.service;

import com.haribo.notification_service.application.dto.MongoDto;
import com.haribo.notification_service.presentation.request.NotificationRequest;

/**
 * ProducerService Interface
 */
public interface NotificationService {
    // 메시지를 큐로 전송 합니다.
    void sendMessage(NotificationRequest notificationRequest);
    void saveMessage(MongoDto mongoDto);
}