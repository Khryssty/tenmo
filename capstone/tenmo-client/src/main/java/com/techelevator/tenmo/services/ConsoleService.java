package com.techelevator.tenmo.services;


import com.techelevator.tenmo.model.AuthenticatedUser;
import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.User;
import com.techelevator.tenmo.model.UserCredentials;

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

    /**
     * Prints the details for the given transfer
     * @param transfer transfer to print
     */
    public void printTransferDetails(Transfer transfer) {
        System.out.println("------------------------------");
        System.out.println("Transfer Details");
        System.out.println("------------------------------");
        System.out.println("ID: " + transfer.getTransfer_id());
        //TODO get names for accounts
        System.out.println("From: " + transfer.getAccount_from());
        System.out.println("To: " + transfer.getAccount_to());
        System.out.println("Type: " + transfer.getTransferType().getTransfer_type_desc());
        System.out.println("Status: " + transfer.getTransferStatus().getTransfer_status_desc());
        System.out.println(String.format("Amount: $%.2s", transfer.getAmount()));
    }

    public void printAllTransfers(List<Transfer> transfers) {
        System.out.println("------------------------------");
        System.out.println("Transfers");
        System.out.println("ID\t\t\tFrom/To\t\t\t\tAmount");
        System.out.println("------------------------------");
        for(Transfer transfer: transfers) {
            System.out.println(formatTransfer(transfer));
        }
        System.out.println("------------------------------");
    }

    /**
     * formats the transfer for printing to the transfer list
     * @param transfer transfer to be formatted
     * @return formatted transfer
     */
    public String formatTransfer(Transfer transfer) {
        //TODO get names for accounts based on send or receive
        String toFrom = "Temp";
        String otherUser = "Temp";
        String printString = String.format("%s\t\t\t%s:%s\t\t\t$ %.2s",
                transfer.getTransfer_id(), toFrom, otherUser, transfer.getAmount());
        return printString;
    }

    /*
    * CG: added this method for viewPendingRequests
    */
    public void printPendingRequests(List<Transfer> transfers) {
        System.out.println("------------------------------");
        System.out.println("Pending Transfers");
        System.out.println("ID\t\t\tTo\t\t\t\tAmount");
        System.out.println("------------------------------");
        for(Transfer transfer: transfers) {
            System.out.println(formatTransfer(transfer));
        }
        System.out.println("------------------------------");

    }

    public void printAvailableUsers(List<User> users) {
        System.out.println("------------------------------");
        System.out.println("Users");
        System.out.println("ID\t\t\tName");
        System.out.println("------------------------------");
        for(User user: users) {
            System.out.println(user.getId() + "\t\t\t" + user.getUsername());
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
