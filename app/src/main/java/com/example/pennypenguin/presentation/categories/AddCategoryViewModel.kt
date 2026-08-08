package com.example.pennypenguin.presentation.categories

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pennypenguin.domain.model.Category
import com.example.pennypenguin.domain.model.TransactionType
import com.example.pennypenguin.domain.repository.CategoryRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class AddCategoryViewModel @Inject constructor(
    private val repository: CategoryRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val categoryId: String? = savedStateHandle["categoryId"]
    val isEditing = categoryId != null

    private val _name = MutableStateFlow("")
    val name = _name.asStateFlow()

    private val _type = MutableStateFlow(TransactionType.EXPENSE)
    val type = _type.asStateFlow()

    private val _icon = MutableStateFlow("category")
    val icon = _icon.asStateFlow()

    init {
        categoryId?.let { id ->
            viewModelScope.launch {
                repository.getCategoryById(id)?.let { category ->
                    _name.value = category.name
                    _type.value = category.type
                    _icon.value = category.icon
                }
            }
        }
    }

    fun onNameChange(newName: String) {
        _name.value = newName
    }

    fun onTypeChange(newType: TransactionType) {
        _type.value = newType
    }

    fun onIconChange(newIcon: String) {
        _icon.value = newIcon
    }

    fun saveCategory(onSuccess: () -> Unit) {
        if (_name.value.isBlank()) return

        viewModelScope.launch {
            val category = Category(
                id = categoryId ?: UUID.randomUUID().toString(),
                name = _name.value,
                icon = _icon.value,
                type = _type.value,
                isCustom = true
            )
            repository.insertCategory(category)
            onSuccess()
        }
    }
}
