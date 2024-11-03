package com.sion.javamsaaccount.controller;

import com.sion.javamsaaccount.exceptionCase.UnauthorizedException;

import com.sion.javamsaaccount.reqDTO.CreateAccountRequest;
import com.sion.javamsaaccount.resDTO.AccountDetailResponse;
import com.sion.javamsaaccount.resDTO.AccountResponse;
import com.sion.javamsaaccount.resDTO.AccountSummaryResponse;
import com.sion.javamsaaccount.service.AccountService;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import com.sion.javamsaaccount.resDTO.AccountResponse;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/accounts")
@RequiredArgsConstructor
@Slf4j
public class AccountController {

    @Autowired
    private final AccountService accountService;


    @PostMapping
    public ResponseEntity<AccountResponse> createAccount(
            @RequestBody CreateAccountRequest request,
            HttpServletRequest httpRequest) {
        Long userId = (Long) httpRequest.getAttribute("userId");
        String username = (String) httpRequest.getAttribute("username");

        log.debug("Creating account for user: {} ({})", username, userId);

        request.setUserId(userId);

        try {
            AccountResponse response = accountService.createAccount(request);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Failed to create account: {}", e.getMessage());
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Failed to create account: " + e.getMessage()
            );
        }
    }

    @GetMapping("/my")
    public ResponseEntity<AccountSummaryResponse> getMyAccounts(
            HttpServletRequest httpRequest) {
        Long userId = (Long) httpRequest.getAttribute("userId");
        String username = (String) httpRequest.getAttribute("username");

        log.debug("Fetching accounts for user: {} ({})", username, userId);

        try {
            AccountSummaryResponse response = accountService.getUserAccountSummary(userId);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Failed to fetch accounts: {}", e.getMessage());
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    "Failed to fetch accounts: " + e.getMessage()
            );
        }
    }
//    @GetMapping("/{accountId}")
//    public ResponseEntity<AccountDetailResponse> getAccountDetail(
//            @PathVariable Long accountId,
//            HttpServletRequest request) {
//        Long userId = (Long) request.getAttribute("userId");
//        try {
//            AccountDetailResponse response = accountService.getAccountDetail(accountId);
//            // 계좌 소유자 확인
//            if (!response.getAccount().getUserid().equals(userId)) {
//                throw new UnauthorizedException("Not authorized to access this account");
//            }
//            return ResponseEntity.ok(response);
//        } catch (Exception e) {
//            log.error("Error fetching account detail: {}", e.getMessage());
//            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Account not found");
//        }
//    }

    @GetMapping("/actuator/health")
    public ResponseEntity<Map<String, String>> healthCheck() {
        Map<String, String> response = new HashMap<>();
        response.put("status", "UP");
        return ResponseEntity.ok(response);
    }
}