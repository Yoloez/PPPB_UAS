package com.example.pppb_uas.ui.Course

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.pppb_uas.viewmodel.CourseViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddCourseScreen(
    navController: NavController,
    viewModel: CourseViewModel = viewModel(),
    token: String
) {
    // TAMBAHAN 1: Context untuk Toast
    val context = LocalContext.current

    // TAMBAHAN 2: Pantau Error Message dari ViewModel
    // (Pastikan di ViewModel variabel errorMessage bersifat public)
    val errorMessage by viewModel.errorMessage

    // Jika errorMessage berubah (ada isinya), tampilkan Toast
    LaunchedEffect(errorMessage) {
        if (errorMessage.isNotEmpty()) {
            Toast.makeText(context, errorMessage, Toast.LENGTH_LONG).show()
        }
    }
    // UPDATED: Sesuaikan variable state dengan kebutuhan ViewModel (Name, Code, SKS)
    var name by remember { mutableStateOf("") }
    var code by remember { mutableStateOf("") } // Dulunya ID, sekarang Code
    var sks by remember { mutableStateOf("") }  // Dulunya Semester, sekarang SKS

    // Warna tema
    val darkGreen = Color(0xFF015023)
    val lightGreen = Color(0xFF015023)
    val yellowButton = Color(0xFFDABC4E)

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Add Course",
                        color = Color.White,
                        fontSize = 20.sp
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back",
                            tint = Color.White
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF015023)
                )
            )
        },
        containerColor = Color(0xFF015023)
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(20.dp))

            // --- INPUT 1: COURSE NAME ---
            Column(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = "Course Name",
                    color = Color.White,
                    fontSize = 14.sp,
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = lightGreen,
                        unfocusedContainerColor = lightGreen,
                        focusedBorderColor = Color.White, // Ubah border jadi putih agar terlihat
                        unfocusedBorderColor = Color.White,
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White,
                        cursorColor = Color.White
                    ),
                    shape = RoundedCornerShape(12.dp)
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // --- INPUT 2: CODE (Pengganti ID) ---
            // Di ViewModel diminta 'code' (misal: IF1234), bukan ID database.
            Column(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = "Code (Kode Matkul):",
                    color = Color.White,
                    fontSize = 14.sp,
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                OutlinedTextField(
                    value = code,
                    onValueChange = { code = it },
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(darkGreen),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = lightGreen,
                        unfocusedContainerColor = lightGreen,
                        focusedBorderColor = Color.White,
                        unfocusedBorderColor = Color.White,
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White,
                        cursorColor = Color.White
                    ),
                    shape = RoundedCornerShape(12.dp)
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // --- INPUT 3: SKS (Pengganti Semester) ---
            // Di ViewModel diminta 'sks', bukan semester.
            Column(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = "SKS:",
                    color = Color.White,
                    fontSize = 14.sp,
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                OutlinedTextField(
                    value = sks,
                    onValueChange = { sks = it },
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(darkGreen),
                    // Keyboard type number karena SKS itu angka
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = lightGreen,
                        unfocusedContainerColor = lightGreen,
                        focusedBorderColor = Color.White,
                        unfocusedBorderColor = Color.White,
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White,
                        cursorColor = Color.White
                    ),
                    shape = RoundedCornerShape(12.dp)
                )
            }

            Spacer(modifier = Modifier.height(50.dp))

            // --- SAVE BUTTON ---
            Button(
                onClick = {
                    if (name.isNotEmpty() && code.isNotEmpty() && sks.isNotEmpty()) {

                        // Perhatikan kurung kurawal buka "{" setelah parameter sks
                        viewModel.addCourse(token, name, code, sks) {

                            // --- POSISI YANG BENAR ---
                            // Kode ini hanya akan jalan kalau ViewModel bilang "Sukses"
                            navController.popBackStack()

                        } // <--- Kurung kurawal tutup callback ada DI SINI

                    } else {
                        // Opsional: Beri tahu user kalau data belum lengkap
                    }
                },
                enabled = !viewModel.isLoading.value, // Tombol mati saat loading
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = yellowButton
                ),
                shape = RoundedCornerShape(28.dp)
            ) {
                if (viewModel.isLoading.value) {
                    CircularProgressIndicator(color = darkGreen, modifier = Modifier.size(24.dp))
                } else {
                    Text(
                        text = "Save",
                        color = darkGreen,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
        }
    }
}