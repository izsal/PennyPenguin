package com.example.pennypenguin.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Dashboard
import androidx.compose.material.icons.filled.PieChart
import androidx.compose.material.icons.filled.ReceiptLong
import androidx.compose.ui.graphics.vector.ImageVector

sealed class Screen(val route: String, val title: String, val icon: ImageVector? = null) {
    data object Auth : Screen("auth", "Authentication")
    data object Dashboard : Screen("dashboard", "Dashboard", Icons.Default.Dashboard)
    data object Transactions : Screen("transactions", "Transactions", Icons.Default.ReceiptLong)
    data object Reports : Screen("reports", "Reports", Icons.Default.PieChart)
    data object Profile : Screen("profile", "Profile", Icons.Default.AccountCircle)
    data object AddEditTransaction : Screen("add_edit_transaction", "Add Transaction", Icons.Default.Add)
    data object Paywall : Screen("paywall", "Premium")
    data object PrivacyPolicy : Screen("privacy_policy", "Privacy Policy")

    companion object {
        fun items() = listOf(Dashboard, Transactions, Reports, Profile)
    }
}
