package com.techelevator.tenmo.services;

import com.techelevator.tenmo.dao.AccountDao;
import com.techelevator.tenmo.dao.TransferDao;
import com.techelevator.tenmo.dao.UserDao;
import com.techelevator.tenmo.model.Transfer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.EmptyStackException;
import java.util.List;

@Service
public class TransferServiceImplementation implements TransferService{

    private static final int PENDING = 1;
    private static final int APPROVED = 2;
    private static final int REJECTED = 3;
    private static final int REQUEST = 1;
    private static final int SEND = 2;


    private TransferDao transferDao;
    private AccountDao accountDao;
    private UserDao userDao;
    private Principal authUser;

    public TransferServiceImplementation(TransferDao transferDao, AccountDao accountDao, UserDao userDao) {
        this.transferDao = transferDao;
        this.accountDao = accountDao;
        this.userDao = userDao;
    }


    @Override
    public List<Transfer> findAll(int id) {
        List<Transfer> transfers = null;
        try {
            transfers = transferDao.findAll(id);
        } catch (NullPointerException  | EmptyStackException e) {
            e.getCause();
        }
        return transfers;
    }




    @Override
    public Transfer getTransferById(int id) {
        Transfer transfer = null;
        try {
            transfer = transferDao.getTransferById(id);
        } catch (NullPointerException  | EmptyStackException e) {
            e.getCause();
        }

        return transfer;
    }


     @Override
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
            e.getCause();
        }
        return createTransfer;
    }



    @Override
    public List<Transfer> viewPendingTransfer (int id) {
        List<Transfer> listOfPending = null;
        try {
            listOfPending = transferDao.viewPendingTransfers(id);
        } catch (NullPointerException  | EmptyStackException e) {
            e.getCause();
        }

        return listOfPending;
    }





    @Override
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
            e.getCause();
        }
        return updatedTransfer;
    }

}
