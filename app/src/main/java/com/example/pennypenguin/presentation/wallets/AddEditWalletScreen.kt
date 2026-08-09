package com.example.pennypenguin.presentation.wallets

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.pennypenguin.util.IDRVisualTransformation

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEditWalletScreen(
    onPopBackStack: () -> Unit,
    viewModel: AddEditWalletViewModel = hiltViewModel()
) {
    val name by viewModel.name.collectAsState()
    val balance by viewModel.balance.collectAsState()
    val icon by viewModel.icon.collectAsState()
    val isEditing = viewModel.isEditing

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(if (isEditing) "Edit Wallet" else "Add Wallet") },
                navigationIcon = {
                    IconButton(onClick = onPopBackStack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier.fillMaxSize().padding(paddingValues).padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            OutlinedTextField(
                value = name,
                onValueChange = viewModel::onNameChange,
                label = { Text("Wallet Name") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = balance,
                onValueChange = viewModel::onBalanceChange,
                label = { Text("Initial Balance") },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                prefix = { Text("Rp ") },
                visualTransformation = IDRVisualTransformation()
            )

            OutlinedTextField(
                value = icon,
                onValueChange = viewModel::onIconChange,
                label = { Text("Wallet Icon (Emoji)") },
                modifier = Modifier.fillMaxWidth()
            )

            Button(
                onClick = {
                    viewModel.saveWallet {
                        onPopBackStack()
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = name.isNotBlank()
            ) {
                Text(if (isEditing) "Update Wallet" else "Save Wallet")
            }
        }
    }
}
