package com.sion.javamsaaccount.service;

import com.sion.javamsaaccount.reqDTO.CreateAccountRequest;
import com.sion.javamsaaccount.resDTO.AccountDetailResponse;
import com.sion.javamsaaccount.resDTO.AccountResponse;
import com.sion.javamsaaccount.resDTO.AccountSummaryResponse;


public interface AccountService {
    public AccountResponse createAccount(CreateAccountRequest request);
    public AccountSummaryResponse getUserAccountSummary(Long userId);

}
