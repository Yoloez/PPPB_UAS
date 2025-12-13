package com.example.pppb_uas.model

import com.google.gson.annotations.SerializedName

// ==========================================
// 1. MODEL UNTUK MENAMPILKAN DATA (RESPONSE)
// ==========================================
data class Dosen(
    @SerializedName("id_user_si")
    val id: String = "", // ✅ PERBAIKAN: Tambah default value "" agar anti-crash

    @SerializedName("name")
    val name: String = "", // ✅ Tambah default value

    @SerializedName("username")
    val nip: String = "", // ✅ Tambah default value

    @SerializedName("email")
    val email: String = "", // ✅ Tambah default value

    val password: String = "",

    @SerializedName("profile_image")
    val photoUrl: String? = null,

    @SerializedName("is_active")
    val isActive: Boolean = true
)

// ==========================================
// 2. MODEL UNTUK MENGIRIM DATA (REQUEST)
// ==========================================
data class AddDosenRequest(
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

    @SerializedName("id_program")
    val idProgram: String
)