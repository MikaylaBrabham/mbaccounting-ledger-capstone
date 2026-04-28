package com.pluralsight;

import java.io.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
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
                case "d" -> makeDeposit();
                case "p" -> makePayment();
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
                    case "r" -> reportsMenu();
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

    public static void reportsMenu() {
        try {
            boolean menuRunning = true;
            while (menuRunning) {
                System.out.println("= Reports menu =");
                System.out.println("""
                        Choose an option below:
                        1. Month To Date
                        2. Previous Month
                        3. Year To Date
                        4. Previous Year
                        5. Search by Vendor
                        0. Exit to Ledger Menu""");
                System.out.print("Enter command: ");
                int userInput = scanner.nextInt();
                scanner.nextLine();

                switch (userInput) {
                    case 1: {
                        displayCustomReports("monthtd", "");
                        break;
                    }
                    case 2: {
                        displayCustomReports("prevmonth", "");
                        break;
                    }
                    case 3: {
                        displayCustomReports("yeartd", "");
                        break;
                    }
                    case 4: {
                        displayCustomReports("prevyear", "");
                        break;
                    }
                    case 5: {
                        System.out.print("Enter the vendor name: ");
                        String vendorInput = scanner.nextLine();
                        displayCustomReports("vendor", vendorInput);
                    }
                    case 0: {
                        menuRunning = false;
                        break;
                    }

                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    //    Filter transactions by either built-in filters or custom input
    public static void displayCustomReports(String type, String searchParam) {
        ArrayList<Transaction> transactionsToDisplay = new ArrayList<>();
        LocalDate currentDate = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");


        switch (type) {
            case "monthtd": {
                for (Transaction transaction : transactionsArrayList) {
                    LocalDate transactionDate = LocalDate.parse(transaction.getDate(), formatter);
                    if (currentDate.getMonthValue() == transactionDate.getMonthValue() && currentDate.getYear() == transactionDate.getYear()) {
                        transactionsToDisplay.add(transaction);
                    }
                }
                break;
            }
            case "prevmonth": {
                for (Transaction transaction : transactionsArrayList) {
                    LocalDate transactionDate = LocalDate.parse(transaction.getDate(), formatter);

//                    Checks if it is January. If it is go back to December (12) or keep the original answer
                    if (currentDate.minusMonths(1).getMonthValue() == transactionDate.getMonthValue()) {
                        transactionsToDisplay.add(transaction);
                    }

                }
                break;
            }
            case "yeartd": {
                for (Transaction transaction : transactionsArrayList) {
                    LocalDate transactionDate = LocalDate.parse(transaction.getDate(), formatter);

                    if (currentDate.getYear() == transactionDate.getYear()) {
                        transactionsToDisplay.add(transaction);
                    }

                }
                break;
            }

            case "prevyear": {
                for (Transaction transaction : transactionsArrayList) {
                    LocalDate transactionDate = LocalDate.parse(transaction.getDate(), formatter);

                    if (currentDate.minusYears(1).getYear() == transactionDate.getYear()) {
                        transactionsToDisplay.add(transaction);
                    }
                }
                break;
            }
            case "vendor": {
                for (Transaction transaction : transactionsArrayList) {
                    if (transaction.getEntity().equalsIgnoreCase(searchParam)) {
                        transactionsToDisplay.add(transaction);
                    }
                }
            }
        }

        System.out.println();
//        Print all transactions found with filters
        if (!transactionsToDisplay.isEmpty()) {
            for (Transaction transaction : transactionsToDisplay) {
                System.out.println(transaction.getDate() + " " + transaction.getTime() + ": " + transaction.getName() + ", $" + transaction.getAmount() + ", Vendor: " + transaction.getEntity());
            }
        } else {
            System.out.println("No Transactions Found. ");
        }
        System.out.println();
    }

    public static void makeDeposit() {
        try {
            System.out.println("Enter the description: ");
            String transactionName = scanner.nextLine();
            System.out.println("Enter who you are getting money from: ");
            String transactionVendor = scanner.nextLine();
            System.out.println("Enter the amount of money received: ");
            double transactionAmount = scanner.nextDouble();

            LocalDateTime now = LocalDateTime.now();

            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH-mm-ss");

            Transaction createdTransaction = new Transaction(dateFormatter.format(now), timeFormatter.format(now), transactionName, transactionVendor, (transactionAmount * -1));

            System.out.println("Saving transaction...");
            transactionsArrayList.add(createdTransaction);
            writeTransactionToFile(createdTransaction);
            System.out.println("Transaction saved!");
        } catch (Exception e) {
            System.out.println("Error Making Deposit!");
        }

    }
    public static void makePayment() {
        try {
            System.out.println("Enter the description: ");
            String transactionName = scanner.nextLine();
            System.out.println("Enter who you are sending money to: ");
            String transactionVendor = scanner.nextLine();
            System.out.println("Enter the amount of money received: ");
            double transactionAmount = scanner.nextDouble();

            LocalDateTime now = LocalDateTime.now();

            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH-mm-ss");

            Transaction createdTransaction = new Transaction(dateFormatter.format(now), timeFormatter.format(now), transactionName, transactionVendor, Math.abs(transactionAmount));

            System.out.println("Saving transaction...");
            transactionsArrayList.add(createdTransaction);
            writeTransactionToFile(createdTransaction);
            System.out.println("Transaction saved!");
        } catch (Exception e) {
            System.out.println("Error Making Deposit!");
        }

    }

}
