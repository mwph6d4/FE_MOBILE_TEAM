package com.example.accesorismvvm.ui.CreateProduct

import android.net.Uri // Import Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.accesorismvvm.data.Session.SessionManager
import com.example.accesorismvvm.domain.repository.ProductRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject

@HiltViewModel
class CreateProductViewModel @Inject constructor(
    private val productRepository: ProductRepository,
    private val sessionManager: SessionManager
) : ViewModel() {

    private val _productName = MutableStateFlow("")
    val productName: StateFlow<String> = _productName

    private val _productDescription = MutableStateFlow("")
    val productDescription: StateFlow<String> = _productDescription

    private val _productPrice = MutableStateFlow("")
    val productPrice: StateFlow<String> = _productPrice

    private val _productStock = MutableStateFlow("")
    val productStock: StateFlow<String> = _productStock

    // State untuk URI gambar yang dipilih oleh pengguna
    private val _selectedImageUri = MutableStateFlow<Uri?>(null)
    val selectedImageUri: StateFlow<Uri?> = _selectedImageUri

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _message = MutableStateFlow<String?>(null)
    val message: StateFlow<String?> = _message

    fun onProductNameChanged(newValue: String) { _productName.value = newValue }
    fun onProductDescriptionChanged(newValue: String) { _productDescription.value = newValue }
    fun onProductPriceChanged(newValue: String) { _productPrice.value = newValue }
    fun onProductStockChanged(newValue: String) { _productStock.value = newValue }
    fun onImageSelected(uri: Uri?) { _selectedImageUri.value = uri } // Fungsi untuk set URI gambar

    fun clearMessage() { _message.value = null }

    fun createProduct(token: String, imageFile: File?) { // Menerima File gambar
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val price = _productPrice.value.toDoubleOrNull()
                val stock = _productStock.value.toIntOrNull()

                if (price == null || stock == null) {
                    _message.value = "Harga dan Stok harus berupa angka yang valid."
                    _isLoading.value = false
                    return@launch
                }
                if (imageFile == null) {
                    _message.value = "Gambar produk wajib diupload."
                    _isLoading.value = false
                    return@launch
                }

                // Panggil repository dengan parameter yang sesuai
                val result = productRepository.createProduct(
                    name = _productName.value,
                    description = _productDescription.value,
                    price = price,
                    stock = stock,
                    categoryId = null, // Atau categoryId jika Anda mengimplementasikannya
                    mainImageFile = imageFile, // Kirim file gambar
                    token = token
                )

                _isLoading.value = false

                result.fold(
                    onSuccess = { successMessage ->
                        _message.value = successMessage
                        _productName.value = ""
                        _productDescription.value = ""
                        _productPrice.value = ""
                        _productStock.value = ""
                        _selectedImageUri.value = null // Bersihkan URI gambar
                    },
                    onFailure = { throwable ->
                        _message.value = throwable.message ?: "Gagal membuat produk."
                        Log.e("AddProductViewModel", "Error creating product: ${throwable.message}")
                    }
                )
            } catch (e: Exception) {
                _isLoading.value = false
                _message.value = "Terjadi kesalahan: ${e.message}"
                Log.e("AddProductViewModel", "Exception in createProduct: ${e.message}")
            }
        }
    }
}