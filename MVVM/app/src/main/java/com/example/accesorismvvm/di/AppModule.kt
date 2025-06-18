package com.example.accesorismvvm.di

import android.app.Application
import android.content.Context
import androidx.room.Room
import com.example.accesorismvvm.data.Session.SessionManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import com.example.accesorismvvm.data.local.database.AppDatabase
import com.example.accesorismvvm.data.local.entityDAO.BrandDao
import com.example.accesorismvvm.data.local.entityDAO.ProductDao
import com.example.accesorismvvm.data.local.entityDAO.UserDao
import com.example.accesorismvvm.data.remote.ApiClient
import com.example.accesorismvvm.data.remote.ApiService
import com.example.accesorismvvm.data.remote.RemoteDataSource
import com.example.accesorismvvm.data.repositoryImpl.AuthRepositoryImpl
import com.example.accesorismvvm.data.repositoryImpl.BrandRepositoryImpl
import com.example.accesorismvvm.data.repositoryImpl.ProductRepositoryImpl
import com.example.accesorismvvm.data.repositoryImpl.UserRepositoryImpl
import com.example.accesorismvvm.domain.repository.AuthRepository
import com.example.accesorismvvm.domain.repository.BrandRepository
import com.example.accesorismvvm.domain.repository.ProductRepository
import com.example.accesorismvvm.domain.repository.UserRepository
import dagger.Binds
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
//    app: Application
    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "app_db")
            .fallbackToDestructiveMigration() // <- ini WAJIB ada jika tidak pakai Migration manual
            .build()

           
    }

    @Provides
    @Singleton
    fun provideUserDao(db: AppDatabase): UserDao = db.userDao()

//    @Provides
//    @Singleton
//    fun provideProductRepository(
//        appDatabase: AppDatabase,
//        @ApplicationContext context: Context
//    ): ProductRepository {
//        return ProductRepositoryImpl(appDatabase, context)
//    }
    @Provides
    @Singleton
    fun provideApiService(): ApiService = ApiClient.create()

    @Provides
    @Singleton
    fun provideRemoteDataSource(apiService: ApiService): RemoteDataSource {
        return RemoteDataSource(apiService)
    }

    @Provides
    @Singleton
    fun provideAuthRepository(
        remoteDataSource: RemoteDataSource,
        userDao: UserDao
    ): AuthRepository {
        return AuthRepositoryImpl(remoteDataSource, userDao)
    }

    @Provides
    @Singleton
    fun provideSessionManager(@ApplicationContext context: Context): SessionManager {
        return SessionManager(context)
    }

    @Provides
    @Singleton
    fun provideProductDao(db: AppDatabase): ProductDao = db.productDao()

//    @Provides
//    @Singleton
//    fun provideProductRepository(
//        apiService: ApiService,
//        productDao: ProductDao
//    ): ProductRepository {
//        return ProductRepositoryImpl(apiService, productDao)
//    }
    @Provides
    fun provideProductRepository(
        api: ApiService,
        dao: ProductDao
    ): ProductRepository = ProductRepositoryImpl(api, dao)


    @Provides
    @Singleton
    fun provideUserRepository(
        apiService: ApiService,
        userDao: UserDao,
        sessionManager: SessionManager // <- Tambahkan ini
    ): UserRepository {
        return UserRepositoryImpl(apiService, userDao, sessionManager) // <- Tambahkan ini juga
    }

    @Provides
    @Singleton
    fun provideBrandRepository(
        apiService: ApiService,
        brandDao: BrandDao,
        userDao: UserDao
    ): BrandRepository {
        return BrandRepositoryImpl(apiService, brandDao, userDao)
    }

    @Provides
    @Singleton
    fun provideBrandDao(appDatabase: AppDatabase): BrandDao {
        return appDatabase.brandDao()
    }
//    @Provides
//    fun provideBrandRepository(brandDao: BrandDao): BrandRepository {
//        return BrandRepositoryImpl(brandDao)
//    }

}

