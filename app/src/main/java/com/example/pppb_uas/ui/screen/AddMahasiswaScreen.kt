package com.example.pppb_uas.ui.screen

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
// Import penting agar ViewModel bisa dipakai di sini
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.pppb_uas.AddMahasiswaViewModel

// --- Warna Sesuai Desain ---
val GreenMain = Color(0xFF014623)
val GreenDarkInput = Color(0xFF01331A)
val GoldButton = Color(0xFFD4B35A)
val TextWhite = Color.White

@Composable
fun AddMahasiswaScreen(
    onBackClick: () -> Unit = {},
    // Injeksi ViewModel di sini (Otomatis dibuatkan oleh Android)
    viewModel: AddMahasiswaViewModel = viewModel()
) {
    val context = LocalContext.current
    val scrollState = rememberScrollState()

    // --- Efek: Memantau Pesan Error/Sukses ---
    // Kode ini akan jalan otomatis kalau 'submissionStatus' di ViewModel berubah
    LaunchedEffect(viewModel.submissionStatus) {
        viewModel.submissionStatus?.let { message ->
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
            viewModel.clearStatus() // Hapus pesan agar tidak muncul terus-terusan

            // Opsional: Kalau sukses, bisa otomatis kembali ke halaman sebelumnya
            // if (message.contains("Success")) { onBackClick() }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(GreenMain)
            .verticalScroll(scrollState)
            .padding(24.dp)
    ) {
        // --- Header (Tombol Back & Judul) ---
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(bottom = 32.dp)
        ) {
            IconButton(onClick = onBackClick) {
                Icon(Icons.Default.ArrowBack, "Back", tint = TextWhite)
            }
            Spacer(modifier = Modifier.width(8.dp))
            Text("Add Mahasiswa", color = TextWhite, fontSize = 20.sp, fontWeight = FontWeight.Bold)
        }

        // --- Foto Profil (Placeholder) ---
        Box(
            modifier = Modifier
                .size(100.dp)
                .align(Alignment.CenterHorizontally)
                .border(1.dp, GreenDarkInput, CircleShape)
                .background(Color.Transparent, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(Icons.Default.Add, "Add Photo", tint = Color.Black, modifier = Modifier.size(32.dp))
        }
        Text("Add", color = TextWhite, modifier = Modifier.align(Alignment.CenterHorizontally).padding(top = 8.dp))

        Spacer(modifier = Modifier.height(32.dp))

        // --- Form Input (Terhubung ke ViewModel) ---
        // Perhatikan: value diambil dari viewModel.name, bukan variabel lokal lagi
        CustomTextField(
            label = "Name:",
            value = viewModel.name,
            onValueChange = { viewModel.name = it }
        )
        CustomTextField(
            label = "NIM:",
            value = viewModel.nim,
            onValueChange = { viewModel.nim = it }
        )
        CustomTextField(
            label = "Email:",
            value = viewModel.email,
            onValueChange = { viewModel.email = it }
        )
        CustomTextField(
            label = "Password:",
            value = viewModel.password,
            onValueChange = { viewModel.password = it }
        )

        Spacer(modifier = Modifier.height(40.dp))

        // --- Tombol Save ---
        Button(
            onClick = { viewModel.validateAndSave() }, // Panggil fungsi logika di ViewModel
            colors = ButtonDefaults.buttonColors(containerColor = GoldButton),
            shape = RoundedCornerShape(30.dp),
            // Tombol mati (disabled) kalau lagi loading, biar user gak klik berkali-kali
            enabled = !viewModel.isLoading,
            modifier = Modifier.fillMaxWidth().height(50.dp)
        ) {
            if (viewModel.isLoading) {
                // Tampilkan loading muter-muter kalau lagi proses simpan
                CircularProgressIndicator(
                    color = Color.Black,
                    modifier = Modifier.size(24.dp),
                    strokeWidth = 3.dp
                )
            } else {
                Text("Save", color = Color.Black, fontWeight = FontWeight.Bold, fontSize = 18.sp)
            }
        }
    }
}

// --- Komponen Input Custom (Tidak ada perubahan) ---
@Composable
fun CustomTextField(label: String, value: String, onValueChange: (String) -> Unit) {
    Column(modifier = Modifier.padding(bottom = 16.dp)) {
        Text(label, color = TextWhite, fontSize = 16.sp, modifier = Modifier.padding(bottom = 8.dp))
        OutlinedTextField(
            value = value, onValueChange = onValueChange,
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = GreenDarkInput, unfocusedContainerColor = GreenDarkInput,
                focusedBorderColor = Color.Transparent, unfocusedBorderColor = Color.Transparent,
                cursorColor = TextWhite, focusedTextColor = TextWhite, unfocusedTextColor = TextWhite
            ),
            singleLine = true
        )
    }
}