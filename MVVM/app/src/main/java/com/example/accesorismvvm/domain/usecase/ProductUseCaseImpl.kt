//package com.example.accesorismvvm.domain.usecase
//
//import com.example.accesorismvvm.domain.model.Product
//import com.example.accesorismvvm.domain.repository.ProductRepository
//import kotlinx.coroutines.flow.Flow
//import javax.inject.Inject
//
//class ProductUseCaseImpl @Inject constructor(
//    private val repository: ProductRepository
//) : ProductUseCase {
//
//    override suspend fun invoke(): Flow<List<Product>> {
//        return repository.getAllProducts()
//    }
//
//    override suspend fun syncFromServer() {
//        repository.fetchProductsFromServer()
//    }
//}
