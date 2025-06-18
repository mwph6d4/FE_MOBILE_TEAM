    package com.example.accesorismvvm.domain.repository

    import com.example.accesorismvvm.data.local.entityDAO.UserEntity
    import com.example.accesorismvvm.data.remote.response.AuthResponse

    interface AuthRepository {
        suspend fun login(email: String, password: String): AuthResponse
        suspend fun register(name: String, email: String, password: String): AuthResponse
        suspend fun syncUserProfile(token: String)
    }
