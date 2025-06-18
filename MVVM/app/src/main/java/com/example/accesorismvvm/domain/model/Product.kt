package com.example.accesorismvvm.domain.model


data class Product(
    val id: Int,
    val name: String,
    val description: String,
    val price: Double,
    val imageUrl: String,
    val brandName: String?,
    val stock: Int?
)
