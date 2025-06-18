package com.example.accesorismvvm.ui.CreateBrand

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.accesorismvvm.data.Session.SessionManager
import com.example.accesorismvvm.domain.repository.BrandRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import com.example.accesorismvvm.data.remote.request.CreateBrandRequest
import com.example.accesorismvvm.data.remote.response.ProductDto
import com.example.accesorismvvm.domain.model.Product
import com.example.accesorismvvm.domain.repository.ProductRepository

@HiltViewModel
class CreateBrandViewModel @Inject constructor(
    private val brandRepository: BrandRepository,
    private val productRepository: ProductRepository,
    private val sessionManager: SessionManager // Pastikan ini diinjeksi dengan benar
) : ViewModel() {

    private val _brandName = MutableStateFlow("")
    val brandName: StateFlow<String> = _brandName

    private val _brandDescription = MutableStateFlow("")
    val brandDescription: StateFlow<String> = _brandDescription

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _message = MutableStateFlow<String?>(null)
    val message: StateFlow<String?> = _message

    private val _hasStore = MutableStateFlow<Boolean?>(null)
    val hasStore: StateFlow<Boolean?> = _hasStore

    private val _existingBrandName = MutableStateFlow("")
    val existingBrandName: StateFlow<String> = _existingBrandName

    // >>> Ini adalah SATU-SATUNYA StateFlow untuk produk brand yang akan diekspos ke UI
    private val _myBrandProducts = MutableStateFlow<List<Product>>(emptyList())
    val myBrandProducts: StateFlow<List<Product>> = _myBrandProducts

    // >>> HAPUS ATAU KOMENTARI INI KARENA SUDAH TIDAK DIGUNAKAN:
    // private val _brandProducts = MutableStateFlow<List<ProductDto>>(emptyList())
    // val brandProducts: StateFlow<List<ProductDto>> = _brandProducts
    // private val _offlineBrandProducts = MutableStateFlow<List<Product>>(emptyList())
    // val offlineBrandProducts: StateFlow<List<Product>> = _offlineBrandProducts


    fun onBrandNameChanged(newValue: String) {
        _brandName.value = newValue
    }

    fun onBrandDescriptionChanged(newValue: String) {
        _brandDescription.value = newValue
    }

    fun clearMessage() {
        _message.value = null
    }

    fun createBrand(token: String) {
        viewModelScope.launch {
            _isLoading.value = true
            val request = CreateBrandRequest(
                name = _brandName.value,
                description = _brandDescription.value
            )
            val result = brandRepository.openStore(request, token)
            _isLoading.value = false

            _message.value = result.fold(
                onSuccess = {
                    _hasStore.value = true
                    _existingBrandName.value = _brandName.value
                    // Setelah brand dibuat, coba ambil brand dan produknya (online-first)
                    checkBrand(token)
                    it
                },
                onFailure = {
                    it.message ?: "Terjadi kesalahan"
                }
            )
        }
    }

    fun checkBrand(token: String) {
        viewModelScope.launch {
            try {
                // 1. Coba ambil brand dari server
                val brand = brandRepository.getBrandByUser(token)
                Log.d("CHECK_BRAND", "Brand dari server: $brand")

                if (brand != null) {
                    _existingBrandName.value = brand.name
                    _hasStore.value = true

                    // Simpan brand ke Room
                    brandRepository.saveBrandToLocal(brand)

                    // 2. Jika brand ditemukan online, coba ambil produknya dari server dan simpan ke Room
                    loadProductsByBrandFromNetworkAndStore(token, brand.id)
                    // 3. Kemudian, load produk dari Room untuk tampilan awal
                    loadMyBrandProductsFromLocal(brand.id)

                } else {
                    // Jika tidak ada brand di server, coba cek di lokal
                    Log.d("CHECK_BRAND", "Tidak ada brand dari server, coba dari lokal.")
                    val localBrand = brandRepository.getLocalBrand()
                    if (localBrand != null) {
                        _existingBrandName.value = localBrand.name
                        _hasStore.value = true
                        Log.d("CHECK_BRAND", "Brand dari lokal: $localBrand")
                        // Load produk dari Room untuk tampilan offline
                        loadMyBrandProductsFromLocal(localBrand.id)
                    } else {
                        _hasStore.value = false
                        Log.d("CHECK_BRAND", "Tidak ada brand di lokal.")
                    }
                }
            } catch (e: Exception) {
                Log.e("CHECK_BRAND", "Gagal ambil brand dari server: ${e.message}. Mencoba dari lokal.")

                // Fallback ke lokal jika gagal (misalnya offline atau error jaringan)
                val localBrand = brandRepository.getLocalBrand()
                if (localBrand != null) {
                    _existingBrandName.value = localBrand.name
                    _hasStore.value = true
                    Log.d("CHECK_BRAND", "Brand dari lokal (fallback): $localBrand")
                    // Load produk dari Room untuk tampilan offline
                    loadMyBrandProductsFromLocal(localBrand.id)
                } else {
                    _hasStore.value = false
                    Log.d("CHECK_BRAND", "Tidak ada brand di lokal (fallback).")
                }
            }
        }
    }

    // Fungsi ini akan mengambil produk dari API dan menyimpannya ke Room
    // Ini yang akan dipanggil saat online untuk mengisi/memperbarui cache lokal
    private fun loadProductsByBrandFromNetworkAndStore(token: String, brandId: Int) {
        viewModelScope.launch {
            try {
                // productRepository.getMyBrandProducts(token) sudah berisi logika
                // untuk menghapus produk lama brand tersebut dan insert yang baru.
                productRepository.getMyBrandProducts(token)
                Log.d("PRODUCTS_BY_BRAND_NETWORK", "Produk brand di-fetch dan disimpan ke Room.")
                // Setelah disimpan ke Room, _myBrandProducts akan di-update oleh
                // panggilan loadMyBrandProductsFromLocal() atau oleh Composable
                // jika Anda mengubahnya menjadi Flow dari DAO.
                // Untuk saat ini, setelah fetch dari network, panggil lagi loadMyBrandProductsFromLocal
                // untuk memastikan UI mendapatkan data terbaru dari Room.
                loadMyBrandProductsFromLocal(brandId)

            } catch (e: Exception) {
                Log.e("PRODUCTS_BY_BRAND_NETWORK", "Gagal fetch produk brand dari server: ${e.message}")
                // Tidak perlu lakukan apa-apa di sini, karena sudah ada fallback lokal
            }
        }
    }

    // Fungsi ini akan mengambil produk dari Room dan mengupdate _myBrandProducts
    // Ini yang akan dipanggil untuk menampilkan data baik saat online (setelah fetch) maupun offline
    fun loadMyBrandProductsFromLocal(brandId: Int) {
        viewModelScope.launch {
            val result = productRepository.getMyBrandProductsOffline(brandId)
            _myBrandProducts.value = result // Update StateFlow ini
            Log.d("MY_BRAND_PRODUCTS_LOCAL", "Total produk brand dari lokal: ${result.size}")
        }
    }

    // >>> HAPUS ATAU KOMENTARI FUNGSI-FUNGSI LAMA INI:
    // private fun loadProductsByBrand(token: String) { /* ... */ }
    // private val _brandProducts = MutableStateFlow<List<ProductDto>>(emptyList())
    // val brandProducts: StateFlow<List<ProductDto>> = _brandProducts
    // private val _offlineBrandProducts = MutableStateFlow<List<Product>>(emptyList())
    // val offlineBrandProducts: StateFlow<List<Product>> = _offlineBrandProducts
    // fun loadOfflineBrandProducts(brandId: Int) { /* ... */ }
}

//@HiltViewModel
//class CreateBrandViewModel @Inject constructor(
//    private val brandRepository: BrandRepository,
//    private val productRepository: ProductRepository,
//    private val sessionManager: SessionManager
//) : ViewModel() {
//
//    private val _brandName = MutableStateFlow("")
//    val brandName: StateFlow<String> = _brandName
//
//    private val _myBrandProducts = MutableStateFlow<List<Product>>(emptyList())
//    val myBrandProducts: StateFlow<List<Product>> = _myBrandProducts
//
//    private val _brandDescription = MutableStateFlow("")
//    val brandDescription: StateFlow<String> = _brandDescription
//
//    private val _isLoading = MutableStateFlow(false)
//    val isLoading: StateFlow<Boolean> = _isLoading
//
//    private val _message = MutableStateFlow<String?>(null)
//    val message: StateFlow<String?> = _message
//
//    private val _hasStore = MutableStateFlow<Boolean?>(null)
//    val hasStore: StateFlow<Boolean?> = _hasStore
//
//    private val _existingBrandName = MutableStateFlow("")
//    val existingBrandName: StateFlow<String> = _existingBrandName
//
//    fun onBrandNameChanged(newValue: String) {
//        _brandName.value = newValue
//    }
//
//    fun onBrandDescriptionChanged(newValue: String) {
//        _brandDescription.value = newValue
//    }
//
//    fun clearMessage() {
//        _message.value = null
//    }
//
//    fun createBrand(token: String) {
//        viewModelScope.launch {
//            _isLoading.value = true
//            val request = CreateBrandRequest(
//                name = _brandName.value,
//                description = _brandDescription.value
//            )
//            val result = brandRepository.openStore(request, token)
//            _isLoading.value = false
//
//            _message.value = result.fold(
//                onSuccess = {
//                    _hasStore.value = true
//                    _existingBrandName.value = _brandName.value
//                    it
//                },
//                onFailure = {
//                    it.message ?: "Terjadi kesalahan"
//                }
//            )
//        }
//    }
//
//    fun checkBrand(token: String) {
//        viewModelScope.launch {
//            try {
//                // 🔽 Coba ambil dari server
//                val brand = brandRepository.getBrandByUser(token)
//                Log.d("CHECK_BRAND", "Brand dari server: $brand")
//
//                if (brand != null) {
//                    _existingBrandName.value = brand.name
//                    _hasStore.value = true
//
//                    // 🔽 Simpan ke Room
//                    brandRepository.saveBrandToLocal(brand)
//
//                    loadProductsByBrand(token)
//                    loadOfflineBrandProducts(brand.id)
//                } else {
//                    _hasStore.value = false
//                }
//            } catch (e: Exception) {
//                Log.e("CHECK_BRAND", "Gagal ambil dari server, coba dari lokal: ${e.message}")
//
//                // 🔽 Fallback ke lokal jika gagal (offline)
//                val localBrand = brandRepository.getLocalBrand()
//                if (localBrand != null) {
//                    _existingBrandName.value = localBrand.name
//                    _hasStore.value = true
//                    Log.d("CHECK_BRAND", "Brand dari lokal: $localBrand")
//
//                    // 🔽 Tambahkan ini
//                    loadOfflineBrandProducts(localBrand.id)
//
//                } else {
//                    _hasStore.value = false
//                }
//            }
//        }
//    }
//
//
//    private val _brandProducts = MutableStateFlow<List<ProductDto>>(emptyList())
//    val brandProducts: StateFlow<List<ProductDto>> = _brandProducts
//
//    private fun loadProductsByBrand(token: String) {
//        viewModelScope.launch {
//            try {
//                val products = productRepository.getMyBrandProducts(token)
//                _brandProducts.value = products
//                Log.d("PRODUCTS_BY_BRAND", "Total: ${products.size}")
//            } catch (e: Exception) {
//                Log.e("PRODUCTS_BY_BRAND", "Gagal: ${e.message}")
//            }
//        }
//    }
//
//
//    private val _offlineBrandProducts = MutableStateFlow<List<Product>>(emptyList())
//    val offlineBrandProducts: StateFlow<List<Product>> = _offlineBrandProducts
//
//    fun loadOfflineBrandProducts(brandId: Int) {
//        viewModelScope.launch {
//            val result = productRepository.getMyBrandProductsOffline(brandId)
//            _offlineBrandProducts.value = result
//            Log.d("OFFLINE_PRODUCTS", "Total offline produk: ${result.size}")
//        }
//    }
//
//    private fun loadProductsByBrandFromNetworkAndStore(token: String, brandId: Int) {
//        viewModelScope.launch {
//            try {
//                val products = productRepository.getMyBrandProducts(token) // Ini sudah menyimpan ke Room
//                Log.d("PRODUCTS_BY_BRAND_NETWORK", "Total dari network: ${products.size}")
//                // Setelah disimpan ke Room, myBrandProducts akan otomatis update
//            } catch (e: Exception) {
//                Log.e("PRODUCTS_BY_BRAND_NETWORK", "Gagal fetch produk brand dari server: ${e.message}")
//                // Tidak perlu lakukan apa-apa di sini, karena sudah ada fallback lokal
//            }
//        }
//    }
//
//    // Fungsi untuk memuat produk brand dari Room
//    fun loadMyBrandProductsFromLocal(brandId: Int) {
//        viewModelScope.launch {
//            val result = productRepository.getMyBrandProductsOffline(brandId)
//            _myBrandProducts.value = result // Update StateFlow ini
//            Log.d("MY_BRAND_PRODUCTS_LOCAL", "Total offline produk: ${result.size}")
//        }
//    }
//}
//
//
