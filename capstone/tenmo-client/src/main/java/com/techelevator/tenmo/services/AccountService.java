package com.techelevator.tenmo.services;

import com.techelevator.tenmo.model.User;
import com.techelevator.util.BasicLogger;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

public class AccountService {

   private final String baseUrl;
   private RestTemplate restTemplate = new RestTemplate();

   private String token;

   public AccountService(String url) {
      this.baseUrl = url;
   }

   public void setToken(String token) {
      this.token = token;
   }

   /**
    * Gets all users
    * @return a list of all users
    */
   public List<User> getAllUsers() {
      User[] users = null;
      String url = baseUrl + "users";
      try {
         ResponseEntity<User[]> response = restTemplate.exchange(url, HttpMethod.GET, makeAuthEntity(), User[].class);
         users = response.getBody();
      } catch (RestClientResponseException | ResourceAccessException e) {
         BasicLogger.log(e.getMessage());
      }
      return Arrays.asList(users);
   }

   /**
    * Get the balance for the current user
    * @param id current users ID
    * @return the current users balance
    */
   public BigDecimal getBalance(int id) {
      BigDecimal balance = null;
      String url = baseUrl + "account/" + id;
      try {
         ResponseEntity<BigDecimal> response = restTemplate.exchange(url, HttpMethod.GET, makeAuthEntity(), BigDecimal.class);
         balance = response.getBody();
      } catch (RestClientResponseException | ResourceAccessException e) {
         BasicLogger.log(e.getMessage());
      }
      return balance;
   }

   private HttpEntity<Void> makeAuthEntity() {
      HttpHeaders headers = new HttpHeaders();
      headers.setBearerAuth(token);
      return new HttpEntity<>(headers);
   }
}
