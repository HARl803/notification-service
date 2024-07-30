package com.haribo.notification_service.application.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.haribo.notification_service.application.dto.MessageDto;
import com.haribo.notification_service.domain.repository.MongoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * ProducerService 구현체
 */
@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {

    @Autowired
    private MongoRepository mongoRepository;

    private final RabbitTemplate rabbitTemplate;

    @Override
    public MessageDto sendMessage(MessageDto messageDto) {
        MessageDto result = new MessageDto(messageDto.getUserId(), messageDto.getMessage());
        try {
            // 객체를 JSON으로 변환
            ObjectMapper objectMapper = new ObjectMapper();
            String objectToJSON = objectMapper.writeValueAsString(messageDto);
            rabbitTemplate.convertAndSend("notificationExchange", "noti" + messageDto.getUserId(), objectToJSON);
        } catch (JsonProcessingException jpe) {
            System.out.println("파싱 오류 발생");
            result = null;
        }

        return result;
    }

    @Override
    public void saveMessage(MessageDto messageDto) {
        mongoRepository.save(messageDto);
    }
}