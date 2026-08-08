package com.example.pennypenguin.data.local

import androidx.room.*
import kotlinx.coroutines.flow.Flow
import java.time.LocalDateTime

@Dao
interface TransactionDao {
    @Query("SELECT * FROM transactions ORDER BY date DESC")
    fun getAllTransactions(): Flow<List<TransactionEntity>>

    @Query("SELECT * FROM transactions WHERE date BETWEEN :start AND :end ORDER BY date DESC")
    fun getTransactionsByRange(start: LocalDateTime, end: LocalDateTime): Flow<List<TransactionEntity>>

    @Query("SELECT * FROM transactions WHERE id = :id")
    suspend fun getTransactionById(id: Long): TransactionEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTransaction(transaction: TransactionEntity)

    @Update
    suspend fun updateTransaction(transaction: TransactionEntity)

    @Delete
    suspend fun deleteTransaction(transaction: TransactionEntity)

    @Query("SELECT SUM(CASE WHEN type = 'INCOME' THEN amount ELSE -amount END) FROM transactions")
    fun getBalance(): Flow<Double?>

    @Query("SELECT SUM(amount) FROM transactions WHERE type = 'INCOME' AND date BETWEEN :start AND :end")
    fun getIncomeByRange(start: LocalDateTime, end: LocalDateTime): Flow<Double?>

    @Query("SELECT SUM(amount) FROM transactions WHERE type = 'EXPENSE' AND date BETWEEN :start AND :end")
    fun getExpenseByRange(start: LocalDateTime, end: LocalDateTime): Flow<Double?>

    @Query("SELECT categoryId, categoryName, categoryIcon, type, SUM(amount) as totalAmount FROM transactions WHERE date BETWEEN :start AND :end GROUP BY categoryId")
    fun getCategorySummaries(start: LocalDateTime, end: LocalDateTime): Flow<List<CategorySummaryEntity>>
}

data class CategorySummaryEntity(
    val categoryId: String,
    val categoryName: String,
    val categoryIcon: String,
    val type: com.example.pennypenguin.domain.model.TransactionType,
    val totalAmount: Double
)
