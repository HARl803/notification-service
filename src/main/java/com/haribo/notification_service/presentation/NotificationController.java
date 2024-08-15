package com.haribo.notification_service.presentation;

import com.haribo.notification_service.application.service.NotificationService;
import com.haribo.notification_service.presentation.exception.CustomErrorCode;
import com.haribo.notification_service.presentation.exception.CustomException;
import com.haribo.notification_service.presentation.request.MarkRequest;
import com.haribo.notification_service.presentation.request.NotificationRequest;
import com.haribo.notification_service.presentation.response.NotificationResponseForCompterChat;
import com.haribo.notification_service.presentation.response.NotificationResponseForEtc;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/v1/notification")
public class NotificationController {

    private final NotificationService notificationService;

    @GetMapping("/etc")
    public ResponseEntity<List<NotificationResponseForEtc>> getMessageListByReceiverFromMongodbForEtc() {

        log.info("신고.문의.게시글 댓글 알림 리스트 Get 컨트롤러 접근");
        List<NotificationResponseForEtc> responseList = notificationService.getMessageListByReceiverFromMongodbForEtc(notificationService.authorizedProfileId());
        log.info("신고.문의.게시글 댓글 알림 responseList.isEmpty() : {}", responseList.isEmpty());

        return ResponseEntity.status(HttpStatus.OK).body(responseList);
    }

    @GetMapping("/compter-chat")
    public ResponseEntity<List<NotificationResponseForCompterChat>> getMessageListByReceiverFromMongodbForCompterChat() {

        log.info("컴터챗 알림 리스트 Get 컨트롤러 접근");
        List<NotificationResponseForCompterChat> responseList = notificationService.getMessageListByReceiverFromMongodbForCompterChat(notificationService.authorizedProfileId());
        log.info("컴터챗 알림 responseList.isEmpty() : {}", responseList.isEmpty());

        return ResponseEntity.status(HttpStatus.OK).body(responseList);
    }

    @PatchMapping
    public ResponseEntity<String> markAsRead(@RequestBody MarkRequest request) {

        log.info("알림 읽음 표시 Patch 컨트롤러 접근");
        notificationService.markAsRead(request.notiId());
        return ResponseEntity.status(HttpStatus.OK).body("알림 읽음 표시 완료");
    }

    @PostMapping
    public ResponseEntity<String> saveNotification(@RequestBody NotificationRequest request) {

        log.info("알림 저장 Post 컨트롤러 접근");
        String typeId = request.typeId();
        log.info("typeId: {}", typeId);

        switch (typeId) {
            case "NT01", "NT02", "NT03", "NT04" -> notificationService.saveNotificationWithMatchingId(request);
            case "NT05" -> notificationService.saveNotificationForCscenter(request);
            case "NT06" -> notificationService.saveNotificationForCommunity(request);
            default -> throw new CustomException(CustomErrorCode.NOTIFICATION_TYPE_ID_NOT_FOUND);
        }
        return ResponseEntity.status(HttpStatus.OK).body("알림 저장 성공");
    }
}
