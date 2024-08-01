package com.haribo.notification_service.presentation.request;

import jakarta.validation.constraints.*;
import lombok.*;

/**
 * 메시지 정보를 관리합니다.
 */
@Getter
@NoArgsConstructor // 기본 생성자
@AllArgsConstructor
@Builder
public class NotificationRequest {

    @NotBlank
    private String userId;

    @NotBlank
    private String message;
}