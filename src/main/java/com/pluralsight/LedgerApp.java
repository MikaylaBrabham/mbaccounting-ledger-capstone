package com.pluralsight;

import org.jline.consoleui.prompt.ConsolePrompt;
import org.jline.consoleui.prompt.PromptResultItemIF;
import org.jline.consoleui.prompt.builder.PromptBuilder;
import org.jline.reader.LineReader;
import org.jline.reader.LineReaderBuilder;
import org.jline.terminal.Terminal;
import org.jline.terminal.TerminalBuilder;
import org.jline.utils.AttributedString;
import org.jline.utils.AttributedStringBuilder;
import org.jline.utils.AttributedStyle;
import org.jline.utils.InfoCmp;

import java.io.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Map;

public class LedgerApp {
    static boolean appRunning = true;

    static ArrayList<Transaction> transactionsArrayList = new ArrayList<>();

    public static void main(String[] args) {
        try {
//            Initialize the terminal and line reader
            Terminal terminal = TerminalBuilder.builder().system(true).provider("jni").build();
            LineReader lineReader = LineReaderBuilder.builder().terminal(terminal).option(LineReader.Option.CASE_INSENSITIVE, true).build();

//            Add transactions from the file to the array for the app to use
            readTransactionFile();

            mainMenu(terminal, lineReader);

        } catch (Exception e) {
            System.out.println("An Error Occurred within the application. Exiting...");
            System.exit(500);
        }
    }

    public static void mainMenu(Terminal terminal, LineReader lineReader) {
        ConsolePrompt prompt = new ConsolePrompt(terminal);
//        Put terminal.writer in a separate variable to avoid writing it out 10000x
        PrintWriter writer = terminal.writer();

        int timesLooped = 0;
//        Menu selection Prompt
        try {
            while (appRunning) {
                terminal.puts(InfoCmp.Capability.clear_screen);
                terminal.flush();

                printTitle(terminal, "Marc's Computer Store Ledger");

//                This is purely to fix the spacing being overwritten by the prompt when the loop loops back
                if (timesLooped > 0) {
                    writer.println();
                }

                terminal.flush();

                PromptBuilder builder = prompt.getPromptBuilder();
                builder.createListPrompt()
                        .name("mainMenuOption")
                        .message("Choose an option:")
                        .newItem().text("Add Deposit").add()
                        .newItem().text("Make Payment").add()
                        .newItem().text("View Ledger").add()
                        .newItem().text("Exit Program").add()
                        .addPrompt();


                Map<String, PromptResultItemIF> result = prompt.prompt(builder.build());

                switch (result.get("mainMenuOption").getResult()) {
                    case "Add Deposit" -> makeDeposit(terminal, lineReader);
                    case "Make Payment" -> makePayment(terminal, lineReader);
                    case "View Ledger" -> ledgerMenu(terminal, lineReader);
                    case "Exit Program" -> appRunning = false;

                }
                timesLooped++;
            }
            writer.println("Goodbye!");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static void ledgerMenu(Terminal terminal, LineReader lineReader) {
        transactionsArrayList.sort(Comparator.comparing(Transaction::getDate).thenComparing(Transaction::getTime).reversed());

        PrintWriter writer = terminal.writer();
        ConsolePrompt prompt = new ConsolePrompt(terminal);

        int timesLooped = 0;
//        Menu selection Prompt
        try {
            boolean menuRunning = true;
            while (menuRunning) {
                terminal.puts(InfoCmp.Capability.clear_screen);
                terminal.flush();

                printTitle(terminal, "Ledger Menu");

//                This is purely to fix the spacing being overwritten by the prompt when the loop loops back
                if (timesLooped > 0) {
                    writer.println();
                }

                terminal.flush();

                PromptBuilder builder = prompt.getPromptBuilder();
                builder.createListPrompt()
                        .name("ledgerMenuOption")
                        .message("Choose an option: ")
                        .newItem().text("View All Entries").add()
                        .newItem().text("View Deposits").add()
                        .newItem().text("View Payments").add()
                        .newItem().text("View Reports").add()
                        .newItem().text("Back to Main Menu").add()
                        .addPrompt();

                Map<String, PromptResultItemIF> result = prompt.prompt(builder.build());
                switch (result.get("ledgerMenuOption").getResult()) {
                    case "View All Entries" -> displayLedgerEntries(terminal, lineReader, "all");
                    case "View Deposits" -> displayLedgerEntries(terminal, lineReader, "deposits");
                    case "View Payments" -> displayLedgerEntries(terminal, lineReader, "payments");
                    case "View Reports" -> reportsMenu(terminal, lineReader);
                    case "Back to Main Menu" -> menuRunning = false;
                }
                timesLooped++;
            }
        } catch (Exception e) {
            System.out.println("Error within ledgerMenu");
            throw new RuntimeException(e);
        }
    }

    public static void reportsMenu(Terminal terminal, LineReader lineReader) {
        try {
            ConsolePrompt prompt = new ConsolePrompt(terminal);
            PrintWriter writer = terminal.writer();

            int timesLooped = 0;

            boolean menuRunning = true;
            while (menuRunning) {
                terminal.puts(InfoCmp.Capability.clear_screen);
                terminal.flush();

                printTitle(terminal, "Custom Reports");

//                This is purely to fix the spacing being overwritten by the prompt when the loop loops back
                if (timesLooped > 0) {
                    writer.println();
                }

                terminal.flush();
                PromptBuilder builder = prompt.getPromptBuilder();
                builder.createListPrompt()
                        .name("reportsMenuOption")
                        .message("Choose a report to view:")
                        .newItem().text("Month To Date").add()
                        .newItem().text("Previous Month").add()
                        .newItem().text("Year To Date").add()
                        .newItem().text("Previous Year").add()
                        .newItem().text("Search by Vendor").add()
                        .newItem().text("Back to Ledger Menu").add()
                        .addPrompt();

                Map<String, PromptResultItemIF> result = prompt.prompt(builder.build());

                switch (result.get("reportsMenuOption").getResult()) {
                    case "Month To Date": {
                        displayCustomReports("monthtd", "");
                        break;
                    }
                    case "Previous Month": {
                        displayCustomReports("prevmonth", "");
                        break;
                    }
                    case "Year To Date": {
                        displayCustomReports("yeartd", "");
                        break;
                    }
                    case "Previous Year": {
                        displayCustomReports("prevyear", "");
                        break;
                    }
                    case "Search By Vendor": {
                        String vendorInput = lineReader.readLine("Enter the vendor name: ");
                        displayCustomReports("vendor", vendorInput);
                    }
                    case "Back to Ledger Menu": {
                        menuRunning = false;
                        break;
                    }

                }

                timesLooped++;
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static void printTitle(Terminal terminal, String text) {
        AttributedStringBuilder builder = new AttributedStringBuilder();
        PrintWriter writer = terminal.writer();

//        Dynamically creates the top/bottom lines and spaces for the title based on how long the input text is
        AttributedString title = builder
                .style(AttributedStyle.BOLD.foreground(AttributedStyle.GREEN))
                .append("=".repeat(text.length() + 8)).append("\n")
                .append("||").append(" ".repeat(text.length() + 4)).append("||").append("\n")
                .append("||  ").append(text).append("  ||\n")
                .append("||").append(" ".repeat(text.length() + 4)).append("||").append("\n")
                .append("=".repeat(text.length() + 8)).append("\n")
                .style(AttributedStyle.DEFAULT)
                .toAttributedString();

        writer.println(title.toAnsi());
        terminal.flush();
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

            bufferedWriter.write("\n" + transaction.getDate() + "|" + transaction.getTime() + "|" + transaction.getName() + "|" + transaction.getEntity() + "|" + transaction.getAmount());

            bufferedWriter.close();
        } catch (Exception e) {
            System.out.println("Error writing file!");
            throw new RuntimeException(e);
        }
    }

    public static void displayLedgerEntries(Terminal terminal, LineReader lineReader, String filter) {

        ArrayList<Transaction> transactionsToDisplay = new ArrayList<>();

        int maxDateLength = 10;
        int maxTimeLength = 8;
        int maxDescriptionLength = 0;
        int maxVendorLength = 0;
        int maxAmountLength = 0;

        for (Transaction transaction : transactionsArrayList) {


            switch (filter.toLowerCase()) {
                case "all": {
                    transactionsToDisplay.add(transaction);
                    break;
                }
                case "deposits": {
                    if (transaction.getAmount() > 0) {
                        transactionsToDisplay.add(transaction);
                    }
                    break;
                }
                case "payments": {
                    if (transaction.getAmount() < 0) {
                        transactionsToDisplay.add(transaction);
                    }
                    break;
                }
            }

        }

        for (Transaction transaction : transactionsToDisplay) {
            //            Find the longest transaction string for styling
            if (transaction.getName().length() > maxDescriptionLength) {
                maxDescriptionLength = transaction.getName().length();
            }
            if (transaction.getEntity().length() > maxVendorLength) {
                maxVendorLength = transaction.getEntity().length();
            }
            if (Double.toString(transaction.getAmount()).length() > maxAmountLength) {
                maxAmountLength = Double.toString(transaction.getAmount()).length();
            }
        }

        PrintWriter writer = terminal.writer();

        AttributedStringBuilder transactionListBuilder = new AttributedStringBuilder();
        for (Transaction transaction : transactionsToDisplay) {

//            Append each transaction to builder with correct formatting for the table (the math is very precise per-column)
            transactionListBuilder
                    .append("|| ")
                    .append(transaction.getDate()).append(" ".repeat(maxDateLength / 2 - 2))
                    .append("|| ")
                    .append(transaction.getTime()).append(" ".repeat(maxTimeLength / 2 - 1))
                    .append("|| ")
                    .append(transaction.getName()).append(" ".repeat(((maxDescriptionLength / 2 - 4) + (maxDescriptionLength == transaction.getName().length() ? 0 : maxDescriptionLength - transaction.getName().length()))))
                    .append("|| ")
                    .append(transaction.getEntity()).append(" ".repeat((maxVendorLength / 3 - 9) + (maxDescriptionLength == transaction.getEntity().length() ? 0 : maxDescriptionLength - transaction.getEntity().length())))
                    .append("|| ")
                    .style(AttributedStyle.BOLD.foreground(transaction.getAmount() > 0 ? AttributedStyle.GREEN : AttributedStyle.RED))
                    .append(" ".repeat((maxAmountLength / 2 - 11) + (maxDescriptionLength == Double.toString(transaction.getAmount()).length() ? 0 : maxDescriptionLength - Double.toString(transaction.getAmount()).length()))).append(Double.toString(transaction.getAmount()))
                    .style(AttributedStyle.DEFAULT)
                    .append(" ||\n");
        }

        String dateTitle = "DATE";
        String timeTitle = "TIME";
        String descriptionTitle = "DESC.";
        String vendorTitle = "VENDOR";
        String amountTitle = "AMOUNT";

        int maxLedgerLength = maxAmountLength + maxDateLength + maxDescriptionLength + maxTimeLength + maxVendorLength + dateTitle.length() + timeTitle.length() + descriptionTitle.length() + vendorTitle.length() + amountTitle.length() + 4;

        AttributedString transactionList = transactionListBuilder.toAttributedString();

//        Builds the table header and adds the transaction list
        AttributedStringBuilder builder = new AttributedStringBuilder();
        AttributedString title = builder
                .append("=".repeat(maxLedgerLength + 8)).append("\n")
                .append("||").append(" ".repeat(maxDateLength / 2)).append("DATE").append(" ".repeat(maxDateLength / 2)).append("|")
                .append("|").append(" ".repeat(maxTimeLength / 2)).append("TIME").append(" ".repeat(maxTimeLength / 2)).append("|")
                .append("|").append(" ".repeat(maxDescriptionLength / 2)).append("DESC.").append(" ".repeat(maxDescriptionLength / 2)).append(" |")
                .append("|").append(" ".repeat(maxVendorLength / 2)).append("VENDOR").append(" ".repeat(maxVendorLength / 2)).append("|")
                .append("|").append(" ".repeat(maxAmountLength / 2)).append("AMOUNT").append(" ".repeat(maxAmountLength / 2)).append("||\n")
                .append(transactionList)
                .append("=".repeat(maxLedgerLength + 8)).append("\n")
                .toAttributedString();

        terminal.puts(InfoCmp.Capability.clear_screen);
        writer.println(title.toAnsi());
        lineReader.readLine("Press ENTER to continue...");
        terminal.flush();

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

    public static void makeDeposit(Terminal terminal, LineReader lineReader) {
        try {
            System.out.print("Enter the description: ");
            String transactionName = lineReader.readLine();
            System.out.print("Enter who you are getting money from: ");
            String transactionVendor = lineReader.readLine();
            System.out.print("Enter the amount of money received: ");
            double transactionAmount = Double.parseDouble(lineReader.readLine());

            LocalDateTime now = LocalDateTime.now();

            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");

            Transaction createdTransaction = new Transaction(dateFormatter.format(now), timeFormatter.format(now), transactionName, transactionVendor, Math.abs(transactionAmount));

            System.out.println("Saving transaction...");
            transactionsArrayList.add(createdTransaction);
            writeTransactionToFile(createdTransaction);
            System.out.println("Transaction saved!");
        } catch (Exception e) {
            System.out.println("Error Making Deposit!");
        }

    }

    public static void makePayment(Terminal terminal, LineReader lineReader) {
        try {
            System.out.print("Enter the description: ");
            String transactionName = lineReader.readLine();
            System.out.print("Enter who you are sending money to: ");
            String transactionVendor = lineReader.readLine();
            System.out.print("Enter the amount of money received: ");
            double transactionAmount = Double.parseDouble(lineReader.readLine());

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

}
