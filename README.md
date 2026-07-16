# Penny Wise Accounting Ledger

A full-stack accounting ledger application designed to help small businesses record, organize, and manage financial transactions using a Java application and a MySQL relational database.

## What the application does

The application lets users:

- Record deposits
- Record payments
- View the account ledger
- Search and filter transactions
- Generate financial reports, including:
  - Month-to-Date
  - Previous Month
  - Year-to-Date
  - Previous Year
  - Vendor Search
- Manage user and transaction records
- Store financial data in a MySQL relational database

The application uses a MySQL database (`pennywisedb.sql`) to securely store user and transaction information.

## Requirements

Install these tools before starting the application:

- Java 17
- Maven 3.9 or newer
- MySQL 8.0 or newer

## How to start the application

1. Clone the repository:

```bash
git clone https://github.com/Mamii868/accounting-ledger-capstone.git
```

2. Navigate to the project directory:

```bash
cd accounting-ledger-capstone
```

3. Import the provided database:

```bash
mysql -u root -p < pennywisedb.sql
```

4. Compile the application:

```bash
mvn clean compile
```

5. Run the application:

```bash
mvn exec:java
```

The application launches and allows users to manage financial transactions, generate reports, and interact with the Penny Wise accounting system.

## Technologies Used

- Java 17
- Maven
- MySQL
- SQL
- Git
- GitHub
- JLine
