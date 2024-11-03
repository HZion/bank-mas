package com.sion.javamsaredis.ResDTO;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class TokenInfo {
    private Long userId;
    private String username;
    private String token;
    private LocalDateTime createdAt;
}
