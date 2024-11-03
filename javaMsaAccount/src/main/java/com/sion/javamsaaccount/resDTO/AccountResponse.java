package com.sion.javamsaaccount.resDTO;

import com.sion.javamsaaccount.model.Account;
import com.sion.javamsaaccount.model.AccountType;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
public class AccountResponse {
    private Long id;
    private String accountNumber;
    private String accountName;
    private String bankName;
    private AccountType accountType;
    private BigDecimal balance;
    private String currency;
    private String message;           // 성공/실패 메시지
    private String redirectUrl;       // 리다이렉트 URL

    public static AccountResponse from(Account account) {
        return AccountResponse.builder()
                .id(account.getId())
                .accountNumber(account.getAccountNumber())
                .accountName(account.getAccountName())
                .bankName(account.getBankName())
                .accountType(account.getAccountType())
                .balance(account.getBalance())
                .currency(account.getCurrency())
                .message("계좌가 성공적으로 생성되었습니다.")
                .redirectUrl("/home")  // 성공 시 홈으로 리다이렉트
                .build();
    }
}