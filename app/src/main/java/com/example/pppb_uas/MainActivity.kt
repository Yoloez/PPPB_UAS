package com.example.pppb_uas

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
// Import library Splash Screen
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.rememberNavController
import com.example.pppb_uas.navigation.AppNavGraph
import com.example.pppb_uas.preferences.PreferencesManager
import com.example.pppb_uas.ui.theme.PPPB_UASTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        // 1. HANDLE SPLASH SCREEN
        // PENTING: Harus dipanggil SEBELUM super.onCreate()
        // Ini akan menggunakan theme 'Theme.App.Starting' yang kita set di Manifest
        // lalu otomatis beralih ke 'Theme.PPPB_UAS' (postSplashScreenTheme) setelah selesai.
        installSplashScreen()

        super.onCreate(savedInstanceState)

        // 2. Setup Edge to Edge (Status bar transparan)
        enableEdgeToEdge()

        // (Opsional) Jika kamu ingin menahan Splash Screen lebih lama
        // sampai kondisi tertentu terpenuhi (misal cek login user),
        // kamu bisa pakai setKeepOnScreenCondition.
        // Contoh:
        /*
        var isReady = false
        // Lakukan loading data async di sini... lalu set isReady = true
        splashScreen.setKeepOnScreenCondition {
            !isReady // Tahan selama isReady masih false
        }
        */

        setContent {
            PPPB_UASTheme {
                // 3. Setup NavController & Preferences
                val navController = rememberNavController()
                val context = LocalContext.current
                val preferencesManager = remember { PreferencesManager(context) }

                // 4. Panggil Navigation Graph Utama
                AppNavGraph(
                    navController = navController,
                    preferencesManager = preferencesManager
                )
            }
        }
    }
}