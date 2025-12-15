package com.example.pppb_uas.viewmodel

import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pppb_uas.model.Subject
import com.example.pppb_uas.model.SubjectRequest
import com.example.pppb_uas.network.RetrofitInstance  // ✅ Import RetrofitInstance
import kotlinx.coroutines.launch
import org.json.JSONObject

class CourseViewModel : ViewModel() {

    private val apiService = RetrofitInstance.api

    // State untuk menyimpan data
    var courses = mutableStateListOf<Subject>()
    var isLoading = mutableStateOf(false)
    var errorMessage = mutableStateOf("")

    // ==========================================
    // 1. GET COURSES (READ)
    // ==========================================
    fun getCourses(token: String) {
        viewModelScope.launch {
            isLoading.value = true
            errorMessage.value = ""

            try {
                Log.d("CourseVM", "📡 Fetching courses...")

                val response = apiService.getSubjects("Bearer $token")

                Log.d("CourseVM", "Response code: ${response.code()}")

                if (response.isSuccessful) {
                    val body = response.body()

                    if (body?.data != null) {
                        courses.clear()
                        courses.addAll(body.data)
                        Log.d("CourseVM", "✅ Loaded ${courses.size} courses")
                    } else {
                        errorMessage.value = "Data kosong dari server"
                        Log.e("CourseVM", "❌ Response body null")
                    }
                } else {
                    val errorBody = response.errorBody()?.string()
                    errorMessage.value = "Gagal memuat data: ${response.code()}"
                    Log.e("CourseVM", "❌ Error ${response.code()}: $errorBody")
                }
            } catch (e: Exception) {
                errorMessage.value = "Error: ${e.message}"
                Log.e("CourseVM", "❌ Exception: ${e.message}", e)
            } finally {
                isLoading.value = false
            }
        }
    }

    // ==========================================
    // 2. ADD COURSE (CREATE)
    // ==========================================
    fun addCourse(
        token: String,
        name: String,
        code: String,
        sks: String,
        onSuccess: () -> Unit
    ) {
        viewModelScope.launch {
            isLoading.value = true
            errorMessage.value = ""

            try {
                val sksInt = sks.toIntOrNull() ?: 0
                val request = SubjectRequest(name, code, sksInt)

                Log.d("CourseVM", "📤 Adding course: $request")

                val response = apiService.createSubject("Bearer $token", request)

                Log.d("CourseVM", "Add response code: ${response.code()}")

                if (response.isSuccessful) {
                    Log.d("CourseVM", "Mata Kuliah Berhasil Ditambahkan")

                    // Refresh data
                    getCourses(token)

                    // Navigate back
                    onSuccess()
                } else {
                    val errorBody = response.errorBody()?.string()
                    Log.e("CourseVM", "❌ Add failed: $errorBody")

                    // Parse error message dari backend
                    try {
                        val json = JSONObject(errorBody ?: "{}")
                        errorMessage.value = json.optString("message", "Gagal menambahkan data")
                    } catch (e: Exception) {
                        errorMessage.value = "Gagal menambahkan: ${response.code()}"
                    }
                }
            } catch (e: Exception) {
                errorMessage.value = "Error: ${e.message}"
                Log.e("CourseVM", "❌ Add exception: ${e.message}", e)
            } finally {
                isLoading.value = false
            }
        }
    }

    // ==========================================
    // 3. UPDATE COURSE (EDIT)
    // ==========================================
    fun updateCourse(
        token: String,
        id: Int,
        name: String,
        code: String,
        sks: String,
        onSuccess: () -> Unit
    ) {
        viewModelScope.launch {
            isLoading.value = true
            errorMessage.value = ""

            try {
                val sksInt = sks.toIntOrNull() ?: 0
                val request = SubjectRequest(name, code, sksInt)

                Log.d("CourseVM", "📝 Updating course ID $id: $request")

                val response = apiService.updateSubject("Bearer $token", id, request)

                if (response.isSuccessful) {
                    Log.d("CourseVM", "✅ Course updated successfully")
                    getCourses(token)
                    onSuccess()
                } else {
                    val errorBody = response.errorBody()?.string()
                    errorMessage.value = "Gagal update: ${response.code()}"
                    Log.e("CourseVM", "❌ Update failed: $errorBody")
                }
            } catch (e: Exception) {
                errorMessage.value = "Error: ${e.message}"
                Log.e("CourseVM", "❌ Update exception: ${e.message}", e)
            } finally {
                isLoading.value = false
            }
        }
    }

    // ==========================================
    // 4. DELETE COURSE (HAPUS)
    // ==========================================
    fun deleteCourse(token: String, id: Int) {
        viewModelScope.launch {
            isLoading.value = true
            errorMessage.value = ""

            try {
                Log.d("CourseVM", "🗑️ Deleting course ID: $id")

                val response = apiService.deleteSubject("Bearer $token", id)

                if (response.isSuccessful) {
                    Log.d("CourseVM", "✅ Course deleted successfully")
                    getCourses(token)
                } else {
                    val errorBody = response.errorBody()?.string()
                    errorMessage.value = "Gagal hapus: ${response.code()}"
                    Log.e("CourseVM", "❌ Delete failed: $errorBody")
                }
            } catch (e: Exception) {
                errorMessage.value = "Error: ${e.message}"
                Log.e("CourseVM", "❌ Delete exception: ${e.message}", e)
            } finally {
                isLoading.value = false
            }
        }
    }
}