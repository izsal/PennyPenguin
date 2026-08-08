package com.example.pennypenguin.data.repository

import com.example.pennypenguin.data.local.CategoryDao
import com.example.pennypenguin.data.local.toDomain
import com.example.pennypenguin.data.local.toEntity
import com.example.pennypenguin.domain.model.Category
import com.example.pennypenguin.domain.model.TransactionType
import com.example.pennypenguin.domain.repository.CategoryRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class CategoryRepositoryImpl @Inject constructor(
    private val dao: CategoryDao
) : CategoryRepository {

    override fun getAllCategories(): Flow<List<Category>> {
        return dao.getAllCategories().map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override fun getCategoriesByType(type: TransactionType): Flow<List<Category>> {
        return dao.getCategoriesByType(type.name).map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override suspend fun insertCategory(category: Category) {
        dao.insertCategory(category.toEntity())
    }

    override suspend fun deleteCategory(category: Category) {
        dao.deleteCategory(category.toEntity())
    }

    override suspend fun getCategoryById(id: String): Category? {
        return dao.getCategoryById(id)?.toDomain()
    }
}
