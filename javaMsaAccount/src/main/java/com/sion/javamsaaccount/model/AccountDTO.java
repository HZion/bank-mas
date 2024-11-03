package com.sion.javamsaaccount.model;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@Getter
@Setter
public class AccountDTO {
    private Long id;
    private Long userid; //
    private String accountNumber;
    private String accountName;
    private String bankName;
    private AccountType accountType;
    private BigDecimal balance;
    private String currency;
    private LocalDateTime createdAt;
}