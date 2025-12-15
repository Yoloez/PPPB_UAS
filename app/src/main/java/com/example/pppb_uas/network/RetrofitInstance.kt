package com.example.pppb_uas.network

import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object RetrofitInstance {
    // ✅ URL SUDAH DIUPDATE KE PRODUCTION
    // Penting: Harus diakhiri dengan tanda '/' (slash)
    private const val BASE_URL = "https://api.trisuladana.com/api/"

    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        // Level BODY berguna untuk melihat request/response lengkap di Logcat
        level = HttpLoggingInterceptor.Level.BODY
    }

    // Gson dengan setLenient untuk menangani format JSON yang mungkin tidak standar
    private val gson = GsonBuilder()
        .setLenient()
        .create()

    private val client = OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor)
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .writeTimeout(30, TimeUnit.SECONDS)
        .build()

    val api: ApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
            .create(ApiService::class.java)
    }
}