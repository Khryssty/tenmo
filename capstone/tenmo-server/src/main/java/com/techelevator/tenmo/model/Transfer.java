package com.techelevator.tenmo.model;

import javax.validation.constraints.*;
import java.math.BigDecimal;

public class Transfer {

    private int transfer_id;

    @Min(value = 1, message = "Either 1 for SEND or 2 for REQUEST")
    @Max(value = 2, message = "Either 1 for SEND or 2 for REQUEST")
    private int transfer_type_id;

    @Min(value = 1, message = "Either 1 for PENDING, 2 for APPROVED or 3 for REJECTED")
    @Max(value = 3, message = "Either 1 for PENDING, 2 for APPROVED or 3 for REJECTED")
    private int transfer_status_id;

    @Positive(message = "Please provide a valid FROM account number")
    private int account_from;

    @Positive(message = "Please provide a valid TO account number")
    private int account_to;

    @DecimalMin(value = "0", message = "Amount should be greater than 0")
    private BigDecimal amount;

    public Transfer() {
    }

    public Transfer(int transfer_id, int transfer_type_id, int transfer_status_id, int account_from, int account_to, BigDecimal amount) {
        this.transfer_id = transfer_id;
        this.transfer_type_id = transfer_type_id;
        this.transfer_status_id = transfer_status_id;
        this.account_from = account_from;
        this.account_to = account_to;
        this.amount = amount;
    }

    public int getTransfer_id() {
        return transfer_id;
    }

    public void setTransfer_id(int transfer_id) {
        this.transfer_id = transfer_id;
    }

    public int getTransfer_type_id() {
        return transfer_type_id;
    }

    public void setTransfer_type_id(int transfer_type_id) {
        this.transfer_type_id = transfer_type_id;
    }

    public int getTransfer_status_id() {
        return transfer_status_id;
    }

    public void setTransfer_status_id(int transfer_status_id) {
        this.transfer_status_id = transfer_status_id;
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

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }

    @Override
    public String toString() {
        return super.toString();
    }
}
