package com.sion.javamsaaccount.repo;

import com.sion.javamsaaccount.model.Account;
import com.sion.javamsaaccount.model.AccountType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {
    boolean existsByAccountNumber(String accountNumber);

    Optional<Account> findByAccountNumber(String accountNumber);

    @Query("SELECT a FROM Account a WHERE a.userId = :userId AND a.isActive = true")
    List<Account> findByUserIdAndIsActiveTrue(@Param("userId") Long userId);

    @Query("SELECT COUNT(a) > 0 FROM Account a WHERE a.userId = :userId " +
            "AND a.accountType = :accountType AND a.isActive = true")
    boolean hasAccountOfType(@Param("userId") Long userId,
                             @Param("accountType") AccountType accountType);


}
