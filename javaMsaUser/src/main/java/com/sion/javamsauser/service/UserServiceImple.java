package com.sion.javamsauser.service;


import com.sion.javamsauser.config.JwtTokenGenerator;

import com.sion.javamsauser.event.publish.TokenEventPublisher;
import com.sion.javamsauser.model.User;
import com.sion.javamsauser.model.ResDTO.UserResponse;
import com.sion.javamsauser.repo.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
public class UserServiceImple implements UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenGenerator jwtTokenGenerator;
    private final TokenEventPublisher tokenEventPublisher;


    @Override
    @Transactional
    public User registerUser(String username, String password) {
        // 중복 사용자 검사
        if (userRepository.existsByUsername(username)) {
            throw new RuntimeException("Username already exists");
        }

        // 새 사용자 생성
        User user = new User();
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(password));
        user.setRoles("ROLE_USER");

        return userRepository.save(user);
    }

    @Override
    public User loginUser(String username, String password) {
        // 사용자 찾기
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // 비밀번호 검증
        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new RuntimeException("Invalid password");
        }

        return user;
    }

    @Override
    public String generateToken(User user) {

        String token = jwtTokenGenerator.generateToken(user);
        // Kafka를 통해 토큰 이벤트 발행

        tokenEventPublisher.publishTokenCreated(token, user);
        return token;

    }
}