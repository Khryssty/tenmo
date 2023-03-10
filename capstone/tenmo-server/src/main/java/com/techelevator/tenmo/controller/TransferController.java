package com.techelevator.tenmo.controller;

import com.techelevator.tenmo.dao.AccountDao;
import com.techelevator.tenmo.dao.TransferDao;
import com.techelevator.tenmo.model.Transfer;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.lang.ref.PhantomReference;
import java.util.List;

@RestController
@PreAuthorize("isAuthenticated()")
@RequestMapping(path = "/transfer")

public class TransferController {

    private static final int PENDING = 1;
    private static final int APPROVED = 2;
    private static final int REJECTED = 3;
    private static final int REQUEST = 1;
    private static final int SEND = 2;
    private TransferDao transferDao;
    private AccountDao accountDao;

    public TransferController(TransferDao transferDao, AccountDao accountDao) {
        this.transferDao = transferDao;
        this.accountDao = accountDao;
    }

    /**
     * Gets all transfers to and from the given id
     * @param id given id
     * @return list of all transfers
     */
    @RequestMapping(path = "/{id}/account", method = RequestMethod.GET)
    public List<Transfer> findAll(@PathVariable int id) {
        List<Transfer> transfers = transferDao.findAll(id);
        if(transfers == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No transfers for this account");
        } else {
            return transfers;
        }
    }

    /*
    * 6. As an authenticated user of the system, I need to be able to retrieve the details of any transfer based upon the transfer ID.
    * View transfer details using transfer_id
    */

    @RequestMapping(path = "/{id}", method = RequestMethod.GET)
    public Transfer getTransferById(@PathVariable int id) {
        Transfer transfer = transferDao.getTransferById(id);
        if(transfer == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Transfer transaction cannot be found.");
        } else {
            return transfer;
        }
    }

    /*
    * 4. As an authenticated user of the system, I need to be able to *send* a transfer of a specific amount of TE Bucks to a registered user.
    * 7. As an authenticated user of the system, I need to be able to *request* a transfer of a specific amount of TE Bucks from another registered user.
    * @body transfer
    * passes the account_from as user_id if it's sending a transfer
    * passes the account_to as user_id if it's requesting for a transfer
    */

    @ResponseStatus(HttpStatus.CREATED)
    @RequestMapping(method = RequestMethod.POST)
    public Transfer sendTransfer(@Valid @RequestBody Transfer transfer) {
        Transfer createTransfer = new Transfer();
        if (transfer.getTransfer_type_id() == SEND) {
            createTransfer = transferDao.sendTransfer(transfer);
            accountDao.updateBalances(transfer);
        } else if (transfer.getTransfer_type_id() == REQUEST) {
            createTransfer = transferDao.sendTransfer(transfer);
        }
        return createTransfer;
    }


    /*
    * 8. As an authenticated user of the system, I need to be able to see my *Pending* transfers.
    */

    @RequestMapping(path = "/account/{id}", method = RequestMethod.GET)
    public List<Transfer> viewPendingTransfer (@PathVariable int id) {
        List<Transfer> listOfPending = transferDao.viewPendingTransfers(id);

        if (listOfPending == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Unable to find transfer transactions that are pending.");
        } else {
            return listOfPending;
        }
    }


    /*
    *  9. As an authenticated user of the system, I need to be able to either approve or reject a Request Transfer.
    */


    @RequestMapping(path = "/{id}", method = RequestMethod.PUT)
    public Transfer approveOrRejectTransfer(@Valid @RequestBody Transfer transfer, @PathVariable int id) {
        Transfer updatedTransfer = new Transfer();
        if (transfer.getTransfer_status_id() == APPROVED) {
            updatedTransfer = transferDao.approveTransfer(transfer, id);
            //add and subtract
            accountDao.updateBalances(transfer);
        } else if (transfer.getTransfer_status_id() == REJECTED) {
            updatedTransfer = transferDao.rejectTransfer(transfer, id);
        }
        return updatedTransfer;
    }


}
