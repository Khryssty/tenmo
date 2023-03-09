package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.Transfer;
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
    public Account viewBalance(int user_id) {
        SqlRowSet result = jdbcTemplate.queryForRowSet("SELECT account_id, user_id, balance from account WHERE user_id = ?", user_id);
        if(result.next()) {
            return mapRowToAccount(result);
        } else {
            return null;
        }
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
    public void updateBalances(Transfer transfer) {
        String sql = "START TRANSACTION;" +
                    "UPDATE account SET balance = balance + ? WHERE account_id = ?;" +
                    "UPDATE account SET balance = balance - ? WHERE account_id = ?;" +
                    "COMMIT;";
        jdbcTemplate.update(sql, transfer.getAmount(), transfer.getAccount_to(), transfer.getAmount(), transfer.getAccount_from());
    }

    private Account mapRowToAccount(SqlRowSet rowSet) {
        Account account = new Account();
        account.setAccount_id(rowSet.getInt("account_id"));
        account.setUser_id(rowSet.getInt("user_id"));
        account.setBalance(rowSet.getBigDecimal("balance"));

        return account;
    }
}
