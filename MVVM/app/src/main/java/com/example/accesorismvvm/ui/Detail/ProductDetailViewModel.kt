package com.example.accesorismvvm.ui.Detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.accesorismvvm.data.Session.SessionManager
import com.example.accesorismvvm.domain.model.Product
import com.example.accesorismvvm.domain.repository.ProductRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

//
//@HiltViewModel
//class ProductDetailViewModel @Inject constructor(
//    private val repository: ProductRepository,
//    savedStateHandle: SavedStateHandle // Digunakan untuk mengambil argumen dari navigasi
//) : ViewModel() {
//
//    private val _productDetail = MutableStateFlow<Product?>(null)
//    val productDetail: StateFlow<Product?> = _productDetail
//
//    private val _isLoading = MutableStateFlow(true)
//    val isLoading: StateFlow<Boolean> = _isLoading
//
//    private val _errorMessage = MutableStateFlow<String?>(null)
//    val errorMessage: StateFlow<String?> = _errorMessage
//
//    init {
//        val productIdString = savedStateHandle.get<String>("productId") // <--- Perbaikan di sini!
//        val productId = productIdString?.toIntOrNull() // Konversi ke Int atau null jika gagal
//
////        val productId = savedStateHandle.get<Int>("productId")
//        if (productId != null) {
////            fetchProductDetail(productId)
//            viewModelScope.launch(Dispatchers.IO) { // <<< PASTIKAN MENGGUNAKAN Dispatchers.IO untuk operasi I/O
//                _isLoading.value = true
//                _errorMessage.value = null
//                try {
//                    val product = repository.getProductDetail(productId)
//                    _productDetail.value = product
//                    if (product == null) {
//                        _errorMessage.value = "Product not found."
//                    }
//                } catch (e: Exception) {
//                    _errorMessage.value = "Failed to load product detail: ${e.localizedMessage}"
//                } finally {
//                    _isLoading.value = false
//                }
//            }
//        } else {
//            _errorMessage.value = "Product ID not found."
//            _isLoading.value = false
//        }
//    }
//
//    private fun fetchProductDetail(productId: Int) {
//        viewModelScope.launch {
//            _isLoading.value = true
//            _errorMessage.value = null
//            try {
//                val product = repository.getProductDetail(productId)
//                _productDetail.value = product
//                if (product == null) {
//                    _errorMessage.value = "Product not found."
//                }
//            } catch (e: Exception) {
//                _errorMessage.value = "Failed to load product detail: ${e.localizedMessage}"
//            } finally {
//                _isLoading.value = false
//            }
//        }
//    }
//}

@HiltViewModel
class ProductDetailViewModel @Inject constructor(
    private val repository: ProductRepository,
    private val sessionManager: SessionManager
) : ViewModel() {

    private val _product = MutableStateFlow<Product?>(null)
    val product: StateFlow<Product?> = _product

    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> = _isLoading

    fun loadProduct(productId: Int) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val token = sessionManager.getToken()
                if (!token.isNullOrEmpty()) {
                    val result = repository.getProductById(productId, token)
                    _product.value = result
                } else {
                    // Opsional: handle kalau token null
                    _product.value = null
                }
            } catch (e: Exception) {
                e.printStackTrace()
                _product.value = null // fallback aman, biar UI tetap jalan
            } finally {
                _isLoading.value = false
            }
        }
    }

}
