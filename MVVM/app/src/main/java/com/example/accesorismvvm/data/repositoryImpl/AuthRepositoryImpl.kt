package com.example.accesorismvvm.data.repositoryImpl

import android.net.http.HttpException
import android.util.Log
import com.example.accesorismvvm.data.local.entityDAO.UserDao
import com.example.accesorismvvm.data.local.entityDAO.UserEntity
import com.example.accesorismvvm.data.remote.ApiService
import com.example.accesorismvvm.data.remote.RemoteDataSource
import com.example.accesorismvvm.data.remote.request.LoginRequest
import com.example.accesorismvvm.data.remote.request.RegisterRequest
import com.example.accesorismvvm.data.remote.response.AuthResponse
import com.example.accesorismvvm.data.remote.response.toUserDto
import com.example.accesorismvvm.domain.repository.AuthRepository
import java.io.IOException
import java.util.prefs.Preferences
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val remoteDataSource: RemoteDataSource,
    private val userDao: UserDao
) : AuthRepository {

    override suspend fun register(name: String, email: String, password: String): AuthResponse {
        return try {
            val response = remoteDataSource.register(RegisterRequest(name, email, password))

            response.user?.let {
                val userEntity = UserEntity(
                    id = it.id,
                    username = it.name,
                    email = it.email,
                    password = password,
                    role = response.user_role ?: "pembeli"
                )
                userDao.insert(userEntity)
            }

            response
        } catch (e: IOException) {
            // Ini error jaringan, misalnya tidak ada koneksi internet
            Log.e("UserRepository", "Tidak dapat terhubung ke server", e)
            throw Exception("Registrasi Gagal. Tidak dapat terhubung ke server.")
        }
    }


    override suspend fun login(email: String, password: String): AuthResponse {
        return try {
            val response = remoteDataSource.login(LoginRequest(email, password))

            response.user?.let {
                val userEntity = UserEntity(
                    id = it.id,
                    username = it.name,
                    email = it.email,
                    password = password,
                    role = response.user_role ?: "pembeli"
                )
                userDao.clearUserProfile()
                userDao.insert(userEntity)
                Log.d("LOGIN", "User ${userEntity.email} berhasil disimpan ke Room.")
            }

            response
        } catch (e: Exception) {
            Log.e("LOGIN", "Gagal login online: ${e.message}")

            val localUser = userDao.getUserByEmail(email)
            Log.d("LOGIN", "User dari Room: $localUser")

            if (localUser != null) {
                Log.d("LOGIN", "Password disimpan: ${localUser.password}, Password input: $password")
            }

            if (localUser != null && localUser.password == password) {
                Log.d("LOGIN", "Login offline berhasil") // ← Tambah log ini
                AuthResponse(
                    message = "Login offline berhasil",
                    auth_token = "offline",
                    user_role = "pembeli",
                    user = localUser.toUserDto()
                )
            } else {
                throw Exception("Login gagal: tidak terhubung ke server dan user tidak ditemukan di lokal")
            }
        }

    }


    override suspend fun syncUserProfile(token: String) {
        val userDto = remoteDataSource.getUserProfile(token)

        val userEntity = UserEntity(
            id = userDto.id,
            username = userDto.name,
            email = userDto.email,
            password = "",
            role = userDto.user_role ?: "pembeli"
        )

        userDao.insert(userEntity)
    }



}


