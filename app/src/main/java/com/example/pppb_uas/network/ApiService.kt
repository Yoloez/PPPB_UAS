package com.example.pppb_uas.network

import com.example.pppb_uas.model.*
import retrofit2.Response
import retrofit2.http.*

interface ApiService {

    // ========================================================================
    // AUTHENTICATION
    // ========================================================================
    @Headers("Accept: application/json")
    @POST("auth/login")
    suspend fun login(@Body request: LoginRequest): Response<LoginResponse>

    @Headers("Accept: application/json")
    @POST("auth/logout")
    suspend fun logout(@Header("Authorization") token: String): Response<BaseResponse<Any>>

    // ========================================================================
    // SUBJECTS (MATA KULIAH)
    // ========================================================================
    @Headers("Accept: application/json")
    @GET("manager/subjects")
    suspend fun getSubjects(@Header("Authorization") token: String): Response<BaseResponse<List<Subject>>>

    @Headers("Accept: application/json")
    @POST("manager/subjects")
    suspend fun createSubject(
        @Header("Authorization") token: String,
        @Body request: SubjectRequest
    ): Response<BaseResponse<Any>>

    @Headers("Accept: application/json")
    @PUT("manager/subjects/{id}")
    suspend fun updateSubject(
        @Header("Authorization") token: String,
        @Path("id") id: Int,
        @Body request: SubjectRequest
    ): Response<BaseResponse<Any>>

    @Headers("Accept: application/json")
    @DELETE("manager/subjects/{id}")
    suspend fun deleteSubject(
        @Header("Authorization") token: String,
        @Path("id") id: Int
    ): Response<BaseResponse<Any>>

    // ========================================================================
    // MANAJEMEN USER (DOSEN & MAHASISWA)
    // Base URL: /api/manager/...
    // ========================================================================

    // --- DOSEN ---
    @Headers("Accept: application/json")
    @GET("manager/lecturers")
    suspend fun getLecturers(@Header("Authorization") token: String): Response<BaseResponse<List<Dosen>>>

    @Headers("Accept: application/json")
    @POST("manager/lecturers")
    suspend fun createLecturer(
        @Header("Authorization") token: String,
        @Body request: AddDosenRequest
    ): Response<BaseResponse<Any>>

    // --- MAHASISWA ---
    // GET List Mahasiswa
    @Headers("Accept: application/json")
    @GET("manager/students")
    suspend fun getStudents(@Header("Authorization") token: String): Response<BaseResponse<List<Mahasiswa>>>

    // POST Create Mahasiswa
    @Headers("Accept: application/json")
    @POST("manager/students")
    suspend fun createStudent(
        @Header("Authorization") token: String,
        @Body request: AddStudentRequest
    ): Response<BaseResponse<Any>>

    // --- UMUM (Toggle Status untuk Dosen & Mahasiswa) ---
    @Headers("Accept: application/json")
    @PATCH("manager/users/{id}/toggle-status")
    suspend fun toggleUserStatus(
        @Header("Authorization") token: String,
        @Path("id") id: String
    ): Response<BaseResponse<Any>>

    // Di ApiService.kt
    @GET("manager/programs")
    suspend fun getProgramStudi(
        @Header("Authorization") token: String
    ): Response<ProgramStudiResponse>
}