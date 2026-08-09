package com.example.pennypenguin.presentation.budget

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pennypenguin.domain.model.BudgetWithSpent
import com.example.pennypenguin.domain.repository.BudgetRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import javax.inject.Inject

@HiltViewModel
class BudgetViewModel @Inject constructor(
    private val repository: BudgetRepository
) : ViewModel() {

    private val _budgets = MutableStateFlow<List<BudgetWithSpent>>(emptyList())
    val budgets = _budgets.asStateFlow()

    init {
        loadBudgets()
    }

    private fun loadBudgets() {
        val now = LocalDateTime.now()
        repository.getBudgetsWithSpentForMonth(now.monthValue, now.year)
            .onEach {
                _budgets.value = it
            }
            .launchIn(viewModelScope)
    }

    fun deleteBudget(budget: BudgetWithSpent) {
        viewModelScope.launch {
            repository.deleteBudget(budget.budget)
        }
    }
}
