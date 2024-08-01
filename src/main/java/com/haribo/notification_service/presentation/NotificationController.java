package com.haribo.notification_service.presentation;

import com.haribo.notification_service.application.dto.MongoDto;
import com.haribo.notification_service.application.service.NotificationService;
import com.haribo.notification_service.presentation.request.NotificationRequest;
import com.haribo.notification_service.presentation.response.NotificationResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/v1/notification")
public class NotificationController {

    @Autowired
    private NotificationService notificationService;

    /**
     * 생산자(Producer)가 메시지를 전송합니다.
     */
    @PostMapping
    public ResponseEntity<NotificationResponse<MongoDto>> sendMessage(@RequestBody NotificationRequest notificationRequest) {
        notificationService.sendMessage(notificationRequest);
        MongoDto dataSet = MongoDto.builder()
                .userId(notificationRequest.getUserId())
                .message(notificationRequest.getMessage())
                .build();
        notificationService.saveMessage(dataSet);

        return ResponseEntity.ok(NotificationResponse.success(dataSet));
    }
}
