package com.sion.javamsaaccount.resDTO;

import com.sion.javamsaaccount.model.AccountDTO;
import com.sion.javamsaaccount.model.AccountStats;
import lombok.Builder;
import lombok.Data;


import java.util.List;


@Data
@Builder
public class AccountSummaryResponse {
    private List<AccountDTO> accounts;
    private AccountStats stats;

}
