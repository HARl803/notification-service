package com.haribo.notification_service.application.dto;

import com.haribo.notification_service.domain.model.Notification;
import com.haribo.notification_service.domain.model.Notification.IsRead;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class NotificationDto {
    private String _id;
    private String userId;
    private String message;
    private IsRead isRead;
    private LocalDateTime timestamp;

    public NotificationDto(String userId, String message) {
        this.userId = userId;
        this.message = message;
    }

    public NotificationDto(String _id,String userId, String message, IsRead isRead, LocalDateTime timestamp) {
        this._id = _id;
        this.userId = userId;
        this.message = message;
        this.isRead = isRead;
        this.timestamp = timestamp;
    }

    public static NotificationDto fromEntity(Notification notification) {
        return new NotificationDto(
                notification.get_id(),
                notification.getUserId(),
                notification.getMessage(),
                notification.getIsRead(),
                notification.getTimestamp());
    }

    public static Notification toEntity(NotificationDto dto) {
        Notification notification = new Notification(dto.getUserId(), dto.getMessage());
        notification.set_id(dto.get_id());
        if (dto.getIsRead() == IsRead.YES) {
            notification.markAsRead();
        }
        return notification;
    }
}
