package com.haribo.notification_service.presentation.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NotificationRequest {
    private String userId;
    private String message;

    public NotificationRequest() {}

    public NotificationRequest(String userId, String message) {
        this.userId = userId;
        this.message = message;
    }
}
