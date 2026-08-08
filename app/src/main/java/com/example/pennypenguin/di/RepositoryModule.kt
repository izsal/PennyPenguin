package com.example.pennypenguin.di

import com.example.pennypenguin.data.repository.CategoryRepositoryImpl
import com.example.pennypenguin.data.repository.TransactionRepositoryImpl
import com.example.pennypenguin.domain.repository.CategoryRepository
import com.example.pennypenguin.domain.repository.TransactionRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindTransactionRepository(
        transactionRepositoryImpl: TransactionRepositoryImpl
    ): TransactionRepository

    @Binds
    @Singleton
    abstract fun bindCategoryRepository(
        categoryRepositoryImpl: CategoryRepositoryImpl
    ): CategoryRepository
}
