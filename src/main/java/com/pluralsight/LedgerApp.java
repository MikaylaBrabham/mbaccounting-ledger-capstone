package com.pluralsight;

import java.io.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Scanner;

public class LedgerApp {
    static Scanner scanner = new Scanner(System.in);
    static boolean appRunning = true;

    static ArrayList<Transaction> transactionsArrayList = new ArrayList<>();

    public static void main(String[] args) {
        try {
            readTransactionFile();
            mainMenu();

        } catch (Exception e) {
            System.out.println("An Error Occurred within the application. Exiting...");
            System.exit(500);
        }
    }

    public static void mainMenu() throws InterruptedException {
        while (appRunning) {
            System.out.println("=== Marc's Computer Store Ledger ===");
            System.out.println("""
                    Choose an option below:
                    Add (D)eposit
                    Make (P)ayment
                    (L)edger
                    E(X)it""");
            System.out.print("Enter command: ");
            String userInput = scanner.nextLine();

            System.out.println();
            switch (userInput.toLowerCase()) {
                case "d" -> System.out.println("Coming Soon");
                case "p" -> System.out.println("Coming Soon");
                case "l" -> ledgerMenu();
                case "x" -> appRunning = false;
                default -> System.out.println("Enter a letter that matches the options!");

            }
            Thread.sleep(500);
        }
        System.out.println("Goodbye!");
    }

    public static void ledgerMenu() {
        try {
            boolean menuRunning = true;
            while (menuRunning) {
                System.out.println("= Ledger menu =");
                System.out.println("""
                        Choose an option below:
                        (A)ll Entries
                        (D)eposits
                        (P)ayments
                        (R)eports
                        E(X)it to Main Menu""");
                System.out.print("Enter command: ");
                String userInput = scanner.nextLine();

                System.out.println();
                switch (userInput.toLowerCase()) {
                    case "a" -> displayLedgerEntries("all");
                    case "d" -> displayLedgerEntries("deposits");
                    case "p" -> displayLedgerEntries("payments");
                    case "r" -> System.out.println("Coming Soon");
                    case "x" -> menuRunning = false;
                    default -> System.out.println("Enter a letter that matches the options!");
                }
            }
        } catch (Exception e) {
            System.out.println("Error within ledgerMenu");
            throw new RuntimeException(e);
        }
    }

    //    Reads Transactions from file if it exists, otherwise will only print file not found
    public static void readTransactionFile() {
        try {

            FileReader fileReader = new FileReader("src/main/resources/transactions.csv");
            BufferedReader bufferedReader = new BufferedReader(fileReader);

            String line;

            while ((line = bufferedReader.readLine()) != null) {
                String[] splitRawTransaction = line.split("\\|");
                Transaction newTransaction = new Transaction(splitRawTransaction[0], splitRawTransaction[1], splitRawTransaction[2], splitRawTransaction[3], Double.parseDouble(splitRawTransaction[4]));

                transactionsArrayList.add(newTransaction);
            }

            bufferedReader.close();

        } catch (FileNotFoundException e) {
            System.out.println("No transaction file found!");
        } catch (Exception e) {
            System.out.println("Error reading file!");
            throw new RuntimeException(e);
        }

    }

    public static void writeTransactionToFile(Transaction transaction) {
        try {

            FileWriter fileWriter = new FileWriter("src/main/resources/transactions.csv", true);
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);

            bufferedWriter.write(transaction.getDate() + "|" + transaction.getTime() + "|" + transaction.getName() + "|" + transaction.getEntity() + "|" + transaction.getAmount());

            bufferedWriter.close();
        } catch (Exception e) {
            System.out.println("Error writing file!");
            throw new RuntimeException(e);
        }
    }

    public static void displayLedgerEntries(String filter) {


        for (Transaction transaction : transactionsArrayList) {
            switch (filter.toLowerCase()) {
                case "all": {
                    System.out.println(transaction.getDate() + " " + transaction.getTime() + ": " + transaction.getName() + ", $" + transaction.getAmount() + ", Vendor: " + transaction.getEntity());
                    break;
                }
                case "deposits": {
                    if (transaction.getAmount() > 0) {
                        System.out.println(transaction.getDate() + " " + transaction.getTime() + ": " + transaction.getName() + ", $" + transaction.getAmount() + ", Vendor: " + transaction.getEntity());
                    }
                    break;
                }
                case "payments": {
                    if (transaction.getAmount() < 0) {
                        System.out.println(transaction.getDate() + " " + transaction.getTime() + ": " + transaction.getName() + ", $" + transaction.getAmount() + ", Vendor: " + transaction.getEntity());
                    }
                    break;
                }
            }

        }
//        Spacing
        System.out.println();

    }

}
