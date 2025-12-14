package com.example.pppb_uas.ui.screen

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController

// --- Pastikan Import R ini sesuai dengan nama package project kamu ---
import com.example.pppb_uas.R
import com.example.pppb_uas.navigation.Screen
import com.example.pppb_uas.preferences.PreferencesManager
import com.example.pppb_uas.viewmodel.LoginState
import com.example.pppb_uas.viewmodel.LoginViewModel

@Composable
fun LoginScreen(
    navController: NavHostController,
    preferencesManager: PreferencesManager,
    viewModel: LoginViewModel = viewModel()
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    val context = LocalContext.current

    // Mengambil state dari ViewModel
    val loginState by viewModel.loginState.collectAsState()

    // Efek Samping (Side Effect) saat status login berubah
    LaunchedEffect(loginState) {
        when (val state = loginState) {
            is LoginState.Success -> {
                val userData = state.response.data
                if (userData != null) {
                    // 1. Simpan data user ke DataStore/Preferences
                    preferencesManager.saveAuthData(
                        token = userData.access_token,
                        name = userData.user.name,
                        email = userData.user.email,
                        userId = userData.user.id.toString()
                    )

                    Toast.makeText(
                        context,
                        "Login berhasil! Welcome ${userData.user.name}",
                        Toast.LENGTH_LONG
                    ).show()

                    // 2. Pindah ke Dashboard & Hapus Login dari backstack
                    navController.navigate(Screen.Dashboard.route) {
                        popUpTo(Screen.Login.route) { inclusive = true }
                    }
                }
                viewModel.resetState()
            }
            is LoginState.Error -> {
                Toast.makeText(
                    context,
                    "Login gagal: ${state.message}",
                    Toast.LENGTH_LONG
                ).show()
                viewModel.resetState()
            }
            else -> {}
        }
    }

    // --- UI LAYOUT ---
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF015023)) // Warna Hijau Gelap
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            Spacer(modifier = Modifier.height(60.dp))

            // Logo UGN
            Image(
                painter = painterResource(R.drawable.logo_ugn),
                contentDescription = "UGN Logo",
                modifier = Modifier.size(240.dp)
            )

            Spacer(modifier = Modifier.height(40.dp))

            // Teks Header
            Text(
                text = "Login to your account",
                fontSize = 16.sp,
                color = Color.White,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Start
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Input Email
            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Email") },
                enabled = loginState !is LoginState.Loading,
                modifier = Modifier.fillMaxWidth(),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White,
                    disabledContainerColor = Color.White,
                    focusedBorderColor = Color.Transparent,
                    unfocusedBorderColor = Color.Transparent,
                ),
                shape = RoundedCornerShape(8.dp),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Input Password
            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Password") },
                enabled = loginState !is LoginState.Loading,
                modifier = Modifier.fillMaxWidth(),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White,
                    disabledContainerColor = Color.White,
                    focusedBorderColor = Color.Transparent,
                    unfocusedBorderColor = Color.Transparent,
                ),
                shape = RoundedCornerShape(8.dp),
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
            )

            Spacer(modifier = Modifier.height(82.dp))

            // Tombol Login
            Button(
                onClick = {
                    if (email.isNotBlank() && password.isNotBlank()) {
                        viewModel.login(email, password)
                    } else {
                        Toast.makeText(context, "Email dan password harus diisi", Toast.LENGTH_SHORT).show()
                    }
                },
                enabled = loginState !is LoginState.Loading,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFD4AF37) // Warna Emas
                ),
                shape = RoundedCornerShape(28.dp)
            ) {
                if (loginState is LoginState.Loading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp),
                        color = Color.Black
                    )
                } else {
                    Text(
                        text = "Login",
                        fontSize = 16.sp,
                        color = Color.Black
                    )
                }
            }
        }
    }
}