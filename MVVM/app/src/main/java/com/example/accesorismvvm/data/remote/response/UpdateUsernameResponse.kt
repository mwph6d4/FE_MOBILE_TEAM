package com.example.accesorismvvm.data.remote.response

import com.google.gson.annotations.SerializedName

data class UpdateUsernameResponse(
    val message: String,
    @SerializedName("user") // Map JSON 'user' ke Kotlin 'userDto'
    val userDto: UserDto // Sesuaikan jika nama field di server berbeda
)
