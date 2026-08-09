package com.example.pennypenguin.presentation.transactions

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pennypenguin.domain.model.Category
import com.example.pennypenguin.domain.model.Transaction
import com.example.pennypenguin.domain.model.TransactionType
import com.example.pennypenguin.domain.model.Wallet
import com.example.pennypenguin.domain.repository.CategoryRepository
import com.example.pennypenguin.domain.repository.WalletRepository
import com.example.pennypenguin.domain.usecase.AddTransactionUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddEditTransactionViewModel @Inject constructor(
    private val addTransactionUseCase: AddTransactionUseCase,
    private val categoryRepository: CategoryRepository,
    private val walletRepository: WalletRepository
) : ViewModel() {

    private val _amount = MutableStateFlow("")
    val amount = _amount.asStateFlow()

    private val _note = MutableStateFlow("")
    val note = _note.asStateFlow()

    private val _type = MutableStateFlow(TransactionType.EXPENSE)
    val type = _type.asStateFlow()

    private val _category = MutableStateFlow(Category.expenseCategories.first())
    val category = _category.asStateFlow()

    private val _categories = MutableStateFlow<List<Category>>(emptyList())
    val categories = combine(_categories, _type) { categories, type ->
        categories.filter { it.type == type }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private val _wallets = MutableStateFlow<List<Wallet>>(emptyList())
    val wallets = _wallets.asStateFlow()

    private val _selectedWallet = MutableStateFlow<Wallet?>(null)
    val selectedWallet = _selectedWallet.asStateFlow()

    private val _eventFlow = MutableSharedFlow<UiEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    init {
        categoryRepository.getAllCategories().onEach { dbCategories ->
            _categories.value = Category.allDefaults + dbCategories
        }.launchIn(viewModelScope)

        walletRepository.getAllWallets().onEach { dbWallets ->
            _wallets.value = dbWallets
            if (_selectedWallet.value == null && dbWallets.isNotEmpty()) {
                _selectedWallet.value = dbWallets.first()
            }
        }.launchIn(viewModelScope)
    }

    fun onAmountChange(value: String) {
        val numericValue = value.filter { it.isDigit() }
        _amount.value = numericValue
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

    fun onWalletChange(value: Wallet) {
        _selectedWallet.value = value
    }

    fun saveTransaction() {
        val amountValue = _amount.value.toDoubleOrNull() ?: return
        val wallet = _selectedWallet.value ?: return
        
        viewModelScope.launch {
            addTransactionUseCase(
                Transaction(
                    amount = amountValue,
                    categoryId = _category.value.id,
                    categoryName = _category.value.name,
                    categoryIcon = _category.value.icon,
                    walletId = wallet.id,
                    walletName = wallet.name,
                    type = _type.value,
                    note = _note.value
                )
            )
            // Update wallet balance
            val balanceDiff = if (_type.value == TransactionType.INCOME) amountValue else -amountValue
            walletRepository.updateBalance(wallet.id, balanceDiff)
            
            _eventFlow.emit(UiEvent.SaveTransaction)
        }
    }

    sealed class UiEvent {
        object SaveTransaction : UiEvent()
    }
}
