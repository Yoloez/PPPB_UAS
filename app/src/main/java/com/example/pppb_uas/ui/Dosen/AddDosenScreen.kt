package com.example.pppb_uas.ui.Dosen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.ArrowDropUp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.pppb_uas.R // Pastikan import R sesuai package Anda
import com.example.pppb_uas.preferences.PreferencesManager
import com.example.pppb_uas.viewmodel.DosenViewModel

// Definisi Font Urbanist (Private)
private val urbanistFontFamily = FontFamily(
    Font(R.font.urbanist_regular, FontWeight.Normal),
    Font(R.font.urbanist_black, FontWeight.SemiBold),
    Font(R.font.urbanist_bold, FontWeight.Bold)
)

// Data Dummy Program Studi
val programStudiOptions = mapOf(
    "1" to "Teknologi Rekayasa Perangkat Lunak",
    "2" to "Teknik Informatika",
    "3" to "Sistem Informasi",
)

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
            onBackClick()
            viewModel.resetAddFormState()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Add Lecture",
                        color = Color.White,
                        fontSize = 20.sp,
                        fontFamily = urbanistFontFamily
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
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
                .padding(horizontal = 20.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Spacer(modifier = Modifier.height(20.dp))

            // Tampilkan Error jika ada
            if (formState.errorMessage != null) {
                Text(
                    text = formState.errorMessage!!,
                    color = Color.Red,
                    modifier = Modifier.padding(bottom = 16.dp),
                    fontFamily = urbanistFontFamily
                )
            }

            // Input Name
            CustomInput(
                label = "Name:",
                value = formState.name,
                onValueChange = viewModel::onNameChange,
                containerColor = lightGreen
            )
            Spacer(modifier = Modifier.height(16.dp))

            // Input NIP / Username
            CustomInput(
                label = "Username:",
                value = formState.nip,
                onValueChange = viewModel::onNipChange,
                containerColor = lightGreen
            )
            Spacer(modifier = Modifier.height(16.dp))

            // Input Email
            CustomInput(
                label = "Email:",
                value = formState.email,
                onValueChange = viewModel::onEmailChange,
                containerColor = lightGreen,
                keyboardType = KeyboardType.Email
            )
            Spacer(modifier = Modifier.height(16.dp))

            // --- Program Studi Dropdown (Menggantikan Input Angka) ---
            CustomDropdownDosen(
                label = "Program Studi:",
                selectedId = formState.programId,
                onValueChange = viewModel::onProgramIdChange,
                options = programStudiOptions,
                containerColor = lightGreen
            )
            Spacer(modifier = Modifier.height(16.dp))

            // Input Password
            PasswordInput(
                label = "Password:",
                value = formState.password,
                onValueChange = viewModel::onPasswordChange,
                containerColor = lightGreen
            )
            Spacer(modifier = Modifier.height(16.dp))

            // Input Confirm Password
            PasswordInput(
                label = "Confirm Password:",
                value = formState.confirmPassword,
                onValueChange = viewModel::onConfirmPasswordChange,
                containerColor = lightGreen,
                isError = formState.password.isNotEmpty() && formState.confirmPassword.isNotEmpty() && formState.password != formState.confirmPassword
            )

            Spacer(modifier = Modifier.height(50.dp))

            // Button Save
            Button(
                onClick = { viewModel.saveDosen(token, context) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                colors = ButtonDefaults.buttonColors(containerColor = yellowButton),
                shape = RoundedCornerShape(28.dp),
                enabled = !formState.isLoading
            ) {
                if (formState.isLoading) {
                    CircularProgressIndicator(color = darkGreen, modifier = Modifier.size(24.dp))
                } else {
                    Text(
                        "Save",
                        color = darkGreen,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.SemiBold,
                        fontFamily = urbanistFontFamily
                    )
                }
            }
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

// --- Komponen Dropdown Khusus Dosen ---
@Composable
fun CustomDropdownDosen(
    label: String,
    selectedId: String,
    onValueChange: (String) -> Unit,
    options: Map<String, String>,
    containerColor: Color
) {
    var expanded by remember { mutableStateOf(false) }

    // Mencari nama label berdasarkan ID yang tersimpan, jika kosong tampilkan ""
    val displayText = options[selectedId] ?: ""

    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            label,
            color = Color.White,
            fontSize = 14.sp,
            modifier = Modifier.padding(bottom = 8.dp),
            fontFamily = urbanistFontFamily
        )

        Box(modifier = Modifier.fillMaxWidth()) {
            OutlinedTextField(
                value = displayText,
                onValueChange = {}, // Read Only
                readOnly = true,
                modifier = Modifier.fillMaxWidth(),
                // Terapkan Font Style pada text input user
                textStyle = TextStyle(
                    fontFamily = urbanistFontFamily,
                    fontSize = 16.sp,
                    color = Color.White
                ),
                trailingIcon = {
                    Icon(
                        imageVector = if (expanded) Icons.Default.ArrowDropUp else Icons.Default.ArrowDropDown,
                        contentDescription = "Dropdown Icon",
                        tint = Color.White
                    )
                },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = containerColor,
                    unfocusedContainerColor = containerColor,
                    focusedBorderColor = Color.White,
                    unfocusedBorderColor = Color.White,
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White,
                    cursorColor = Color.White
                ),
                shape = RoundedCornerShape(12.dp),
                singleLine = true
            )

            // Layer transparan untuk handle klik agar menu terbuka
            Box(
                modifier = Modifier
                    .matchParentSize()
                    .clickable { expanded = !expanded }
            )

            // Menu Dropdown
            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
                modifier = Modifier
                    .background(Color.White) // Background Putih agar teks hitam terbaca
                    .fillMaxWidth(0.9f)
            ) {
                options.forEach { (id, name) ->
                    DropdownMenuItem(
                        text = {
                            Text(
                                text = name,
                                color = Color.Black, // Text hitam di atas background putih
                                fontFamily = urbanistFontFamily
                            )
                        },
                        onClick = {
                            onValueChange(id) // Mengirim ID (misal "1") ke ViewModel
                            expanded = false
                        }
                    )
                }
            }
        }
    }
}

// Komponen Input Biasa (Tidak Berubah)
@Composable
fun CustomInput(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    containerColor: Color,
    keyboardType: KeyboardType = KeyboardType.Text
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            label,
            color = Color.White,
            fontSize = 14.sp,
            modifier = Modifier.padding(bottom = 8.dp),
            fontFamily = urbanistFontFamily
        )
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
            textStyle = TextStyle(
                fontFamily = urbanistFontFamily,
                fontSize = 16.sp,
                color = Color.White
            ),
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = containerColor,
                unfocusedContainerColor = containerColor,
                focusedBorderColor = Color.White,
                unfocusedBorderColor = Color.White,
                focusedTextColor = Color.White,
                unfocusedTextColor = Color.White,
                cursorColor = Color.White
            ),
            shape = RoundedCornerShape(12.dp)
        )
    }
}

// Komponen Input Password (Tidak Berubah)
@Composable
fun PasswordInput(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    containerColor: Color,
    isError: Boolean = false
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            label,
            color = Color.White,
            fontSize = 14.sp,
            modifier = Modifier.padding(bottom = 8.dp),
            fontFamily = urbanistFontFamily
        )
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier.fillMaxWidth(),
            visualTransformation = PasswordVisualTransformation(),
            isError = isError,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            textStyle = TextStyle(
                fontFamily = urbanistFontFamily,
                fontSize = 16.sp,
                color = Color.White
            ),
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = containerColor,
                unfocusedContainerColor = containerColor,
                focusedBorderColor = Color.White,
                unfocusedBorderColor = Color.White,
                focusedTextColor = Color.White,
                unfocusedTextColor = Color.White,
                cursorColor = Color.White,
                errorBorderColor = Color.Red
            ),
            shape = RoundedCornerShape(12.dp)
        )
    }
}