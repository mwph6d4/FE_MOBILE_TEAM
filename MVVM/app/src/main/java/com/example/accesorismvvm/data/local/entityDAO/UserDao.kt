package com.example.accesorismvvm.data.local.entityDAO

import android.util.Log
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

    @Dao
    interface UserDao {
        @Insert(onConflict = OnConflictStrategy.REPLACE)
        suspend fun insert(user: UserEntity)

        @Query("SELECT * FROM users WHERE email = :email LIMIT 1")
        suspend fun getUserByEmail(email: String): UserEntity?

        @Query("SELECT * FROM users WHERE id = :UserId LIMIT 1 ")
        suspend fun getUserById(UserId: Int): UserEntity?

        // Fungsi untuk mendapatkan satu user profile. ID 0 atau 1 sering digunakan untuk user tunggal.
        // Asumsi tabel 'users' di Room menyimpan profil pengguna yang sedang login.
        @Query("SELECT * FROM users LIMIT 1")
        fun getUserProfile(): Flow<UserEntity?> // Mengembalikan Flow agar bisa diamati perubahan profilnya

        @Query("SELECT role FROM users WHERE id = :userId LIMIT 1")
        suspend fun getUserRole(userId: Int): String

        // Fungsi untuk menghapus semua data user dari Room saat logout
        @Query("DELETE FROM users") // <-- Ganti 'user_profile' jika nama tabel Anda adalah 'users'
        suspend fun clearUserProfile(

        )

        @Query("SELECT * FROM users LIMIT 1")
        suspend fun getFirstUser(): UserEntity?

    }