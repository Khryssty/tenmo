package com.techelevator.dao;

import com.techelevator.tenmo.dao.JdbcAccountDao;
import com.techelevator.tenmo.model.Account;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.jdbc.core.JdbcTemplate;

import java.math.BigDecimal;

public class JdbcAccoutDaoTests extends BaseDaoTests {
   private Account account1 = new Account(2001, 1001, new BigDecimal("1000.00"));

   private JdbcAccountDao sut;

   @Before
   public void setup() {
      JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
      sut = new JdbcAccountDao(jdbcTemplate);
   }

   @Test
   public void assert_get_account_returns_correct_value() {
      Account balance = sut.getAccount(1001);
      System.out.println(balance.toString());
      Assert.assertEquals(balance.getBalance() ,account1.getBalance());
   }

   @Test
   public void assert_view() {

   }

}
