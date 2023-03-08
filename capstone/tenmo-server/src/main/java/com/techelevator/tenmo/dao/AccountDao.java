package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Account;

import java.math.BigDecimal;

public interface AccountDao {

    Account viewBalance(int account_id);

//    void addToBalance(int account_id, BigDecimal amount);
//
//    void deductToBalance(int account_id, BigDecimal amount);

    void updateBalances(int fromAccount, int toAccount, BigDecimal amount);
}
