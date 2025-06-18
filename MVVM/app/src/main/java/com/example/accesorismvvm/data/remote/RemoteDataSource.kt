package com.example.accesorismvvm.data.remote

import com.example.accesorismvvm.data.remote.request.LoginRequest
import com.example.accesorismvvm.data.remote.request.RegisterRequest
import com.example.accesorismvvm.data.remote.response.AuthResponse
import com.example.accesorismvvm.data.remote.response.UserDto
import javax.inject.Inject

class RemoteDataSource @Inject constructor(
    private val authApi: ApiService
) {
    suspend fun login(request: LoginRequest): AuthResponse {
        return authApi.login(request)
    }

    suspend fun register(request: RegisterRequest): AuthResponse {
        return authApi.register(request)
    }

    suspend fun getUserProfile(token: String): UserDto {
        return authApi.getUserProfile("Bearer $token")
    }

}
