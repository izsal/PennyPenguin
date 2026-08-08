package com.example.pennypenguin.presentation.reports

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pennypenguin.domain.model.CategorySummary
import com.example.pennypenguin.domain.repository.TransactionRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import java.time.LocalDateTime
import javax.inject.Inject

@HiltViewModel
class CategoryReportsViewModel @Inject constructor(
    private val repository: TransactionRepository
) : ViewModel() {

    private val _summaries = MutableStateFlow<List<CategorySummary>>(emptyList())
    val summaries = _summaries.asStateFlow()

    init {
        loadSummaries()
    }

    private fun loadSummaries() {
        val now = LocalDateTime.now()
        repository.getCategorySummaries(now.monthValue, now.year)
            .onEach {
                _summaries.value = it
            }
            .launchIn(viewModelScope)
    }
}
