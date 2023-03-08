package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Account;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class JdbcAccountDao implements AccountDao{
    private JdbcTemplate jdbcTemplate;

    public JdbcAccountDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public BigDecimal viewBalance(int user_id) {
        BigDecimal result = jdbcTemplate.queryForObject("SELECT balance from account WHERE user_id = ?", BigDecimal.class, user_id);
        return result;
    }

//    @Override
//    public void addToBalance(int account_id, BigDecimal amount) {
//       jdbcTemplate.update("UPDATE account SET balance += ? WHERE account_id = ?", amount, account_id);
//    }
//
//    @Override
//    public void deductToBalance(int account_id, BigDecimal amount) {
//        jdbcTemplate.update("UPDATE account SET balance -= ? WHERE account_id = ?", amount, account_id);
//    }

    @Override
    public void updateBalances(int fromAccount, int toAccount, BigDecimal amount) {
        String sql = "START TRANSACTION;" +
                    "UPDATE account SET balance = balance + ? WHERE account_id = ?;" +
                    "UPDATE account SET balance = balance - ? WHERE account_id = ?;" +
                    "COMMIT;";
        jdbcTemplate.update(sql, amount, toAccount, amount, fromAccount);
    }

    private Account mapRowToAccount(SqlRowSet rowSet) {
        Account account = new Account();
        account.setAccount_id(rowSet.getInt("account_id"));
        account.setUser_id(rowSet.getInt("user_id"));
        account.setBalance(rowSet.getBigDecimal("balance"));

        return account;
    }
}
