package com.example.pennypenguin.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.pennypenguin.domain.model.Transaction
import com.example.pennypenguin.domain.model.TransactionType
import java.time.LocalDateTime

@Entity(tableName = "transactions")
data class TransactionEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val amount: Double,
    val categoryId: String,
    val categoryName: String,
    val categoryIcon: String,
    val type: TransactionType,
    val note: String,
    val date: LocalDateTime
)

fun TransactionEntity.toDomain(): Transaction {
    return Transaction(
        id = id,
        amount = amount,
        categoryId = categoryId,
        categoryName = categoryName,
        categoryIcon = categoryIcon,
        type = type,
        note = note,
        date = date
    )
}

fun Transaction.toEntity(): TransactionEntity {
    return TransactionEntity(
        id = id,
        amount = amount,
        categoryId = categoryId,
        categoryName = categoryName,
        categoryIcon = categoryIcon,
        type = type,
        note = note,
        date = date
    )
}
