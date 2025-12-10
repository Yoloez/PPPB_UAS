package com.example.pppb_uas.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp



@Composable
fun EditMahasiswaScreen(
    onBackClick: () -> Unit = {},
    onSaveClick: () -> Unit = {}
) {
    // Data dummy langsung diisi
    var name by remember { mutableStateOf("Wachyoudi") }
    var nim by remember { mutableStateOf("24/123456/SV/12345") }
    var email by remember { mutableStateOf("wachyoudi@gmail.com") }
    var password by remember { mutableStateOf("123456") }

    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(GreenMain) // Mengambil dari file sebelah (AddMahasiswa)
            .verticalScroll(scrollState)
            .padding(24.dp)
    ) {
        // --- Header ---
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(bottom = 32.dp)
        ) {
            IconButton(onClick = onBackClick) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Back",
                    tint = TextWhite // Mengambil dari file sebelah
                )
            }
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "Edit",
                color = TextWhite,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
        }

        // --- Foto Profil ---
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .size(100.dp)
                    .clip(CircleShape)
                    .background(Color.Gray)
                    .border(1.dp, Color.LightGray, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = "Profile Photo",
                    tint = Color.White,
                    modifier = Modifier.size(60.dp)
                )
            }
            Text(
                text = "Edit",
                color = TextWhite,
                modifier = Modifier.padding(top = 8.dp),
                fontSize = 14.sp
            )
        }

        Spacer(modifier = Modifier.height(32.dp))

        // --- Form Inputs ---
        // PERBAIKAN: Menggunakan nama fungsi yang berbeda (EditInput)
        EditInput(label = "Name:", value = name, onValueChange = { name = it })
        EditInput(label = "NIM:", value = nim, onValueChange = { nim = it })
        EditInput(label = "Email:", value = email, onValueChange = { email = it })
        EditInput(label = "Password:", value = password, onValueChange = { password = it })

        Spacer(modifier = Modifier.height(40.dp))

        // --- Save Button ---
        Button(
            onClick = onSaveClick,
            colors = ButtonDefaults.buttonColors(containerColor = GoldButton), // Dari file sebelah
            shape = RoundedCornerShape(30.dp),
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
        ) {
            Text(
                text = "Save",
                color = Color.Black,
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp
            )
        }
    }
}

// --- PERBAIKAN: Ganti nama fungsi agar tidak bentrok dengan AddMahasiswa ---
@Composable
fun EditInput(
    label: String,
    value: String,
    onValueChange: (String) -> Unit
) {
    Column(modifier = Modifier.padding(bottom = 16.dp)) {
        Text(
            text = label,
            color = TextWhite,
            fontSize = 16.sp,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = GreenDarkInput, // Dari file sebelah
                unfocusedContainerColor = GreenDarkInput,
                focusedBorderColor = Color.Transparent,
                unfocusedBorderColor = Color.Transparent,
                cursorColor = TextWhite,
                focusedTextColor = TextWhite,
                unfocusedTextColor = TextWhite
            ),
            singleLine = true
        )
    }
}

@Preview(showBackground = true)
@Composable
fun EditMahasiswaPreview() {
    EditMahasiswaScreen()
}