package com.example.pennypenguin.presentation.dashboard

import com.example.pennypenguin.domain.model.BudgetWithSpent
import com.example.pennypenguin.domain.model.Transaction
import com.example.pennypenguin.domain.model.Wallet

data class DashboardState(
    val balance: Double = 0.0,
    val monthlyIncome: Double = 0.0,
    val monthlyExpense: Double = 0.0,
    val recentTransactions: List<Transaction> = emptyList(),
    val wallets: List<Wallet> = emptyList(),
    val budgets: List<BudgetWithSpent> = emptyList(),
    val isLoading: Boolean = false,
    val userName: String = "Buddy",
    val isBalanceVisible: Boolean = true,
    val streak: Int = 5,
    val insightMessage: String = "You've saved 12% more than last week! 👏"
)
