package com.example.pennypenguin.presentation.onboarding

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pennypenguin.util.ThemeManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OnboardingViewModel @Inject constructor(
    private val themeManager: ThemeManager
) : ViewModel() {

    val isOnboardingCompleted = themeManager.isOnboardingCompleted.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = false
    )

    fun completeOnboarding(onSuccess: () -> Unit) {
        viewModelScope.launch {
            themeManager.setOnboardingCompleted(true)
            onSuccess()
        }
    }
}
