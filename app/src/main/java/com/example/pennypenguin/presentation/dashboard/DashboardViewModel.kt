package com.example.pennypenguin.presentation.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pennypenguin.domain.repository.WalletRepository
import com.example.pennypenguin.domain.usecase.GetBalanceUseCase
import com.example.pennypenguin.domain.usecase.GetTransactionsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import javax.inject.Inject

@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val getBalanceUseCase: GetBalanceUseCase,
    private val getTransactionsUseCase: GetTransactionsUseCase,
    private val walletRepository: WalletRepository
) : ViewModel() {

    private val _state = MutableStateFlow(DashboardState())
    val state: StateFlow<DashboardState> = _state.asStateFlow()

    init {
        loadDashboardData()
    }

    private fun loadDashboardData() {
        _state.update { it.copy(isLoading = true) }
        
        combine(
            getBalanceUseCase(),
            getTransactionsUseCase(),
            walletRepository.getAllWallets()
        ) { balance, transactions, wallets ->
            _state.update { 
                it.copy(
                    balance = balance,
                    recentTransactions = transactions.take(5),
                    wallets = wallets,
                    isLoading = false
                )
            }
        }.launchIn(viewModelScope)
    }
}
