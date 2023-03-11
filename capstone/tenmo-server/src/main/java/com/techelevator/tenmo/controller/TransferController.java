package com.techelevator.tenmo.controller;

import com.techelevator.tenmo.dao.UserDao;
import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.User;
import com.techelevator.tenmo.services.TransferService;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;

@RestController
@PreAuthorize("isAuthenticated()")
@RequestMapping(path = "/transfer")

public class TransferController {

    private TransferService service;
    private UserDao userDao;
    private User passedInUser;

    public TransferController(TransferService service, UserDao userDao) {
        this.service = service;
        this.userDao = userDao;
    }

    /**
     * Gets all transfers to and from the given id
     * @param id given id
     * @return list of all transfers
     */
    @RequestMapping(path = "/{id}/account", method = RequestMethod.GET)
    public List<Transfer> findAll(@PathVariable int id, Principal principal) {
        validateAuthenticatedUser(id, principal);
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
    public List<Transfer> viewPendingTransfer (@PathVariable int id, Principal principal) {
        validateAuthenticatedUserByAccountId(id, principal);
        return service.viewPendingTransfer(id);
    }

    /**
     * Update transfer to approve or reject a given transfer
     * @param transfer transfer to update
     * @param id transfer id
     * @return updated transfer
     */
    @RequestMapping(path = "/{id}", method = RequestMethod.PUT)
    public Transfer approveOrRejectTransfer(@Valid @RequestBody Transfer transfer, @PathVariable int id, Principal principal) {
        validateAuthenticatedUserByAccountId(transfer.getAccount_from(), principal);
        return service.approveOrRejectTransfer(transfer, id);
    }

    private void validateAuthenticatedUser(int id, Principal principal) {
        passedInUser = userDao.getUserById(id);

        if (!passedInUser.getUsername().equals(whoAmI(principal))){
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Logged in user does not have access");
        }
    }

    private String whoAmI(Principal principal) {
        return principal.getName();
    }

    private void validateAuthenticatedUserByAccountId(int id, Principal principal) {
        String passedUser = userDao.findByAccountId(id);
        if (!passedUser.equals(whoAmI(principal))) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Logged in user does not have access");
        }

    }

}
