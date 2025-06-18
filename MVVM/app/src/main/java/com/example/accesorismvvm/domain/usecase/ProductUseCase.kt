package com.example.accesorismvvm.domain.usecase

import com.example.accesorismvvm.domain.model.Product
import com.example.accesorismvvm.domain.repository.ProductRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject


class ProductUseCase @Inject constructor(
    private val repository: ProductRepository
) {
    operator fun invoke(): Flow<List<Product>> = repository.getAllProducts()

    suspend fun syncFromServer() = repository.fetchProductsFromServer()
}

