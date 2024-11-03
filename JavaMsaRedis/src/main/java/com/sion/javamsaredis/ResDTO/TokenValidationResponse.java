package com.sion.javamsaredis.ResDTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TokenValidationResponse {
    private boolean valid;
    private Long userId;
    private String username;
    private String message;

    public TokenValidationResponse(boolean valid, Long userId, String username) {
        this.valid = valid;
        this.userId = userId;
        this.username = username;
        this.message = valid ? "Token is valid" : "Token is invalid";
    }
}