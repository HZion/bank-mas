package com.sion.javamsauser.event.publish;

import com.sion.javamsauser.event.TokenEvent;
import com.sion.javamsauser.model.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class TokenEventPublisher {
    private final KafkaTemplate<String, TokenEvent> kafkaTemplate;

    @Value("${token.expiration.seconds:3600}")
    private Long tokenExpirationSeconds;

    public void publishTokenCreated(String token, User user) {
        TokenEvent event = TokenEvent.builder()
                .eventId(UUID.randomUUID().toString())
                .token(token)
                .userId(user.getId())
                .username(user.getUsername())
                .createdAt(LocalDateTime.now())
                .expirationInSeconds(tokenExpirationSeconds)
                .build();

        try {
            // 가장 단순한 방식
            kafkaTemplate.send("token-events", event);
            log.info("Token event published for user: {}", user.getUsername());
        } catch (Exception e) {
            log.error("Error publishing token event: {}", e.getMessage(), e);
        }
    }
}