package com.sion.javamsaredis.Controller;

import com.sion.javamsaredis.ResDTO.TokenValidationResponse;
import com.sion.javamsaredis.ResDTO.ValidateResponse;
import com.sion.javamsaredis.service.TokenService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Slf4j
public class TokenController {
    private final TokenService tokenService;

//    @PostMapping("/validate")
//    public ResponseEntity<ValidateResponse> validateToken(
//            @RequestHeader("Authorization") String authHeader) {
//        try {
//            if (authHeader != null && authHeader.startsWith("Bearer ")) {
//                String token = authHeader.substring(7);
//                boolean isValid = tokenService.validateToken(token);
//                if (isValid) {
//                    Long userId = tokenService.getUserIdFromToken(token);
//                    return ResponseEntity.ok(new ValidateResponse(true, userId));
//                }
//            }
//            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
//                    .body(new ValidateResponse(false, null));
//        } catch (Exception e) {
//            log.error("Token validation error: {}", e.getMessage());
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
//                    .body(new ValidateResponse(false, null));
//        }
//    }


    @PostMapping("/validate")
    public ResponseEntity<TokenValidationResponse> validateTokenFromAccount(
            @RequestHeader("Authorization") String authHeader) {
        try {
            if (authHeader != null && authHeader.startsWith("Bearer ")) {
                String token = authHeader.substring(7);

                // Redis에서 토큰 검증
                boolean isValid = tokenService.validateToken(token);
                if (isValid) {
                    // 토큰에서 사용자 ID 추출
                    Long userId = tokenService.getUserIdFromToken(token);

                    return ResponseEntity.ok(new TokenValidationResponse(true, userId, null));
                }
            }
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new TokenValidationResponse(false, null, "Invalid token"));

        } catch (Exception e) {
            log.error("Token validation error: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new TokenValidationResponse(false, null, e.getMessage()));
        }
    }
    @GetMapping("/actuator/health")
    public ResponseEntity<String> healthCheck() {
        return ResponseEntity.ok("UP");
    }

}