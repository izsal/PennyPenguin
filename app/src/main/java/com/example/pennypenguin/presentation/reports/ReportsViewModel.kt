package com.example.pennypenguin.presentation.reports

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pennypenguin.domain.model.TransactionType
import com.example.pennypenguin.domain.repository.TransactionRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
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
        val startOfMonth = now.withDayOfMonth(1).withHour(0).withMinute(0).withSecond(0)
        val endOfMonth = startOfMonth.plusMonths(1).minusNanos(1)

        combine(
            repository.getMonthlyIncome(now.monthValue, now.year),
            repository.getMonthlyExpense(now.monthValue, now.year),
            repository.getTransactionsByRange(startOfMonth, endOfMonth)
        ) { income, expense, transactions ->
            val dailyTrend = transactions
                .filter { it.type == TransactionType.EXPENSE }
                .groupBy { it.date.dayOfMonth }
                .map { (day, trans) ->
                    DailyTotal(day, trans.sumOf { it.amount })
                }
                .sortedBy { it.day }

            _state.value = ReportsState(
                totalIncome = income,
                totalExpense = expense,
                dailyExpenseTrend = dailyTrend,
                isLoading = false
            )
        }.launchIn(viewModelScope)
    }

    data class ReportsState(
        val totalIncome: Double = 0.0,
        val totalExpense: Double = 0.0,
        val dailyExpenseTrend: List<DailyTotal> = emptyList(),
        val isLoading: Boolean = true
    )

    data class DailyTotal(
        val day: Int,
        val amount: Double
    )
}
