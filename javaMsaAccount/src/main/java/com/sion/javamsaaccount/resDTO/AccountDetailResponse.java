package com.sion.javamsaaccount.resDTO;

import com.sion.javamsaaccount.model.AccountDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AccountDetailResponse {
    private AccountDTO account;
//    private List<TransactionDTO> transactions;
}