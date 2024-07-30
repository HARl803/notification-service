package com.haribo.notification_service.application.dto;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "notification")
public class MongoDto {

    @Id
    private String id;
    private String sender;
    private String receiver;
    private String content;
    private String timestamp;
}