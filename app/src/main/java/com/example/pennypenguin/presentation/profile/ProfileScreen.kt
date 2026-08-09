package com.example.pennypenguin.presentation.profile

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.example.pennypenguin.presentation.auth.AuthViewModel
import com.example.pennypenguin.ui.LanguageViewModel
import com.example.pennypenguin.ui.ThemeViewModel
import com.example.pennypenguin.util.Localization

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    onPrivacyPolicyClick: () -> Unit,
    onCategoriesClick: () -> Unit,
    onWalletsClick: () -> Unit,
    themeViewModel: ThemeViewModel = hiltViewModel(),
    languageViewModel: LanguageViewModel = hiltViewModel(),
    authViewModel: AuthViewModel = hiltViewModel()
) {
    val isDarkModePref by themeViewModel.isDarkMode.collectAsState()
    val isDarkMode = isDarkModePref ?: isSystemInDarkTheme()
    val lang by languageViewModel.language.collectAsState()
    val user by authViewModel.currentUser.collectAsState()

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
            // User Info Header
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    if (user?.profilePictureUrl != null) {
                        AsyncImage(
                            model = user?.profilePictureUrl,
                            contentDescription = null,
                            modifier = Modifier
                                .size(64.dp)
                                .clip(CircleShape),
                            contentScale = ContentScale.Crop
                        )
                    } else {
                        Surface(
                            modifier = Modifier.size(64.dp),
                            shape = CircleShape,
                            color = MaterialTheme.colorScheme.primaryContainer
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(
                                    Icons.Default.Person,
                                    contentDescription = null,
                                    modifier = Modifier.size(32.dp)
                                )
                            }
                        }
                    }
                    Spacer(modifier = Modifier.width(16.dp))
                    Column {
                        Text(
                            text = user?.name ?: "User",
                            style = MaterialTheme.typography.titleLarge
                        )
                        Text(
                            text = user?.email ?: "",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }

            item {
                HorizontalDivider()
            }

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
                    text = "Management",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.primary
                )
            }

            item {
                Surface(
                    onClick = onCategoriesClick,
                    shape = MaterialTheme.shapes.medium,
                    color = MaterialTheme.colorScheme.surface
                ) {
                    ListItem(
                        headlineContent = { Text("Categories") },
                        leadingContent = {
                            Icon(Icons.Default.Category, contentDescription = null)
                        },
                        trailingContent = {
                            Icon(Icons.Default.ChevronRight, contentDescription = null)
                        }
                    )
                }
            }

            item {
                Surface(
                    onClick = onWalletsClick,
                    shape = MaterialTheme.shapes.medium,
                    color = MaterialTheme.colorScheme.surface
                ) {
                    ListItem(
                        headlineContent = { Text("Wallets") },
                        leadingContent = {
                            Icon(Icons.Default.AccountBalanceWallet, contentDescription = null)
                        },
                        trailingContent = {
                            Icon(Icons.Default.ChevronRight, contentDescription = null)
                        }
                    )
                }
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

            item {
                Spacer(modifier = Modifier.height(16.dp))
                Button(
                    onClick = { authViewModel.signOut() },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.errorContainer,
                        contentColor = MaterialTheme.colorScheme.onErrorContainer
                    ),
                    shape = MaterialTheme.shapes.medium
                ) {
                    Icon(Icons.AutoMirrored.Filled.Logout, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Logout")
                }
            }
        }
    }
}
