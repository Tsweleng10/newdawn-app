package com.example.uinewdawn.data.remote

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import com.example.uinewdawn.data.local.TokenManager

object RetrofitClient {
    private const val BASE_URL = "http://10.0.2.2:3000/"

    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor { chain ->
            val request = chain.request().newBuilder()
            TokenManager.getToken()?.let { token ->
                request.addHeader("Authorization", "Bearer $token")
            }
            chain.proceed(request.build())
        }
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .build()

    // Notice we don't need to import ApiService if it's in the same package!
    val apiService: ApiService = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(ApiService::class.java)
}