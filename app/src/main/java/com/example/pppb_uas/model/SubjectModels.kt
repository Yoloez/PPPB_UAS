package com.example.pppb_uas.model

import com.google.gson.annotations.SerializedName

// Model untuk Data Mata Kuliah (Sesuai database Laravel)
data class Subject(
    // ⚠️ PERBAIKAN UTAMA ADA DI SINI ⚠️
    // Ubah "id" menjadi "id_subject" sesuai respon JSON server
    @SerializedName("id_subject")
    val id: Int,

    @SerializedName("name_subject")
    val nameSubject: String,

    @SerializedName("code_subject")
    val codeSubject: String,

    @SerializedName("sks")
    val sks: Int
)

// Request body untuk Create & Update
data class SubjectRequest(
    @SerializedName("name_subject") val nameSubject: String,
    @SerializedName("code_subject") val codeSubject: String,
    @SerializedName("sks") val sks: Int
)

// Response pembungkus umum (Wrapper)
data class BaseResponse<T>(
    @SerializedName("status") val status: String?,
    @SerializedName("message") val message: String?,
    @SerializedName("data") val data: T?
)