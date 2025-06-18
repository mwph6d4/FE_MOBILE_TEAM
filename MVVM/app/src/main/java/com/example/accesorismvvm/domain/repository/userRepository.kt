package com.example.accesorismvvm.domain.repository

import com.example.accesorismvvm.data.local.entityDAO.UserDao
import com.example.accesorismvvm.data.local.entityDAO.UserEntity
import com.example.accesorismvvm.data.remote.request.UpdateUsernameRequest
import com.example.accesorismvvm.domain.model.User
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

interface UserRepository  {
//    suspend fun register(user: UserEntity) = userDao.insert(User)
//    suspend fun getUserByEmail(email: String) = userDao.getUserByEmail(email)
//    suspend fun getUserById(UserId: Int) = userDao.getUserById(UserId)

    // Fungsi-fungsi yang diakses dari ViewModel
    suspend fun register(user: UserEntity) // Fungsi register (jika masih diperlukan di sini)
    suspend fun getUserByEmail(email: String): UserEntity?
    suspend fun getUserById(userId: Int): UserEntity? // Perbaiki nama parameter menjadi userId

    // Fungsi-fungsi untuk profil
    suspend fun fetchUserProfile(token: String): User?
    fun getUserProfileStream(): Flow<User?> // Untuk mendapatkan pembaruan profil secara real-time dari Room
    suspend fun updateUsername(token: String, newUsername: String): User?

    // Fungsi untuk membersihkan profil dari Room saat logout (ditambahkan sebelumnya)
    suspend fun clearUserProfile()
    suspend fun getUserRole(): String

}
