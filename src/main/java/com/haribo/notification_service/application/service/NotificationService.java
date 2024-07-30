package com.haribo.notification_service.application.service;

import com.haribo.notification_service.application.dto.MessageDto;

/**
 * ProducerService Interface
 */
public interface NotificationService {
    // 메시지를 큐로 전송 합니다.
    MessageDto sendMessage(MessageDto messageDto);
    void saveMessage(MessageDto messageDto);
}