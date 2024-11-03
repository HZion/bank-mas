package com.sion.javamsaredis.Event;

import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class TokenEvent {
    private String eventId = UUID.randomUUID().toString();
    private String token;
    private TokenEventType eventType;
    private Long userId;
    private String username;
    private LocalDateTime createdAt;
    private Long expirationInSeconds;
}