package com.example.pppb_uas.model

import com.google.gson.annotations.SerializedName

data class ProgramStudi(
    @SerializedName("id")
    val id: Int,

    @SerializedName("name")
    val name: String
)

// Response wrapper dari API
data class ProgramStudiResponse(
    @SerializedName("data")
    val data: List<ProgramStudi>
)