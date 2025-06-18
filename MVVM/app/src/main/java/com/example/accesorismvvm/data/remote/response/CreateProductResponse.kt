package com.example.accesorismvvm.data.remote.response

import com.google.gson.annotations.SerializedName

data class CreateProductResponse(
    val message: String,
    @SerializedName("product_id") // Jika server mengembalikan ID produk
    val productId: Int? = null,
    val product: ProductDto
)
