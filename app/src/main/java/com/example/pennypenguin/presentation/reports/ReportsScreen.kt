package com.example.pennypenguin.presentation.reports

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
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

    val total = state.totalIncome + state.totalExpense
    val incomePercent = if (total > 0) (state.totalIncome / total * 100).toInt() else 50
    val expensePercent = 100 - incomePercent

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
                    shape = MaterialTheme.shapes.extraLarge,
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "Income vs Expense",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f)
                        )
                        Spacer(modifier = Modifier.height(32.dp))
                        
                        CuteDonutChart(
                            income = state.totalIncome,
                            expense = state.totalExpense
                        )
                        
                        Spacer(modifier = Modifier.height(40.dp))
                        
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceEvenly
                        ) {
                            ReportSummaryItem(
                                label = Localization.getString("income", lang),
                                amount = state.totalIncome,
                                percentage = incomePercent,
                                color = Color(0xFF4CAF50),
                                gradientColors = listOf(Color(0xFF4CAF50), Color(0xFF81C784))
                            )
                            ReportSummaryItem(
                                label = Localization.getString("expense", lang),
                                amount = state.totalExpense,
                                percentage = expensePercent,
                                color = Color(0xFFF44336),
                                gradientColors = listOf(Color(0xFFF44336), Color(0xFFFF8A80))
                            )
                        }
                    }
                }
            }

            item {
                Text(
                    text = "Spending Trend",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Start
                )
            }

            item {
                Card(
                    modifier = Modifier.fillMaxWidth().height(250.dp),
                    shape = MaterialTheme.shapes.large,
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                ) {
                    ExpenseTrendChart(data = state.dailyExpenseTrend)
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
    val scaleProgress by animateFloatAsState(
        targetValue = if (animationProgress.value > 0.1f) 1f else 0.8f,
        animationSpec = tween(durationMillis = 800)
    )

    LaunchedEffect(key1 = income, key2 = expense) {
        animationProgress.animateTo(1f, animationSpec = tween(durationMillis = 1200))
    }

    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.scale(scaleProgress)
    ) {
        Canvas(modifier = Modifier.size(220.dp)) {
            val strokeWidth = 34.dp.toPx()
            val innerRadius = (size.minDimension - strokeWidth) / 2
            
            // Draw background glow
            drawCircle(
                color = Color.LightGray.copy(alpha = 0.1f),
                radius = innerRadius + strokeWidth / 2,
                style = Stroke(width = 2.dp.toPx())
            )

            // Income Arc with Gradient
            drawArc(
                brush = Brush.sweepGradient(
                    0f to Color(0xFF4CAF50),
                    0.5f to Color(0xFF81C784),
                    1f to Color(0xFF4CAF50)
                ),
                startAngle = -90f,
                sweepAngle = incomeAngle * animationProgress.value,
                useCenter = false,
                style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
            )

            // Expense Arc with Gradient
            drawArc(
                brush = Brush.sweepGradient(
                    0f to Color(0xFFF44336),
                    0.5f to Color(0xFFFF8A80),
                    1f to Color(0xFFF44336)
                ),
                startAngle = -90f + (incomeAngle * animationProgress.value),
                sweepAngle = expenseAngle * animationProgress.value,
                useCenter = false,
                style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
            )
        }
        
        Surface(
            modifier = Modifier.size(110.dp),
            shape = androidx.compose.foundation.shape.CircleShape,
            color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f),
            shadowElevation = 4.dp
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(text = "Stats", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.primary)
                Text(text = "🐟", fontSize = 32.sp)
            }
        }
    }
}

@Composable
fun ExpenseTrendChart(data: List<ReportsViewModel.DailyTotal>) {
    if (data.isEmpty()) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("Not enough data for this month 🐧", style = MaterialTheme.typography.bodyMedium)
        }
        return
    }

    val maxAmount = remember(data) { data.maxOf { it.amount }.coerceAtLeast(1.0) }
    val points = data.size
    
    Canvas(modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp, vertical = 32.dp)) {
        val width = size.width
        val height = size.height
        val spacing = width / (points.coerceAtLeast(2) - 1).toFloat()
        
        val path = Path()
        val fillPath = Path()
        
        data.forEachIndexed { index, dailyTotal ->
            val x = index * spacing
            val y = height - (dailyTotal.amount / maxAmount * height).toFloat()
            
            if (index == 0) {
                path.moveTo(x, y)
                fillPath.moveTo(x, height)
                fillPath.lineTo(x, y)
            } else {
                // Simplified cubic bezier for "wavy" effect
                val prevX = (index - 1) * spacing
                val prevY = height - (data[index - 1].amount / maxAmount * height).toFloat()
                path.cubicTo(
                    prevX + spacing / 2, prevY,
                    x - spacing / 2, y,
                    x, y
                )
                fillPath.cubicTo(
                    prevX + spacing / 2, prevY,
                    x - spacing / 2, y,
                    x, y
                )
            }
            
            if (index == data.size - 1) {
                fillPath.lineTo(x, height)
                fillPath.close()
            }
        }

        // Draw fill gradient
        drawPath(
            path = fillPath,
            brush = Brush.verticalGradient(
                colors = listOf(Color.Red.copy(alpha = 0.3f), Color.Transparent)
            )
        )

        // Draw line
        drawPath(
            path = path,
            color = Color.Red,
            style = Stroke(width = 3.dp.toPx(), cap = StrokeCap.Round)
        )
        
        // Draw points
        data.forEachIndexed { index, dailyTotal ->
            val x = index * spacing
            val y = height - (dailyTotal.amount / maxAmount * height).toFloat()
            drawCircle(
                color = Color.Red,
                radius = 4.dp.toPx(),
                center = Offset(x, y)
            )
        }
    }
}

@Composable
fun ReportSummaryItem(
    label: String, 
    amount: Double, 
    percentage: Int,
    color: Color,
    gradientColors: List<Color>
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Box(
            modifier = Modifier
                .size(16.dp)
                .padding(2.dp)
        ) {
            Canvas(modifier = Modifier.fillMaxSize()) {
                drawCircle(
                    brush = Brush.linearGradient(gradientColors)
                )
            }
        }
        Text(text = label, style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f))
        Text(
            text = CurrencyUtil.formatRupiah(amount),
            style = MaterialTheme.typography.titleMedium,
            color = color,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = "$percentage%",
            style = MaterialTheme.typography.bodySmall,
            fontWeight = FontWeight.Light,
            color = color.copy(alpha = 0.7f)
        )
    }
}
