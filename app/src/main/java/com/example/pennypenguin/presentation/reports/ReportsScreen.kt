package com.example.pennypenguin.presentation.reports

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.pennypenguin.ui.LanguageViewModel
import com.example.pennypenguin.util.CurrencyUtil
import com.example.pennypenguin.util.Localization

@Composable
fun ReportsScreen(
    onSeeCategoryReportsClick: () -> Unit,
    viewModel: ReportsViewModel = hiltViewModel(),
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
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = Localization.getString("reports", lang),
                        style = MaterialTheme.typography.headlineMedium,
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.Bold
                    )
                    TextButton(onClick = onSeeCategoryReportsClick) {
                        Text("Category Breakdown")
                    }
                }
            }

            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = MaterialTheme.shapes.large,
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                ) {
                    Column(
                        modifier = Modifier.padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "Income vs Expense",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(32.dp))
                        
                        CuteDonutChart(
                            income = state.totalIncome,
                            expense = state.totalExpense
                        )
                        
                        Spacer(modifier = Modifier.height(32.dp))
                        
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceEvenly
                        ) {
                            ReportSummaryItem(
                                label = Localization.getString("income", lang),
                                amount = state.totalIncome,
                                color = Color(0xFF4CAF50)
                            )
                            ReportSummaryItem(
                                label = Localization.getString("expense", lang),
                                amount = state.totalExpense,
                                color = Color.Red
                            )
                        }
                    }
                }
            }
            
            item {
                Text(
                    text = "Waddle forward! You're doing great.",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.secondary
                )
                Text(text = "🐧", fontSize = 64.sp)
            }
        }
    }
}

@Composable
fun CuteDonutChart(income: Double, expense: Double) {
    val total = income + expense
    val incomeAngle = if (total > 0) (income / total * 360f).toFloat() else 180f
    val expenseAngle = 360f - incomeAngle

    val animationProgress = remember { Animatable(0f) }
    LaunchedEffect(key1 = income, key2 = expense) {
        animationProgress.animateTo(1f, animationSpec = tween(durationMillis = 1000))
    }

    Box(contentAlignment = Alignment.Center) {
        Canvas(modifier = Modifier.size(200.dp)) {
            val strokeWidth = 30.dp.toPx()
            val innerRadius = (size.minDimension - strokeWidth) / 2
            
            // Draw background circle
            drawCircle(
                color = Color.LightGray.copy(alpha = 0.2f),
                radius = innerRadius,
                style = Stroke(width = strokeWidth)
            )

            // Draw Income part
            drawArc(
                color = Color(0xFF4CAF50),
                startAngle = -90f,
                sweepAngle = incomeAngle * animationProgress.value,
                useCenter = false,
                style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
            )

            // Draw Expense part
            drawArc(
                color = Color.Red,
                startAngle = -90f + (incomeAngle * animationProgress.value),
                sweepAngle = expenseAngle * animationProgress.value,
                useCenter = false,
                style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
            )
        }
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(text = "Stats", style = MaterialTheme.typography.labelSmall)
            Text(text = "🐟", fontSize = 24.sp)
        }
    }
}

@Composable
fun ReportSummaryItem(label: String, amount: Double, color: Color) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Box(modifier = Modifier.size(12.dp).padding(2.dp)) {
            Canvas(modifier = Modifier.fillMaxSize()) {
                drawCircle(color = color)
            }
        }
        Text(text = label, style = MaterialTheme.typography.labelMedium)
        Text(
            text = CurrencyUtil.formatRupiah(amount),
            style = MaterialTheme.typography.titleMedium,
            color = color,
            fontWeight = FontWeight.Bold
        )
    }
}
