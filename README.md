# CIA 2 Expense Tracker

A simple expense-tracking web application for the CIA 2 end-to-end CI/CD assignment.

## Features
- Add an expense with title, category, amount, date and optional notes
- View recent expenses
- Filter expenses by category
- Delete an expense
- View total spending and transaction count
- View spending totals by category
- Check backend health

## Technology
- Java 17
- Spring Boot 3
- Maven
- REST API
- HTML, CSS and JavaScript
- JUnit 5 and Spring MockMvc

## Run locally
Requirements: Java 17+ and Maven.

```bash
mvn clean test
mvn spring-boot:run
```

Open http://localhost:8080.

## REST endpoints

| Method | Endpoint | Description |
|---|---|---|
| GET | `/api/expenses` | List all expenses |
| GET | `/api/expenses?category=Food` | Filter by category |
| GET | `/api/expenses/{id}` | Get one expense |
| POST | `/api/expenses` | Add an expense |
| PUT | `/api/expenses/{id}` | Update an expense |
| DELETE | `/api/expenses/{id}` | Delete an expense |
| GET | `/api/summary` | Get total and category summaries |
| GET | `/api/health` | Health check |

### Example POST body
```json
{
  "title": "Lunch",
  "category": "Food",
  "amount": 250.00,
  "date": "2026-11-04",
  "notes": "Lunch with friends"
}
```

## Build a JAR
```bash
mvn clean package
java -jar target/expense-tracker-0.0.1-SNAPSHOT.jar
```

## Important note
This starter version stores expenses in memory. Records reset when the application restarts. A database can be added later if persistent storage is needed.


CI/CD automatic deployment test.