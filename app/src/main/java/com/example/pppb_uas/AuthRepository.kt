package com.example.pppb_uas
import com.example.pppb_uas.model.LoginRequest
import com.example.pppb_uas.model.LoginResponse
import com.example.pppb_uas.network.RetrofitInstance

class AuthRepository {
    private val api = RetrofitInstance.api

    suspend fun login(email: String, password: String): Result<LoginResponse> {
        return try {
            val response = api.login(LoginRequest(email, password))
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception(response.message() ?: "Login failed"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}