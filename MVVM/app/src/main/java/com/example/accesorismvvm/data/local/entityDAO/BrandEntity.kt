package com.example.accesorismvvm.data.local.entityDAO

import androidx.room.ColumnInfo
import androidx.room.Entity

import androidx.room.PrimaryKey
// BrandEntity.kt
//@Entity(tableName = "brands")
//data class BrandEntity(
//    @PrimaryKey val id: Int,
//    val name: String,
//    val description: String
//)
@Entity(tableName = "brands")
data class BrandEntity(
    @PrimaryKey val id: Int,
    val name: String,
    val description: String,
    val ownerId: Int // ✅ Ganti userId jadi ownerId
)

