package com.sion.javamsaredis.Event.consumer;


import com.sion.javamsaredis.Event.TokenEvent;
import com.sion.javamsaredis.Event.TokenEventType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
@Slf4j
public class TokenEventConsumer {
    private final RedisTemplate<String, Object> redisTemplate;

    @KafkaListener(topics = "token-events", groupId = "redis-service")
    public void handleTokenEvent(TokenEvent event) {
        try {
            if (event.getEventType() == TokenEventType.CREATED) {
                handleTokenCreation(event);
            } else if (event.getEventType() == TokenEventType.INVALIDATED) {
                handleTokenInvalidation(event);
            }
        } catch (Exception e) {
            log.error("Error processing token event: {}", e.getMessage(), e);
        }
    }

    private void handleTokenCreation(TokenEvent event) {
        String key = "token:" + event.getToken();
        redisTemplate.opsForHash().put(key, "userId", event.getUserId().toString());
        redisTemplate.opsForHash().put(key, "username", event.getUsername());
        redisTemplate.opsForHash().put(key, "createdAt", event.getCreatedAt().toString());
        redisTemplate.expire(key, event.getExpirationInSeconds(), TimeUnit.SECONDS);
        log.info("Token stored in Redis for user: {}", event.getUsername());
    }

    private void handleTokenInvalidation(TokenEvent event) {
        String key = "token:" + event.getToken();
        Boolean deleted = redisTemplate.delete(key);
        if (Boolean.TRUE.equals(deleted)) {
            log.info("Token invalidated in Redis for user: {}", event.getUsername());
        } else {
            log.warn("Token not found in Redis for invalidation: {}", event.getToken());
        }
    }
}