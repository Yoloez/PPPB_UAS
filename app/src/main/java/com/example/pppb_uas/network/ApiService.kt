package com.example.pppb_uas.network
import com.example.pppb_uas.model.LoginRequest
import com.example.pppb_uas.model.LoginResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiService {
    @POST("api/auth/login")
    suspend fun login(@Body request: LoginRequest): Response<LoginResponse>
}