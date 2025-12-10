package com.example.pppb_uas.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// --- Warna Baru Khusus List ---
val CreamItem = Color(0xFFFBF8E8) // Warna cream untuk kartu
val GrayText = Color(0xFF888888)  // Warna abu-abu untuk NIM

@Composable
fun ListMahasiswaScreen(
    onBackClick: () -> Unit = {},
    onAddClick: () -> Unit = {},
    onEditClick: (String) -> Unit = {}, // Mengirim ID/Nama saat klik edit
    onDeleteClick: (String) -> Unit = {}
) {
    // Data Dummy (Nanti diganti data dari Database/API)
    val mahasiswaList = listOf(
        MahasiswaData("Wachyoudi", "24/123456/SV/12345"),
        MahasiswaData("Wachyoudi", "24/123456/SV/12345"), // Ini nanti jadi GOLD
        MahasiswaData("Wachyoudi", "24/123456/SV/12345"),
        MahasiswaData("Wachyoudi", "24/123456/SV/12345"),
        MahasiswaData("Wachyoudi", "24/123456/SV/12345")
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(GreenMain) // Pastikan GreenMain ada di file lain/package ini
            .padding(24.dp)
    ) {
        // --- Header (Back, Title, Add) ---
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 24.dp)
        ) {
            IconButton(onClick = onBackClick) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Back",
                    tint = TextWhite
                )
            }

            Text(
                text = "Student",
                color = TextWhite,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.weight(1f).padding(start = 8.dp)
            )

            // Tombol Tambah (+)
            IconButton(onClick = onAddClick) {
                Icon(
                    imageVector = Icons.Default.Add, // Ikon plus bulat
                    contentDescription = "Add Student",
                    tint = TextWhite,
                    modifier = Modifier.size(30.dp)
                )
            }
        }

        // --- List Mahasiswa (LazyColumn) ---
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(16.dp), // Jarak antar kartu
            modifier = Modifier.fillMaxSize()
        ) {
            itemsIndexed(mahasiswaList) { index, mahasiswa ->
                // Logika Warna: Jika index ke-1 (item kedua), warnanya Gold. Sisanya Cream.
                val cardColor = if (index == 1) GoldButton else CreamItem

                MahasiswaCard(
                    data = mahasiswa,
                    backgroundColor = cardColor,
                    onEdit = { onEditClick(mahasiswa.name) },
                    onDelete = { onDeleteClick(mahasiswa.name) }
                )
            }
        }
    }
}

// --- Komponen Kartu Mahasiswa ---
@Composable
fun MahasiswaCard(
    data: MahasiswaData,
    backgroundColor: Color,
    onEdit: () -> Unit,
    onDelete: () -> Unit
) {
    Card(
        colors = CardDefaults.cardColors(containerColor = backgroundColor),
        shape = RoundedCornerShape(20.dp),
        modifier = Modifier
            .fillMaxWidth()
            .height(100.dp) // Tinggi kartu disesuaikan
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // 1. Foto Profil (Kucing/Placeholder)
            Box(
                modifier = Modifier
                    .size(60.dp)
                    .clip(RoundedCornerShape(12.dp)) // Kotak tapi sudut tumpul dikit
                    .background(Color.Gray.copy(alpha = 0.3f)),
                contentAlignment = Alignment.Center
            ) {
                // Ganti Icon ini dengan Image() jika mau pakai foto kucingmu
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = "Foto",
                    modifier = Modifier.size(40.dp),
                    tint = Color.DarkGray
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            // 2. Teks (Nama & NIM)
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = data.name,
                    color = Color.Black, // Teks selalu hitam/gelap
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
                Text(
                    text = data.nim,
                    color = GrayText, // Warna abu-abu
                    fontSize = 12.sp
                )
            }

            // 3. Tombol Aksi (Edit & Delete)
            Row {
                IconButton(onClick = onEdit) {
                    Icon(
                        imageVector = Icons.Default.Edit, // Ikon pensil
                        contentDescription = "Edit",
                        tint = Color.Black
                    )
                }
                IconButton(onClick = onDelete) {
                    Icon(
                        imageVector = Icons.Default.Delete, // Ikon sampah
                        contentDescription = "Delete",
                        tint = Color.Black
                    )
                }
            }
        }
    }
}

// Data Class Sederhana untuk Dummy
data class MahasiswaData(val name: String, val nim: String)

// --- Preview ---
@Preview(showBackground = true)
@Composable
fun ListMahasiswaPreview() {
    ListMahasiswaScreen()
}