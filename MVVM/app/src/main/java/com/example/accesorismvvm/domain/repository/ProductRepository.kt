package com.example.accesorismvvm.domain.repository


import com.example.accesorismvvm.data.local.entityDAO.ProductEntity
import com.example.accesorismvvm.data.remote.request.CreateProductRequest
import com.example.accesorismvvm.data.remote.response.ProductDto
import com.example.accesorismvvm.domain.model.Product
import kotlinx.coroutines.flow.Flow
import java.io.File

interface ProductRepository {
    fun getAllProducts(): Flow<List<Product>>
    suspend fun fetchProductsFromServer()
//    suspend fun getProductDetail(productId: Int): Product?

    suspend fun getProductById(productId: Int, token: String): Product?
    suspend fun searchProducts(query: String, sortBy: String? = null, sortOrder: String? = null ): List<Product>

    suspend fun getMyBrandProducts(token: String): List<ProductDto>
    suspend fun getMyBrandProductsOffline(brandId: Int): List<Product>

    suspend fun createProduct(
        name: String,
        description: String?,
        price: Double,
        stock: Int?,
        categoryId: Int?, // Jika ingin kirim
        mainImageFile: File, // Parameter untuk file gambar
        token: String
    ): Result<String>
}
//    suspend fun searchProducts(query: String): List<Product>
// suspend fun getProductDetail(productId: Int): Product


