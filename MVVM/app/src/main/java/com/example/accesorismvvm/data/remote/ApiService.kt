package com.example.accesorismvvm.data.remote

//import androidx.room.Query
import com.example.accesorismvvm.data.local.entityDAO.ProductEntity
import com.example.accesorismvvm.data.remote.request.CreateBrandRequest
import com.example.accesorismvvm.data.remote.request.CreateProductRequest
import com.example.accesorismvvm.data.remote.request.LoginRequest
import com.example.accesorismvvm.data.remote.request.RegisterRequest
import com.example.accesorismvvm.data.remote.response.AuthResponse
import com.example.accesorismvvm.data.remote.response.ProductResponse
import com.example.accesorismvvm.data.remote.request.UpdateUsernameRequest
import com.example.accesorismvvm.data.remote.response.BrandDto
import com.example.accesorismvvm.data.remote.response.CreateBrandResponse
import com.example.accesorismvvm.data.remote.response.CreateProductResponse
import com.example.accesorismvvm.data.remote.response.ProductDto
import com.example.accesorismvvm.data.remote.response.UpdateUsernameResponse
import com.example.accesorismvvm.data.remote.response.UserDto
import okhttp3.MultipartBody
import okhttp3.RequestBody

import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.Response
import retrofit2.http.Multipart
import retrofit2.http.PUT
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {

    @POST("auth/login")
    suspend fun login(@Body request: LoginRequest): AuthResponse

    @POST("auth/register")
    suspend fun register(@Body request: RegisterRequest): AuthResponse

    @GET("users/profile")
    suspend fun getUserProfile(
        @Header("Authorization") token: String
    ): UserDto

    @GET("products/")
    suspend fun getProducts(
        @Query("search") query: String? = null,
        @Query("sort_by") sortBy: String? = null,
        @Query("sort_order") sortOrder: String? = null
    ): Response<ProductResponse>

    @PUT("users/profile/username") // URL Path sesuai dengan yang di server
    suspend fun updateUsername(
        @Header("Authorization") token: String,
        @Body request: UpdateUsernameRequest // Menggunakan data class untuk request body
    ): UpdateUsernameResponse

    @GET("products/{id}")
    suspend fun getProductById(
        @Path("id") productId: Int,
        @Header("Authorization") token: String
    ): Response<ProductDto>

    @POST("auth/open_store")
    suspend fun openStore(
        @Body request: CreateBrandRequest,
        @Header("Authorization") token: String
    ): Response<CreateBrandResponse>

    @GET("auth/brands/my")
    suspend fun getBrandByUser(@Header("Authorization") token: String): Response<BrandDto>

    @GET("products/my-brand")
    suspend fun getMyBrandProducts(
        @Header("Authorization") token: String
    ): List<ProductDto>

    @Multipart // Ini menandakan bahwa permintaan adalah multipart/form-data
    @POST("products/create") // Pastikan ini cocok dengan route Flask
    suspend fun createProduct(
        @Part("name") name: RequestBody,
        @Part("description") description: RequestBody?,
        @Part("price") price: RequestBody,
        @Part("stock") stock: RequestBody,
        @Part("category_id") categoryId: RequestBody?,
        @Part mainImage: MultipartBody.Part,
        @Header("Authorization") token: String
    ): Response<CreateProductResponse>
}




//    @GET("products/{id}")
//    suspend fun getProductDetail(
//        @Path("id") id: Int,
//        @Header("Authorization") token: String
//    ): ProductDetailDto




//    @GET("products/")
//    suspend fun getAllProducts(
//        @retrofit2.http.Query("page") page: Int = 1,
//        @retrofit2.http.Query("per_page") perPage: Int = 20,
//        @retrofit2.http.Query("sort_by") sortBy: String = "created_at",
//        @retrofit2.http.Query("sort_order") sortOrder: String = "desc"
//    ): ProductResponse
