package com.haribo.notification_service.presentation.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor
public class NotificationResponseForCompterChat implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;
    private String notiId;
    private String receiver;
    private String messageContent;
    private LocalDateTime createdTime;
    private Boolean isRead;
    private String matchingId;
    private String typeId;
}
