package com.example.accesorismvvm.data.remote.response

data class AuthResponse(
    val message: String,
    val auth_token: String?,
    val user_role: String?,
    val user: UserDto?
) {
    val success: Boolean
        get() = !auth_token.isNullOrEmpty() && user != null
}


