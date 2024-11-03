package com.sion.javamsaaccount.reqDTO;

import com.sion.javamsaaccount.model.AccountType;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.PrePersist;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class CreateAccountRequest {
    private Long userId;
    private String accountName;
    private String bankName;

    @Enumerated(EnumType.STRING)
    private AccountType accountType;
    private BigDecimal initialBalance;

    @PrePersist
    public void setDefaults() {
        if (initialBalance == null) {
            initialBalance = BigDecimal.ZERO;
        }
    }
}