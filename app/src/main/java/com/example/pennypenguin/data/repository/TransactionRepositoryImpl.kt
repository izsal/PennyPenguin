package com.example.pennypenguin.data.repository

import com.example.pennypenguin.data.local.TransactionDao
import com.example.pennypenguin.data.local.toDomain
import com.example.pennypenguin.data.local.toEntity
import com.example.pennypenguin.domain.model.CategorySummary
import com.example.pennypenguin.domain.model.Transaction
import com.example.pennypenguin.domain.repository.TransactionRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.time.LocalDateTime
import java.time.LocalTime
import javax.inject.Inject

class TransactionRepositoryImpl @Inject constructor(
    private val dao: TransactionDao
) : TransactionRepository {

    override fun getTransactions(): Flow<List<Transaction>> {
        return dao.getAllTransactions().map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override fun getTransactionsByRange(start: LocalDateTime, end: LocalDateTime): Flow<List<Transaction>> {
        return dao.getTransactionsByRange(start, end).map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override suspend fun getTransactionById(id: Long): Transaction? {
        return dao.getTransactionById(id)?.toDomain()
    }

    override suspend fun insertTransaction(transaction: Transaction) {
        dao.insertTransaction(transaction.toEntity())
    }

    override suspend fun updateTransaction(transaction: Transaction) {
        dao.updateTransaction(transaction.toEntity())
    }

    override suspend fun deleteTransaction(transaction: Transaction) {
        dao.deleteTransaction(transaction.toEntity())
    }

    override fun getBalance(): Flow<Double> {
        return dao.getBalance().map { it ?: 0.0 }
    }

    override fun getMonthlyIncome(month: Int, year: Int): Flow<Double> {
        val start = LocalDateTime.of(year, month, 1, 0, 0)
        val end = start.plusMonths(1).minusNanos(1)
        return dao.getIncomeByRange(start, end).map { it ?: 0.0 }
    }

    override fun getMonthlyExpense(month: Int, year: Int): Flow<Double> {
        val start = LocalDateTime.of(year, month, 1, 0, 0)
        val end = start.plusMonths(1).minusNanos(1)
        return dao.getExpenseByRange(start, end).map { it ?: 0.0 }
    }

    override fun getCategorySummaries(month: Int, year: Int): Flow<List<CategorySummary>> {
        val start = LocalDateTime.of(year, month, 1, 0, 0)
        val end = start.plusMonths(1).minusNanos(1)
        return dao.getCategorySummaries(start, end).map { entities ->
            entities.map {
                CategorySummary(
                    categoryId = it.categoryId,
                    categoryName = it.categoryName,
                    categoryIcon = it.categoryIcon,
                    type = it.type,
                    totalAmount = it.totalAmount
                )
            }
        }
    }
}
