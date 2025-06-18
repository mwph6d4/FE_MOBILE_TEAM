package com.example.accesorismvvm.domain.repository
//
//import com.example.accesorismvvm.data.local.entityDAO.BrandDao
//import com.example.accesorismvvm.data.local.entityDAO.BrandEntity
import com.example.accesorismvvm.data.mapper.toEntity
import com.example.accesorismvvm.data.remote.request.CreateBrandRequest
import com.example.accesorismvvm.data.remote.response.BrandDto
import com.example.accesorismvvm.data.remote.response.ProductDto


interface BrandRepository {
    suspend fun openStore(request: CreateBrandRequest, token: String): Result<String>
    suspend fun getBrandByUser(token: String): BrandDto?
    suspend fun getProductsByMyBrand(token: String): List<ProductDto>
    suspend fun saveBrandToLocal(brand: BrandDto)
    suspend fun getLocalBrand(): BrandDto?

}
