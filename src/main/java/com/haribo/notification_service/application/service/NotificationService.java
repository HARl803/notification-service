package com.haribo.notification_service.application.service;

import com.haribo.notification_service.application.dto.NotificationDto;
import com.haribo.notification_service.domain.model.Notification;
import com.haribo.notification_service.domain.repository.NotificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class NotificationService {

    private final NotificationRepository notificationRepository;

    @Autowired
    public NotificationService(NotificationRepository notificationRepository) {
        this.notificationRepository = notificationRepository;
    }

    public Mono<NotificationDto> createNotification(String userId, String message) {
        Notification notification = new Notification(userId, message);
        return notificationRepository.save(notification)
                .map(NotificationDto::fromEntity);
    }

    public Flux<NotificationDto> getUserNotifications(String userId) {
        return notificationRepository.findByUserId(userId)
                .map(NotificationDto::fromEntity);
    }

    public Mono<Void> markAsRead(String notificationId) {
        return notificationRepository.findById(notificationId)
                .flatMap(notification -> {
                    notification.markAsRead();
                    return notificationRepository.save(notification);
                })
                .then();
    }
}
