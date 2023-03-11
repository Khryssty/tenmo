package com.techelevator.tenmo.services;

import com.techelevator.tenmo.model.Transfer;

import java.security.Principal;
import java.util.List;

public interface TransferService {

   List<Transfer> findAll(int id);
   Transfer getTransferById(int id);
   Transfer sendTransfer(Transfer transfer);
   List<Transfer> viewPendingTransfer (int id);
   Transfer approveOrRejectTransfer(Transfer transfer,int id);
}
