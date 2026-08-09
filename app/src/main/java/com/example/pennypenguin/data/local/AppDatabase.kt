package com.example.pennypenguin.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(
    entities = [TransactionEntity::class, CategoryEntity::class, BudgetEntity::class, WalletEntity::class],
    version = 4,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract val transactionDao: TransactionDao
    abstract val categoryDao: CategoryDao
    abstract val budgetDao: BudgetDao
    abstract val walletDao: WalletDao
    
    companion object {
        const val DATABASE_NAME = "penny_penguin_db"
    }
}
