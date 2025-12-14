package com.example.pppb_uas.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pppb_uas.model.AddStudentRequest
import com.example.pppb_uas.model.Mahasiswa
import com.example.pppb_uas.model.ProgramStudi // ✅ Import ProgramStudi
import com.example.pppb_uas.network.RetrofitInstance
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.json.JSONObject

data class AddMahasiswaUiState(
    val isLoading: Boolean = false,
    val isSuccess: Boolean = false,
    val errorMessage: String? = null
)

data class MahasiswaListUiState(
    val listMahasiswa: List<Mahasiswa> = emptyList(),
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)

class MahasiswaViewModel : ViewModel() {

    private val apiService = RetrofitInstance.api

    // --- STATE ---
    private val _addUiState = MutableStateFlow(AddMahasiswaUiState())
    val addUiState: StateFlow<AddMahasiswaUiState> = _addUiState.asStateFlow()

    private val _listUiState = MutableStateFlow(MahasiswaListUiState())
    val listUiState: StateFlow<MahasiswaListUiState> = _listUiState.asStateFlow()

    // ✅ State List Program Studi
    private val _programListState = MutableStateFlow<List<ProgramStudi>>(emptyList())
    val programListState: StateFlow<List<ProgramStudi>> = _programListState.asStateFlow()

    // --- FUNGSI ---

    // 1. Fetch Program Studi (Dropdown)
    fun fetchPrograms(token: String) {
        viewModelScope.launch {
            try {
                val finalToken = if (token.startsWith("Bearer ", true)) token else "Bearer $token"

                // ✅ Panggil fungsi yang benar: getProgramStudi
                val response = apiService.getProgramStudi(finalToken)

                if (response.isSuccessful) {
                    // ✅ Ambil data dari ProgramStudiResponse
                    // response.body() adalah ProgramStudiResponse
                    // response.body()?.data adalah List<ProgramStudi>
                    _programListState.value = response.body()?.data ?: emptyList()
                } else {
                    Log.e("ViewModel", "Gagal fetch program: ${response.message()}")
                }
            } catch (e: Exception) {
                Log.e("ViewModel", "Error fetch program: ${e.message}")
            }
        }
    }

    // 2. Fetch Mahasiswa
    fun fetchMahasiswa(token: String) {
        viewModelScope.launch {
            _listUiState.value = _listUiState.value.copy(isLoading = true, errorMessage = null)
            try {
                val finalToken = if (token.startsWith("Bearer ", true)) token else "Bearer $token"
                val response = apiService.getStudents(finalToken)

                if (response.isSuccessful && response.body()?.status == "success") {
                    _listUiState.value = MahasiswaListUiState(
                        listMahasiswa = response.body()?.data ?: emptyList(),
                        isLoading = false
                    )
                } else {
                    _listUiState.value = MahasiswaListUiState(isLoading = false, errorMessage = "Gagal: ${response.message()}")
                }
            } catch (e: Exception) {
                _listUiState.value = MahasiswaListUiState(isLoading = false, errorMessage = "Error: ${e.message}")
            }
        }
    }

    // 3. Add Mahasiswa
    fun addMahasiswa(
        token: String,
        name: String,
        username: String,
        email: String,
        password: String,
        nim: String,
        programId: Int
    ) {
        viewModelScope.launch {
            _addUiState.value = AddMahasiswaUiState(isLoading = true)

            val request = AddStudentRequest(
                name = name,
                username = username,
                email = email,
                password = password,
                passwordConfirmation = password,
                nim = nim,
                programId = programId
            )

            try {
                val finalToken = if (token.startsWith("Bearer ", true)) token else "Bearer $token"
                val response = apiService.createStudent(finalToken, request)

                if (response.isSuccessful) {
                    _addUiState.value = AddMahasiswaUiState(isLoading = false, isSuccess = true)
                    fetchMahasiswa(token)
                } else {
                    val errorBody = response.errorBody()?.string()
                    val msg = parseLaravelError(errorBody) ?: "Gagal: Code ${response.code()}"
                    _addUiState.value = AddMahasiswaUiState(isLoading = false, errorMessage = msg)
                }
            } catch (e: Exception) {
                _addUiState.value = AddMahasiswaUiState(isLoading = false, errorMessage = "Jaringan Error: ${e.message}")
            }
        }
    }

    // ... toggleStatus & resetAddState & parseLaravelError sama seperti sebelumnya ...

    fun toggleStatus(token: String, id: String) {
        viewModelScope.launch {
            try {
                val finalToken = if (token.startsWith("Bearer ", true)) token else "Bearer $token"
                val response = apiService.toggleUserStatus(finalToken, id)
                if (response.isSuccessful) fetchMahasiswa(token)
            } catch (e: Exception) { e.printStackTrace() }
        }
    }

    fun resetAddState() { _addUiState.value = AddMahasiswaUiState() }

    private fun parseLaravelError(errorBody: String?): String? {
        if (errorBody.isNullOrEmpty()) return null
        return try {
            val json = JSONObject(errorBody)
            val message = json.optString("message", "")
            val errors = json.optJSONObject("errors")
            if (errors != null) {
                val keys = errors.keys()
                if (keys.hasNext()) {
                    val firstKey = keys.next()
                    val firstErrorArray = errors.getJSONArray(firstKey)
                    return "$message \n$firstKey: ${firstErrorArray.getString(0)}"
                }
            }
            if (message.isNotEmpty()) return message
            null
        } catch (e: Exception) { null }
    }
}