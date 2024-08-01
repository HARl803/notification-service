package com.haribo.notification_service.config;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.mongodb.MongoDatabaseFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoClientDatabaseFactory;

@Configuration
@PropertySource("classpath:application.yml")
public class MongoConfig {

    @Value("${MONGODB_URI}")
    private String mongodbUri;

    @Value("${MONGODB_DATABASE}")
    private String haribo;

    @Bean
    public MongoClient mongoClient() {
        return MongoClients.create(mongodbUri); // 여기에 MongoDB URI를 설정하세요
    }

    @Bean
    public MongoDatabaseFactory mongoDbFactory() {
        return new SimpleMongoClientDatabaseFactory(mongoClient(), haribo);
    }

    @Bean
    public MongoTemplate mongoTemplate() {
        return new MongoTemplate(mongoDbFactory());
    }
}

