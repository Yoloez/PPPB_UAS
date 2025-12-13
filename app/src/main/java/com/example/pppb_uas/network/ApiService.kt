package com.example.pppb_uas.network

import com.example.pppb_uas.model.BaseResponse
import com.example.pppb_uas.model.LoginRequest
import com.example.pppb_uas.model.LoginResponse
import com.example.pppb_uas.model.Subject
import com.example.pppb_uas.model.SubjectRequest
import com.example.pppb_uas.model.Dosen
import com.example.pppb_uas.model.AddDosenRequest

import retrofit2.Response
import retrofit2.http.*

interface ApiService {

    // ========================================================================
    // AUTHENTICATION
    // ========================================================================

    @Headers("Accept: application/json")
    @POST("auth/login")
    suspend fun login(
        @Body request: LoginRequest
    ): Response<LoginResponse>

    @Headers("Accept: application/json")
    @POST("auth/logout")
    suspend fun logout(
        @Header("Authorization") token: String
    ): Response<BaseResponse<Any>>

    // ========================================================================
    // SUBJECTS (MATA KULIAH)
    // ========================================================================

    // READ - Get All Courses
    @Headers("Accept: application/json")
    @GET("manager/subjects")
    suspend fun getSubjects(
        @Header("Authorization") token: String
    ): Response<BaseResponse<List<Subject>>>

    // CREATE - Add New Course
    @Headers("Accept: application/json")
    @POST("manager/subjects")
    suspend fun createSubject(
        @Header("Authorization") token: String,
        @Body request: SubjectRequest
    ): Response<BaseResponse<Any>>

    // UPDATE - Edit Course
    @Headers("Accept: application/json")
    @PUT("manager/subjects/{id}")
    suspend fun updateSubject(
        @Header("Authorization") token: String,
        @Path("id") id: Int,
        @Body request: SubjectRequest
    ): Response<BaseResponse<Any>>

    // DELETE - Hapus Course
    @Headers("Accept: application/json")
    @DELETE("manager/subjects/{id}")
    suspend fun deleteSubject(
        @Header("Authorization") token: String,
        @Path("id") id: Int
    ): Response<BaseResponse<Any>>

    // ========================================================================
    // LECTURERS (DOSEN)
    // Base URL: /api/manager/lecturers
    // ========================================================================

    // READ - Get All Lecturers
    @Headers("Accept: application/json")
    @GET("manager/lecturers")
    suspend fun getLecturers(
        @Header("Authorization") token: String
    ): Response<BaseResponse<List<Dosen>>>

    // CREATE - Add New Lecturer
    // Request Body: name, username, email, password, password_confirmation
    @Headers("Accept: application/json")
    @POST("manager/lecturers")
    suspend fun createLecturer(
        @Header("Authorization") token: String,
        @Body request: AddDosenRequest
    ): Response<BaseResponse<Any>>

    // TOGGLE STATUS - Mengaktifkan/Nonaktifkan Dosen
    // Route: PATCH /api/manager/users/{id}/toggle-status
    @Headers("Accept: application/json")
    @PATCH("manager/users/{id}/toggle-status")
    suspend fun toggleUserStatus(
        @Header("Authorization") token: String,
        @Path("id") id: String // Gunakan String atau Int sesuai tipe ID di database (UUID vs ID biasa)
    ): Response<BaseResponse<Any>>
}