package com.techelevator.tenmo;

import com.techelevator.tenmo.model.*;
import com.techelevator.tenmo.services.AccountService;
import com.techelevator.tenmo.services.AuthenticationService;
import com.techelevator.tenmo.services.ConsoleService;
import com.techelevator.tenmo.services.TransferService;
import com.techelevator.util.BasicLogger;

import java.math.BigDecimal;
import java.util.ArrayList;
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
         } else if (menuSelection == 0) {
            System.exit(0);
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
            menuSelection = -1;
            currentUser = null;
            loginMenu();
         } else {
            System.out.println("Invalid Selection");
            continue;
         }
         //consoleService.pause();
      }
   }

   /**
    * Displays the balance for the current user
    */
   private void viewCurrentBalance() {
      Account currentAccount = accountService.getAccountForUserId(currentUser.getUser().getId());
      System.out.println(String.format("Your current account balance is: $%.2f", currentAccount.getBalance()));
   }

   /**
    * Displays the full transfer history for the current user
    * Then allows the user to select a transfer to be shown more details
    */
   private void viewTransferHistory() {
      List<Transfer> transfers = transferService.getAllTransfers(currentUser.getUser().getId());
      consoleService.printTransfers(getFormattedTransfers(transfers), "Transfers", "To/From");

      int getTransferId = consoleService.promptForInt("Please enter transfer ID to view details (0 to cancel):");
      getTransferDetails(transfers, getTransferId, false);
   }

   /**
    * Prints all pending transfers and allows the user to approve or reject
    */
   private void viewPendingRequests() {
      List<Transfer> transfers = transferService.viewPendingTransfers(
              accountService.getAccountForUserId(currentUser.getUser().getId()).getAccount_id());
      consoleService.printTransfers(getFormattedTransfers(transfers), "Pending Transfers", "To");

      int pendingTransferId = consoleService.promptForInt("Please enter transfer ID to approve/reject (0 to cancel): ");
      getTransferDetails(transfers, pendingTransferId, true);
   }

   /**
    * Business logic for displaying transfer details to the user
    * @param transfers list of transfers
    * @param transferDetailsId transfer to examine
    * @param isPending if true -> allow user to approve or reject
    */
   private void getTransferDetails(List<Transfer> transfers, int transferDetailsId, boolean isPending) {
      if(transferDetailsId != 0 && idIsInTransferList(transfers, transferDetailsId)) {
         Transfer transferToExamine = transferService.getTransferAtId(transferDetailsId);
         String toUsername = accountService.getUsernameByAccountId(transferToExamine.getAccount_to());
         String fromUsername = accountService.getUsernameByAccountId(transferToExamine.getAccount_from());
         consoleService.printTransferDetails(transferToExamine, fromUsername, toUsername);
         if(isPending) {
            approveOrReject(transferToExamine);
         }
      } else if(transferDetailsId == 0) {
         //cancelled
      } else {
         consoleService.printErrorMessage();
         BasicLogger.log("Transfer not found");
      }
   }

   /**
    * Lets the user approve or reject a pending transfer
    * @param transfer transferId
    */
   private void approveOrReject(Transfer transfer) {
      consoleService.printApproveOrRejectMenu();
      int input = consoleService.promptForInt("Please choose an option: ");
      if(input == 1) { //approve
         approveTransfer(transfer);
      } else if(input == 2) { //reject
         transfer.setTransfer_status_id(TransferStatus.REJECTED_ID);
         System.out.println("Pending transfer is successfully rejected.");
      } else if(input == 0) {
         //do nothing
      } else {
         consoleService.printErrorMessage();
         BasicLogger.log("Invalid menu selection");
      }
      transferService.updatePendingTransfer(transfer);
   }

   /**
    * Verifies that the user has enough balance and approves transfer if true
    * @param transfer transfer to approve
    */
   private void approveTransfer(Transfer transfer) {
      BigDecimal currentUserBalance = accountService.getAccountForUserId(currentUser.getUser().getId()).getBalance();
      if(transfer.getAmount().compareTo(currentUserBalance) <= 0) {
         transfer.setTransfer_status_id(TransferStatus.APPROVED_ID);
         System.out.println("Pending transfer is successfully approved.");
      } else {
         consoleService.printErrorMessage();
         BasicLogger.log("Insufficient Funds");
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
         System.out.println("Successfully sent $ " + transferAmount + " to " + accountService.getUsernameByAccountId(accountService.getAccountForUserId(selectedUser).getAccount_id()));
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
         System.out.println("Your request for transfer of $ " + transferAmount + " from " +
                 accountService.getUsernameByAccountId(accountService.getAccountForUserId(selectedUser).getAccount_id()) + " is successfully sent and pending for approval.");
      } else {
         consoleService.printErrorMessage();
         BasicLogger.log("Transfer amount or ID is invalid");
      }
   }

   /**
    * Transforms a list of transfers into a list of formatted strings
    * @param transfers transfer list to transform
    * @return formatted transfers list
    */
   private List<String> getFormattedTransfers(List<Transfer> transfers) {
      List<String> formattedTransfers = new ArrayList<>();
      for (Transfer transfer : transfers) {
         String formattedString = String.format("%s\t\t%s\t\t$ %.2f", transfer.getTransfer_id(), formatToFrom(transfer), transfer.getAmount());
         formattedTransfers.add(formattedString);
      }
      return formattedTransfers;
   }

   /**
    * Formats the "To/From: Username" for a transfer
    * @param transfer the transfer to format
    * @return formatted string
    */
   private String formatToFrom(Transfer transfer) {
      String formattedString = "";
      String toUsername = accountService.getUsernameByAccountId(transfer.getAccount_to());
      String fromUsername = accountService.getUsernameByAccountId(transfer.getAccount_from());
      if (transfer.getTransfer_type_id() == TransferType.SEND_ID) {
         if (transfer.getAccount_from() == accountService.getAccountForUserId(currentUser.getUser().getId()).getAccount_id()) {
            formattedString += " To:  " + toUsername;
         } else {
            formattedString += "From: " + fromUsername;
         }
      } else if (transfer.getTransfer_type_id() == TransferType.REQUEST_ID) {
         if (transfer.getAccount_to() == accountService.getAccountForUserId(currentUser.getUser().getId()).getAccount_id()) {
            formattedString += "From: " + fromUsername;;
         } else {
            formattedString += "To: " + toUsername;
         }
      }
      return formattedString;
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
         if(transferAmount.compareTo(accountService.getAccountForUserId(currentUser.getUser().getId()).getBalance()) <= 0) {
            if(selectedUser != currentUser.getUser().getId()) {
               valid = true;
            }
         }
      }
      return valid;
   }

   /**
    * Checks that the given id is in the list
    * @param transfers list to check
    * @param pendingTransferId id to search for
    * @return true if list contains id
    */
   private boolean idIsInTransferList(List<Transfer> transfers, int pendingTransferId) {
      for(Transfer transfer: transfers) {
         if(transfer.getTransfer_id() == pendingTransferId) {
            return true;
         }
      }
      return false;
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
