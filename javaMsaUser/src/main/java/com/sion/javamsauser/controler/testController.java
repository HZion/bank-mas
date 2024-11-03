package com.sion.javamsauser.controler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.UUID;


@RestController
@RequestMapping("/healthz")
@RequiredArgsConstructor
@Slf4j
public class testController {

    @GetMapping("/ready")
    public ResponseEntity<String> healthCheck() {
        return ResponseEntity.ok("UP");
    }
}