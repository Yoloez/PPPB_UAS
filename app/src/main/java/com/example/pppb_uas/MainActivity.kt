package com.example.pppb_uas

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.rememberNavController
import com.example.pppb_uas.navigation.AppNavGraph // Import AppNavGraph yang tadi kita perbaiki
import com.example.pppb_uas.preferences.PreferencesManager
import com.example.pppb_uas.ui.theme.PPPB_UASTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PPPB_UASTheme {
                // 1. Setup NavController & Preferences
                val navController = rememberNavController()
                val context = LocalContext.current
                val preferencesManager = remember { PreferencesManager(context) }

                // 2. Panggil Navigation Graph Utama
                // Ini akan otomatis memuat Login, Dashboard, Dosen, & Course
                // dengan konfigurasi ViewModel yang BENAR.
                AppNavGraph(
                    navController = navController,
                    preferencesManager = preferencesManager
                )
            }
        }
    }
}