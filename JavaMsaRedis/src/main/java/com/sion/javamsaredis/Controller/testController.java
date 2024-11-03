package com.sion.javamsaredis.Controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


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