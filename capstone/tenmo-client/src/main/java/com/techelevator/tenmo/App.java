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
      consoleService.printTransfers(transfers, accountService.getAccountForUserId(currentUser.getUser().getId()));

      int getTransferId = consoleService.promptForInt("Please enter transfer ID to view details (0 to cancel):");
      Transfer transferToExamine = transferService.getTransferAtId(getTransferId);

      String toUsername = accountService.getUsernameByAccountId(transferToExamine.getAccount_to());
      String fromUsername = accountService.getUsernameByAccountId(transferToExamine.getAccount_from());
      consoleService.printTransferDetails(transferToExamine, fromUsername, toUsername);
   }

   /**
    * Prints all pending transfers and allows the user to approve or reject
    */
   private void viewPendingRequests() {
      List<Transfer> transfers = transferService.viewPendingTransfers(currentUser.getUser().getId());
      consoleService.printPendingTransfers(transfers);

      int pendingTransferId = consoleService.promptForInt("Please enter transfer ID to approve/reject (0 to cancel): ");
      approveOrReject(pendingTransferId);
   }

   /**
    * Lets the user approve or reject a pending transfer
    * @param pendingTransferId transferId
    */
   private void approveOrReject(int pendingTransferId) {
      consoleService.printApproveOrRejectMenu();
      int input = consoleService.promptForInt("Please choose an option: ");
      if(input >= 0 && input <= 2) {
         //TODO approval and rejection logic
      } else {
         consoleService.printErrorMessage();
         BasicLogger.log("Invalid menu selection");
      }
   }

   /**
    * Make a new sending transfer for the current user
    */
   private void sendBucks() {
      List<User> users = accountService.getAllUsers();
      consoleService.printAvailableUsers(users, currentUser.getUser().getId());

      int selectedUser = consoleService.promptForInt("Enter ID of user you are sending to (0 to cancel):");
      BigDecimal transferAmount = consoleService.promptForBigDecimal("Enter amount:");

      if(inputIsValid(selectedUser, transferAmount)) {
         transferService.createTransfer(makeTransfer(selectedUser, transferAmount, TransferType.SEND_ID));
      } else {
         consoleService.printErrorMessage();
         BasicLogger.log("Transfer amount or ID is invalid");
      }
   }

   /**
    * Allows user to send a request for TE bucks to another user
    */
   private void requestBucks() {
      List<User> users = accountService.getAllUsers();
      consoleService.printAvailableUsers(users, currentUser.getUser().getId());

      int selectedUser = consoleService.promptForInt("Enter ID of user you are requesting from (0 to cancel):");
      BigDecimal transferAmount = consoleService.promptForBigDecimal("Enter amount:");
      //if not current user AND amount > 0
      if(selectedUser != currentUser.getUser().getId() && transferAmount.compareTo(new BigDecimal(0)) > 0) {
         transferService.createTransfer(makeTransfer(selectedUser, transferAmount, TransferType.REQUEST_ID));
      } else {
         consoleService.printErrorMessage();
         BasicLogger.log("Transfer amount or ID is invalid");
      }
   }

   /**
    * Validates input for sending transfer
    * @param selectedUser the id for the selected user
    * @param transferAmount the amount to send
    * @return true is selected user != current user AND amount is non-negative and less than balance
    */
   private boolean inputIsValid(int selectedUser, BigDecimal transferAmount) {
      boolean valid = false;
      if(transferAmount.compareTo(new BigDecimal(0)) > 0) {
         if(transferAmount.compareTo(accountService.getBalance(currentUser.getUser().getId())) <= 0) {
            if(selectedUser != currentUser.getUser().getId()) {
               valid = true;
            }
         }
      }
      return valid;
   }

   /**
    * Creates a new transfer
    *
    * @param selectedUser   user to send or receive
    * @param transferAmount amount to send or receive
    * @param transferTypeId the type id for this transfer
    * @return Transfer object
    */
   private Transfer makeTransfer(int selectedUser, BigDecimal transferAmount, int transferTypeId) {
      Transfer transfer = new Transfer();
      //if transfer type = 2 -> send
      if(transferTypeId == TransferType.SEND_ID) { //SEND
         transfer.setAccount_to(accountService.getAccountForUserId(selectedUser).getAccount_id());
         transfer.setAccount_from(accountService.getAccountForUserId(currentUser.getUser().getId()).getAccount_id());
         transfer.setTransfer_status_id(TransferStatus.APPROVED_ID);
      } else { //REQUEST
         transfer.setAccount_from(accountService.getAccountForUserId(selectedUser).getAccount_id());
         transfer.setAccount_to(accountService.getAccountForUserId(currentUser.getUser().getId()).getAccount_id());
         transfer.setTransfer_status_id(TransferStatus.PENDING_ID);
      }
      transfer.setTransfer_type_id(transferTypeId);
      transfer.setAmount(transferAmount);
      return transfer;
   }
}
