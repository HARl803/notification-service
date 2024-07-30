package com.haribo.notification_service.domain.repository;

import com.haribo.notification_service.application.dto.MessageDto;

public interface MongoRepository extends org.springframework.data.mongodb.repository.MongoRepository<MessageDto, String> {
}
