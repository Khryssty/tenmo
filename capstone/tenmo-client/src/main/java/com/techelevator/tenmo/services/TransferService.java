package com.techelevator.tenmo.services;

import com.techelevator.tenmo.model.Transfer;
import com.techelevator.util.BasicLogger;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;

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
      String url = baseUrl + "account/" + userId + "/transfer";
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
   public void sendTransfer(Transfer transfer) {
      String url = baseUrl + "transfer";
      try {
         //TODO Add post logic
      } catch (RestClientResponseException | ResourceAccessException e) {
         BasicLogger.log(e.getMessage());
      }
   }

   private HttpEntity<Void> makeAuthEntity() {
      HttpHeaders headers = new HttpHeaders();
      headers.setBearerAuth(token);
      return new HttpEntity<>(headers);
   }
}
