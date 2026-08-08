package com.example.pennypenguin.presentation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.navigation.NavType
import com.example.pennypenguin.navigation.Screen
import com.example.pennypenguin.presentation.auth.AuthScreen
import com.example.pennypenguin.presentation.auth.AuthViewModel
import com.example.pennypenguin.presentation.categories.AddCategoryScreen
import com.example.pennypenguin.presentation.categories.CategoryListScreen
import com.example.pennypenguin.presentation.dashboard.DashboardScreen
import com.example.pennypenguin.presentation.profile.PrivacyPolicyScreen
import com.example.pennypenguin.presentation.profile.ProfileScreen
import com.example.pennypenguin.presentation.reports.CategoryReportsScreen
import com.example.pennypenguin.presentation.reports.ReportsScreen
import com.example.pennypenguin.presentation.transactions.AddEditTransactionScreen
import com.example.pennypenguin.presentation.transactions.TransactionListScreen
import com.example.pennypenguin.ui.LanguageViewModel
import com.example.pennypenguin.util.Localization

@Composable
fun MainScreen(
    authViewModel: AuthViewModel = hiltViewModel(),
    languageViewModel: LanguageViewModel = hiltViewModel()
) {
    val isAuthenticated by authViewModel.isAuthenticated.collectAsState()
    val lang by languageViewModel.language.collectAsState()
    val navController = rememberNavController()

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination
    val currentRoute = currentDestination?.route

    LaunchedEffect(isAuthenticated) {
        if (isAuthenticated) {
            if (currentRoute == Screen.Auth.route) {
                navController.navigate(Screen.Dashboard.route) {
                    popUpTo(Screen.Auth.route) { inclusive = true }
                }
            }
        } else {
            if (currentRoute != Screen.Auth.route) {
                navController.navigate(Screen.Auth.route) {
                    popUpTo(0) { inclusive = true }
                }
            }
        }
    }

    Scaffold(
        bottomBar = {
            val bottomNavItems = Screen.items()
            val showBottomBar = bottomNavItems.any { it.route == currentRoute }
            
            if (showBottomBar) {
                NavigationBar {
                    bottomNavItems.forEach { screen ->
                        NavigationBarItem(
                            icon = {
                                screen.icon?.let { icon ->
                                    Icon(icon, contentDescription = null)
                                }
                            },
                            label = { Text(Localization.getString(screen.route, lang)) },
                            selected = currentDestination?.hierarchy?.any { it.route == screen.route } == true,
                            onClick = {
                                navController.navigate(screen.route) {
                                    popUpTo(navController.graph.findStartDestination().id) {
                                        saveState = true
                                    }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            }
                        )
                    }
                }
            }
        },
        floatingActionButton = {
            val showFab = currentRoute == Screen.Dashboard.route || currentRoute == Screen.Transactions.route
            if (showFab) {
                FloatingActionButton(onClick = { navController.navigate(Screen.AddEditTransaction.route) }) {
                    Screen.AddEditTransaction.icon?.let { icon ->
                        Icon(icon, contentDescription = null)
                    }
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Screen.Auth.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(Screen.Auth.route) {
                AuthScreen(onSignInClick = { authViewModel.onSignInResult("placeholder_token") })
            }
            composable(Screen.Dashboard.route) {
                DashboardScreen(
                    onSeeAllClick = { navController.navigate(Screen.Transactions.route) }
                )
            }
            composable(Screen.Transactions.route) {
                TransactionListScreen()
            }
            composable(Screen.Reports.route) {
                ReportsScreen(
                    onSeeCategoryReportsClick = { navController.navigate(Screen.CategoryReports.route) }
                )
            }
            composable(Screen.CategoryReports.route) {
                CategoryReportsScreen(
                    onPopBackStack = { navController.popBackStack() }
                )
            }
            composable(Screen.Profile.route) {
                ProfileScreen(
                    onPrivacyPolicyClick = { navController.navigate(Screen.PrivacyPolicy.route) },
                    onCategoriesClick = { navController.navigate(Screen.Categories.route) }
                )
            }
            composable(Screen.Categories.route) {
                CategoryListScreen(
                    onBackClick = { navController.popBackStack() },
                    onAddCategoryClick = { navController.navigate("add_category") },
                    onEditCategoryClick = { id -> navController.navigate("add_category?categoryId=$id") }
                )
            }
            composable(Screen.PrivacyPolicy.route) {
                PrivacyPolicyScreen(
                    onBackClick = { navController.popBackStack() }
                )
            }
            composable(Screen.AddEditTransaction.route) {
                AddEditTransactionScreen(
                    onPopBackStack = { navController.popBackStack() }
                )
            }
            composable(
                route = Screen.AddCategory.route,
                arguments = listOf(
                    navArgument("categoryId") {
                        type = NavType.StringType
                        nullable = true
                        defaultValue = null
                    }
                )
            ) {
                AddCategoryScreen(
                    onPopBackStack = { navController.popBackStack() }
                )
            }
        }
    }
}
