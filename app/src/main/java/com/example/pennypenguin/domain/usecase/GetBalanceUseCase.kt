package com.example.pennypenguin.domain.usecase

import com.example.pennypenguin.domain.repository.TransactionRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetBalanceUseCase @Inject constructor(
    private val repository: TransactionRepository
) {
    operator fun invoke(): Flow<Double> {
        return repository.getBalance()
    }
}
