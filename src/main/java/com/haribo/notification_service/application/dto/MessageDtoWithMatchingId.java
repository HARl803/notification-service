package com.haribo.notification_service.application.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.io.Serial;
import java.io.Serializable;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "messageDto")
public class MessageDtoWithMatchingId implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @JsonProperty("notiId")
    private String notiId;

    @JsonProperty("receiver")
    private String receiver;

    @JsonProperty("messageContent")
    private String messageContent;

    @JsonProperty("createdTime")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX")
    private String createdTime;

    @JsonProperty("isRead")
    @Field("isRead")
    private boolean isRead;

    @JsonProperty("matchingId")
    private String matchingId;

    @JsonProperty("typeId")
    private String typeId;
}
