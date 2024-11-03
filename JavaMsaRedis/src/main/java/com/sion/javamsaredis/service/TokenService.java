package com.sion.javamsaredis.service;

import com.sion.javamsaredis.ResDTO.TokenInfo;
import com.sion.javamsaredis.ResDTO.TokenValidationResponse;


public interface TokenService {
    boolean validateToken(String token);
    Long getUserIdFromToken(String token);
    void invalidateToken(String token);
}
