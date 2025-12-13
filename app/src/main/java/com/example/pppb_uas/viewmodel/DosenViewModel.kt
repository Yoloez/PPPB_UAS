package com.example.pppb_uas.viewmodel

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import com.example.pppb_uas.model.Dosen
import com.example.pppb_uas.model.AddDosenRequest
// PAKE INI SESUAI REQUEST LO:
import com.example.pppb_uas.network.RetrofitInstance

// State List
data class DosenListUiState(
    val dosenList: List<Dosen> = emptyList(),
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)

// State Form Add
data class AddDosenFormState(
    val name: String = "",
    val nip: String = "",
    val email: String = "",
    val password: String = "",
    val confirmPassword: String = "",
    val programId: String = "",
    val imageUri: Uri? = null,
    val isLoading: Boolean = false,
    val isSuccess: Boolean = false,
    val errorMessage: String? = null
)

class DosenViewModel : ViewModel() {

    private val _listUiState = MutableStateFlow(DosenListUiState())
    val listUiState: StateFlow<DosenListUiState> = _listUiState.asStateFlow()

    private val _addFormState = MutableStateFlow(AddDosenFormState())
    val addFormState: StateFlow<AddDosenFormState> = _addFormState.asStateFlow()

    // ==========================================
    // 1. FETCH DOSEN (Pake RetrofitInstance)
    // ==========================================
    fun fetchDosenList(token: String) {
        viewModelScope.launch {
            _listUiState.value = _listUiState.value.copy(isLoading = true)

            try {
                // PANGGIL VIA RETROFIT INSTANCE
                val response = RetrofitInstance.api.getLecturers("Bearer $token")

                if (response.isSuccessful) {
                    // Ambil data dengan aman (?.let) biar ga crash kalau null
                    val dataDosen = response.body()?.data ?: emptyList()

                    _listUiState.value = DosenListUiState(
                        dosenList = dataDosen,
                        isLoading = false
                    )
                } else {
                    val msg = response.message()
                    Log.e("DosenVM", "Gagal Fetch: $msg")
                    _listUiState.value = _listUiState.value.copy(
                        isLoading = false,
                        errorMessage = "Gagal: $msg"
                    )
                }
            } catch (e: Exception) {
                Log.e("DosenVM", "Error Fetch: ${e.message}")
                _listUiState.value = _listUiState.value.copy(
                    isLoading = false,
                    errorMessage = "Koneksi Error: ${e.message}"
                )
            }
        }
    }

    // ==========================================
    // 2. TOGGLE STATUS (ANTI FORCE CLOSE)
    // ==========================================
    fun toggleDosenStatus(token: String, dosenId: String) {
        viewModelScope.launch {
            try {
                Log.d("DosenVM", "Toggling ID: $dosenId")

                // PANGGIL API
                val response = RetrofitInstance.api.toggleUserStatus("Bearer $token", dosenId)

                if (response.isSuccessful) {
                    Log.d("DosenVM", "✅ Toggle Sukses di Server")
                    // Sukses -> Refresh data list biar sinkron
                    fetchDosenList(token)
                } else {
                    // Baca pesan error dengan aman
                    val errorBody = try { response.errorBody()?.string() } catch (e: Exception) { "Unknown error" }
                    Log.e("DosenVM", "❌ Gagal Toggle: ${response.code()} - $errorBody")
                }
            } catch (t: Throwable) {
                // 👇 INI YANG PENTING: Pakai 'Throwable' biar gak Force Close kalau JSON error
                Log.e("DosenVM", "⚠️ FORCE CLOSE DITANGKAP: ${t.message}")
                t.printStackTrace()
            }
        }
    }

    // ==========================================
    // 3. SAVE DOSEN (Pake RetrofitInstance)
    // ==========================================
    fun saveDosen(token: String, context: Context) {
        val form = _addFormState.value

        // Validasi
        if (form.password != form.confirmPassword) {
            _addFormState.value = form.copy(errorMessage = "Password tidak sama!")
            return
        }
        if (form.name.isBlank() || form.nip.isBlank() || form.programId.isBlank()) {
            _addFormState.value = form.copy(errorMessage = "Data wajib diisi!")
            return
        }

        viewModelScope.launch {
            _addFormState.value = form.copy(isLoading = true, errorMessage = null)

            try {
                val requestBody = AddDosenRequest(
                    name = form.name,
                    username = form.nip,
                    email = form.email,
                    password = form.password,
                    passwordConfirmation = form.confirmPassword,
                    idProgram = form.programId // Pastikan user input angka
                )

                // PANGGIL VIA RETROFIT INSTANCE
                val response = RetrofitInstance.api.createLecturer("Bearer $token", requestBody)

                if (response.isSuccessful) {
                    _addFormState.value = form.copy(isLoading = false, isSuccess = true)
                    fetchDosenList(token)
                } else {
                    val errorMsg = response.errorBody()?.string() ?: response.message()
                    Log.e("DosenVM", "Gagal Save: $errorMsg")
                    _addFormState.value = form.copy(
                        isLoading = false,
                        errorMessage = "Gagal: $errorMsg"
                    )
                }
            } catch (e: Exception) {
                Log.e("DosenVM", "Crash Save: ${e.message}")
                _addFormState.value = form.copy(
                    isLoading = false,
                    errorMessage = "Error: ${e.message}"
                )
            }
        }
    }

    // --- FORM HANDLERS ---
    fun onNameChange(v: String) { _addFormState.value = _addFormState.value.copy(name = v) }
    fun onNipChange(v: String) { _addFormState.value = _addFormState.value.copy(nip = v) }
    fun onEmailChange(v: String) { _addFormState.value = _addFormState.value.copy(email = v) }
    fun onPasswordChange(v: String) { _addFormState.value = _addFormState.value.copy(password = v) }
    fun onConfirmPasswordChange(v: String) { _addFormState.value = _addFormState.value.copy(confirmPassword = v) }
    fun onProgramIdChange(v: String) { _addFormState.value = _addFormState.value.copy(programId = v) }
    fun onImageSelected(uri: Uri?) { _addFormState.value = _addFormState.value.copy(imageUri = uri) }

    fun resetAddFormState() {
        _addFormState.value = AddDosenFormState()
    }
}