package com.example.pennypenguin.presentation.dashboard

import com.example.pennypenguin.domain.model.Transaction
import com.example.pennypenguin.domain.model.Wallet

data class DashboardState(
    val balance: Double = 0.0,
    val monthlyIncome: Double = 0.0,
    val monthlyExpense: Double = 0.0,
    val recentTransactions: List<Transaction> = emptyList(),
    val wallets: List<Wallet> = emptyList(),
    val isLoading: Boolean = false,
    val userName: String = "Buddy"
)
