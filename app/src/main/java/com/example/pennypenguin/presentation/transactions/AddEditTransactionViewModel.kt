package com.example.pennypenguin.presentation.transactions

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pennypenguin.domain.model.Category
import com.example.pennypenguin.domain.model.Transaction
import com.example.pennypenguin.domain.model.TransactionType
import com.example.pennypenguin.domain.usecase.AddTransactionUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddEditTransactionViewModel @Inject constructor(
    private val addTransactionUseCase: AddTransactionUseCase
) : ViewModel() {

    private val _amount = MutableStateFlow("")
    val amount = _amount.asStateFlow()

    private val _note = MutableStateFlow("")
    val note = _note.asStateFlow()

    private val _type = MutableStateFlow(TransactionType.EXPENSE)
    val type = _type.asStateFlow()

    private val _category = MutableStateFlow(Category.expenseCategories.first())
    val category = _category.asStateFlow()

    private val _eventFlow = MutableSharedFlow<UiEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    fun onAmountChange(value: String) {
        _amount.value = value
    }

    fun onNoteChange(value: String) {
        _note.value = value
    }

    fun onTypeChange(value: TransactionType) {
        _type.value = value
        _category.value = if (value == TransactionType.INCOME) {
            Category.incomeCategories.first()
        } else {
            Category.expenseCategories.first()
        }
    }

    fun onCategoryChange(value: Category) {
        _category.value = value
    }

    fun saveTransaction() {
        val amountValue = _amount.value.toDoubleOrNull() ?: return
        
        viewModelScope.launch {
            addTransactionUseCase(
                Transaction(
                    amount = amountValue,
                    categoryId = _category.value.id,
                    categoryName = _category.value.name,
                    categoryIcon = _category.value.icon,
                    type = _type.value,
                    note = _note.value
                )
            )
            _eventFlow.emit(UiEvent.SaveTransaction)
        }
    }

    sealed class UiEvent {
        object SaveTransaction : UiEvent()
    }
}
