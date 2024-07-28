package com.haribo.notification_service.domain.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@Document(collection = "notifications")
public class Notification {
    @Id
    private String _id;
    private String userId;
    private String message;
    private IsRead isRead;
    private LocalDateTime timestamp;

    public enum IsRead {
        YES, NO
    }

    public Notification(String userId, String message) {
        this.userId = userId;
        this.message = message;
        this.isRead = IsRead.NO;
        this.timestamp = LocalDateTime.now();
    }

    public void markAsRead() {
        this.isRead = IsRead.YES;
    }
}
