package com.haribo.notification_service.application.service;

import com.haribo.notification_service.presentation.request.NotificationRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class NotificationListener {

    private final NotificationService notificationService;

    public NotificationListener(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @RabbitListener(queues = "${rabbitmq.notification.queue}")
    public void handleNotification(NotificationRequest notificationRequest) {
        log.info("Received notification request: {}", notificationRequest);
        notificationService.createNotification(notificationRequest.getUserId(), notificationRequest.getMessage())
                .subscribe();
    }
}
