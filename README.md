# Accounting Ledger

A Java console app for tracking ledger transactions for a small business.

## What the application does

The app lets you:
- add deposits
- record payments
- view the account ledger
- filter the ledger entries by deposits or payments
- run built-in reports, including month-to-date, previous month, year-to-date, previous year, and vendor searches

Transactions are loaded from and saved to `src/main/resources/transactions.csv`.

## Requirements

Install these tools on the machine before starting the app:
- Java 17
- Maven 3.9 or newer

## How to start the application

1. Open a terminal.
2. Go to the project folder:

   ```bash
   cd /path/to/accounting-ledger-capstone
   ```

3. Compile the application:
   ```bash
   mvn clean compile
   ```
   
4. Start the app from the repository root:

   ```bash
   mvn exec:java -Dexec.mainClass=com.pluralsight.LedgerApp
   ```

The app opens an interactive menu in the terminal where you can add transactions and view reports.
Use the arrow and enter keys to navigate through the app.

## FAQ

- Why was there a class not found error compiling the code?

  Double check that the project has the Jline dependencies downloaded
