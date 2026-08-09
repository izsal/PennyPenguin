package com.example.pennypenguin.di

import com.example.pennypenguin.data.repository.BudgetRepositoryImpl
import com.example.pennypenguin.data.repository.CategoryRepositoryImpl
import com.example.pennypenguin.data.repository.TransactionRepositoryImpl
import com.example.pennypenguin.data.repository.WalletRepositoryImpl
import com.example.pennypenguin.domain.repository.BudgetRepository
import com.example.pennypenguin.domain.repository.CategoryRepository
import com.example.pennypenguin.domain.repository.TransactionRepository
import com.example.pennypenguin.domain.repository.WalletRepository
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

    @Binds
    @Singleton
    abstract fun bindBudgetRepository(
        budgetRepositoryImpl: BudgetRepositoryImpl
    ): BudgetRepository

    @Binds
    @Singleton
    abstract fun bindWalletRepository(
        walletRepositoryImpl: WalletRepositoryImpl
    ): WalletRepository
}
