# Accounting Ledger Capstone

This is a simple Java console app for tracking ledger transactions for a small business.

## What the application does

The app lets you:
- add deposits
- record payments
- view the full ledger
- filter entries by deposits or payments
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

3. Start the app from the repository root:

   ```bash
   mvn exec:java -Dexec.mainClass=com.pluralsight.LedgerApp
   ```

The app opens an interactive menu in the terminal where you can add transactions and view reports.

## Optional build check

If you want to confirm the project builds successfully first, run:

```bash
mvn test
```
