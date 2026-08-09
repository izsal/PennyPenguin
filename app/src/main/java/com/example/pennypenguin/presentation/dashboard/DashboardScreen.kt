package com.example.pennypenguin.presentation.dashboard

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.pennypenguin.domain.model.Transaction
import com.example.pennypenguin.domain.model.TransactionType
import com.example.pennypenguin.ui.LanguageViewModel
import com.example.pennypenguin.util.CurrencyUtil
import com.example.pennypenguin.util.Localization
import java.time.format.DateTimeFormatter

@Composable
fun DashboardScreen(
    onSeeAllClick: () -> Unit,
    viewModel: DashboardViewModel = hiltViewModel(),
    languageViewModel: LanguageViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()
    val lang by languageViewModel.language.collectAsState()

    Scaffold(
        modifier = Modifier.fillMaxSize()
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                GreetingSection(state.userName, lang)
            }
            item {
                BalanceCard(state.balance, state.monthlyIncome, state.monthlyExpense, lang)
            }
            if (state.wallets.isNotEmpty()) {
                item {
                    Text(
                        text = "My Wallets",
                        style = MaterialTheme.typography.titleLarge,
                        modifier = Modifier.padding(top = 8.dp)
                    )
                    LazyRow(
                        modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(state.wallets) { wallet ->
                            WalletSummaryCard(wallet)
                        }
                    }
                }
            }
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = Localization.getString("recent_transactions", lang),
                        style = MaterialTheme.typography.titleLarge
                    )
                    TextButton(onClick = onSeeAllClick) {
                        Text(Localization.getString("see_all", lang))
                    }
                }
            }
            items(state.recentTransactions) { transaction ->
                TransactionItem(transaction)
            }
        }
    }
}

@Composable
fun GreetingSection(userName: String, lang: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = Localization.getString("waddle_back", lang).format(userName),
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.primary
            )
            Text(
                text = Localization.getString("track_fish", lang),
                style = MaterialTheme.typography.bodyLarge
            )
        }
        Text(text = "🐧", fontSize = 48.sp)
    }
}

@Composable
fun BalanceCard(balance: Double, income: Double, expense: Double, lang: String) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.large,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        )
    ) {
        Column(
            modifier = Modifier.padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = Localization.getString("total_balance", lang), style = MaterialTheme.typography.labelMedium)
            Text(
                text = CurrencyUtil.formatRupiah(balance),
                style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Black),
                fontSize = 36.sp
            )
            Spacer(modifier = Modifier.height(16.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                SummaryItem(Localization.getString("income", lang), income, Color(0xFF4CAF50))
                SummaryItem(Localization.getString("expense", lang), expense, Color.Red)
            }
        }
    }
}

@Composable
fun SummaryItem(label: String, amount: Double, color: Color) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = label, style = MaterialTheme.typography.labelMedium)
        Text(
            text = CurrencyUtil.formatRupiah(amount),
            style = MaterialTheme.typography.titleLarge,
            color = color
        )
    }
}

@Composable
fun WalletSummaryCard(wallet: com.example.pennypenguin.domain.model.Wallet) {
    Card(
        modifier = Modifier.width(160.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = wallet.icon, fontSize = 24.sp)
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = wallet.name, style = MaterialTheme.typography.titleSmall, maxLines = 1)
            Text(
                text = CurrencyUtil.formatRupiah(wallet.balance),
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
        }
    }
}

@Composable
fun TransactionItem(transaction: Transaction) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.medium,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Surface(
                modifier = Modifier.size(40.dp),
                shape = MaterialTheme.shapes.small,
                color = MaterialTheme.colorScheme.secondaryContainer
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Text(text = if (transaction.type == TransactionType.INCOME) "💰" else "🛒")
                }
            }
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(text = transaction.categoryName, style = MaterialTheme.typography.titleLarge, fontSize = 16.sp)
                Text(
                    text = "${transaction.walletName}${if (transaction.note.isNotBlank()) " • ${transaction.note}" else ""}",
                    style = MaterialTheme.typography.bodyLarge,
                    fontSize = 12.sp,
                    color = Color.Gray,
                    maxLines = 1
                )
            }
            Text(
                text = "${if (transaction.type == TransactionType.INCOME) "+" else "-"}${CurrencyUtil.formatRupiah(transaction.amount)}",
                style = MaterialTheme.typography.titleLarge,
                fontSize = 16.sp,
                color = if (transaction.type == TransactionType.INCOME) Color(0xFF4CAF50) else Color.Red
            )
        }
    }
}
