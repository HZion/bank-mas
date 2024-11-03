package com.sion.javamsaaccount.service;

import org.springframework.stereotype.Component;

import java.util.Random;

@Component
public class AccountNumberGenerator {
    private static final String BANK_CODE = "001"; // 은행 코드

    public String generate() {
        // 계좌번호 형식: 은행코드(3) + 랜덤숫자(11)
        String randomPart = String.format("%011d",
                new Random().nextInt(100000000));
        return BANK_CODE + randomPart;
    }
}