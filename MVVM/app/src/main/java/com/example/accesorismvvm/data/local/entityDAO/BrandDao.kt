package com.example.accesorismvvm.data.local.entityDAO

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

// BrandDao.kt
@Dao
interface BrandDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBrand(brand: BrandEntity)

    @Query("SELECT * FROM brands LIMIT 1")
    suspend fun getMyBrand(): BrandEntity?

    @Query("DELETE FROM brands")
    suspend fun deleteAll()

    @Query("SELECT * FROM brands WHERE ownerId = :userId LIMIT 1")
    suspend fun getBrandByUserId(userId: Int): BrandEntity?

}