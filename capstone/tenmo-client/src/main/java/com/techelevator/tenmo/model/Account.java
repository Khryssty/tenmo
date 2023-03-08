package com.techelevator.tenmo.model;

import java.math.BigDecimal;
import java.util.Objects;

public class Account {

   private int account_id;
   private int user_id;
   private BigDecimal balance;

   @Override
   public String toString() {
      return "Account{" +
              "account_id=" + account_id +
              ", user_id=" + user_id +
              ", balance=" + balance +
              '}';
   }

   @Override
   public boolean equals(Object o) {
      if (this == o) return true;
      if (o == null || getClass() != o.getClass()) return false;
      Account account = (Account) o;
      return account_id == account.account_id && user_id == account.user_id && balance.equals(account.balance);
   }

   @Override
   public int hashCode() {
      return Objects.hash(account_id, user_id, balance);
   }

   public int getAccount_id() {
      return account_id;
   }

   public void setAccount_id(int account_id) {
      this.account_id = account_id;
   }

   public int getUser_id() {
      return user_id;
   }

   public void setUser_id(int user_id) {
      this.user_id = user_id;
   }

   public BigDecimal getBalance() {
      return balance;
   }

   public void setBalance(BigDecimal balance) {
      this.balance = balance;
   }
}
