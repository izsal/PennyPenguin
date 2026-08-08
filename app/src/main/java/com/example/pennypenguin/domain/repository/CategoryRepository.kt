package com.example.pennypenguin.domain.repository

import com.example.pennypenguin.domain.model.Category
import com.example.pennypenguin.domain.model.TransactionType
import kotlinx.coroutines.flow.Flow

interface CategoryRepository {
    fun getAllCategories(): Flow<List<Category>>
    fun getCategoriesByType(type: TransactionType): Flow<List<Category>>
    suspend fun insertCategory(category: Category)
    suspend fun deleteCategory(category: Category)
    suspend fun getCategoryById(id: String): Category?
}
