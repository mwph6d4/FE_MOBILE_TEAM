package com.example.accesorismvvm.data.remote.request


import com.google.gson.annotations.SerializedName
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody

// Ini akan menjadi "model" untuk data teks yang dikirim sebagai bagian dari form-data
// Penting: Tidak ada @SerializedName karena ini akan dikirim sebagai part terpisah,
// bukan sebagai JSON body tunggal. Nama properti harus cocok dengan request.form.get() di Flask.
data class CreateProductRequest(
    val name: String,
    val description: String?,
    val price: Double,
    val stock: Int?, // Nama ini harus cocok dengan 'stock' di Flask
    val categoryId: Int? = null // Jika Anda ingin mengirim category_id
) {
    // Fungsi helper untuk mengonversi properti ke RequestBody (Plain Text)
    fun toRequestBodyMap(): Map<String, RequestBody> {
        return mapOf(
            "name" to name.toRequestBody("text/plain".toMediaTypeOrNull()),
            "description" to (description ?: "").toRequestBody("text/plain".toMediaTypeOrNull()),
            "price" to price.toString().toRequestBody("text/plain".toMediaTypeOrNull()),
            "stock" to (stock?.toString() ?: "0").toRequestBody("text/plain".toMediaTypeOrNull())
        ).toMutableMap().apply {
            categoryId?.let {
                put("category_id", it.toString().toRequestBody("text/plain".toMediaTypeOrNull()))
            }
        }
    }
}