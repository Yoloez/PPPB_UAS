package com.example.pppb_uas.model

import com.google.gson.annotations.SerializedName

data class Dosen(
    @SerializedName("id_user_si")
    val id: String = "", //

    @SerializedName("name")
    val name: String = "", //

    @SerializedName("username")
    val nip: String = "", //

    @SerializedName("email")
    val email: String = "",

    val password: String = "",

    @SerializedName("profile_image")
    val photoUrl: String? = null,

    @SerializedName("is_active")
    val isActive: Boolean = true
)

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