package com.example.pppb_uas.model

import com.google.gson.annotations.SerializedName

data class Mahasiswa(
    // ⚠️ PERHATIKAN: key json harus sama persis dengan backend
    @SerializedName("id_user_si") val id: String? = null,
    @SerializedName("full_name") val name: String? = null,
    @SerializedName("username") val username: String? = null,
    @SerializedName("email") val email: String? = null,
    @SerializedName("registration_number") val nim: String? = null,
    @SerializedName("program_name") val programName: String? = null,
    @SerializedName("profile_image") val profilImage: String? = null,
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

data class AddStudentRequest(
    @SerializedName("full_name")
    val name: String,

    @SerializedName("username")
    val username: String,

    @SerializedName("email")
    val email: String,

    @SerializedName("password")
    val password: String,

    @SerializedName("password_confirmation")
    val passwordConfirmation: String,

    @SerializedName("registration_number")
    val nim: String,

    @SerializedName("profile_image")
    val profilImage: String? = null,

    @SerializedName("id_program")
    val programId: Int,


)

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