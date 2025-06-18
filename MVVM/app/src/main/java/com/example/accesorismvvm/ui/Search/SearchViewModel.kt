package com.example.accesorismvvm.ui.Search
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.accesorismvvm.domain.model.Product
import com.example.accesorismvvm.domain.repository.ProductRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

//
//
//// SearchViewModel.kt
//
//
//import androidx.lifecycle.ViewModel
//import androidx.lifecycle.viewModelScope
//import com.example.accesorismvvm.domain.model.Product // Asumsi ini domain model Product Anda
//import com.example.accesorismvvm.domain.repository.ProductRepository // Sesuaikan dengan package repository Anda
//import dagger.hilt.android.lifecycle.HiltViewModel
//import kotlinx.coroutines.FlowPreview
//import kotlinx.coroutines.Job
//import kotlinx.coroutines.flow.MutableStateFlow
//import kotlinx.coroutines.flow.StateFlow
//import kotlinx.coroutines.flow.asStateFlow
//import kotlinx.coroutines.flow.debounce // Untuk debounce input pencarian
//import kotlinx.coroutines.flow.distinctUntilChanged // Untuk menghindari pencarian berulang
//import kotlinx.coroutines.flow.filter // Untuk filter query kosong
//import kotlinx.coroutines.flow.flatMapLatest // Untuk membatalkan pencarian sebelumnya jika ada input baru
//import kotlinx.coroutines.launch
//import javax.inject.Inject
//
//@HiltViewModel
//class SearchViewModel @Inject constructor(
//    private val repository: ProductRepository
//) : ViewModel() {
//
//    private val _searchQuery = MutableStateFlow("")
//    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()
//
//    private val _searchResults = MutableStateFlow<List<Product>>(emptyList())
//    val searchResults: StateFlow<List<Product>> = _searchResults.asStateFlow()
//
//    private val _isLoading = MutableStateFlow(false)
//    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()
//
//    private val _hasSearched = MutableStateFlow(false) // Untuk melacak apakah pencarian sudah dilakukan
//    val hasSearched: StateFlow<Boolean> = _hasSearched.asStateFlow()
//
//    private var searchJob: Job? = null
//
//    // Menggunakan Flow untuk pencarian yang efisien
//    init {
//        @OptIn(FlowPreview::class)
//        viewModelScope.launch {
//            _searchQuery
//                .debounce(300L) // Tunggu 300ms setelah user berhenti mengetik
//                .filter { query ->
//                    _hasSearched.value = true // Set hasSearched menjadi true saat query mulai diproses
//                    query.isNotBlank() // Hanya lakukan pencarian jika query tidak kosong
//                }
//                .distinctUntilChanged() // Hanya lakukan pencarian jika query berubah
//                .flatMapLatest { query ->
//                    _isLoading.value = true
//                    try {
//                        // Memanggil fungsi pencarian dari repository
//                        // Anda perlu menambahkan fungsi searchProducts(query: String) ke ProductRepository Anda
//                        repository.searchProducts(query)
//                    } catch (e: Exception) {
//                        // Handle error, log atau tampilkan pesan
//                        e.printStackTrace()
//                        emptyList()
//                    } finally {
//                        _isLoading.value = false
//                    }
//                }
//                .collect { results ->
//                    _searchResults.value = results
//                }
//        }
//    }
//
//    fun onSearchQueryChanged(query: String) {
//        _searchQuery.value = query
//    }
//
//    // Fungsi ini akan Anda tambahkan ke ProductRepository
//    // Contoh implementasi di ProductRepository:
//    // override suspend fun searchProducts(query: String): List<Product> {
//    //     // Lakukan pencarian di Room atau API
//    //     // Contoh dari Room: return dao.searchProducts(query).map { it.toDomain() }
//    //     // Contoh dari API: val response = api.searchProducts(query); return response.body()?.data?.map { it.toDomain() } ?: emptyList()
//    // }
//}
@HiltViewModel
class SearchViewModel @Inject constructor(
    private val repository: ProductRepository
) : ViewModel() {

    var query by mutableStateOf("")
        private set

    var searchResults by mutableStateOf<List<Product>>(emptyList())
        private set

    var isLoading by mutableStateOf(false)
        private set

    var isError by mutableStateOf(false)
        private set

    fun onQueryChange(newQuery: String) {
        query = newQuery
    }

    var sortOrder by mutableStateOf<String>("asc")
        private set

    fun onSortOrderChange(order: String) {
        sortOrder = order
        performSearch() // Otomatis cari lagi setelah pilih urutan
    }

    fun performSearch() {
        viewModelScope.launch {
            isLoading = true
            isError = false
            try {
                val results = repository.searchProducts(query, "price", sortOrder)
                searchResults = results
            } catch (e: Exception) {
                isError = true
                searchResults = emptyList()
            } finally {
                isLoading = false
            }
        }
    }
}
