package com.example.accesorismvvm.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.accesorismvvm.data.local.entityDAO.BrandDao
import com.example.accesorismvvm.data.local.entityDAO.BrandEntity
import com.example.accesorismvvm.data.local.entityDAO.ProductDao
import com.example.accesorismvvm.data.local.entityDAO.ProductEntity
import com.example.accesorismvvm.data.local.entityDAO.UserEntity
import com.example.accesorismvvm.data.local.entityDAO.UserDao

@Database(entities = [UserEntity::class, ProductEntity::class, BrandEntity::class], version = 24, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun productDao(): ProductDao
    abstract fun brandDao(): BrandDao
}
