package com.example.pennypenguin.presentation.categories

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.pennypenguin.domain.model.TransactionType

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddCategoryScreen(
    onPopBackStack: () -> Unit,
    viewModel: AddCategoryViewModel = hiltViewModel()
) {
    val name by viewModel.name.collectAsState()
    val type by viewModel.type.collectAsState()
    val icon by viewModel.icon.collectAsState()
    val isEditing = viewModel.isEditing

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(if (isEditing) "Edit Category" else "Add Category") },
                navigationIcon = {
                    IconButton(onClick = onPopBackStack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            OutlinedTextField(
                value = name,
                onValueChange = viewModel::onNameChange,
                label = { Text("Category Name") },
                modifier = Modifier.fillMaxWidth()
            )

            Text("Type")
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                TransactionType.values().forEach { t ->
                    FilterChip(
                        selected = type == t,
                        onClick = { viewModel.onTypeChange(t) },
                        label = { Text(t.name) }
                    )
                }
            }

            // Placeholder for icon selection
            OutlinedTextField(
                value = icon,
                onValueChange = viewModel::onIconChange,
                label = { Text("Icon Name (Material)") },
                modifier = Modifier.fillMaxWidth()
            )

            Button(
                onClick = {
                    viewModel.saveCategory {
                        onPopBackStack()
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = name.isNotBlank()
            ) {
                Text(if (isEditing) "Update Category" else "Save Category")
            }
        }
    }
}
