package com.techelevator.tenmo.model;

import java.math.BigDecimal;
import java.util.Objects;

public class Transfer {

   private int transfer_id, account_from, account_to;
   //These may need to be changed to ID
   private TransferStatus transferStatus;
   private TransferType transferType;
   private BigDecimal amount;

   @Override
   public String toString() {
      return "Transfer{" +
              "transfer_id=" + transfer_id +
              ", account_from=" + account_from +
              ", account_to=" + account_to +
              ", transferStatus=" + transferStatus +
              ", transferType=" + transferType +
              ", amount=" + amount +
              '}';
   }

   @Override
   public boolean equals(Object o) {
      if (this == o) return true;
      if (o == null || getClass() != o.getClass()) return false;
      Transfer transfer = (Transfer) o;
      return transfer_id == transfer.transfer_id && account_from == transfer.account_from && account_to == transfer.account_to && transferStatus.equals(transfer.transferStatus) && transferType.equals(transfer.transferType) && amount.equals(transfer.amount);
   }

   @Override
   public int hashCode() {
      return Objects.hash(transfer_id, account_from, account_to, transferStatus, transferType, amount);
   }

   public int getTransfer_id() {
      return transfer_id;
   }

   public void setTransfer_id(int transfer_id) {
      this.transfer_id = transfer_id;
   }

   public int getAccount_from() {
      return account_from;
   }

   public void setAccount_from(int account_from) {
      this.account_from = account_from;
   }

   public int getAccount_to() {
      return account_to;
   }

   public void setAccount_to(int account_to) {
      this.account_to = account_to;
   }

   public TransferStatus getTransferStatus() {
      return transferStatus;
   }

   public void setTransferStatus(TransferStatus transferStatus) {
      this.transferStatus = transferStatus;
   }

   public TransferType getTransferType() {
      return transferType;
   }

   public void setTransferType(TransferType transferType) {
      this.transferType = transferType;
   }

   public BigDecimal getAmount() {
      return amount;
   }

   public void setAmount(BigDecimal amount) {
      this.amount = amount;
   }
}
