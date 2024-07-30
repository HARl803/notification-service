package com.haribo.notification_service.application.dto;

import lombok.*;

/**
 * 메시지 정보를 관리합니다.
 */
@Getter
@Setter
@NoArgsConstructor // 기본 생성자
public class MessageDto {
    private String userId;
    private String message;

    @Builder
    public MessageDto(String userId, String message) {
        this.userId = userId;
        this.message = message;
    }
}