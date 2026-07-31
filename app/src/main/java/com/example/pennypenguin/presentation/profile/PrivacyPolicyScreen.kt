package com.example.pennypenguin.presentation.profile

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PrivacyPolicyScreen(
    onBackClick: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Privacy Policy") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
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
                    text = "Kebijakan Privasi PennyPenguin",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold
                )
            }
            item {
                Text(
                    text = "Terakhir Diperbarui: 26 Juli 2026",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            item {
                PrivacySection(
                    title = "1. Informasi yang Kami Kumpulkan",
                    content = "PennyPenguin mengumpulkan informasi transaksi keuangan yang Anda masukkan secara manual, termasuk jumlah uang, kategori, dan catatan. Kami juga mengumpulkan data autentikasi melalui layanan pihak ketiga seperti Firebase Auth."
                )
            }
            item {
                PrivacySection(
                    title = "2. Penggunaan Data",
                    content = "Data Anda digunakan semata-mata untuk memberikan fitur pelacakan keuangan, laporan bulanan, dan personalisasi pengalaman pengguna di dalam aplikasi. Kami tidak menjual data Anda kepada pihak ketiga."
                )
            }
            item {
                PrivacySection(
                    title = "3. Penyimpanan Data",
                    content = "Data transaksi Anda disimpan secara lokal di perangkat Anda menggunakan database Room dan mungkin disinkronkan ke server Firebase yang aman jika Anda masuk menggunakan akun Google."
                )
            }
            item {
                PrivacySection(
                    title = "4. Keamanan",
                    content = "Kami berkomitmen untuk melindungi data Anda dengan standar keamanan industri. Namun, harap diingat bahwa tidak ada metode transmisi atau penyimpanan elektronik yang 100% aman."
                )
            }
            item {
                PrivacySection(
                    title = "5. Perubahan Kebijakan",
                    content = "Kami dapat memperbarui Kebijakan Privasi ini dari waktu ke waktu. Perubahan akan diberitahukan melalui pembaruan aplikasi."
                )
            }
            item {
                Text(
                    text = "Jika Anda memiliki pertanyaan tentang Kebijakan Privasi ini, silakan hubungi kami di support@pennypenguin.example.com",
                    style = MaterialTheme.typography.bodyMedium
                )
            }
            item {
                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }
}

@Composable
fun PrivacySection(title: String, content: String) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.SemiBold
        )
        Text(
            text = content,
            style = MaterialTheme.typography.bodyMedium
        )
    }
}
