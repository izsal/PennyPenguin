package com.example.pennypenguin.presentation.reports

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pennypenguin.domain.repository.TransactionRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import java.time.LocalDateTime
import javax.inject.Inject

@HiltViewModel
class ReportsViewModel @Inject constructor(
    private val repository: TransactionRepository
) : ViewModel() {

    private val _state = MutableStateFlow(ReportsState())
    val state = _state.asStateFlow()

    init {
        loadReportData()
    }

    private fun loadReportData() {
        val now = LocalDateTime.now()
        combine(
            repository.getMonthlyIncome(now.monthValue, now.year),
            repository.getMonthlyExpense(now.monthValue, now.year)
        ) { income, expense ->
            _state.value = ReportsState(
                totalIncome = income,
                totalExpense = expense,
                isLoading = false
            )
        }.launchIn(viewModelScope)
    }

    data class ReportsState(
        val totalIncome: Double = 0.0,
        val totalExpense: Double = 0.0,
        val isLoading: Boolean = true
    )
}
