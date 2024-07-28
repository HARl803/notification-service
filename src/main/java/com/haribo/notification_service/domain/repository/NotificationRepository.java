package com.haribo.notification_service.domain.repository;

import com.haribo.notification_service.domain.model.Notification;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;

public interface NotificationRepository extends ReactiveMongoRepository<Notification, String> {
    Flux<Notification> findByUserId(String userId);
}
