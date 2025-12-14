package com.example.pppb_uas.model

import com.google.gson.annotations.SerializedName

// ==========================================
// 1. MODEL UNTUK MENAMPILKAN DATA (GET)
// ==========================================
data class Mahasiswa(
    // ⚠️ PERHATIKAN: key json harus sama persis dengan backend
    @SerializedName("id_user_si") val id: String? = null,
    @SerializedName("name") val name: String? = null,
    @SerializedName("username") val username: String? = null,
    @SerializedName("email") val email: String? = null,

    // Backend mengirim 'nim', bukan 'registration_number'
    @SerializedName("nim") val nim: String? = null,

    // Backend mengirim 'program_name', bukan 'program_id'
    @SerializedName("program_name") val programName: String? = null,

    @SerializedName("is_active") val isActiveRaw: Any? = null
) {
    val isActiveBoolean: Boolean
        get() {
            return when (isActiveRaw) {
                is Boolean -> isActiveRaw
                is Number -> isActiveRaw.toInt() == 1
                is String -> isActiveRaw == "1" || isActiveRaw.equals("true", ignoreCase = true)
                else -> false
            }
        }

    val safeId get() = id ?: ""
}

// ==========================================
// 2. MODEL UNTUK MENGIRIM DATA (POST) -> YANG TADI ERROR
// ==========================================
data class AddStudentRequest(
    @SerializedName("name")
    val name: String,

    @SerializedName("username")
    val username: String,

    @SerializedName("email")
    val email: String,

    @SerializedName("password")
    val password: String,

    @SerializedName("password_confirmation")
    val passwordConfirmation: String,

    // Backend minta "registration_number" untuk NIM saat input
    @SerializedName("registration_number")
    val nim: String,

    // ⚠️ PERBAIKAN UTAMA DI SINI ⚠️
    // Sebelumnya "program_id", tapi errornya minta "id_program"
    @SerializedName("id_program")
    val programId: Int
)

// ==========================================
// 3. RESPONSE WRAPPER
// ==========================================
data class MahasiswaResponse(
    @SerializedName("status") val status: String,
    @SerializedName("message") val message: String? = null,
    @SerializedName("data") val data: List<Mahasiswa>? = emptyList()
)

data class MahasiswaOperationResponse(
    @SerializedName("status") val status: String,
    @SerializedName("message") val message: String?,
    @SerializedName("data") val data: Any? = null
)