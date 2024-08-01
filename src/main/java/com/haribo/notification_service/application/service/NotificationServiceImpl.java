package com.haribo.notification_service.application.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.haribo.notification_service.application.dto.MongoDto;
import com.haribo.notification_service.presentation.request.NotificationRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

/**
 * ProducerService 구현체
 */
@RequiredArgsConstructor
@Service
@Slf4j
public class NotificationServiceImpl implements NotificationService {

    private final MongoTemplate mongoTemplate;
    private final RabbitTemplate rabbitTemplate;

    @Override
    public void sendMessage(NotificationRequest notificationRequest) {
        try {
            // 객체를 JSON으로 변환
            ObjectMapper objectMapper = new ObjectMapper();
            String objectToJSON = objectMapper.writeValueAsString(notificationRequest);
            rabbitTemplate.convertAndSend("notificationExchange", "notificationKey", objectToJSON);
        } catch (JsonProcessingException jpe) {
            log.info("파싱 오류 발생");
            throw new RuntimeException("notification not sent");
        }
    }

    @Override
    public void saveMessage(MongoDto mongoDto) {
        try {
            mongoTemplate.insert(mongoDto);
        } catch (RuntimeException e) {
            log.info("알림 저장 과정에 오류 발생");
            throw new RuntimeException("notification not saved");
        }
    }
}