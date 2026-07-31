package com.example.pennypenguin.domain.repository

import kotlinx.coroutines.flow.StateFlow

interface AuthRepository {
    val isAuthenticated: StateFlow<Boolean>
    suspend fun signInWithGoogle(idToken: String): Result<Unit>
    suspend fun signOut()
}
