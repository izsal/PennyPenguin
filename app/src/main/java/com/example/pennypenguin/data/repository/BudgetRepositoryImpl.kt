package com.example.pennypenguin.data.repository

import com.example.pennypenguin.data.local.BudgetDao
import com.example.pennypenguin.data.local.BudgetEntity
import com.example.pennypenguin.data.local.TransactionDao
import com.example.pennypenguin.domain.model.Budget
import com.example.pennypenguin.domain.model.BudgetWithSpent
import com.example.pennypenguin.domain.repository.BudgetRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import java.time.LocalDateTime
import javax.inject.Inject

class BudgetRepositoryImpl @Inject constructor(
    private val budgetDao: BudgetDao,
    private val transactionDao: TransactionDao
) : BudgetRepository {

    override fun getBudgetsForMonth(month: Int, year: Int): Flow<List<Budget>> {
        return budgetDao.getBudgetsForMonth(month, year).map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override fun getBudgetsWithSpentForMonth(month: Int, year: Int): Flow<List<BudgetWithSpent>> {
        val start = LocalDateTime.of(year, month, 1, 0, 0)
        val end = start.plusMonths(1).minusNanos(1)
        
        return combine(
            budgetDao.getBudgetsForMonth(month, year),
            transactionDao.getCategorySummaries(start, end)
        ) { budgetEntities, summaries ->
            budgetEntities.map { budgetEntity ->
                val spent = summaries.find { it.categoryId == budgetEntity.categoryId }?.totalAmount ?: 0.0
                BudgetWithSpent(budgetEntity.toDomain(), spent)
            }
        }
    }

    override suspend fun insertBudget(budget: Budget) {
        budgetDao.insertBudget(budget.toEntity())
    }

    override suspend fun deleteBudget(budget: Budget) {
        budgetDao.deleteBudget(budget.toEntity())
    }

    override suspend fun getBudgetByCategory(categoryId: String, month: Int, year: Int): Budget? {
        return budgetDao.getBudgetByCategory(categoryId, month, year)?.toDomain()
    }

    private fun BudgetEntity.toDomain() = Budget(id, categoryId, categoryName, categoryIcon, amount, month, year)
    private fun Budget.toEntity() = BudgetEntity(id, categoryId, categoryName, categoryIcon, amount, month, year)
}
