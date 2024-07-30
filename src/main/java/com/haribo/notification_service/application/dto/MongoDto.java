package com.haribo.notification_service.application.dto;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

@Data
@Document(collection = "notification")
public class MongoDto {

    @Id
    private String id;
    private String userId;
    private String message;
    private String timestamp;

    @Builder
    public MongoDto(String userId, String message) {
        this.userId = userId;
        this.message = message;
        setTimestamp();
    }

    public void setTimestamp() {
        LocalDateTime tmpDateTime = LocalDateTime.now(ZoneId.of("Asia/Seoul"));
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        this.timestamp = tmpDateTime.format(formatter);
    }
}
