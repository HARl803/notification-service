package com.haribo.notification_service.presentation.response;

import lombok.*;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor
public class NotificationResponseForEtc implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;
    private String notiId;
    private String receiver;
    private String messageContent;
    private LocalDateTime createdTime;
    private Boolean isRead;
    private String link;
    private String typeId;
}
