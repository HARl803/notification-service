package com.haribo.notification_service.presentation;

import com.haribo.notification_service.application.dto.MessageDto;
import com.haribo.notification_service.application.service.NotificationService;
import com.haribo.notification_service.presentation.response.NotificationResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping(value = "/api/v1/notification")
public class NotificationController {

    @Autowired
    private NotificationService notificationService;

    /**
     * 생산자(Producer)가 메시지를 전송합니다.
     */
    @PostMapping
    public ResponseEntity<NotificationResponse<MessageDto>> sendMessage(@RequestBody MessageDto messageDto) {

        MessageDto result = notificationService.sendMessage(messageDto);
        if (result != null) {
            notificationService.saveMessage(result);
            return ResponseEntity.ok(NotificationResponse.success(result));
        } else {
            return ResponseEntity
                    .status(NotificationResponse.error("Failed to send message", HttpStatus.INTERNAL_SERVER_ERROR).getStatusCode())
                    .body(NotificationResponse.error("Failed to send message", HttpStatus.INTERNAL_SERVER_ERROR));
        }
    }
}
