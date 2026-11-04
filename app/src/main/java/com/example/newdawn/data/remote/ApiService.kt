package com.example.newdawn.data.remote

import com.example.newdawn.data.models.*
import retrofit2.Response
import retrofit2.http.*

interface ApiService {

    // ─── AUTH ─────────────────────────────────────────────
    @POST("api/auth/register")
    suspend fun register(@Body request: RegisterRequest): Response<AuthResponse>

    @POST("api/auth/login")
    suspend fun login(@Body request: LoginRequest): Response<AuthResponse>

    @GET("api/auth/me")
    suspend fun getCurrentUser(@Header("Authorization") token: String): Response<User>

    @PUT("api/auth/settings")
    suspend fun updateSettings(
        @Header("Authorization") token: String,
        @Body body: Map<String, @JvmSuppressWildcards Any>
    ): Response<Map<String, String>>

    @PUT("api/auth/change-password")
    suspend fun changePassword(
        @Header("Authorization") token: String,
        @Body body: Map<String, @JvmSuppressWildcards Any>
    ): Response<Map<String, String>>

    // ─── JOBS ─────────────────────────────────────────────
    @GET("api/jobs")
    suspend fun getJobs(
        @Header("Authorization") token: String,
        @Query("search") search: String? = null,
        @Query("category") category: String? = null,
        @Query("location") location: String? = null
    ): Response<List<Job>>

    @GET("api/jobs/my")
    suspend fun getMyJobs(@Header("Authorization") token: String): Response<List<Job>>

    @GET("api/jobs/{id}")
    suspend fun getJobById(
        @Header("Authorization") token: String,
        @Path("id") jobId: Int
    ): Response<Job>

    @POST("api/jobs")
    suspend fun createJob(
        @Header("Authorization") token: String,
        @Body job: Job
    ): Response<Job>

    // ─── OFFERS ───────────────────────────────────────────
    @POST("api/offers/job/{jobId}")
    suspend fun submitOffer(
        @Header("Authorization") token: String,
        @Path("jobId") jobId: Int,
        @Body offer: Offer
    ): Response<Offer>

    @GET("api/offers/job/{jobId}")
    suspend fun getOffersForJob(
        @Header("Authorization") token: String,
        @Path("jobId") jobId: Int
    ): Response<List<Offer>>

    @GET("api/offers/my")
    suspend fun getMyOffers(@Header("Authorization") token: String): Response<List<Offer>>

    @PUT("api/offers/{id}/accept")
    suspend fun acceptOffer(
        @Header("Authorization") token: String,
        @Path("id") offerId: Int
    ): Response<Map<String, String>>
}