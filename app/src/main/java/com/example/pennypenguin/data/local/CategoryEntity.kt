package com.example.pennypenguin.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.pennypenguin.domain.model.Category
import com.example.pennypenguin.domain.model.TransactionType

@Entity(tableName = "categories")
data class CategoryEntity(
    @PrimaryKey val id: String,
    val name: String,
    val icon: String,
    val type: TransactionType,
    val isCustom: Boolean = false
)

fun CategoryEntity.toDomain(): Category {
    return Category(
        id = id,
        name = name,
        icon = icon,
        type = type,
        isCustom = isCustom
    )
}

fun Category.toEntity(): CategoryEntity {
    return CategoryEntity(
        id = id,
        name = name,
        icon = icon,
        type = type,
        isCustom = isCustom
    )
}
