package com.techelevator.tenmo.services;

import com.techelevator.tenmo.dao.AccountDao;
import com.techelevator.tenmo.dao.TransferDao;
import com.techelevator.tenmo.model.Transfer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.util.EmptyStackException;
import java.util.List;

@Service
public class TransferService {

    private static final int PENDING = 1;
    private static final int APPROVED = 2;
    private static final int REJECTED = 3;
    private static final int REQUEST = 1;
    private static final int SEND = 2;

    @Autowired
    private TransferDao transferDao;

    @Autowired
    private AccountDao accountDao;

    public TransferService(TransferDao transferDao, AccountDao accountDao) {
        this.transferDao = transferDao;
        this.accountDao = accountDao;
    }

    /**
     * Gets all transfers to and from the given id
     * @param id given id
     * @return list of all transfers
     */

    public List<Transfer> findAll(int id) {
        List<Transfer> transfers = null;
        try {
            transfers = transferDao.findAll(id);
        } catch (NullPointerException  | EmptyStackException e) {
            e.printStackTrace();
        }
        return transfers;
    }


    /*
     * 6. As an authenticated user of the system, I need to be able to retrieve the details of any transfer based upon the transfer ID.
     * View transfer details using transfer_id
     */


    public Transfer getTransferById(int id) {
        Transfer transfer = null;
        try {
            transfer = transferDao.getTransferById(id);
        } catch (NullPointerException  | EmptyStackException e) {
            e.printStackTrace();
        }

        return transfer;
    }


    /*
     * 4. As an authenticated user of the system, I need to be able to *send* a transfer of a specific amount of TE Bucks to a registered user.
     * 7. As an authenticated user of the system, I need to be able to *request* a transfer of a specific amount of TE Bucks from another registered user.
     * @body transfer
     * passes the account_from as user_id if it's sending a transfer
     * passes the account_to as user_id if it's requesting for a transfer
     */

    public Transfer sendTransfer(Transfer transfer) {
        Transfer createTransfer = null;
        try {
            createTransfer = new Transfer();
            if (transfer.getTransfer_type_id() == SEND) {
                createTransfer = transferDao.sendTransfer(transfer);
                accountDao.updateBalances(transfer);
            } else if (transfer.getTransfer_type_id() == REQUEST) {
                createTransfer = transferDao.sendTransfer(transfer);
            }
        } catch (NullPointerException  | EmptyStackException e) {
            e.printStackTrace();
        }
        return createTransfer;
    }


    /*
     * 8. As an authenticated user of the system, I need to be able to see my *Pending* transfers.
     */


    public List<Transfer> viewPendingTransfer (int id) {
        List<Transfer> listOfPending = null;
        try {
            listOfPending = transferDao.viewPendingTransfers(id);
        } catch (NullPointerException  | EmptyStackException e) {
            e.printStackTrace();
        }

        return listOfPending;
    }



    /*
     *  9. As an authenticated user of the system, I need to be able to either approve or reject a Request Transfer.
     */


    public Transfer approveOrRejectTransfer(Transfer transfer,int id) {
        Transfer updatedTransfer = null;
        try {
            updatedTransfer = new Transfer();
            if (transfer.getTransfer_status_id() == APPROVED) {
                updatedTransfer = transferDao.approveTransfer(transfer, id);
                //add and subtract
                accountDao.updateBalances(transfer);
            } else if (transfer.getTransfer_status_id() == REJECTED) {
                updatedTransfer = transferDao.rejectTransfer(transfer, id);
            }
        } catch (NullPointerException  | EmptyStackException e) {
            e.printStackTrace();
        }
        return updatedTransfer;
    }

}
