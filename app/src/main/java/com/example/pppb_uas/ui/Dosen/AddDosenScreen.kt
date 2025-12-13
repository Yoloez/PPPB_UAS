package com.example.pppb_uas.ui.Dosen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.pppb_uas.preferences.PreferencesManager
import com.example.pppb_uas.viewmodel.DosenViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddDosenScreen(
    viewModel: DosenViewModel,
    onBackClick: () -> Unit
) {
    val formState by viewModel.addFormState.collectAsState()
    val context = LocalContext.current

    // Ambil Token
    val preferencesManager = remember { PreferencesManager(context) }
    val token by preferencesManager.token.collectAsState(initial = "")

    // Warna
    val darkGreen = Color(0xFF015023)
    val lightGreen = Color(0xFF015023)
    val yellowButton = Color(0xFFDABC4E)

    // Cek Sukses
    LaunchedEffect(formState.isSuccess) {
        if (formState.isSuccess) {
            onBackClick() // Kembali ke list
            viewModel.resetAddFormState() // Reset form
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Add Lecture", color = Color.White, fontSize = 20.sp) },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        // Menggunakan AutoMirrored agar tidak deprecated
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, "Back", tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = darkGreen)
            )
        },
        containerColor = darkGreen
    ) { paddingValues ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 24.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Spacer(modifier = Modifier.height(32.dp))

            // --- BAGIAN FOTO SUDAH DIHAPUS ---

            // Tampilkan Error jika ada
            if (formState.errorMessage != null) {
                Text(
                    text = formState.errorMessage!!,
                    color = Color.Red,
                    modifier = Modifier.padding(bottom = 16.dp)
                )
            }

            // Input Name
            CustomInput(
                label = "Name:",
                value = formState.name,
                onValueChange = viewModel::onNameChange,
                color = lightGreen
            )
            Spacer(modifier = Modifier.height(16.dp))

            // Input NIP
            CustomInput(
                label = "NIP:",
                value = formState.nip,
                onValueChange = viewModel::onNipChange,
                color = lightGreen
            )
            Spacer(modifier = Modifier.height(16.dp))

            // Input Email
            CustomInput(
                label = "Email:",
                value = formState.email,
                onValueChange = viewModel::onEmailChange,
                color = lightGreen,
                keyboardType = KeyboardType.Email // Keyboard Email
            )
            Spacer(modifier = Modifier.height(16.dp))

            // Input Program ID (Harus Angka)
            CustomInput(
                label = "Program Studi ID (Input Angka, cth: 1):",
                value = formState.programId,
                onValueChange = viewModel::onProgramIdChange,
                color = lightGreen,
                keyboardType = KeyboardType.Number // Keyboard Angka
            )
            Spacer(modifier = Modifier.height(16.dp))

            // Input Password
            PasswordInput(
                label = "Password:",
                value = formState.password,
                onValueChange = viewModel::onPasswordChange,
                color = lightGreen
            )
            Spacer(modifier = Modifier.height(16.dp))

            // Input Confirm Password
            PasswordInput(
                label = "Confirm Password:",
                value = formState.confirmPassword,
                onValueChange = viewModel::onConfirmPasswordChange,
                color = lightGreen,
                isError = formState.password.isNotEmpty() && formState.confirmPassword.isNotEmpty() && formState.password != formState.confirmPassword
            )

            Spacer(modifier = Modifier.height(32.dp))

            // Button Save
            Button(
                onClick = { viewModel.saveDosen(token, context) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                colors = ButtonDefaults.buttonColors(containerColor = yellowButton),
                enabled = !formState.isLoading
            ) {
                if (formState.isLoading) {
                    CircularProgressIndicator(color = darkGreen)
                } else {
                    Text(
                        "Save",
                        color = darkGreen,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

// Komponen Input Biasa
@Composable
fun CustomInput(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    color: Color,
    keyboardType: KeyboardType = KeyboardType.Text
) {
    Text(
        label,
        color = Color.White,
        fontSize = 14.sp,
        modifier = Modifier.padding(bottom = 8.dp)
    )
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = Modifier.fillMaxWidth(),
        keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
        colors = OutlinedTextFieldDefaults.colors(
            focusedContainerColor = color,
            unfocusedContainerColor = color,
            focusedBorderColor = Color.Black,
            unfocusedBorderColor = Color.Black,
            focusedTextColor = Color.White,
            unfocusedTextColor = Color.White,
            cursorColor = Color.White
        ),
        shape = RoundedCornerShape(8.dp)
    )
}

// Komponen Input Password
@Composable
fun PasswordInput(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    color: Color,
    isError: Boolean = false
) {
    Text(
        label,
        color = Color.White,
        fontSize = 14.sp,
        modifier = Modifier.padding(bottom = 8.dp)
    )
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = Modifier.fillMaxWidth(),
        visualTransformation = PasswordVisualTransformation(),
        isError = isError,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
        colors = OutlinedTextFieldDefaults.colors(
            focusedContainerColor = color,
            unfocusedContainerColor = color,
            focusedBorderColor = Color.Black,
            unfocusedBorderColor = Color.Black,
            focusedTextColor = Color.White,
            unfocusedTextColor = Color.White,
            cursorColor = Color.White,
            errorBorderColor = Color.Red
        ),
        shape = RoundedCornerShape(8.dp)
    )
}