package com.example.pennypenguin.presentation.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pennypenguin.domain.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val repository: AuthRepository
) : ViewModel() {

    val isAuthenticated = repository.isAuthenticated
    val currentUser = repository.currentUser

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    fun onSignInResult(idToken: String?) {
        if (idToken != null) {
            viewModelScope.launch {
                _isLoading.value = true
                val result = repository.signInWithGoogle(idToken)
                _isLoading.value = false
                result.onFailure {
                    _error.value = it.message ?: "Sign in failed"
                }
            }
        } else {
            _error.value = "Sign in failed: ID Token is null"
        }
    }

    fun setError(message: String) {
        _error.value = message
    }

    fun clearError() {
        _error.value = null
    }

    fun signOut() {
        viewModelScope.launch {
            repository.signOut()
        }
    }
}
