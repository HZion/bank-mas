package com.sion.javamsaaccount.model;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Map;

@Data
@Builder
public class AccountStats {
    private BigDecimal totalBalance;
    private int totalAccounts;
    private Map<AccountType, Integer> accountTypeDistribution;
}