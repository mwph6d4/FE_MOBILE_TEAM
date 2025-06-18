package com.example.accesorismvvm.data.remote.response

//data class ProductDto(
//    val id: Int,
//    val name: String,
//    val description: String?,
//    val price: Double,
//    val image_url: String,
//    val brand_name: String?,
//    val brand_id: Int,
//    val stock: Int?
//)

data class ProductDto(
    val id: Int,
    val name: String,
    val description: String?,
    val price: Double,
    val images: List<ImageDto>,           // ✅ Tambahkan ini
    val brand: BrandDto?,                 // ✅ Tambahkan ini
    val stock: Int?
)

data class ImageDto(
    val id: Int,
    val image_url: String,
    val is_main: Boolean
)

data class BrandDto(
    val id: Int,
    val name: String,
    val description: String?,
    val owner_id: Int

)


//data class ImageDto(
//    val id: Int,
//    val image_url: String,
//    val is_main: Boolean
//)
//
//data class BrandDto(
//    val id: Int,
//    val name: String,
//    val description: String?
//)
