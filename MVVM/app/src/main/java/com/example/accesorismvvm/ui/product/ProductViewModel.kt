package com.example.accesorismvvm.ui.product

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.accesorismvvm.domain.model.Product
import com.example.accesorismvvm.domain.repository.ProductRepository
import com.example.accesorismvvm.domain.usecase.ProductUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductViewModel @Inject constructor(
    private val repository: ProductRepository
) : ViewModel() {

    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> = _isLoading


    val products: StateFlow<List<Product>> = flow {
        repository.getAllProducts().collect { localProducts ->
            emit(localProducts)
        }
    }
        .onStart { _isLoading.value = true } // Mulai loading
        .onEach { _isLoading.value = false } // Selesai loading setelah data Room tersedia
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            emptyList()
        )

//    val produk = repository.getAllProducts().stateIn(
//        viewModelScope,
//        SharingStarted.WhileSubscribed(5000),
//        emptyList()
//    )

    init {
        viewModelScope.launch {
            try {
                repository.fetchProductsFromServer()
            } catch (e: Exception) {
                Log.e("ProductViewModel", "Gagal ambil data dari server: ${e.message}")
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun refreshProducts() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                repository.fetchProductsFromServer()
            } catch (e: Exception) {
                Log.e("ProductViewModel", "Gagal refresh data: ${e.message}")
            } finally {
                _isLoading.value = false
            }
        }
    }

}
