package com.techelevator.tenmo.services;

import com.techelevator.tenmo.model.Transfer;
import com.techelevator.util.BasicLogger;
import org.springframework.http.*;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TransferService {

   private final String baseUrl;
   private RestTemplate restTemplate = new RestTemplate();

   private String token;

   public TransferService(String url) {
      this.baseUrl = url;
   }

   public void setToken(String token) {
      this.token = token;
   }

   /**
    * Gets all transfer going to and from the current user
    * @param userId current users ID
    * @return list of all transfers
    */
   public List<Transfer> getAllTransfers(int userId) {
      Transfer[] transfers = null;
      String url = baseUrl + "transfer/" + userId + "/account";
      try {
         ResponseEntity<Transfer[]> response = restTemplate.exchange(url, HttpMethod.GET, makeAuthEntity(), Transfer[].class);
         transfers = response.getBody();
      } catch (RestClientResponseException | ResourceAccessException e) {
         BasicLogger.log(e.getMessage());
      }
      return Arrays.asList(transfers);
   }

   /**
    * Gets the transfer with the given ID
    * @param transferId the transfer ID
    * @return transfer at ID
    */
   public Transfer getTransferAtId(int transferId) {
      String url = baseUrl + "transfer/" + transferId;
      Transfer transfer = null;
      try {
         ResponseEntity<Transfer> response = restTemplate.exchange(url, HttpMethod.GET, makeAuthEntity(), Transfer.class);
         transfer = response.getBody();
      } catch (RestClientResponseException | ResourceAccessException e) {
         BasicLogger.log(e.getMessage());
      }
      return transfer;
   }

   /**
    * Created a new send transfer
    * @param transfer transfer to be sent
    */
   public Transfer createTransfer(Transfer transfer) {
      Transfer sentTransfer = null;
      String url = baseUrl + "transfer";
      try {
         ResponseEntity<Transfer> response = restTemplate.exchange(url, HttpMethod.POST, makeTransferEntity(transfer), Transfer.class);
         sentTransfer = response.getBody();
      } catch (RestClientResponseException | ResourceAccessException e) {
         BasicLogger.log(e.getMessage());
      }
      return sentTransfer;
   }

   /* Christy
   * 8. As an authenticated user of the system, I need to be able to see my *Pending* transfers.
   * @param userid
   */
   public List<Transfer> viewPendingTransfers(int accountId) {
      String url = baseUrl + "transfer/account/" + accountId;
      Transfer[] transfers = null;
      try {
         ResponseEntity<Transfer[]> response = restTemplate.exchange(url, HttpMethod.GET, makeAuthEntity(), Transfer[].class);
         transfers = response.getBody();
      }  catch (RestClientResponseException | ResourceAccessException e) {
         BasicLogger.log(e.getMessage());
      }
         return Arrays.asList(transfers);
   }

   /* Christy
   *  9. As an authenticated user of the system, I need to be able to either approve or reject a Request Transfer.
   */
   public boolean updatePendingTransfer(Transfer updatedTransfer) {
      boolean isUpdated = false;
      String url = baseUrl + "/transfer/account/" + updatedTransfer.getAccount_from();
      Transfer transfer = null;
      try {
         restTemplate.put(url, makeTransferEntity(updatedTransfer));
         isUpdated = true;
      } catch (RestClientResponseException | ResourceAccessException e) {
         BasicLogger.log(e.getMessage());
      }

      return isUpdated;
   }


   /* Christy
   *  Creates a new HttpEntity with the `Authorization: Bearer:` header and a reservation request body
   */

   private HttpEntity<Transfer> makeTransferEntity(Transfer transfer) {
      HttpHeaders headers = new HttpHeaders();
      headers.setContentType(MediaType.APPLICATION_JSON);
      headers.setBearerAuth(token);
      return new HttpEntity<>(transfer, headers);
   }


   private HttpEntity<Void> makeAuthEntity() {
      HttpHeaders headers = new HttpHeaders();
      headers.setContentType(MediaType.APPLICATION_JSON);
      headers.setBearerAuth(token);
      return new HttpEntity<>(headers);
   }
}
