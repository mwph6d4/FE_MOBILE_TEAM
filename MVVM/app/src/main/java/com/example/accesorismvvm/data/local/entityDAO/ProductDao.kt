package com.example.accesorismvvm.data.local.entityDAO

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

//@Dao
//interface ProductDao {
//    @Insert(onConflict = OnConflictStrategy.REPLACE)
//    suspend fun insertProducts(products: List<ProductEntity>)
//
//    @Query("SELECT * FROM products ORDER BY productName ASC")
//    fun getAllProducts(): Flow<List<ProductEntity>>
//
//    @Query("SELECT COUNT(*) FROM products")
//    suspend fun getProductCount(): Int
//    fun clearAll()
//}
@Dao
interface ProductDao {
    @Query("SELECT * FROM products")
    fun getAllProducts(): Flow<List<ProductEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(products: List<ProductEntity>)

    @Query("DELETE FROM products")
    suspend fun clearAll()

    @Query("SELECT * FROM products WHERE id = :productId")
    suspend fun getProductById(productId: Int): ProductEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(product: ProductEntity)

    @Query("SELECT * FROM products WHERE name LIKE '%' || :query || '%'")
    suspend fun searchProducts(query: String): List<ProductEntity>

    @Query("SELECT * FROM products WHERE brandId = :brandId")
    suspend fun getProductsByBrand(brandId: Int): List<ProductEntity>

    @Query("DELETE FROM products WHERE brandId = :brandId")
    suspend fun deleteProductsByBrand(brandId: Int)

}
