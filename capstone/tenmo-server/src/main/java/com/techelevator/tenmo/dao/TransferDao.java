package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Transfer;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransferDao {

    List<Transfer> findAll(int id);

    Transfer getTransferById(int transfer_id);

    Transfer sendTransfer(Transfer transfer);

    Transfer requestTransfer(Transfer transfer, int user_id);

    List<Transfer> viewPendingTransfers(int transfer_status_id);

    Transfer approveTransfer(Transfer transfer, int transfer_id);

    Transfer rejectTransfer(Transfer transfer, int transfer_id);


}
