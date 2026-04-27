package com.pluralsight;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Scanner;

public class LedgerApp {
    static Scanner scanner = new Scanner(System.in);
    static boolean appRunning = true;

    public static void main(String[] args) {

        try {

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
                    case "a" -> System.out.println("Coming Soon");
                    case "d" -> System.out.println("Coming Soon");
                    case "p" -> System.out.println("Coming Soon");
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

    public static ArrayList<Transaction> readTransactionFile() {
        try {
            ArrayList<Transaction> transactionArrayList = new ArrayList<>();

            FileReader fileReader = new FileReader("src/main/resources/transactions.csv");
            BufferedReader bufferedReader = new BufferedReader(fileReader);

            String line;

            while ((line = bufferedReader.readLine()) != null) {
                String[] splitRawTransaction = line.split("\\|");
                Transaction newTransaction = new Transaction(splitRawTransaction[0], splitRawTransaction[1], splitRawTransaction[2], splitRawTransaction[3], Double.parseDouble(splitRawTransaction[4]));

                transactionArrayList.add(newTransaction);
            }

            return transactionArrayList;

        } catch (FileNotFoundException e) {
            System.out.println("No file found!");
            throw new RuntimeException(e);
        } catch (Exception e) {
            System.out.println("Error reading file!");
            throw new RuntimeException(e);
        }

    }
}
