package com.haribo.notification_service.application.service;

import com.haribo.notification_service.presentation.request.NotificationRequest;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class NotificationMessage {

    private final RabbitTemplate rabbitTemplate;
    private final String notificationQueue;

    public NotificationMessage(RabbitTemplate rabbitTemplate, @Value("${rabbitmq.notification.queue}") String notificationQueue) {
        this.rabbitTemplate = rabbitTemplate;
        this.notificationQueue = notificationQueue;
    }

    public void sendNotification(NotificationRequest notificationRequest) {
        rabbitTemplate.convertAndSend(notificationQueue, notificationRequest);
    }
}
