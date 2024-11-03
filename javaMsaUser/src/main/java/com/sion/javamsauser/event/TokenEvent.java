package com.sion.javamsauser.event;

import com.sion.javamsauser.model.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TokenEvent {
    private String eventId = UUID.randomUUID().toString();
    private TokenEventType eventType;
    private String token;
    private Long userId;
    private String username;
    private LocalDateTime createdAt;
    private Long expirationInSeconds;
   }