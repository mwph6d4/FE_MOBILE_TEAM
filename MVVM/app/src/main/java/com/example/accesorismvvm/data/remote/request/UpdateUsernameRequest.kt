package com.example.accesorismvvm.data.remote.request

import com.google.gson.annotations.SerializedName

data class UpdateUsernameRequest(
    @SerializedName("name") // <<< PENTING: Map Kotlin 'username' ke JSON 'name'
    val username: String
)