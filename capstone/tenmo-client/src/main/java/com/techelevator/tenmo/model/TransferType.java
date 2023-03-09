package com.techelevator.tenmo.model;

import java.util.Objects;

public class TransferType {

   private int transfer_id;
   private String transfer_type_desc;

   public static final int REQUEST_ID = 1;
   public static final int SEND_ID = 2;

   public TransferType(){}
   public TransferType(int transfer_id, String transfer_type_desc) {
      this.transfer_id = transfer_id;
      this.transfer_type_desc = transfer_type_desc;
   }

   @Override
   public String toString() {
      return "TransferType{" +
              "transfer_id=" + transfer_id +
              ", transfer_type_desc=" + transfer_type_desc +
              '}';
   }

   @Override
   public boolean equals(Object o) {
      if (this == o) return true;
      if (o == null || getClass() != o.getClass()) return false;
      TransferType that = (TransferType) o;
      return transfer_id == that.transfer_id && transfer_type_desc.equals(that.transfer_type_desc);
   }

   @Override
   public int hashCode() {
      return Objects.hash(transfer_id, transfer_type_desc);
   }

   public int getTransfer_id() {
      return transfer_id;
   }

   public void setTransfer_id(int transfer_id) {
      this.transfer_id = transfer_id;
   }

   public String getTransfer_type_desc() {
      return transfer_type_desc;
   }

   public void setTransfer_type_desc(String transfer_type_desc) {
      this.transfer_type_desc = transfer_type_desc;
   }
}
