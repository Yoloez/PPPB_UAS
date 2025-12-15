package com.example.pppb_uas.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.pppb_uas.R
// Set of Material typography styles to start with
val UrbanistFontFamily = FontFamily(
    Font(R.font.urbanist_regular, FontWeight.Normal),
    Font(R.font.urbanist_bold, FontWeight.Bold)
    // Tambahkan varian lain jika ada (misal semibold)
)

// 2. Terapkan ke Typography Material 3
val Typography = Typography(
    // Terapkan font family ke style default (bodyLarge)
    bodyLarge = TextStyle(
        fontFamily = UrbanistFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.5.sp
    ),
    // Terapkan juga ke style judul (Title)
    titleLarge = TextStyle(
        fontFamily = UrbanistFontFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 22.sp,
        lineHeight = 28.sp,
        letterSpacing = 0.sp
    ),
    // Terapkan ke Label (biasanya untuk Button)
    labelLarge = TextStyle(
        fontFamily = UrbanistFontFamily,
        fontWeight = FontWeight.Medium,
        fontSize = 14.sp,
        lineHeight = 20.sp,
        letterSpacing = 0.1.sp
    )

    /* CATATAN:
       Jika ingin BENAR-BENAR semua teks berubah, kamu idealnya mendefinisikan
       semua varian (displayLarge, headlineMedium, bodyMedium, dll).

       Namun, cara cepatnya adalah memastikan komponen kamu menggunakan style
       yang sudah didefinisikan di atas.
    */
)