package com.example.uinewdawn.data.remote.dto

data class LoginRequest(val email: String, val password: String)
data class UserDto(val id: String, val fullName: String, val email: String)
data class LoginResponse(val token: String, val user: UserDto)
data class RegisterRequest(val fullName: String, val email: String, val password: String)
data class RegisterResponse(val token: String, val user: UserDto)