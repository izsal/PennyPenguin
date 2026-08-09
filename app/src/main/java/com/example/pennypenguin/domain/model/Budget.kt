package com.example.pennypenguin.domain.model

data class Budget(
    val id: Int = 0,
    val categoryId: String,
    val categoryName: String,
    val categoryIcon: String,
    val amount: Double,
    val month: Int,
    val year: Int
)

data class BudgetWithSpent(
    val budget: Budget,
    val spent: Double
) {
    val progress: Float
        get() = if (budget.amount > 0) (spent / budget.amount).toFloat() else 0f
    
    val isOverBudget: Boolean
        get() = spent > budget.amount
}
