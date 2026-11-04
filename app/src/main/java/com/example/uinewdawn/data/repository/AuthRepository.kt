package com.example.uinewdawn.data.repository

import com.example.uinewdawn.data.local.TokenManager
import com.example.uinewdawn.data.remote.ApiService
import com.example.uinewdawn.data.remote.dto.*
import java.security.MessageDigest

class AuthRepository(
    private val apiService: ApiService = com.example.uinewdawn.data.remote.RetrofitClient.apiService
) {

    // ---------- LOGIN ----------
    suspend fun login(email: String, password: String): Result<LoginResponse> {
        return try {
            val encryptedPassword = hashPassword(password)
            val response = apiService.login(
                LoginRequest(email = email, password = encryptedPassword)
            )

            if (response.isSuccessful && response.body() != null) {
                val body = response.body()!!
                TokenManager.saveToken(body.token)
                TokenManager.saveUser(body.user.id, body.user.fullName, body.user.email)
                Result.success(body)
            } else {
                val errorMsg = when (response.code()) {
                    401 -> "Invalid email or password"
                    400 -> "Bad request. Please check your inputs."
                    else -> "Login failed (${response.code()})"
                }
                Result.failure(Exception(errorMsg))
            }
        } catch (e: Exception) {
            Result.failure(Exception("Network error: ${e.localizedMessage ?: "Unknown"}"))
        }
    }

    // ---------- REGISTER ----------
    suspend fun register(fullName: String, email: String, password: String): Result<RegisterResponse> {
        return try {
            val encryptedPassword = hashPassword(password)
            val response = apiService.register(
                RegisterRequest(fullName = fullName, email = email, password = encryptedPassword)
            )

            if (response.isSuccessful && response.body() != null) {
                val body = response.body()!!
                TokenManager.saveToken(body.token)
                TokenManager.saveUser(body.user.id, body.user.fullName, body.user.email)
                Result.success(body)
            } else {
                val errorMsg = when (response.code()) {
                    409 -> "Email already registered"
                    400 -> "Invalid input. Check your details."
                    else -> "Registration failed (${response.code()})"
                }
                Result.failure(Exception(errorMsg))
            }
        } catch (e: Exception) {
            Result.failure(Exception("Network error: ${e.localizedMessage ?: "Unknown"}"))
        }
    }

    // ---------- LOGOUT ----------
    suspend fun logout() {
        TokenManager.clear()
    }

    // ---------- PASSWORD HASHING (SHA-256) ----------
    private fun hashPassword(password: String): String {
        val bytes = MessageDigest.getInstance("SHA-256").digest(password.toByteArray())
        return bytes.joinToString("") { "%02x".format(it) }
    }
}