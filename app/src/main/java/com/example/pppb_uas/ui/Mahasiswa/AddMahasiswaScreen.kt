package com.example.pppb_uas.ui.mahasiswa

import android.widget.Toast
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.pppb_uas.R
import com.example.pppb_uas.preferences.PreferencesManager
import com.example.pppb_uas.viewmodel.AddMahasiswaUiState
import com.example.pppb_uas.viewmodel.MahasiswaViewModel

// Definisi Font (Private)
private val urbanistFontFamily = FontFamily(
    Font(R.font.urbanist_regular, FontWeight.Normal),
    Font(R.font.urbanist_black, FontWeight.SemiBold),
    Font(R.font.urbanist_bold, FontWeight.Bold)
)

// --- Interface State ---
interface AddMahasiswaState {
    val name: String
    val username: String
    val nim: String
    val email: String
    val password: String
    val confirmPassword: String
    val programId: String

    val isLoading: Boolean
    val submissionStatus: String?
    val isSuccess: Boolean

    fun updateName(newValue: String)
    fun updateUsername(newValue: String)
    fun updateNim(newValue: String)
    fun updateEmail(newValue: String)
    fun updatePassword(newValue: String)
    fun updateConfirmPassword(newValue: String)
    fun updateProgramId(newValue: String)

    fun validateAndSave(context: android.content.Context)
    fun clearStatus()
}

// --- Warna Sesuai Desain ---
private val GreenMain = Color(0xFF015023)
private val GreenDarkInput = Color(0xFF01331A)
private val GoldButton = Color(0xFFD4B35A)
private val TextWhite = Color.White

// --- DATA DUMMY PROGRAM STUDI ---
// Key (Kiri) = ID yang akan dikirim ke Server
// Value (Kanan) = Nama yang tampil di Dropdown
val programStudiOptions = mapOf(
    "1" to "Teknologi Rekayasa Perangkat Lunak",
    "2" to "Teknik Informatika",
    "3" to "Sistem Informasi",
)

// --- COMPOSABLE UTAMA ---
@Composable
fun AddMahasiswaScreen(
    onBackClick: () -> Unit = {},
    viewModel: MahasiswaViewModel = viewModel()
) {
    val context = LocalContext.current
    val preferencesManager = remember { PreferencesManager(context) }
    val tokenState = preferencesManager.token.collectAsState(initial = "")
    val token = tokenState.value

    val stateWrapper = remember(viewModel, token) {
        RealAddMahasiswaState(viewModel, token)
    }

    val addUiState by viewModel.addUiState.collectAsState()
    stateWrapper.currentState = addUiState

    AddMahasiswaContent(
        onBackClick = onBackClick,
        state = stateWrapper
    )
}

// --- UI MURNI ---
@Composable
fun AddMahasiswaContent(
    onBackClick: () -> Unit = {},
    state: AddMahasiswaState,
) {
    val context = LocalContext.current
    val scrollState = rememberScrollState()

    LaunchedEffect(state.submissionStatus, state.isSuccess) {
        if (state.isSuccess) {
            Toast.makeText(context, "Berhasil menambahkan mahasiswa!", Toast.LENGTH_SHORT).show()
            state.clearStatus()
            onBackClick()
        } else {
            state.submissionStatus?.let { message ->
                Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
                state.clearStatus()
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(GreenMain)
            .verticalScroll(scrollState)
            .padding(24.dp)
    ) {
        // --- Header ---
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(bottom = 32.dp)
        ) {
            IconButton(onClick = onBackClick) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, "Back", tint = TextWhite)
            }
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                "Add Mahasiswa",
                color = TextWhite,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = urbanistFontFamily
            )
        }

        // --- Form Input ---
        CustomTextField(label = "Name:", value = state.name, onValueChange = state::updateName)
        CustomTextField(label = "Username:", value = state.username, onValueChange = state::updateUsername)
        CustomTextField(label = "NIM:", value = state.nim, onValueChange = state::updateNim, isNumber = true)
        CustomTextField(label = "Email:", value = state.email, onValueChange = state::updateEmail)

        // Password
        CustomTextField(label = "Password:", value = state.password, onValueChange = state::updatePassword, isPassword = true)

        // Confirm Password
        CustomTextField(label = "Confirm Password:", value = state.confirmPassword, onValueChange = state::updateConfirmPassword, isPassword = true)

        // --- Program ID (Diganti jadi Dropdown) ---
        CustomDropdownField(
            label = "Program Study:",
            selectedId = state.programId,
            onValueChange = state::updateProgramId,
            options = programStudiOptions
        )

        Spacer(modifier = Modifier.height(20.dp))

        // --- Tombol Save ---
        Button(
            onClick = { state.validateAndSave(context) },
            colors = ButtonDefaults.buttonColors(containerColor = GoldButton),
            shape = RoundedCornerShape(30.dp),
            enabled = !state.isLoading,
            modifier = Modifier.fillMaxWidth().height(50.dp)
        ) {
            if (state.isLoading) {
                CircularProgressIndicator(
                    color = Color.Black,
                    modifier = Modifier.size(24.dp),
                    strokeWidth = 3.dp
                )
            } else {
                Text(
                    "Save",
                    color = Color.Black,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    fontFamily = urbanistFontFamily
                )
            }
        }
    }
}

// --- Komponen Dropdown Baru (Meniru Style CustomTextField) ---
@Composable
fun CustomDropdownField(
    label: String,
    selectedId: String,
    onValueChange: (String) -> Unit,
    options: Map<String, String>
) {
    var expanded by remember { mutableStateOf(false) }

    // Tampilkan Nama Program berdasarkan ID yang dipilih, jika kosong tampilkan ""
    val displayText = options[selectedId] ?: ""

    Column(modifier = Modifier.padding(bottom = 16.dp).fillMaxWidth()) {
        Text(
            text = label,
            color = TextWhite,
            fontSize = 14.sp,
            modifier = Modifier.padding(bottom = 8.dp),
            fontFamily = urbanistFontFamily
        )

        Box(modifier = Modifier.fillMaxWidth()) {
            OutlinedTextField(
                value = displayText,
                onValueChange = {}, // ReadOnly
                readOnly = true,
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                trailingIcon = {
                    Icon(
                        imageVector = if (expanded) Icons.Default.ArrowDropUp else Icons.Default.ArrowDropDown,
                        contentDescription = null,
                        tint = TextWhite
                    )
                },
                textStyle = TextStyle(
                    fontFamily = urbanistFontFamily,
                    fontSize = 16.sp,
                    color = TextWhite
                ),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = GreenMain,
                    unfocusedContainerColor = GreenMain,
                    focusedBorderColor = Color.White,
                    unfocusedBorderColor = Color.White,
                    cursorColor = TextWhite,
                    focusedTextColor = TextWhite,
                    unfocusedTextColor = TextWhite
                ),
                singleLine = true
            )

            // Layer transparan untuk menangkap klik agar dropdown muncul
            Box(
                modifier = Modifier
                    .matchParentSize()
                    .clickable { expanded = !expanded }
            )

            // Menu Dropdown
            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
                modifier = Modifier.background(Color.White) // Background putih agar teks hitam terbaca
            ) {
                options.forEach { (id, name) ->
                    DropdownMenuItem(
                        text = {
                            Text(
                                text = name,
                                color = Color.Black,
                                fontFamily = urbanistFontFamily
                            )
                        },
                        onClick = {
                            onValueChange(id) // Simpan ID (misal "1") ke state
                            expanded = false
                        }
                    )
                }
            }
        }
    }
}

// --- Komponen Input Custom (Tidak Berubah) ---
@Composable
fun CustomTextField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    isNumber: Boolean = false,
    isPassword: Boolean = false
) {
    Column(modifier = Modifier.padding(bottom = 16.dp).fillMaxWidth()) {
        Text(
            text = label,
            color = TextWhite,
            fontSize = 14.sp,
            modifier = Modifier.padding(bottom = 8.dp),
            fontFamily = urbanistFontFamily
        )
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            visualTransformation = if (isPassword) androidx.compose.ui.text.input.PasswordVisualTransformation() else androidx.compose.ui.text.input.VisualTransformation.None,
            keyboardOptions = if (isNumber) KeyboardOptions(keyboardType = KeyboardType.Number)
            else if (isPassword) KeyboardOptions(keyboardType = KeyboardType.Password)
            else KeyboardOptions.Default,

            // Terapkan font Urbanist pada input text
            textStyle = TextStyle(
                fontFamily = urbanistFontFamily,
                fontSize = 16.sp,
                color = TextWhite
            ),

            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = GreenMain,
                unfocusedContainerColor = GreenMain,
                focusedBorderColor = Color.White,
                unfocusedBorderColor = Color.White,
                cursorColor = TextWhite,
                focusedTextColor = TextWhite,
                unfocusedTextColor = TextWhite
            ),
            singleLine = true
        )
    }
}

// --- PENGHUBUNG (LOGIKA UI - Tidak Berubah) ---
class RealAddMahasiswaState(
    private val viewModel: MahasiswaViewModel,
    private val token: String
) : AddMahasiswaState {

    override var name by mutableStateOf("")
    override var username by mutableStateOf("")
    override var nim by mutableStateOf("")
    override var email by mutableStateOf("")
    override var password by mutableStateOf("")
    override var confirmPassword by mutableStateOf("")
    override var programId by mutableStateOf("")

    var currentState by mutableStateOf(AddMahasiswaUiState())

    override val isLoading: Boolean get() = currentState.isLoading
    override val submissionStatus: String? get() = currentState.errorMessage
    override val isSuccess: Boolean get() = currentState.isSuccess

    override fun updateName(newValue: String) { name = newValue }
    override fun updateUsername(newValue: String) { username = newValue }
    override fun updateNim(newValue: String) { nim = newValue }
    override fun updateEmail(newValue: String) { email = newValue }
    override fun updatePassword(newValue: String) { password = newValue }
    override fun updateConfirmPassword(newValue: String) { confirmPassword = newValue }
    override fun updateProgramId(newValue: String) { programId = newValue }

    override fun validateAndSave(context: android.content.Context) {
        if (name.isBlank() || username.isBlank() || nim.isBlank() || email.isBlank() || password.isBlank() || programId.isBlank()) {
            Toast.makeText(context, "Semua field harus diisi!", Toast.LENGTH_SHORT).show()
            return
        }

        if (password != confirmPassword) {
            Toast.makeText(context, "Password dan Konfirmasi tidak cocok!", Toast.LENGTH_SHORT).show()
            return
        }

        if (token.isNotEmpty()) {
            // Logika konversi programId ke Int tetap aman
            viewModel.addMahasiswa(
                token = token,
                name = name,
                username = username,
                email = email,
                password = password,
                nim = nim,
                programId = programId.toIntOrNull() ?: 0
            )
        } else {
            Toast.makeText(context, "Token tidak valid. Silakan Login ulang.", Toast.LENGTH_SHORT).show()
        }
    }

    override fun clearStatus() {
        viewModel.resetAddState()
    }
}

// --- Preview Dummy ---
class DummyAddMahasiswaState : AddMahasiswaState {
    override val name = ""
    override val username = ""
    override val nim = ""
    override val email = ""
    override val password = ""
    override val confirmPassword = ""
    override val programId = ""
    override val isLoading = false
    override val submissionStatus: String? = null
    override val isSuccess = false

    override fun updateName(newValue: String) {}
    override fun updateUsername(newValue: String) {}
    override fun updateNim(newValue: String) {}
    override fun updateEmail(newValue: String) {}
    override fun updatePassword(newValue: String) {}
    override fun updateConfirmPassword(newValue: String) {}
    override fun updateProgramId(newValue: String) {}
    override fun validateAndSave(context: android.content.Context) {}
    override fun clearStatus() {}
}

@Preview(showBackground = true)
@Composable
fun PreviewAddMahasiswaScreen() {
    AddMahasiswaContent(state = DummyAddMahasiswaState())
}