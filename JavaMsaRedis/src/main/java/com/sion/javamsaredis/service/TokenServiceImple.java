package com.sion.javamsaredis.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class TokenServiceImple implements TokenService {
    private final RedisTemplate<String, Object> redisTemplate;

    @Override
    public boolean validateToken(String token) {
        String key = "token:" + token;
        return Boolean.TRUE.equals(redisTemplate.hasKey(key));
    }

    @Override
    public Long getUserIdFromToken(String token) {
        try {
            String key = "token:" + token;
            String userId = (String) redisTemplate.opsForHash().get(key, "userId");
            return userId != null ? Long.parseLong(userId) : null;
        } catch (Exception e) {
            log.error("Error getting userId from token: {}", e.getMessage());
            return null;
        }
    }

    @Override
    public void invalidateToken(String token) {
        String key = "token:" + token;
        redisTemplate.delete(key);
    }
}