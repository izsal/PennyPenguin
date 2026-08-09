# Walkthrough - Multiple Wallets (Dompet Ganda)

I have successfully implemented the **Multiple Wallets** feature, allowing you to manage your money across different accounts like Cash, Bank, and E-Wallets.

## Changes Made

### Data Layer
- **Wallet Persistence**: Created `WalletEntity` and `WalletDao` to store account information and balances.
- **Transaction Linking**: Updated `TransactionEntity` to include `walletId` and `walletName`, ensuring every record is tied to a specific wallet.
- **Database Migration**: Incremented `AppDatabase` to version 4.
  > [!WARNING]
  > This update cleared existing data to establish the new wallet-transaction relationship correctly.

### Domain Layer
- **Wallet Model**: Added `Wallet` domain model.
- **Wallet Repository**: Implemented CRUD operations and balance update logic.
- **Balance Logic**: Updated `GetBalanceUseCase` to calculate the total balance as the sum of all wallet balances.

### User Interface
- **Wallet Management**: Added a new **Wallets** screen accessible from the Profile menu. You can add, edit, and delete wallets here.
- **Wallet Selection**: When adding a transaction, you can now select which wallet to use. The wallet's balance will automatically update based on the transaction (Income increases, Expense decreases).
- **Dashboard Updates**:
    - Shows a scrollable list of your wallets and their individual balances.
    - The main balance card now displays the total sum of all your wallets.
- **Transaction History**: Each transaction item now displays the name of the wallet used.

## How to Test

1. **Create Wallets**:
   - Go to **Profile > Wallets**.
   - Tap **+** and create "Cash" with an initial balance (e.g., `500.000`).
   - Create another wallet "Bank BCA" (e.g., `2.000.000`).
2. **Add a Transaction**:
   - Go to **Dashboard** or **Transactions** and tap **+**.
   - Select "Bank BCA" as the wallet.
   - Add an expense (e.g., `100.000`).
3. **Verify Balance**:
   - Return to the **Dashboard**.
   - You should see "Bank BCA" balance decreased to `1.900.000`.
   - The total balance should be `2.400.000` (Cash + Bank BCA).
4. **Delete Transaction**:
   - Delete the transaction from the **Transactions** list.
   - Verify the wallet balance reverts back to its previous state.
