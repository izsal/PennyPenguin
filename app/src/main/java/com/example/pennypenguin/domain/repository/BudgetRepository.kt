package com.example.pennypenguin.domain.repository

import com.example.pennypenguin.domain.model.Budget
import com.example.pennypenguin.domain.model.BudgetWithSpent
import kotlinx.coroutines.flow.Flow

interface BudgetRepository {
    fun getBudgetsForMonth(month: Int, year: Int): Flow<List<Budget>>
    fun getBudgetsWithSpentForMonth(month: Int, year: Int): Flow<List<BudgetWithSpent>>
    suspend fun insertBudget(budget: Budget)
    suspend fun deleteBudget(budget: Budget)
    suspend fun getBudgetByCategory(categoryId: String, month: Int, year: Int): Budget?
}
