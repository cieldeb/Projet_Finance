# Projet Finance

A desktop financial portfolio management application built with **Java** and **JavaFX**. It lets a user register, authenticate, manage one or more portfolios, and trade **stocks (Actions)** and **cryptocurrencies**, with crypto purchases recorded on a simple custom blockchain.

> Student project by **Gabgab58** and **cieldeb**.

## Features

- **User authentication** — sign-up and login screens, with registered users persisted to CSV.
- **Portfolio management** — create and manage multiple portfolios (`Portefeuille`), each holding stocks and crypto positions.
- **Stock trading (Actions)** — buy and sell stocks, with live/reference price data pulled via the Alpha Vantage API.
- **Crypto trading** — buy and sell cryptocurrencies, with each transaction recorded as a block on a lightweight custom blockchain (`Block`, `TransactionCrypto`).
- **Bank accounts & transfers (Virement)** — manage accounts, add recipients (`Destinataire`), and perform transfers.
- **JSON/CSV persistence** — portfolios, transactions, account lists, and blockchain state are stored locally under `files/`.

## Tech Stack

| Layer | Technology |
|---|---|
| Language | Java 9 (module system / `module-info.java`) |
| UI | JavaFX 17 (Controls + FXML) |
| Build tool | Maven (with Maven Wrapper) |
| Market data | [Alpha Vantage](https://www.alphavantage.co/) via `alphavantage-java` |
| Data formats | JSON (`org.json`, `json-simple`, Jackson) and CSV |
| Testing | JUnit 5 |

## Project Structure

```
Projet_Finance/
├── src/main/java/
│   ├── front_end_Authentification/       # JavaFX controllers & app entry point
│   │   ├── Accueil/                      # Home screen
│   │   ├── Virement/                     # Accounts & transfers screens
│   │   ├── Actions/                      # Stock trading screens
│   │   ├── Crypto_Front/                 # Crypto trading screens
│   │   ├── Portefeuilles/                # Portfolio creation/management screens
│   │   └── Application.java              # JavaFX application entry point
│   └── com/example/projet_finance/back_end/
│       ├── Actions/                      # Stock domain logic
│       ├── Banque/                       # Bank account domain logic
│       ├── Crypto/                       # Crypto & blockchain domain logic
│       └── Entite/                       # User & portfolio domain logic
├── src/main/resources/                   # FXML layouts for each screen
├── files/                                # Local data store (CSV/JSON)
├── pom.xml
└── mvnw / mvnw.cmd                       # Maven Wrapper
```

## Prerequisites

- **JDK 17** or later (JavaFX 17 dependencies are used)
- **Maven** (or use the bundled Maven Wrapper — no separate install required)
- An **Alpha Vantage API key** (free tier available) for live stock price lookups

## Getting Started

1. **Clone the repository**
   ```bash
   git clone https://github.com/cieldeb/Projet_Finance.git
   cd Projet_Finance
   ```

2. **Configure your API key**
   Add your Alpha Vantage API key to the `files/Clés API` file (used by the app to fetch stock data).

3. **Build the project**
   ```bash
   ./mvnw clean install
   ```

4. **Run the application**
   ```bash
   ./mvnw javafx:run
   ```
   On Windows, use `mvnw.cmd` instead of `./mvnw`.

   Alternatively, run the `Application` class (`front_end_Authentification.Application`) directly from your IDE.

## Data Storage

The app persists its state locally in the `files/` directory:

- `listeInscrits.csv` / `listeinscrits.json` — registered users
- `listecomptes.csv` — bank accounts
- `listedestinataires.csv` — transfer recipients
- `transactions.json` — recorded transactions
- `blockChain.json` / `currentBlock.json` — crypto transaction blockchain state

⚠️ These files are treated as a simple local database — no external database is required to run the project.

## Notes

- This is an academic/learning project: the "blockchain" implementation is a simplified educational model, not intended for production use.
- Some backend classes (e.g. `Banque`, `Obligations`) are placeholders for features that are still in progress.

## Authors

- [Gabgab58](https://github.com/Gabgab58)
- [cieldeb](https://github.com/lcieldeb)
