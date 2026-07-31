package com.example.pennypenguin

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.pennypenguin.presentation.MainScreen
import com.example.pennypenguin.ui.ThemeViewModel
import com.example.pennypenguin.ui.theme.PennyPenguinTheme
import com.google.firebase.FirebaseApp
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        FirebaseApp.initializeApp(this)
        enableEdgeToEdge()
        setContent {
            val themeViewModel: ThemeViewModel = viewModel()
            val isDarkModePref by themeViewModel.isDarkMode.collectAsState()
            
            val darkTheme = isDarkModePref ?: isSystemInDarkTheme()
            
            PennyPenguinTheme(darkTheme = darkTheme) {
                MainScreen()
            }
        }
    }
}
