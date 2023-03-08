package com.techelevator.tenmo;

import com.techelevator.tenmo.model.*;
import com.techelevator.tenmo.services.AccountService;
import com.techelevator.tenmo.services.AuthenticationService;
import com.techelevator.tenmo.services.ConsoleService;
import com.techelevator.tenmo.services.TransferService;
import com.techelevator.util.BasicLogger;

import java.math.BigDecimal;
import java.util.List;

public class App {

   private static final String API_BASE_URL = "http://localhost:8080/";

   private final ConsoleService consoleService = new ConsoleService();
   private final AuthenticationService authenticationService = new AuthenticationService(API_BASE_URL);
   private final AccountService accountService = new AccountService(API_BASE_URL);
   private final TransferService transferService = new TransferService(API_BASE_URL);

   private AuthenticatedUser currentUser;

   public static void main(String[] args) {
      App app = new App();
      app.run();
   }

   private void run() {
      consoleService.printGreeting();
      loginMenu();
      if (currentUser != null) {
         mainMenu();
      }
   }

   private void loginMenu() {
      int menuSelection = -1;
      while (menuSelection != 0 && currentUser == null) {
         consoleService.printLoginMenu();
         menuSelection = consoleService.promptForMenuSelection("Please choose an option: ");
         if (menuSelection == 1) {
            handleRegister();
         } else if (menuSelection == 2) {
            handleLogin();
         } else if (menuSelection != 0) {
            System.out.println("Invalid Selection");
            consoleService.pause();
         }
      }
   }

   private void handleRegister() {
      System.out.println("Please register a new user account");
      UserCredentials credentials = consoleService.promptForCredentials();
      if (authenticationService.register(credentials)) {
         System.out.println("Registration successful. You can now login.");
      } else {
         consoleService.printErrorMessage();
      }
   }

   private void handleLogin() {
      UserCredentials credentials = consoleService.promptForCredentials();
      currentUser = authenticationService.login(credentials);
      if (currentUser == null) {
         consoleService.printErrorMessage();
      }
   }

   private void mainMenu() {
      int menuSelection = -1;
      while (menuSelection != 0) {
         accountService.setToken(currentUser.getToken());
         transferService.setToken(currentUser.getToken());
         consoleService.printMainMenu();
         menuSelection = consoleService.promptForMenuSelection("Please choose an option: ");
         if (menuSelection == 1) {
            viewCurrentBalance();
         } else if (menuSelection == 2) {
            viewTransferHistory();
         } else if (menuSelection == 3) {
            viewPendingRequests();
         } else if (menuSelection == 4) {
            sendBucks();
         } else if (menuSelection == 5) {
            requestBucks();
         } else if (menuSelection == 0) {
            continue;
         } else {
            System.out.println("Invalid Selection");
         }
         consoleService.pause();
      }
   }

   /**
    * Displays the balance for the current user
    */
   private void viewCurrentBalance() {
      BigDecimal currentBalance = accountService.getBalance(currentUser.getUser().getId());
      System.out.println(String.format("Your current account balance is: $%.2f", currentBalance));
   }

   /**
    * Displays the full transfer history for the current user
    * Then allows the user to select a transfer to be shown more details
    */
   private void viewTransferHistory() {
      List<Transfer> transfers = transferService.getAllTransfers(currentUser.getUser().getId());
      consoleService.printAllTransfers(transfers);

      int getTransferId = consoleService.promptForInt("Please enter transfer ID to view details (0 to cancel):");
      Transfer transferToExamine = transferService.getTransferAtId(getTransferId);
      consoleService.printTransferDetails(transferToExamine);
   }

   /* Christy
   *
   */
   private void viewPendingRequests() {
      List<Transfer> transfers = transferService.viewPendingTransfers(currentUser.getUser().getId());
      consoleService.printPendingRequests(transfers);

      int getTransferId = consoleService.promptForInt("Please enter transfer ID to approve/reject (0 to cancel): ");
   }

   /* Christy
   * approve/reject pending requests
   */
//   private void approveOrReject() {
//
//   }

   /**
    * Make a new sending transfer for the current user
    */
   private void sendBucks() {
      // TODO COME BACK TO THIS
      List<User> users = accountService.getAllUsers();
      consoleService.printAvailableUsers(users);
      int selectedUser = consoleService.promptForInt("Enter ID of user you are sending to (0 to cancel):");
      BigDecimal transferAmount = consoleService.promptForBigDecimal("Enter amount:");
      //transferAmount <= currentBalance
      if (transferAmount.compareTo(accountService.getBalance(currentUser.getUser().getId())) <= 0) {
         //TODO sendTransfer(): if in here -> transfer can be made
         transferService.sendTransfer(makeSendTransfer(selectedUser, transferAmount));
      } else {
         consoleService.printErrorMessage();
         BasicLogger.log("Transfer amount is more than balance or ID is invalid");
      }
   }

   private void requestBucks() {
      // TODO Auto-generated method stub

   }

   /**
    * Creates a new sending transfer
    *
    * @param selectedUser   user to send to
    * @param transferAmount amount to send
    * @return Transfer object
    */
   private Transfer makeSendTransfer(int selectedUser, BigDecimal transferAmount) {
      Transfer transfer = new Transfer();
      transfer.setAccount_to(selectedUser);
      transfer.setAccount_from(currentUser.getUser().getId());
      transfer.setAmount(transferAmount);
      transfer.setTransferStatus(new TransferStatus(2, "Approved"));
      transfer.setTransferType(new TransferType(2, "Send"));
      return transfer;
   }

}
