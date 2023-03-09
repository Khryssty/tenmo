package com.techelevator.tenmo.services;

import com.techelevator.tenmo.model.Account;
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

   public String getUsernameByAccountId(int id) {
      String username = null;
      String url = baseUrl + "users/account/" + id;
      try {
         ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, makeAuthEntity(), String.class);
         username = response.getBody();
      } catch (RestClientResponseException | ResourceAccessException e) {
         BasicLogger.log(e.getMessage());
      }
      return username;
   }

   public Account getAccountForUserId(int user_id) {
      Account account = null;
      String url = baseUrl + "account/" + user_id;
      try {
         ResponseEntity<Account> response = restTemplate.exchange(url, HttpMethod.GET, makeAuthEntity(), Account.class);
         account = response.getBody();
      } catch (RestClientResponseException | ResourceAccessException e) {
         BasicLogger.log(e.getMessage());
      }
      return account;
   }

   private HttpEntity<Void> makeAuthEntity() {
      HttpHeaders headers = new HttpHeaders();
      headers.setBearerAuth(token);
      return new HttpEntity<>(headers);
   }
}
