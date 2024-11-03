package com.sion.javamsaaccount.exceptionCase;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.FORBIDDEN)
public class UnauthorizedAccountAccessException extends RuntimeException {
    public UnauthorizedAccountAccessException(Long userId, Long accountId) {
        super("User " + userId + " is not authorized to access account " + accountId);
    }
}