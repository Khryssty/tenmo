package com.techelevator.tenmo.services;


import com.techelevator.tenmo.model.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Scanner;

public class ConsoleService {

    private final Scanner scanner = new Scanner(System.in);

    public int promptForMenuSelection(String prompt) {
        int menuSelection;
        System.out.print(prompt);
        try {
            menuSelection = Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            menuSelection = -1;
        }
        return menuSelection;
    }

    public void printGreeting() {
        System.out.println("*********************");
        System.out.println("* Welcome to TEnmo! *");
        System.out.println("*********************");
    }

    public void printLoginMenu() {
        System.out.println();
        System.out.println("1: Register");
        System.out.println("2: Login");
        System.out.println("0: Exit");
        System.out.println();
    }

    public void printMainMenu() {
        System.out.println();
        System.out.println("1: View your current balance");
        System.out.println("2: View your past transfers");
        System.out.println("3: View your pending requests");
        System.out.println("4: Send TE bucks");
        System.out.println("5: Request TE bucks");
        System.out.println("0: Exit");
        System.out.println();
    }

    public void printApproveOrRejectMenu() {
        System.out.println("------------------------------");
        System.out.println("1: Approve");
        System.out.println("2: Reject");
        System.out.println("0: Don't approve or reject");
        System.out.println("------------------------------");
    }

    /**
     * Prints the details for the given transfer
     * @param transfer transfer to print
     */
    public void printTransferDetails(Transfer transfer, String fromUsername, String toUsername) {
        System.out.println("------------------------------");
        System.out.println("Transfer Details");
        System.out.println("------------------------------");
        System.out.println("ID: " + transfer.getTransfer_id());
        System.out.println("From: " + fromUsername);
        System.out.println("To: " + toUsername);
        printTypeAndStatus(transfer);
        System.out.println(String.format("Amount: $%.2f", transfer.getAmount()));
    }

    public void printTypeAndStatus(Transfer transfer) {
        if(transfer.getTransfer_type_id() == TransferType.SEND_ID) {
            System.out.println("Type: Send");
        } else {
            System.out.println("Type: Request");
        }
        if(transfer.getTransfer_status_id() == TransferStatus.PENDING_ID) {
            System.out.println("Status: Pending");
        } else if(transfer.getTransfer_status_id() == TransferStatus.APPROVED_ID) {
            System.out.println("Status: Approved");
        } else {
            System.out.println("Status: Rejected");
        }
    }

    /**
     * Prints a list of transfers
     * @param transfers list of transfers
     */
    public void printTransfers(List<String> transfers, String header, String location) {
        System.out.println("------------------------------");
        System.out.println(header);
        System.out.println("ID\t\t\t" + location + "\t\t\tAmount");
        System.out.println("------------------------------");
        for(String transfer: transfers) {
            System.out.println(transfer);
        }
        System.out.println("------------------------------");
    }

    /**
     * Prints a list of all users with the current user excluded
     * @param users
     * @param currentUserId
     */
    public void printAvailableUsers(List<User> users, int currentUserId) {
        System.out.println("------------------------------");
        System.out.println("Users");
        System.out.println("ID\t\t\tName");
        System.out.println("------------------------------");
        for(User user: users) {
            if(user.getId() != currentUserId) {
                System.out.println(user.getId() + "\t\t\t" + user.getUsername());
            }
        }
        System.out.println("------------------------------");
    }

    public UserCredentials promptForCredentials() {
        String username = promptForString("Username: ");
        String password = promptForString("Password: ");
        return new UserCredentials(username, password);
    }

    public String promptForString(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine();
    }

    public int promptForInt(String prompt) {
        System.out.print(prompt);
        while (true) {
            try {
                return Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Please enter a number.");
            }
        }
    }

    public BigDecimal promptForBigDecimal(String prompt) {
        System.out.print(prompt);
        while (true) {
            try {
                return new BigDecimal(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Please enter a decimal number.");
            }
        }
    }

    public void pause() {
        System.out.println("\nPress Enter to continue...");
        scanner.nextLine();
    }

    public void printErrorMessage() {
        System.out.println("An error occurred. Check the log for details.");
    }

}
