package com.example.pennypenguin.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "budgets")
data class BudgetEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val categoryId: String,
    val categoryName: String,
    val categoryIcon: String,
    val amount: Double,
    val month: Int,
    val year: Int
)
