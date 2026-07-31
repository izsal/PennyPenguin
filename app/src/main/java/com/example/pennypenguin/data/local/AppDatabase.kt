package com.example.pennypenguin.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(entities = [TransactionEntity::class], version = 1, exportSchema = false)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract val transactionDao: TransactionDao
    
    companion object {
        const val DATABASE_NAME = "penny_penguin_db"
    }
}
