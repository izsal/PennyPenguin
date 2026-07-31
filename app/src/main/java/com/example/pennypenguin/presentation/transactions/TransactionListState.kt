package com.example.pennypenguin.presentation.transactions

import com.example.pennypenguin.domain.model.Transaction

data class TransactionListState(
    val transactions: List<Transaction> = emptyList(),
    val isLoading: Boolean = false
)
