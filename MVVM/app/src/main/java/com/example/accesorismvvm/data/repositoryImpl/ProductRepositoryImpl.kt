package com.example.accesorismvvm.data.repositoryImpl

import android.content.Context
import android.util.Log
import com.example.accesorismvvm.data.local.database.AppDatabase
import com.example.accesorismvvm.data.local.entityDAO.ProductDao
import com.example.accesorismvvm.data.local.entityDAO.ProductEntity
import com.example.accesorismvvm.data.remote.ApiService
import com.example.accesorismvvm.domain.model.Product
import com.example.accesorismvvm.domain.repository.ProductRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import com.example.accesorismvvm.data.mapper.toDomain
import com.example.accesorismvvm.data.mapper.toEntity
import com.example.accesorismvvm.data.remote.request.CreateProductRequest
import com.example.accesorismvvm.data.remote.response.ProductDto
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File


class ProductRepositoryImpl @Inject constructor(
    private val api: ApiService,
    private val dao: ProductDao
) : ProductRepository {

    override fun getAllProducts(): Flow<List<Product>> {
        return dao.getAllProducts().map { it.map { entity -> entity.toDomain() } }
    }

    override suspend fun fetchProductsFromServer() {
        try {
            val response = api.getProducts()
            if (response.isSuccessful) {
                response.body()?.let { productResponse ->
                    val entities = productResponse.products.map { it.toEntity() }
                    // dao.clearAll() // bisa aktifkan kalau memang ingin reset data
                    dao.insertAll(entities)
                }
            } else {
                throw Exception("Server Error: ${response.code()}")
            }
        } catch (e: Exception) {
            Log.e("ProductRepo", "Gagal fetch produk dari server: ${e.message}")
            // Jangan dilempar lagi supaya tidak crash
        }
    }


    override suspend fun getProductById(productId: Int, token: String): Product? {
        return try {
            val response = api.getProductById(productId, "Bearer $token")
            if (response.isSuccessful) {
                val product = response.body()?.toDomain()
                // Optional: simpan ke Room jika mau update lokal
                response.body()?.let { dao.insert(it.toEntity()) }
                product
            } else {
                // fallback ke lokal kalau response gagal
                dao.getProductById(productId)?.toDomain()
            }
        } catch (e: Exception) {
            // fallback ke lokal kalau offline atau timeout
            dao.getProductById(productId)?.toDomain()
        }
    }


    override suspend fun searchProducts(
        query: String,
        sortBy: String?,
        sortOrder: String?
    ): List<Product> {
        return try {
            val response = api.getProducts(query, sortBy, sortOrder)
            if (response.isSuccessful) {
                response.body()?.let { productResponse ->
                    val entities = productResponse.products.map { it.toEntity() }
                    dao.insertAll(entities) // Optional: update lokal juga
                    return entities.map { it.toDomain() }
                } ?: emptyList()
            } else {
                dao.searchProducts(query).map { it.toDomain() } // fallback ke lokal
            }
        } catch (e: Exception) {
            dao.searchProducts(query).map { it.toDomain() } // offline fallback
        }
    }

    override suspend fun getMyBrandProducts(token: String): List<ProductDto> {
        val products = api.getMyBrandProducts("Bearer $token")

        // Simpan ke Room
        val brandId = products.firstOrNull()?.brand?.id
//        val brandId = products.firstOrNull()?.brand
        if (brandId != null) {
            dao.deleteProductsByBrand(brandId)
            dao.insertAll(products.map { it.toEntity() })
        }

        return products
    }


    override suspend fun getMyBrandProductsOffline(brandId: Int): List<Product> {
        return dao.getProductsByBrand(brandId).map { it.toDomain() }
    }


    override suspend fun createProduct(
        name: String,
        description: String?,
        price: Double,
        stock: Int?,
        categoryId: Int?, // Jika Anda ingin mengirim category_id
        mainImageFile: File, // Parameter baru untuk file gambar
        token: String
    ): Result<String> {
        return try {
            // Buat RequestBody untuk setiap field teks
            val nameBody = name.toRequestBody("text/plain".toMediaTypeOrNull())
            val descriptionBody = description?.toRequestBody("text/plain".toMediaTypeOrNull())
            val priceBody = price.toString().toRequestBody("text/plain".toMediaTypeOrNull())
            val stockBody =
                (stock?.toString() ?: "0").toRequestBody("text/plain".toMediaTypeOrNull())
            val categoryIdBody =
                categoryId?.toString()?.toRequestBody("text/plain".toMediaTypeOrNull())

            // Buat MultipartBody.Part untuk file gambar
            val requestImageFile = mainImageFile.asRequestBody("image/*".toMediaTypeOrNull())
            val mainImagePart = MultipartBody.Part.createFormData(
                "main_image",
                mainImageFile.name,
                requestImageFile
            ) // "main_image" harus sama dengan nama field di Flask (request.files.get('main_image'))

            val response = api.createProduct(
                name = nameBody,
                description = descriptionBody,
                price = priceBody,
                stock = stockBody,
                categoryId = categoryIdBody,
                mainImage = mainImagePart,
                token = "Bearer $token"
            )

            if (response.isSuccessful) {
                val productDto = response.body()?.product
                if (productDto != null) {
                    val lokalproduk = ProductEntity(
                        id = productDto.id,
                        name = productDto.name,
                        description = productDto.description,
                        price = productDto.price,
                        imageUrl = productDto.images.firstOrNull { it.is_main }?.image_url ?: "",
                        brandName = productDto.brand?.name,
                        brandId = productDto.brand?.id ?: 0,
                        stock = productDto.stock
                    )
                    dao.insert(lokalproduk)
                }
                    Result.success(response.body()?.message ?: "Produk berhasil dibuat!")
                } else {
                    val errorMsg = response.errorBody()?.string() ?: "Gagal membuat produk."
                    Result.failure(Exception(errorMsg))
                }
            } catch (e: Exception) {
                val errorMessage =
                    if (e is java.net.UnknownHostException || e is java.net.ConnectException) {
                        "Tidak ada koneksi internet. Silakan periksa jaringan Anda."
                    } else {
                        e.message ?: "Terjadi kesalahan."
                    }
                Result.failure(Exception(errorMessage))
            }
        }
    }




//    override suspend fun getProductDetail(productId: Int): Product? {
//        return try {
//            // Coba ambil dari database lokal (Room) terlebih dahulu
//            val productEntity = dao.getProductById(productId)
//            if (productEntity != null) {
//                return productEntity.toDomain()
//            }
//
//            // Jika tidak ada di Room, ambil dari server
//            val apiResponse = api.getProductDetail(productId)
//            // Lakukan pemetaan dari ProductEntity (yang didapat dari API) ke Product domain model
//            val productDetail = apiResponse.toDomain() // Asumsi ada fungsi ekstensi toDomain()
//
//            // Opsional: Simpan ke Room setelah diambil dari server
//            dao.insert(apiResponse)
//
//            productDetail
//        } catch (e: Exception) {
//            // Handle error, misalnya log exception atau tampilkan pesan ke user
//            e.printStackTrace()
//            null
//        }
//    }







