package com.example.pennypenguin.presentation.profile

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.pennypenguin.ui.LanguageViewModel
import com.example.pennypenguin.ui.ThemeViewModel
import com.example.pennypenguin.util.Localization

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    onPrivacyPolicyClick: () -> Unit,
    themeViewModel: ThemeViewModel = hiltViewModel(),
    languageViewModel: LanguageViewModel = hiltViewModel()
) {
    val isDarkModePref by themeViewModel.isDarkMode.collectAsState()
    val isDarkMode = isDarkModePref ?: isSystemInDarkTheme()
    val lang by languageViewModel.language.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(Localization.getString("profile", lang)) }
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                Text(
                    text = Localization.getString("appearance", lang),
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.primary
                )
            }
            
            item {
                ListItem(
                    headlineContent = { Text(Localization.getString("dark_mode", lang)) },
                    supportingContent = { Text(if (isDarkMode) "On" else "Off") },
                    leadingContent = {
                        Icon(
                            if (isDarkMode) Icons.Default.DarkMode else Icons.Default.LightMode,
                            contentDescription = null
                        )
                    },
                    trailingContent = {
                        Switch(
                            checked = isDarkMode,
                            onCheckedChange = { themeViewModel.toggleTheme(it) }
                        )
                    }
                )
            }

            item {
                ListItem(
                    headlineContent = { Text(Localization.getString("language", lang)) },
                    supportingContent = { Text(if (lang == "in") "Bahasa Indonesia" else "English") },
                    leadingContent = {
                        Icon(Icons.Default.Language, contentDescription = null)
                    },
                    trailingContent = {
                        Row {
                            FilterChip(
                                selected = lang == "in",
                                onClick = { languageViewModel.setLanguage("in") },
                                label = { Text("IN") }
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            FilterChip(
                                selected = lang == "en",
                                onClick = { languageViewModel.setLanguage("en") },
                                label = { Text("EN") }
                            )
                        }
                    }
                )
            }

            item {
                HorizontalDivider()
            }

            item {
                Text(
                    text = Localization.getString("legal", lang),
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.primary
                )
            }

            item {
                Surface(
                    onClick = onPrivacyPolicyClick,
                    shape = MaterialTheme.shapes.medium,
                    color = MaterialTheme.colorScheme.surface
                ) {
                    ListItem(
                        headlineContent = { Text(Localization.getString("privacy_policy", lang)) },
                        leadingContent = {
                            Icon(Icons.Default.PrivacyTip, contentDescription = null)
                        },
                        trailingContent = {
                            Icon(Icons.Default.ChevronRight, contentDescription = null)
                        }
                    )
                }
            }
        }
    }
}
