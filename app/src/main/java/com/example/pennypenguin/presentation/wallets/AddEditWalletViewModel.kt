package com.example.pennypenguin.presentation.wallets

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pennypenguin.domain.model.Wallet
import com.example.pennypenguin.domain.repository.WalletRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddEditWalletViewModel @Inject constructor(
    private val repository: WalletRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val walletId: Int? = savedStateHandle.get<String>("walletId")?.toIntOrNull()
    val isEditing = walletId != null

    private val _name = MutableStateFlow("")
    val name = _name.asStateFlow()

    private val _balance = MutableStateFlow("")
    val balance = _balance.asStateFlow()

    private val _icon = MutableStateFlow("💳")
    val icon = _icon.asStateFlow()

    private val _backgroundImageUri = MutableStateFlow<String?>(null)
    val backgroundImageUri = _backgroundImageUri.asStateFlow()

    init {
        walletId?.let { id ->
            viewModelScope.launch {
                repository.getWalletById(id)?.let { wallet ->
                    _name.value = wallet.name
                    _balance.value = wallet.balance.toInt().toString()
                    _icon.value = wallet.icon
                    _backgroundImageUri.value = wallet.backgroundImageUri
                }
            }
        }
    }

    fun onNameChange(value: String) {
        _name.value = value
    }

    fun onBalanceChange(value: String) {
        _balance.value = value.filter { it.isDigit() }
    }

    fun onIconChange(value: String) {
        _icon.value = value
    }

    fun onBackgroundImageChange(uri: String?) {
        _backgroundImageUri.value = uri
    }

    fun saveWallet(onSuccess: () -> Unit) {
        if (_name.value.isBlank()) return
        val balanceValue = _balance.value.toDoubleOrNull() ?: 0.0

        viewModelScope.launch {
            val wallet = Wallet(
                id = walletId ?: 0,
                name = _name.value,
                balance = balanceValue,
                icon = _icon.value,
                backgroundImageUri = _backgroundImageUri.value
            )
            if (isEditing) {
                repository.updateWallet(wallet)
            } else {
                repository.insertWallet(wallet)
            }
            onSuccess()
        }
    }
}
