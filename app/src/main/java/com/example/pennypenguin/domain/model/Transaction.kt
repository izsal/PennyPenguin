package com.example.pennypenguin.domain.model

import java.time.LocalDateTime

data class Transaction(
    val id: Long = 0,
    val amount: Double,
    val categoryId: String,
    val categoryName: String,
    val categoryIcon: String,
    val type: TransactionType,
    val note: String = "",
    val date: LocalDateTime = LocalDateTime.now()
)
