package com.pluralsight;

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
                    e(X)it""");
            System.out.print("Enter command: ");
            String userInput = scanner.nextLine();
            scanner.nextLine();

            System.out.println();
            switch (userInput.toLowerCase()) {
                case "d" -> System.out.println("Coming Soon");
                case "p" -> System.out.println("Coming Soon");
                case "l" -> System.out.println("Coming Soon");
                case "x" -> appRunning = false;

            }
            Thread.sleep(500);
            System.out.println();
        }
    }
}
