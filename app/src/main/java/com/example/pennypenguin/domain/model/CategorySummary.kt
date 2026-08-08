package com.example.pennypenguin.domain.model

data class CategorySummary(
    val categoryId: String,
    val categoryName: String,
    val categoryIcon: String,
    val type: TransactionType,
    val totalAmount: Double
)
