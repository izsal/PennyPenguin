package com.example.pennypenguin.domain.repository

import com.example.pennypenguin.domain.model.User
import kotlinx.coroutines.flow.StateFlow

interface AuthRepository {
    val isAuthenticated: StateFlow<Boolean>
    val currentUser: StateFlow<User?>
    suspend fun signInWithGoogle(idToken: String): Result<Unit>
    suspend fun signOut()
}
