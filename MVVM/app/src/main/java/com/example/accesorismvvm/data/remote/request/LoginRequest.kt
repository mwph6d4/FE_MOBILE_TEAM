package com.example.accesorismvvm.data.remote.request

data class LoginRequest(
    val email: String,
    val password: String,
    val client_type: String = "mobile"
)

