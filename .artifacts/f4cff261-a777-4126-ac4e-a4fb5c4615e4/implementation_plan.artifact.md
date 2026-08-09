# Implementation Plan - Multiple Wallets (Dompet Ganda)

The goal is to allow users to manage money across multiple accounts (e.g., Cash, Bank, E-Wallet). This is a core premium feature that provides better financial clarity.

## User Review Required

> [!IMPORTANT]
> This change requires a database migration to version 4. I will enable destructive migration, which will reset your data once more to ensure the new wallet relationships are correctly established.

## Proposed Changes

### Data & Domain Layer

#### [NEW] [WalletEntity.kt](file:///Users/qwarts/AndroidStudioProjects/PennyPenguin/app/src/main/java/com/example/pennypenguin/data/local/WalletEntity.kt)
- Define Room entity for `Wallet` (id, name, balance, icon).

#### [MODIFY] [TransactionEntity.kt](file:///Users/qwarts/AndroidStudioProjects/PennyPenguin/app/src/main/java/com/example/pennypenguin/data/local/TransactionEntity.kt)
- Add `walletId` and `walletName` to link transactions to a specific wallet.

#### [MODIFY] [AppDatabase.kt](file:///Users/qwarts/AndroidStudioProjects/PennyPenguin/app/src/main/java/com/example/pennypenguin/data/local/AppDatabase.kt)
- Register `WalletEntity` and increment version to `4`.

#### [NEW] [WalletRepository.kt](file:///Users/qwarts/AndroidStudioProjects/PennyPenguin/domain/repository/WalletRepository.kt)
- Define interface for wallet operations (Add, Delete, Get All).

### Presentation Layer

#### [MODIFY] [Screen.kt](file:///Users/qwarts/AndroidStudioProjects/PennyPenguin/app/src/main/java/com/example/pennypenguin/navigation/Screen.kt)
- Add routes for `Wallets` management.

#### [NEW] [WalletListScreen.kt](file:///Users/qwarts/AndroidStudioProjects/PennyPenguin/app/src/main/java/com/example/pennypenguin/presentation/wallets/WalletListScreen.kt)
- A screen to view all wallets and their individual balances.

#### [MODIFY] [DashboardScreen.kt](file:///Users/qwarts/AndroidStudioProjects/PennyPenguin/app/src/main/java/com/example/pennypenguin/presentation/dashboard/DashboardScreen.kt)
- Update the balance card to show a summary of all wallets or allow switching between them.

#### [MODIFY] [AddEditTransactionScreen.kt](file:///Users/qwarts/AndroidStudioProjects/PennyPenguin/app/src/main/java/com/example/pennypenguin/presentation/transactions/AddEditTransactionScreen.kt)
- Add a wallet selector so users can choose which account the money is coming from or going to.

#### [MODIFY] [ProfileScreen.kt](file:///Users/qwarts/AndroidStudioProjects/PennyPenguin/app/src/main/java/com/example/pennypenguin/presentation/profile/ProfileScreen.kt)
- Add a "Wallets" menu item.

## Verification Plan

### Manual Verification
1. Navigate to **Profile > Wallets**.
2. Create two wallets: "Cash" and "Bank BCA".
3. Navigate to **Add Transaction**.
4. Create a transaction and select "Bank BCA".
5. Verify the balance of "Bank BCA" updates, but "Cash" remains the same.
6. Verify the **Dashboard** shows the total balance across all wallets.
