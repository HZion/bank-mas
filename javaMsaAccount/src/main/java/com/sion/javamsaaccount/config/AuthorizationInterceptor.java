package com.sion.javamsaaccount.config;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.sion.javamsaaccount.exceptionCase.UnauthorizedException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import org.springframework.web.servlet.HandlerInterceptor;


@Component
@RequiredArgsConstructor
@Slf4j
public class AuthorizationInterceptor implements HandlerInterceptor {
    private final JwtTokenValidator tokenValidator;

    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response,
                             Object handler) throws Exception {
        String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new UnauthorizedException("Authorization header required");
        }

        String token = authHeader.substring(7);
        try {
            DecodedJWT jwt = tokenValidator.validateToken(token);

            // 사용자 정보를 요청 속성에 저장
            request.setAttribute("userId", jwt.getClaim("userId").asLong());
            request.setAttribute("username", jwt.getClaim("username").asString());
            return true;
        } catch (Exception e) {
            log.error("Authorization failed: {}", e.getMessage());
            throw new UnauthorizedException("Invalid token");
        }
    }
}