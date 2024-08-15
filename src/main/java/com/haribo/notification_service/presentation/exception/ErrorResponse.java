package com.haribo.notification_service.presentation.exception;

import lombok.*;

@Getter
@Builder
@AllArgsConstructor
public class ErrorResponse {

    private final String customErrorCode;
    private final String message;
}
