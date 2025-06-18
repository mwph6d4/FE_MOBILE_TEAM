package com.example.accesorismvvm.data.local.entityDAO

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.example.accesorismvvm.data.remote.response.ProductDto


@Entity(tableName = "products")
data class ProductEntity(
    @PrimaryKey val id: Int,
    val name: String,
    val description: String?,
    val price: Double,
    val imageUrl: String,
    val brandName: String?,
    val brandId: Int, // ⬅️ TAMBAHKAN INI
    val stock: Int?
)

//@Entity(tableName = "products",
//    foreignKeys = [ForeignKey(
//        entity = BrandEntity::class,
//        parentColumns = ["id"],
//        childColumns = ["brandId"],
//        onDelete = ForeignKey.CASCADE
//    )]
//)
//data class ProductEntity(
//    @PrimaryKey val id: Int,
//    val name: String,
//    val description: String?,
//    val price: Double,
//    val imageUrl: String,
//    val brandId: Int, // foreign key ke tabel brand
//    val stock: Int?
//)


//@Entity(tableName = "products")
//data class ProductEntity(
//    @PrimaryKey(autoGenerate = true)
//    val id: Int = 0, // Unique ID for each product
//    val productLink: String,
//    val imageLink: String,
//    val storeName: String,
//    val location: String,
//    val productName: String,
//    val price: String, // Keep as String for "Rp 5.200", you can parse to Long/Double later if needed
//    val rating: Double,
//    val reviewCount: Int
//)