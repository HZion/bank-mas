package com.sion.javamsaaccount.service;

import com.sion.javamsaaccount.model.Account;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
@Slf4j
public class AccountCacheService {
    private final RedisTemplate<String, Object> redisTemplate;
    private static final String ACCOUNT_KEY_PREFIX = "account:";
    private static final String USER_ACCOUNTS_KEY_PREFIX = "user:accounts:";
    private static final long CACHE_DURATION = 3600; // 1시간

    public void saveAccount(Account account) {
        try {
            String accountKey = getAccountKey(account.getId());
            redisTemplate.opsForValue().set(accountKey, account, CACHE_DURATION, TimeUnit.SECONDS);

            // 사용자의 계좌 목록에도 추가
            String userAccountsKey = getUserAccountsKey(account.getUserId());
            redisTemplate.opsForSet().add(userAccountsKey, account.getId());
            redisTemplate.expire(userAccountsKey, CACHE_DURATION, TimeUnit.SECONDS);

            log.debug("Account cached: {}", accountKey);
        } catch (Exception e) {
            log.error("Error caching account: {}", e.getMessage());
        }
    }

    // 사용자의 모든 계좌 캐싱
    public void saveUserAccounts(Long userId, List<Account> accounts) {
        try {
            String userAccountsKey = getUserAccountsKey(userId);

            // 기존 캐시 삭제
            redisTemplate.delete(userAccountsKey);

            // 새로운 데이터 캐싱
            accounts.forEach(account -> {
                // 개별 계좌 저장
                String accountKey = getAccountKey(account.getId());
                redisTemplate.opsForValue().set(accountKey, account, CACHE_DURATION, TimeUnit.SECONDS);

                // 사용자의 계좌 목록에 추가
                redisTemplate.opsForSet().add(userAccountsKey, account.getId());
            });

            redisTemplate.expire(userAccountsKey, CACHE_DURATION, TimeUnit.SECONDS);
            log.debug("User accounts cached successfully for user: {}", userId);
        } catch (Exception e) {
            log.error("Failed to cache user accounts: {}", e.getMessage());
        }
    }

    public Optional<Account> getAccount(Long accountId) {
        try {
            String accountKey = getAccountKey(accountId);
            Account account = (Account) redisTemplate.opsForValue().get(accountKey);
            return Optional.ofNullable(account);
        } catch (Exception e) {
            log.error("Error fetching account from cache: {}", e.getMessage());
            return Optional.empty();
        }
    }

    public List<Account> getUserAccounts(Long userId) {
        try {
            String userAccountsKey = getUserAccountsKey(userId);
            Set<Object> accountIds = redisTemplate.opsForSet().members(userAccountsKey);

            if (accountIds == null || accountIds.isEmpty()) {
                return Collections.emptyList();
            }

            List<Account> accounts = new ArrayList<>();
            for (Object accountId : accountIds) {
                getAccount(((Integer) accountId).longValue())
                        .ifPresent(accounts::add);
            }
            return accounts;
        } catch (Exception e) {
            log.error("Error fetching user accounts from cache: {}", e.getMessage());
            return Collections.emptyList();
        }
    }

    public void deleteAccount(Long accountId) {
        try {
            String accountKey = getAccountKey(accountId);
            Account account = (Account) redisTemplate.opsForValue().get(accountKey);

            if (account != null) {
                String userAccountsKey = getUserAccountsKey(account.getUserId());
                redisTemplate.opsForSet().remove(userAccountsKey, accountId);
                redisTemplate.delete(accountKey);
            }
        } catch (Exception e) {
            log.error("Error deleting account from cache: {}", e.getMessage());
        }
    }

    private String getAccountKey(Long accountId) {
        return ACCOUNT_KEY_PREFIX + accountId;
    }

    private String getUserAccountsKey(Long userId) {
        return USER_ACCOUNTS_KEY_PREFIX + userId;
    }
}