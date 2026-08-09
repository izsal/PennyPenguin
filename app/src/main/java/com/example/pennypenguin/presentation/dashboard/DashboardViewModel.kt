package com.example.pennypenguin.presentation.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pennypenguin.domain.repository.AuthRepository
import com.example.pennypenguin.domain.repository.BudgetRepository
import com.example.pennypenguin.domain.repository.WalletRepository
import com.example.pennypenguin.domain.usecase.GetBalanceUseCase
import com.example.pennypenguin.domain.usecase.GetTransactionsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val getBalanceUseCase: GetBalanceUseCase,
    private val getTransactionsUseCase: GetTransactionsUseCase,
    private val walletRepository: WalletRepository,
    private val budgetRepository: BudgetRepository,
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _state = MutableStateFlow(DashboardState())
    val state: StateFlow<DashboardState> = _state.asStateFlow()

    init {
        loadDashboardData()
    }

    private fun loadDashboardData() {
        val now = LocalDate.now()
        _state.update { it.copy(isLoading = true) }
        
        combine(
            getBalanceUseCase(),
            getTransactionsUseCase(),
            walletRepository.getAllWallets(),
            budgetRepository.getBudgetsWithSpentForMonth(now.monthValue, now.year),
            authRepository.currentUser
        ) { balance, transactions, wallets, budgets, user ->
            _state.update { 
                it.copy(
                    balance = balance,
                    recentTransactions = transactions.take(5),
                    wallets = wallets,
                    budgets = budgets,
                    isLoading = false,
                    userName = user?.name ?: "Buddy"
                )
            }
        }.launchIn(viewModelScope)
    }

    fun toggleBalanceVisibility() {
        _state.update { it.copy(isBalanceVisible = !it.isBalanceVisible) }
    }
}
