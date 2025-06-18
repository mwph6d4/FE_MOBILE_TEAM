package com.example.accesorismvvm.data.remote.response

data class ProductResponse(
    val products: List<ProductDto>,
    val total_items: Int,
    val total_pages: Int,
    val current_page: Int,
    val per_page: Int,
    val has_next: Boolean,
    val has_prev: Boolean
)
