package com.example.pppb_uas.ui.mahasiswa

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.pppb_uas.preferences.PreferencesManager
import com.example.pppb_uas.viewmodel.AddMahasiswaUiState
import com.example.pppb_uas.viewmodel.MahasiswaViewModel

// --- Interface State (Updated: Tambah Confirm Password) ---
interface AddMahasiswaState {
    val name: String
    val username: String
    val nim: String
    val email: String
    val password: String
    val confirmPassword: String // Kolom Baru
    val programId: String

    val isLoading: Boolean
    val submissionStatus: String?
    val isSuccess: Boolean

    fun updateName(newValue: String)
    fun updateUsername(newValue: String)
    fun updateNim(newValue: String)
    fun updateEmail(newValue: String)
    fun updatePassword(newValue: String)
    fun updateConfirmPassword(newValue: String) // Fungsi Baru
    fun updateProgramId(newValue: String)

    fun validateAndSave(context: android.content.Context) // Tambah context untuk Toast error validasi
    fun clearStatus()
}

// --- Warna Sesuai Desain ---
val GreenMain = Color(0xFF014623)
val GreenDarkInput = Color(0xFF01331A)
val GoldButton = Color(0xFFD4B35A)
val TextWhite = Color.White

// --- COMPOSABLE UTAMA ---
@Composable
fun AddMahasiswaScreen(
    onBackClick: () -> Unit = {},
    viewModel: MahasiswaViewModel = viewModel()
) {
    val context = LocalContext.current

    // 1. Ambil Token Asli
    val preferencesManager = remember { PreferencesManager(context) }
    val tokenState = preferencesManager.token.collectAsState(initial = "")
    val token = tokenState.value

    // 2. Hubungkan ViewModel ke Helper Class
    val stateWrapper = remember(viewModel, token) {
        RealAddMahasiswaState(viewModel, token)
    }

    // 3. Collect State dari ViewModel
    val addUiState by viewModel.addUiState.collectAsState()
    stateWrapper.currentState = addUiState

    // 4. Panggil UI Murni
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

    // Efek Samping: Sukses / Error dari API
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
            Text("Add Mahasiswa", color = TextWhite, fontSize = 20.sp, fontWeight = FontWeight.Bold)
        }

        // --- Form Input ---
        CustomTextField(label = "Name:", value = state.name, onValueChange = state::updateName)
        CustomTextField(label = "Username:", value = state.username, onValueChange = state::updateUsername)
        CustomTextField(label = "NIM:", value = state.nim, onValueChange = state::updateNim, isNumber = true)
        CustomTextField(label = "Email:", value = state.email, onValueChange = state::updateEmail)

        // Password
        CustomTextField(label = "Password:", value = state.password, onValueChange = state::updatePassword)

        // Confirm Password (BARU)
        CustomTextField(label = "Confirm Password:", value = state.confirmPassword, onValueChange = state::updateConfirmPassword)

        // Program ID
        CustomTextField(
            label = "Program ID (Angka, cth: 1):",
            value = state.programId,
            onValueChange = state::updateProgramId,
            isNumber = true
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
                Text("Save", color = Color.Black, fontWeight = FontWeight.Bold, fontSize = 18.sp)
            }
        }
    }
}

// --- Komponen Input Custom ---
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
            text = label, // Pastikan parameter bernama text
            color = TextWhite,
            fontSize = 14.sp,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier.fillMaxWidth(),
            // UBAH DISINI: Ganti shape jadi 12.dp
            shape = RoundedCornerShape(12.dp),
            visualTransformation = if (isPassword) androidx.compose.ui.text.input.PasswordVisualTransformation() else androidx.compose.ui.text.input.VisualTransformation.None,
            keyboardOptions = if (isNumber) KeyboardOptions(keyboardType = KeyboardType.Number)
            else if (isPassword) KeyboardOptions(keyboardType = KeyboardType.Password)
            else KeyboardOptions.Default,

            // UBAH DISINI: Ganti warna border jadi Putih & Background jadi DarkGreen
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = DarkGreen,
                unfocusedContainerColor = DarkGreen,
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

// --- PENGHUBUNG (LOGIKA UI) ---
class RealAddMahasiswaState(
    private val viewModel: MahasiswaViewModel,
    private val token: String
) : AddMahasiswaState {

    // State Form
    override var name by mutableStateOf("")
    override var username by mutableStateOf("")
    override var nim by mutableStateOf("")
    override var email by mutableStateOf("")
    override var password by mutableStateOf("")
    override var confirmPassword by mutableStateOf("") // State Baru
    override var programId by mutableStateOf("")

    // State dari ViewModel
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
        // 1. Validasi Input Kosong
        if (name.isBlank() || username.isBlank() || nim.isBlank() || email.isBlank() || password.isBlank() || programId.isBlank()) {
            Toast.makeText(context, "Semua field harus diisi!", Toast.LENGTH_SHORT).show()
            return
        }

        // 2. Validasi Password Sama
        if (password != confirmPassword) {
            Toast.makeText(context, "Password dan Konfirmasi tidak cocok!", Toast.LENGTH_SHORT).show()
            return
        }

        // 3. Cek Token
        if (token.isNotEmpty()) {
            // Kita kirim 'password' saja ke ViewModel
            // ViewModel akan menduplikasi 'password' ke 'password_confirmation' untuk API
            viewModel.addMahasiswa(
                token = token,
                name = name,
                username = username,
                email = email,
                password = password,
                nim = nim,
                // Pastikan Program ID diubah ke Int, jika gagal default 0
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