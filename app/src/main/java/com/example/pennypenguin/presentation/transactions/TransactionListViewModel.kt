package com.example.pennypenguin.presentation.transactions

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pennypenguin.domain.model.Transaction
import com.example.pennypenguin.domain.model.TransactionType
import com.example.pennypenguin.domain.repository.TransactionRepository
import com.example.pennypenguin.domain.repository.WalletRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TransactionListViewModel @Inject constructor(
    private val repository: TransactionRepository,
    private val walletRepository: WalletRepository
) : ViewModel() {

    private val _state = MutableStateFlow(TransactionListState())
    val state = _state.asStateFlow()

    init {
        getTransactions()
    }

    private fun getTransactions() {
        repository.getTransactions()
            .onEach { transactions ->
                _state.value = state.value.copy(
                    transactions = transactions
                )
            }.launchIn(viewModelScope)
    }

    fun deleteTransaction(transaction: Transaction) {
        viewModelScope.launch {
            repository.deleteTransaction(transaction)
            // Revert wallet balance
            val balanceDiff = if (transaction.type == TransactionType.INCOME) -transaction.amount else transaction.amount
            walletRepository.updateBalance(transaction.walletId, balanceDiff)
        }
    }
}
