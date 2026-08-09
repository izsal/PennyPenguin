package com.example.pennypenguin.presentation.budget

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pennypenguin.domain.model.Budget
import com.example.pennypenguin.domain.model.Category
import com.example.pennypenguin.domain.repository.BudgetRepository
import com.example.pennypenguin.domain.repository.CategoryRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import javax.inject.Inject

@HiltViewModel
class AddEditBudgetViewModel @Inject constructor(
    private val budgetRepository: BudgetRepository,
    private val categoryRepository: CategoryRepository
) : ViewModel() {

    private val _amount = MutableStateFlow("")
    val amount = _amount.asStateFlow()

    private val _categories = MutableStateFlow<List<Category>>(emptyList())
    val categories = _categories.asStateFlow()

    private val _selectedCategory = MutableStateFlow<Category?>(null)
    val selectedCategory = _selectedCategory.asStateFlow()

    init {
        categoryRepository.getAllCategories().onEach { dbCategories ->
            val all = Category.expenseCategories + dbCategories.filter { it.type == com.example.pennypenguin.domain.model.TransactionType.EXPENSE }
            _categories.value = all
            if (_selectedCategory.value == null && all.isNotEmpty()) {
                _selectedCategory.value = all.first()
            }
        }.launchIn(viewModelScope)
    }

    fun onAmountChange(value: String) {
        _amount.value = value.filter { it.isDigit() }
    }

    fun onCategoryChange(category: Category) {
        _selectedCategory.value = category
    }

    fun saveBudget(onSuccess: () -> Unit) {
        val amountValue = _amount.value.toDoubleOrNull() ?: return
        val category = _selectedCategory.value ?: return
        val now = LocalDateTime.now()

        viewModelScope.launch {
            val budget = Budget(
                categoryId = category.id,
                categoryName = category.name,
                categoryIcon = category.icon,
                amount = amountValue,
                month = now.monthValue,
                year = now.year
            )
            budgetRepository.insertBudget(budget)
            onSuccess()
        }
    }
}
