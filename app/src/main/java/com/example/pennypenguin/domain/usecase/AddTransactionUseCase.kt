package com.example.pennypenguin.domain.usecase

import com.example.pennypenguin.domain.model.Transaction
import com.example.pennypenguin.domain.repository.TransactionRepository
import javax.inject.Inject

class AddTransactionUseCase @Inject constructor(
    private val repository: TransactionRepository
) {
    suspend operator fun invoke(transaction: Transaction) {
        repository.insertTransaction(transaction)
    }
}
