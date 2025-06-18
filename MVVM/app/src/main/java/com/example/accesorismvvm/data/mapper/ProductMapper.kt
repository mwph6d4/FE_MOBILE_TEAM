package com.example.accesorismvvm.data.mapper

import com.example.accesorismvvm.data.local.entityDAO.BrandEntity
import com.example.accesorismvvm.data.local.entityDAO.ProductEntity
import com.example.accesorismvvm.data.remote.response.BrandDto
import com.example.accesorismvvm.data.remote.response.ProductDto
import com.example.accesorismvvm.domain.model.Product

fun ProductDto.toEntity(): ProductEntity {
    return ProductEntity(
        id = id,
        name = name,
        description = description ?: "",
        price = price,
        imageUrl = images.firstOrNull { it.is_main }?.image_url ?: "",
        brandName = brand?.name,
        brandId = brand?.id ?: 0,
        stock = stock
    )
}


fun ProductEntity.toDomain(): Product {
    return Product(
        id = this.id,
        name = this.name,
        description = description ?: "",
        price = this.price,
        imageUrl = this.imageUrl,
        brandName = this.brandName,
        stock = stock
    )
}

// ProductMapper.kt
fun ProductDto.toDomain(): Product {
    return Product(
        id = id,
        name = name,
        description = description ?: "",
        price = price,
        imageUrl = images.firstOrNull { it.is_main }?.image_url ?: "",
        brandName = brand?.name,
        stock = stock
    )
}

//fun BrandDto.toEntity(): BrandEntity {
//    return BrandEntity(
//        id = id,
//        name = name,
//        description = description ?: ""
//    )
//}
fun BrandDto.toEntity(): BrandEntity {
    return BrandEntity(
        id = id,
        name = name,
        description = description ?: "",
        ownerId = owner_id
    )
}

fun BrandEntity.toDomain(): BrandDto {
    return BrandDto(
        id = id,
        name = name,
        description = description,
        owner_id = ownerId
    )
}







//fun ProductDetailDto.toDomain(): ProductDetail = ProductDetail(
//    id, name, description, price, imageUrl, brand?.name ?: "Unknown", stock ?: 0
//)
//
//data class ProductDetail(
//    val id: Int,
//    val name: String,
//    val description: String?,
//    val price: Double,
//    val imageUrl: String,
//    val brandName: String,
//    val stock: Int
//)
