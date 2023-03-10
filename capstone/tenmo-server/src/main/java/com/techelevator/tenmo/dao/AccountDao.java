package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.Transfer;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;

@Repository
public interface AccountDao {

    Account getAccount(int account_id);

//    void addToBalance(int account_id, BigDecimal amount);
//
//    void deductToBalance(int account_id, BigDecimal amount);

    void updateBalances(Transfer transfer);
}
