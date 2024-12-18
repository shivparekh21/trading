# Trading App
Trading Platform Management System

Overview

This project implements a comprehensive trading platform featuring secure wallet transactions, user authentication, trading functionality, and real-time coin market integration. It supports wallet balance management, user registration, trading operations, and transaction history tracking.

Features

1. User Management

User Registration & Authentication: JWT-based authentication for secure access.

Two-Factor Authentication (2FA): Implements OTP verification for enhanced security.

Forgot Password: Supports password recovery through OTP-based verification.

User Profile Fetching: Retrieves user details using JWT tokens.

2. Wallet Management

Automatic Wallet Creation: A wallet is created automatically when a user registers.

Wallet Balance Retrieval: Users can view wallet balances securely.

3. Trading Operations

Buy/Sell Coins: Execute buy/sell orders for various cryptocurrencies.

Order Tracking: Monitor pending, completed, and canceled orders.

Market Data Integration: Fetch real-time coin prices using external APIs.

4. Fund Transfers

Wallet-to-Wallet Transfer: Transfers funds between user wallets.

Balance Validation: Ensures sufficient funds before processing transfers.

Transaction Logging: Logs every transfer for auditing and reporting.

5. Transaction Management

Transaction History Tracking: Keeps records of all trading and wallet transactions.

Transaction Details: Tracks sender, receiver, amount, date, and order details.

6. Coin Market Integration

Coin Data Fetching: Retrieves real-time coin prices from external APIs.

Market Analysis Tools: Provides tools for analyzing coin price trends.