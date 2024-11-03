package com.sion.javamsaaccount.service;

import com.sion.javamsaaccount.exceptionCase.AccountNotFoundException;
import com.sion.javamsaaccount.model.*;
import com.sion.javamsaaccount.repo.AccountRepository;
import com.sion.javamsaaccount.reqDTO.CreateAccountRequest;
import com.sion.javamsaaccount.resDTO.AccountDetailResponse;
import com.sion.javamsaaccount.resDTO.AccountResponse;
import com.sion.javamsaaccount.resDTO.AccountSummaryResponse;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
@Service
@RequiredArgsConstructor
@Slf4j
public class AccountServiceImple implements AccountService {
    private final AccountRepository accountRepository;
    private final AccountNumberGenerator accountNumberGenerator;
    private final AccountCacheService accountCacheService;

    @Override
    @Transactional
    public AccountResponse createAccount(CreateAccountRequest request) {
        // 1. 계좌 생성
        Account account = Account.builder()
                .userId(request.getUserId())
                .accountNumber(accountNumberGenerator.generate())
                .accountName(request.getAccountName())
                .bankName(request.getBankName())
                .accountType(request.getAccountType())
                .balance(request.getInitialBalance() != null ?
                        request.getInitialBalance() : BigDecimal.ZERO)
                .currency("KRW")
                .isActive(true)
                .build();

        // 2. DB에 저장
        Account savedAccount = accountRepository.save(account);
        log.debug("Account saved to DB: {}", savedAccount.getAccountNumber());

        // 3. Redis에 캐시 저장
        try {
            accountCacheService.saveAccount(savedAccount);
            log.debug("Account cached successfully: {}", savedAccount.getAccountNumber());
        } catch (Exception e) {
            log.error("Failed to cache account: {}", e.getMessage());
            // 캐시 저장 실패는 전체 트랜잭션을 실패시키지 않음
        }

        return AccountResponse.from(savedAccount);
    }

    @Override
    @Transactional(readOnly = true)
    public AccountSummaryResponse getUserAccountSummary(Long userId) {
        List<Account> accounts;

        try {
            // 1. Redis에서 먼저 조회
            accounts = accountCacheService.getUserAccounts(userId);
            if (!accounts.isEmpty()) {
                log.debug("Cache hit for user accounts: {}", userId);
            } else {
                // 2. 캐시에 없으면 DB에서 조회
                log.debug("Cache miss for user accounts: {}", userId);
                accounts = accountRepository.findByUserIdAndIsActiveTrue(userId);

                if (!accounts.isEmpty()) {
                    // DB에서 조회한 데이터를 캐시에 저장
                    accountCacheService.saveUserAccounts(userId, accounts);
                    log.debug("User accounts cached for: {}", userId);
                } else {
                    log.debug("No accounts found for user: {}", userId);
                }
            }
        } catch (Exception e) {
            // 캐시 조회/저장 실패 시 DB에서 직접 조회
            log.error("Cache operation failed, falling back to DB: {}", e.getMessage());
            accounts = accountRepository.findByUserIdAndIsActiveTrue(userId);
        }

        // 3. 응답 생성
        List<AccountDTO> accountDtos = accounts.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());

        AccountStats stats = calculateStats(accounts);

        return AccountSummaryResponse.builder()
                .accounts(accountDtos)
                .stats(stats)
                .build();
    }

    private AccountDTO convertToDto(Account account) {
        return AccountDTO.builder()
                .id(account.getId())
                .accountNumber(maskAccountNumber(account.getAccountNumber()))
                .accountName(account.getAccountName())
                .bankName(account.getBankName())
                .accountType(account.getAccountType())
                .balance(account.getBalance())
                .currency(account.getCurrency())

                .build();
    }

    private AccountStats calculateStats(List<Account> accounts) {
        BigDecimal totalBalance = accounts.stream()
                .map(Account::getBalance)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        Map<AccountType, Integer> distribution = accounts.stream()
                .collect(Collectors.groupingBy(
                        Account::getAccountType,
                        Collectors.collectingAndThen(Collectors.counting(), Long::intValue)
                ));

        return AccountStats.builder()
                .totalBalance(totalBalance)
                .totalAccounts(accounts.size())
                .accountTypeDistribution(distribution)
                .build();
    }

    private String maskAccountNumber(String accountNumber) {
        if (accountNumber == null || accountNumber.length() < 8) {
            return accountNumber;
        }
        return accountNumber.substring(0, 4) + "****" +
                accountNumber.substring(accountNumber.length() - 4);
    }
}