package com.example.accesorismvvm.data.repositoryImpl

import android.util.Log
import com.example.accesorismvvm.data.Session.SessionManager
import com.example.accesorismvvm.data.local.entityDAO.UserDao
import com.example.accesorismvvm.data.local.entityDAO.UserEntity
import com.example.accesorismvvm.data.remote.ApiService
import com.example.accesorismvvm.data.remote.request.UpdateUsernameRequest
import com.example.accesorismvvm.data.remote.response.UpdateUsernameResponse
import com.example.accesorismvvm.data.remote.response.toDomain
import com.example.accesorismvvm.domain.model.User
import com.example.accesorismvvm.domain.repository.UserRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val api: ApiService,
    private val userDao: UserDao,
    private val sessionManager: SessionManager
) : UserRepository { // <-- Tambahkan ': UserRepository' untuk implementasi interface

    // Implementasi fungsi register
    override suspend fun register(user: UserEntity) = userDao.insert(user) // <<< Ganti 'insert' menjadi 'insertUser'

    override suspend fun getUserByEmail(email: String) = userDao.getUserByEmail(email)
//
//    override suspend fun getUserById(userId: Int) = userDao.getUserById(userId)
    override suspend fun getUserById(userId: Int): UserEntity? {
        return userDao.getUserById(userId)
    }
    // Mengambil profil dari server dan menyimpannya ke Room
    override suspend fun fetchUserProfile(token: String): User? {
        return try {
            val userDto = api.getUserProfile(token)

            Log.d("UserRepository", "User dari server: ${userDto.email} - ${userDto.name} - ID: ${userDto.id}")

            val localUser = userDao.getUserByEmail(userDto.email)

            val userEntity = UserEntity(
                id = userDto.id,
                username = userDto.name,
                email = userDto.email,
                password = localUser?.password,
                role = userDto.user_role ?: "pembeli"
            )

            userDao.insert(userEntity)
            Log.d("UserRepository", "User disimpan ke Room: ${userEntity.email}")

            userEntity.toDomain()
        } catch (e: Exception) {
            Log.e("UserRepository", "Error fetchUserProfile: ${e.message}", e)
            null
        }
    }


    // Mendapatkan profil dari Room sebagai Flow (untuk pembaruan UI real-time)
//    override fun getUserProfileStream(): Flow<User?> { // <-- Perbaiki tipe return 'user' menjadi 'User'
//        return userDao.getUserProfile().map { it?.toDomain() }
//    }
    override fun getUserProfileStream(): Flow<User?> {
        return userDao.getUserProfile().map { it?.toDomain() }
    }

    // Memperbarui username di server dan juga di Room
    override suspend fun updateUsername(token: String, newUsername: String): User? {
        return try {
            val request = UpdateUsernameRequest(username = newUsername)
            val response: UpdateUsernameResponse = api.updateUsername(token, request)

            if (response.userDto != null) {
                val localUser = userDao.getUserByEmail(response.userDto.email)

                val updatedUserEntity = UserEntity(
                    id = response.userDto.id,
                    username = response.userDto.name,
                    email = response.userDto.email,
                    password = localUser?.password,
                    role = response.userDto.user_role ?: "pembeli"                 )

                userDao.insert(updatedUserEntity)
                updatedUserEntity.toDomain()
            } else {
                null
            }
        } catch (e: Exception) {
            e.printStackTrace()
            throw e
        }
    }


    // Implementasi fungsi clearUserProfile (ditambahkan untuk logout)
    override suspend fun clearUserProfile() {
        userDao.clearUserProfile()
    }

    override suspend fun getUserRole(): String {
        val userId = sessionManager.getUserId()
        return userDao.getUserRole(userId)
    }
}