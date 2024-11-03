package com.sion.javamsauser.controler;
import com.sion.javamsauser.config.JwtTokenGenerator;
import com.sion.javamsauser.event.TokenEvent;
import com.sion.javamsauser.event.TokenEventType;
import com.sion.javamsauser.model.ReqDTO.LoginRequest;
import com.sion.javamsauser.model.ReqDTO.SignupRequest;
import com.sion.javamsauser.model.ResDTO.ApiResponse;
import com.sion.javamsauser.model.ResDTO.LoginResponse;
import com.sion.javamsauser.model.ResDTO.SignupResponse;
import com.sion.javamsauser.model.User;
import com.sion.javamsauser.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.UUID;


@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@Slf4j
public class UserController {
    private final UserService userService;
    private final KafkaTemplate<String, TokenEvent> kafkaTemplate;
    private final JwtTokenGenerator tokenGenerator;
    @Value("${token.expiration.seconds:3600}")
    private Long tokenExpirationSeconds;


    @GetMapping("/test")
    public void test() {
        System.out.println(LocalDateTime.now());
        System.out.println("서버 작동중");
    }

    @PostMapping("/signup")
    public ResponseEntity<ApiResponse<?>> signUp(@RequestBody SignupRequest request) {
        log.info("회원가입 시작: {}", request.getUsername());

        try {
            User user = userService.registerUser(request.getUsername(), request.getPassword());
            SignupResponse response = new SignupResponse(user.getId(), user.getUsername());
            return ResponseEntity.ok(ApiResponse.success(response, "/login"));
        } catch (RuntimeException e) {
            log.error("회원가입 실패: {}", e.getMessage());
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error(e.getMessage(), "/signup"));
        }
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<?>> login(@RequestBody LoginRequest request) {
        try {
            // 1. 로그인 검증
            User user = userService.loginUser(request.getUsername(), request.getPassword());

            // 2. 토큰 생성
            String token = userService.generateToken(user);

            // 3. Kafka로 토큰 이벤트 전송
            TokenEvent tokenEvent = TokenEvent.builder()
                    .eventId(UUID.randomUUID().toString())
                    .eventType(TokenEventType.CREATED)
                    .token(token)
                    .userId(user.getId())
                    .username(user.getUsername())
                    .createdAt(LocalDateTime.now())
                    .expirationInSeconds(tokenExpirationSeconds)
                    .build();

            try {
                kafkaTemplate.send("token-events", tokenEvent);
                log.info("Token event sent for user: {}", user.getUsername());
            } catch (Exception e) {
                log.error("Token event 전송 실패: {}", e.getMessage());
                // 토큰 이벤트 전송 실패해도 로그인은 허용
            }

            // 4. 응답 생성
            LoginResponse response = new LoginResponse(token, user.getUsername());
            System.out.println(response);
            return ResponseEntity.ok(ApiResponse.success(response, "/home"));

        } catch (RuntimeException e) {
            log.error("로그인 실패: {}", e.getMessage());
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error(e.getMessage(), "/login"));
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<ApiResponse<?>> logout(@RequestHeader("Authorization") String authHeader) {
        try {
            if (authHeader != null && authHeader.startsWith("Bearer ")) {
                String token = authHeader.substring(7);

                // 토큰에서 사용자 정보 추출
                User user = tokenGenerator.getUserFromToken(token);

                // 토큰 무효화 이벤트 생성 및 전송
                TokenEvent invalidationEvent = TokenEvent.builder()
                        .eventId(UUID.randomUUID().toString())
                        .eventType(TokenEventType.INVALIDATED)
                        .token(token)
                        .userId(user.getId())
                        .username(user.getUsername())
                        .createdAt(LocalDateTime.now())
                        .build();

                try {
                    kafkaTemplate.send("token-events", invalidationEvent);
                    log.info("Token invalidation event sent for user: {}", user.getUsername());
                } catch (Exception e) {
                    log.error("Token invalidation event 전송 실패: {}", e.getMessage());
                }

                return ResponseEntity.ok(ApiResponse.success(null, "/login"));
            }

            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("Invalid token format", "/login"));

        } catch (Exception e) {
            log.error("로그아웃 실패: {}", e.getMessage());
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("Logout failed", "/home"));
        }
    }
    @GetMapping("/actuator/health")
    public ResponseEntity<String> healthCheck() {
        return ResponseEntity.ok("UP");
    }
}