package com.example.accesorismvvm.data.remote.response

import com.example.accesorismvvm.data.local.entityDAO.UserEntity
import com.example.accesorismvvm.domain.model.User

data class UserDto(
    val id: Int,
    val name: String,
    val email: String,
    val password: String?,
    val user_role: String? = null // ⬅️ WAJIB ADA INI!
)


//fun UserDto.toEntity(): UserEntity {
//    return UserEntity(
//        id = this.id,
//        username = this.name,
//        email = this.email,
//        password = this.password ?: "",
//        role = this.user_role ?: "pembeli" // ⬅️ fallback kalau null
//    )
//}

fun UserEntity.toDomain(): User {
    return User(
        id = this.id,
        username = this.username,
        email = this.email,
        password = this.password ?: " "
        // Tambahkan role jika diperlukan
    )
}

fun UserEntity.toUserDto(): UserDto {
    return UserDto(
        id = this.id,
        name = this.username,
        email = this.email,
        password = this.password,
        user_role = this.role // ⬅️ AMBIL DARI ENTITY YANG UDAH DISIMPAN
    )
}
