package com.example.pennypenguin.data.repository

import com.example.pennypenguin.domain.repository.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor() : AuthRepository {
    private val _isAuthenticated = MutableStateFlow(false)
    override val isAuthenticated: StateFlow<Boolean> = _isAuthenticated.asStateFlow()

    override suspend fun signInWithGoogle(idToken: String): Result<Unit> {
        // Placeholder implementation
        _isAuthenticated.value = true
        return Result.success(Unit)
    }

    override suspend fun signOut() {
        _isAuthenticated.value = false
    }
}
