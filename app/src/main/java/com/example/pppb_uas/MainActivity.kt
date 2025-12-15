package com.example.pppb_uas

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen // ✅ Import Wajib
import androidx.navigation.compose.rememberNavController
import com.example.pppb_uas.navigation.AppNavGraph
import com.example.pppb_uas.navigation.Screen // Pastikan import Screen ada
import com.example.pppb_uas.preferences.PreferencesManager
import com.example.pppb_uas.ui.theme.PPPB_UASTheme
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        // ✅ 1. Pasang Native Splash Screen
        // Ini akan menampilkan gambar yang disetting di themes.xml tadi
        val splashScreen = installSplashScreen()

        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // ✅ 2. Cek Token (Login atau Belum?)
        // Kita gunakan runBlocking agar app "menunggu" sebentar di splash screen
        // sampai kita tahu harus ke Login atau Dashboard.
        val preferencesManager = PreferencesManager(context = this)
        var startDestination = Screen.Login.route // Default ke Login

        runBlocking {
            val token = preferencesManager.token.first()
            if (token.isNotEmpty()) {
                startDestination = Screen.Dashboard.route // Kalau ada token, ke Dashboard
            }
        }

        setContent {
            PPPB_UASTheme {
                val navController = rememberNavController()

                // Tidak perlu init PreferencesManager lagi disini karena sudah diatas,
                // tapi kalau AppNavGraph butuh pass ulang, boleh pakai remember.
                val pm = remember { preferencesManager }

                // ✅ 3. Panggil NavGraph dengan startDestination dinamis
                AppNavGraph(
                    navController = navController,
                    preferencesManager = pm,
                    startDestination = startDestination // Kirim hasil cek token tadi
                )
            }
        }
    }
}