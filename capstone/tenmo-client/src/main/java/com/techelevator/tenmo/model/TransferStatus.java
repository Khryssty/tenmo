package com.techelevator.tenmo.model;

import java.util.Objects;

public class TransferStatus {
   private int transfer_status_id;
   private String transfer_status_desc;

   public static final int PENDING_ID = 1;
   public static final int APPROVED_ID = 2;
   public static final int REJECTED_ID = 3;

   public TransferStatus(){}
   public TransferStatus(int transfer_status_id, String transfer_status_desc) {
      this.transfer_status_id = transfer_status_id;
      this.transfer_status_desc = transfer_status_desc;
   }

   @Override
   public String toString() {
      return "TransferStatus{" +
              "transfer_status_id=" + transfer_status_id +
              ", transfer_status_desc='" + transfer_status_desc + '\'' +
              '}';
   }

   @Override
   public boolean equals(Object o) {
      if (this == o) return true;
      if (o == null || getClass() != o.getClass()) return false;
      TransferStatus that = (TransferStatus) o;
      return transfer_status_id == that.transfer_status_id && transfer_status_desc.equals(that.transfer_status_desc);
   }

   @Override
   public int hashCode() {
      return Objects.hash(transfer_status_id, transfer_status_desc);
   }

   public int getTransfer_status_id() {
      return transfer_status_id;
   }

   public void setTransfer_status_id(int transfer_status_id) {
      this.transfer_status_id = transfer_status_id;
   }

   public String getTransfer_status_desc() {
      return transfer_status_desc;
   }

   public void setTransfer_status_desc(String transfer_status_desc) {
      this.transfer_status_desc = transfer_status_desc;
   }
}
