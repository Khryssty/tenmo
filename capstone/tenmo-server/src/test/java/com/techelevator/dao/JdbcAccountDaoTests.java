package com.techelevator.dao;

import com.techelevator.tenmo.dao.JdbcAccountDao;
import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.Transfer;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.jdbc.core.JdbcTemplate;

import java.math.BigDecimal;

public class JdbcAccountDaoTests extends BaseDaoTests {

   private Account account1 = new Account(2003, 1003, new BigDecimal(1000.00));
   private Account account2 = new Account(2001, 1001, new BigDecimal(900.00));
   private Account account3 = new Account(2002, 1002, new BigDecimal(1100.00));

   private JdbcAccountDao sut;

   @Before
   public void setup() {
      JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
      sut = new JdbcAccountDao(jdbcTemplate);
   }

   @Test
   public void assert_get_account_returns_correct_value() {
      Account actual = sut.getAccount(1003);
      System.out.println(actual.toString());
      assertAccountsMatch(account1, actual);
   }

   @Test
   public void assert_get_account_returns_null_when_invalid() {
      Account actual = sut.getAccount(1099);
      Assert.assertNull(actual);
   }

   @Test
   public void assert_update_balances_updates_when_valid() {
      Transfer transfer = new Transfer(3005,1,1,2001,2002,new BigDecimal(100));
      sut.updateBalances(transfer);

      Account actualAccountFrom = sut.getAccount(1001);
      Account actualAccountTo = sut.getAccount(1002);

      assertAccountsMatch(account2, actualAccountFrom);
      assertAccountsMatch(account3, actualAccountTo);
   }

   private void assertAccountsMatch(Account expected, Account actual) {
      Assert.assertEquals(expected.getAccount_id(), actual.getAccount_id());
      Assert.assertEquals(expected.getUser_id(), actual.getUser_id());
      Assert.assertEquals(expected.getBalance().stripTrailingZeros(), actual.getBalance().stripTrailingZeros());
   }
}
