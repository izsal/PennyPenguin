package com.example.pennypenguin.domain.repository

import com.example.pennypenguin.domain.model.Transaction
import kotlinx.coroutines.flow.Flow
import java.time.LocalDateTime

interface TransactionRepository {
    fun getTransactions(): Flow<List<Transaction>>
    
    fun getTransactionsByRange(start: LocalDateTime, end: LocalDateTime): Flow<List<Transaction>>
    
    suspend fun getTransactionById(id: Long): Transaction?
    
    suspend fun insertTransaction(transaction: Transaction)
    
    suspend fun updateTransaction(transaction: Transaction)
    
    suspend fun deleteTransaction(transaction: Transaction)
    
    fun getBalance(): Flow<Double>
    
    fun getMonthlyIncome(month: Int, year: Int): Flow<Double>
    
    fun getMonthlyExpense(month: Int, year: Int): Flow<Double>
}
