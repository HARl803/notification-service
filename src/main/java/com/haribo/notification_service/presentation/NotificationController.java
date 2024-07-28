package com.haribo.notification_service.presentation;

import com.haribo.notification_service.application.dto.NotificationDto;
import com.haribo.notification_service.application.service.NotificationService;
import com.haribo.notification_service.application.service.NotificationMessage;
import com.haribo.notification_service.presentation.request.NotificationRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@RestController
@RequestMapping("/api/v1/notification")
public class NotificationController {

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private NotificationMessage notificationMessage;

    @PostMapping
    public Mono<ResponseEntity<NotificationDto>> createNotification(@RequestBody NotificationRequest notificationRequest) {
        // 알림 메시지 전송
        notificationMessage.sendNotification(notificationRequest);

        // 알림 내용을 MongoDB에 저장
        return notificationService.createNotification(notificationRequest.getUserId(), notificationRequest.getMessage())
                .map(ResponseEntity::ok);
    }

    @GetMapping("/{userId}")
    public Flux<NotificationDto> getUserNotifications(@PathVariable String userId) {
        return notificationService.getUserNotifications(userId);
    }

    @PutMapping("/{notificationId}")
    public Mono<ResponseEntity<Void>> markAsRead(@PathVariable String notificationId) {
        return notificationService.markAsRead(notificationId)
                .then(Mono.just(ResponseEntity.noContent().build()));
    }
}
