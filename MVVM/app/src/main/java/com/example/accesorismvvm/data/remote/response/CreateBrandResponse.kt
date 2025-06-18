package com.example.accesorismvvm.data.remote.response

data class CreateBrandResponse(
    val message: String,
    val brand: BrandResponse,
    val user: UserResponse
)

data class BrandResponse(
    val id: Int,
    val name: String,
    val description: String?
)

data class UserResponse(
    val id: Int,
    val name: String,
    val role: String
)