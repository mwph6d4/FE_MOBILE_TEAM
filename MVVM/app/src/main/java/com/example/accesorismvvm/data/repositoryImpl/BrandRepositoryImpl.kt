package com.example.accesorismvvm.data.repositoryImpl
//
//import com.example.accesorismvvm.domain.repository.BrandRepository
import android.util.Log
import com.example.accesorismvvm.data.local.entityDAO.BrandDao
import com.example.accesorismvvm.data.local.entityDAO.UserDao
import com.example.accesorismvvm.data.mapper.toDomain
import com.example.accesorismvvm.data.mapper.toEntity
import com.example.accesorismvvm.data.remote.ApiService
import com.example.accesorismvvm.data.remote.request.CreateBrandRequest
import com.example.accesorismvvm.data.remote.response.BrandDto
import com.example.accesorismvvm.data.remote.response.BrandResponse
import com.example.accesorismvvm.data.remote.response.ProductDto
import com.example.accesorismvvm.domain.repository.BrandRepository
import kotlinx.coroutines.flow.Flow
import okhttp3.Response
import javax.inject.Inject
//import retrofit2.Response


//
//class BrandRepositoryImpl(
//    private val brandDao: BrandDao
//) : BrandRepository {
//
//    override suspend fun insertBrand(brand: BrandEntity) {
//        brandDao.insertBrand(brand)
//    }
//
//    override fun getBrandByOwnerId(ownerId: Int): Flow<BrandEntity?> {
//        return brandDao.getBrandByOwnerId(ownerId)
//    }
//}

class BrandRepositoryImpl @Inject constructor(
    private val api: ApiService,
    private val brandDao: BrandDao,
    private val userDao: UserDao
) : BrandRepository {
    override suspend fun openStore(request: CreateBrandRequest, token: String): Result<String> {
        return try {
            val response = api.openStore(request, "Bearer $token")
            if (response.isSuccessful) {
                Result.success(response.body()?.message ?: "Brand berhasil dibuat!")
            } else {
                val errorMsg = response.errorBody()?.string() ?: "Gagal membuat brand"
                Result.failure(Exception(errorMsg))
            }
        } catch (e: Exception) {
            val errorMessage = if (e is java.net.UnknownHostException || e is java.net.ConnectException) {
                "Tidak ada koneksi internet. Silakan periksa jaringan Anda."
            } else {
                e.message ?: "Terjadi kesalahan."
            }
            Result.failure(Exception(errorMessage))
        }
    }


    override suspend fun getBrandByUser(token: String): BrandDto? {
        return try {
            val response = api.getBrandByUser("Bearer $token")
            Log.d("API_CALL", "Code: ${response.code()}, Body: ${response.body()}, Error: ${response.errorBody()?.string()}")
            if (response.isSuccessful) {
                response.body()
            } else {
                null
            }
        } catch (e: Exception) {
            Log.e("API_CALL", "Exception: ${e.message}")
            null
        }
    }

    override suspend fun getProductsByMyBrand(token: String): List<ProductDto> {
        return api.getMyBrandProducts("Bearer $token") // ✅ TIDAK PERLU .body()
    }

    override suspend fun saveBrandToLocal(brand: BrandDto) {
        brandDao.insertBrand(brand.toEntity())
    }

//    override suspend fun getLocalBrand(): BrandDto? {
//        return brandDao.getMyBrand()?.toDomain()
//    }
//    override suspend fun getLocalBrand(): BrandDto? {
//        val currentUser = userDao.getFirstUser() // ✅ ini bukan Flow, langsung UserEntity?
//        return if (currentUser != null) {
//            brandDao.getBrandByUserId(currentUser.id)?.toDomain()
//        } else {
//            null
//        }
//    }
    override suspend fun getLocalBrand(): BrandDto? {
        val currentUser = userDao.getFirstUser() // Dapatkan user yang login dari Room
        return if (currentUser != null) {
            brandDao.getBrandByUserId(currentUser.id)?.toDomain()
        } else {
            null
        }
    }





}
