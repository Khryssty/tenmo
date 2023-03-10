package com.techelevator.tenmo.controller;

import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.services.TransferService;
import com.techelevator.tenmo.services.TransferServiceImplementation;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@PreAuthorize("isAuthenticated()")
@RequestMapping(path = "/transfer")

public class TransferController {

    private TransferService service;

    public TransferController(TransferService service) {
        this.service = service;
    }

    /**
     * Gets all transfers to and from the given id
     * @param id given id
     * @return list of all transfers
     */
    @RequestMapping(path = "/{id}/account", method = RequestMethod.GET)
    public List<Transfer> findAll(@PathVariable int id) {
        return service.findAll(id);
    }

    /**
     * Get the transfer with the given id
     * @param id transferId to retrieve
     * @return Transfer at id
     */
    @RequestMapping(path = "/{id}", method = RequestMethod.GET)
    public Transfer getTransferById(@PathVariable int id) {
        return service.getTransferById(id);
    }

    /**
     * Create a new transfer of type send
     * @param transfer new transfer
     * @return the created transfer
     */
    @ResponseStatus(HttpStatus.CREATED)
    @RequestMapping(method = RequestMethod.POST)
    public Transfer sendTransfer(@Valid @RequestBody Transfer transfer) {
        return service.sendTransfer(transfer);
    }

    /**
     * Get pending request coming from the given id
     * @param id account that money is coming from
     * @return list of all pending requests
     */
    @RequestMapping(path = "/account/{id}", method = RequestMethod.GET)
    public List<Transfer> viewPendingTransfer (@PathVariable int id) {
        return service.viewPendingTransfer(id);
    }

    /**
     * Update transfer to approve or reject a given transfer
     * @param transfer transfer to update
     * @param id transfer id
     * @return updated transfer
     */
    @RequestMapping(path = "/{id}", method = RequestMethod.PUT)
    public Transfer approveOrRejectTransfer(@Valid @RequestBody Transfer transfer, @PathVariable int id) {
        return service.approveOrRejectTransfer(transfer, id);
    }


}
