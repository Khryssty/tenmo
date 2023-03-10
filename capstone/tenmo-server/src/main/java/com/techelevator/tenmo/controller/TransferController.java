package com.techelevator.tenmo.controller;

import com.techelevator.tenmo.dao.AccountDao;
import com.techelevator.tenmo.dao.TransferDao;
import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.services.TransferService;
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

    private TransferService transferService;

    public TransferController(TransferService transferService) {
        this.transferService = transferService;
    }

    /**
     * Gets all transfers to and from the given id
     * @param id given id
     * @return list of all transfers
     */
    @RequestMapping(path = "/{id}/account", method = RequestMethod.GET)
    public List<Transfer> findAll(@PathVariable int id) {
        return transferService.findAll(id);
    }

    /*
     * 6. As an authenticated user of the system, I need to be able to retrieve the details of any transfer based upon the transfer ID.
     * View transfer details using transfer_id
     */

    @RequestMapping(path = "/{id}", method = RequestMethod.GET)
    public Transfer getTransferById(@PathVariable int id) {
        return transferService.getTransferById(id);
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
        return transferService.sendTransfer(transfer);
    }


    /*
     * 8. As an authenticated user of the system, I need to be able to see my *Pending* transfers.
     */

    @RequestMapping(path = "/account/{id}", method = RequestMethod.GET)
    public List<Transfer> viewPendingTransfer (@PathVariable int id) {
        return transferService.viewPendingTransfer(id);
    }


    /*
     *  9. As an authenticated user of the system, I need to be able to either approve or reject a Request Transfer.
     */

    @RequestMapping(path = "/{id}", method = RequestMethod.PUT)
    public Transfer approveOrRejectTransfer(@Valid @RequestBody Transfer transfer, @PathVariable int id) {
        return transferService.approveOrRejectTransfer(transfer, id);
    }


}
