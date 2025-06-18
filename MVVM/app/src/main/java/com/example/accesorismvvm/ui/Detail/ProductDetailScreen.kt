import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.accesorismvvm.ui.product.ProductViewModel
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.ui.draw.shadow
import androidx.compose.material3.IconButton
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Inventory
import androidx.compose.material.icons.filled.Inventory2
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.Store
import androidx.compose.material.icons.filled.Storefront
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.mutableStateOf
import com.example.accesorismvvm.ui.Detail.ProductDetailViewModel
import com.example.accesorismvvm.ui.Home.toRupiah
import java.text.NumberFormat
import java.util.Locale
import androidx.compose.material3.TopAppBarDefaults
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductDetailScreen(
    productId: Int,
    token: String,
    onBack: () -> Unit,
    viewModel: ProductDetailViewModel = hiltViewModel()
) {
    LaunchedEffect(productId, token) {
        viewModel.loadProduct(productId)
    }

    val product by viewModel.product.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()

    val pinkColor = Color(0xFFFF1BA7)

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Detail Produk",
                        fontWeight = FontWeight.Normal
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.White,
                    titleContentColor = Color.Black,
                    navigationIconContentColor = Color.Black
                )
            )
        },
        bottomBar = {
            if (product != null) {
                BottomAppBar(
                    containerColor = Color.White,
                    tonalElevation = 4.dp
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 8.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Button(
                            onClick = { /* TODO: Tambah ke keranjang */ },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = pinkColor.copy(alpha = 0.15f),
                                contentColor = pinkColor
                            ),
                            modifier = Modifier
                                .weight(1f)
                                .height(48.dp)
                        ) {
                            Icon(Icons.Default.ShoppingCart, contentDescription = "Cart")
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Keranjang")
                        }

                        Spacer(modifier = Modifier.width(12.dp))

                        Button(
                            onClick = { /* TODO: Beli sekarang */ },
                            colors = ButtonDefaults.buttonColors(containerColor = pinkColor),
                            modifier = Modifier
                                .weight(1f)
                                .height(48.dp)
                        ) {
                            Text("Beli Sekarang", color = Color.White)
                        }
                    }
                }
            }
        }
    ) { paddingValues ->
        when {
            isLoading -> Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = pinkColor)
            }

            product != null -> {
                val detail = product!!
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.White)
                        .padding(paddingValues)
                        .verticalScroll(rememberScrollState())
                ) {
                    Image(
                        painter = rememberAsyncImagePainter(detail.imageUrl),
                        contentDescription = detail.name,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(280.dp)
                            .background(Color.LightGray.copy(alpha = 0.2f)),
                        contentScale = ContentScale.Fit
                    )

                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = detail.name,
                            style = MaterialTheme.typography.titleLarge.copy(
                                fontWeight = FontWeight.Medium
                            ),
                            color = Color.Black
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = detail.price.toRupiah(),
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.SemiBold,
                                color = pinkColor
                            )
                        )

                        Spacer(modifier = Modifier.height(16.dp))
                        Divider(thickness = 1.dp, color = Color.LightGray.copy(alpha = 0.6f))
                        Spacer(modifier = Modifier.height(16.dp))

                        Text(
                            text = "Deskripsi Produk",
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.SemiBold,
                                color = pinkColor
                            )
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = detail.description ?: "-",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        Spacer(modifier = Modifier.height(8.dp))

                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.Storefront,
                                contentDescription = "Brand",
                                tint = Color.Gray,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = detail.brandName ?: "-",
                                style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Medium),
                                color = Color.Gray
                            )
                        }

                        Spacer(modifier = Modifier.height(6.dp))

                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.Inventory2,
                                contentDescription = "Stok",
                                tint = Color.Gray,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "Stok tersedia: ${detail.stock}",
                                style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Medium),
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }

                    }
                }
            }

            else -> Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentAlignment = Alignment.Center
            ) {
                Text("Produk tidak ditemukan.")
            }
        }
    }
}



//
//@OptIn(ExperimentalMaterial3Api::class)
//@Composable
//fun ProductDetailScreen(
//    productId: Int,
//    token: String, // Mungkin tidak lagi dibutuhkan di sini jika autentikasi di Retrofit Interceptor
//    onBack: () -> Unit,
//    viewModel: ProductDetailViewModel = hiltViewModel() // Injeksi ViewModel
//) {
//    val product by viewModel.productDetail.collectAsState()
//    val isLoading by viewModel.isLoading.collectAsState()
//    val errorMessage by viewModel.errorMessage.collectAsState()
//
//    Scaffold(
//        topBar = {
//            TopAppBar(
//                title = { Text("Detail Produk") },
//                navigationIcon = {
//                    IconButton(onClick = onBack) {
//                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Kembali")
//                    }
//                }
//            )
//        },
//        bottomBar = {
//            // Bottom Bar untuk Tombol Aksi
//            if (product != null) {
//                BottomAppBar(
//                    modifier = Modifier.fillMaxWidth(),
//                    containerColor = MaterialTheme.colorScheme.surface
//                ) {
//                    Row(
//                        modifier = Modifier
//                            .fillMaxWidth()
//                            .padding(horizontal = 16.dp, vertical = 8.dp),
//                        verticalAlignment = Alignment.CenterVertically,
//                        horizontalArrangement = Arrangement.SpaceBetween
//                    ) {
//                        // Tombol Tambah Keranjang
//                        Button(
//                            onClick = { /* TODO: Implementasi Tambah ke Keranjang */ },
//                            colors = ButtonDefaults.buttonColors(
//                                containerColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f), // Warna latar belakang
//                                contentColor = MaterialTheme.colorScheme.primary // Warna ikon/teks
//                            ),
//                            modifier = Modifier
//                                .weight(0.4f)
//                                .height(48.dp),
//                            shape = RoundedCornerShape(8.dp),
//                        ) {
//                            Icon(Icons.Default.ShoppingCart, contentDescription = "Tambah Keranjang", modifier = Modifier.size(20.dp))
//                            Spacer(modifier = Modifier.width(8.dp))
//                            Text("Keranjang", fontSize = 14.sp)
//                        }
//
//                        Spacer(modifier = Modifier.width(16.dp))
//
//                        // Tombol Beli Sekarang
//                        Button(
//                            onClick = { /* TODO: Implementasi Beli Sekarang */ },
//                            modifier = Modifier
//                                .weight(0.6f)
//                                .height(48.dp),
//                            shape = RoundedCornerShape(8.dp)
//                        ) {
//                            Text("Beli Sekarang", fontSize = 16.sp, fontWeight = FontWeight.Bold)
//                        }
//                    }
//                }
//            }
//        }
//    ) { paddingValues ->
//        when {
//            isLoading -> Box(
//                modifier = Modifier
//                    .fillMaxSize()
//                    .padding(paddingValues),
//                contentAlignment = Alignment.Center
//            ) {
//                CircularProgressIndicator()
//            }
//            errorMessage != null -> Box(
//                modifier = Modifier
//                    .fillMaxSize()
//                    .padding(paddingValues),
//                contentAlignment = Alignment.Center
//            ) {
//                Text(errorMessage ?: "Terjadi kesalahan saat memuat produk.")
//            }
//            product != null -> {
//                Column(
//                    modifier = Modifier
//                        .fillMaxSize()
//                        .padding(paddingValues)
//                        .verticalScroll(rememberScrollState()) // Agar bisa digulir jika konten panjang
//                ) {
//                    // --- Bagian Gambar Produk ---
//                    Box(
//                        modifier = Modifier
//                            .fillMaxWidth()
//                            .height(200.dp) // Ukuran gambar lebih besar
//                            .background(Color.LightGray.copy(alpha = 0.2f)), // Latar belakang abu-abu muda
//                        contentAlignment = Alignment.Center
//                    ) {
//                        Image(
//                            painter = rememberAsyncImagePainter(product!!.imageUrl),
//                            contentDescription = product!!.name,
//                            modifier = Modifier
//                                .fillMaxSize(), // Gambar memenuhi box
//                            contentScale = ContentScale.Fit // Gambar fit dalam area, tidak dipotong
//                        )
//                    }
//
//                    // --- Bagian Informasi Produk ---
//                    Column(
//                        modifier = Modifier.padding(16.dp)
//                    ) {
//                        Text(
//                            text = product!!.price.toRupiah(),
//                            style = MaterialTheme.typography.headlineMedium,
//                            fontWeight = FontWeight.Bold,
//                            color = MaterialTheme.colorScheme.primary // Harga dengan warna utama
//                        )
//                        Spacer(modifier = Modifier.height(4.dp))
//                        Text(
//                            text = product!!.name,
//                            style = MaterialTheme.typography.headlineSmall,
//                            fontWeight = FontWeight.Normal,
//                            color = MaterialTheme.colorScheme.onSurface
//                        )
//                        Spacer(modifier = Modifier.height(8.dp))
//
//                        // Rating dan Review (Jika ada)
//                        Row(
//                            verticalAlignment = Alignment.CenterVertically
//                        ) {
//                            Icon(
//                                imageVector = Icons.Default.Star,
//                                contentDescription = "Rating",
//                                tint = Color(0xFFFFC107), // Warna kuning bintang
//                                modifier = Modifier.size(20.dp)
//                            )
//                            Spacer(modifier = Modifier.width(4.dp))
////                            Text(
////                                text = "${product!!.rating} (${product!!.reviewCount} review)", // Gunakan data rating & reviewCount
////                                style = MaterialTheme.typography.bodyMedium,
////                                color = MaterialTheme.colorScheme.onSurfaceVariant
////                            )
//                        }
//                        Spacer(modifier = Modifier.height(16.dp))
//
//                        Divider(modifier = Modifier.fillMaxWidth().height(1.dp), color = Color.LightGray.copy(alpha = 0.5f))
//                        Spacer(modifier = Modifier.height(16.dp))
//
//                        // Deskripsi Produk
//                        Text(
//                            text = "Deskripsi Produk",
//                            style = MaterialTheme.typography.titleLarge,
//                            fontWeight = FontWeight.SemiBold
//                        )
//                        Spacer(modifier = Modifier.height(8.dp))
//                        Text(
//                            text = product!!.description,
//                            style = MaterialTheme.typography.bodyLarge,
//                            color = MaterialTheme.colorScheme.onSurfaceVariant
//                        )
//                        Spacer(modifier = Modifier.height(16.dp)) // Memberi ruang di bawah deskripsi
//                    }
//                }
//            }
//            else -> Box(
//                modifier = Modifier
//                    .fillMaxSize()
//                    .padding(paddingValues),
//                contentAlignment = Alignment.Center
//            ) {
//                Text("Produk tidak ditemukan.")
//            }
//        }
//    }
//}
//

// ProductDetailScreen.kt
//@OptIn(ExperimentalMaterial3Api::class)
//@Composable
//fun ProductDetailScreen(
//    productId: Int,
//    token: String, // Jika Anda masih memerlukan token di sini
//    onBack: () -> Unit,
//    viewModel: ProductDetailViewModel = hiltViewModel() // Injeksi ViewModel
//) {
//    val product by viewModel.productDetail.collectAsState()
//    val isLoading by viewModel.isLoading.collectAsState()
//    val errorMessage by viewModel.errorMessage.collectAsState()
//
//    Scaffold(
//        topBar = {
//            TopAppBar(
//                title = { Text("Detail Produk") },
//                navigationIcon = {
//                    IconButton(onClick = onBack) {
//                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
//                    }
//                }
//            )
//        }
//    ) { paddingValues ->
//        if (isLoading) {
//            Box(
//                modifier = Modifier
//                    .fillMaxSize()
//                    .padding(paddingValues),
//                contentAlignment = Alignment.Center
//            ) {
//                CircularProgressIndicator()
//            }
//        } else if (errorMessage != null) {
//            Box(
//                modifier = Modifier
//                    .fillMaxSize()
//                    .padding(paddingValues),
//                contentAlignment = Alignment.Center
//            ) {
//                Text(errorMessage ?: "Terjadi kesalahan")
//            }
//        } else if (product != null) {
//            Column(
//                modifier = Modifier
//                    .fillMaxSize()
//                    .padding(paddingValues)
//                    .verticalScroll(rememberScrollState())
//            ) {
//                // Gambar Produk
//                Image(
//                    painter = rememberAsyncImagePainter(product!!.imageUrl),
//                    contentDescription = product!!.name,
//                    modifier = Modifier
//                        .fillMaxWidth()
//                        .height(300.dp),
//                    contentScale = ContentScale.Crop
//                )
//
//                // Detail Produk
//                Column(
//                    modifier = Modifier.padding(16.dp)
//                ) {
//                    Text(
//                        text = product!!.name,
//                        style = MaterialTheme.typography.headlineMedium,
//                        fontWeight = FontWeight.Bold
//                    )
//                    Spacer(modifier = Modifier.height(8.dp))
//                    Text(
//                        text = product!!.price.toRupiah(),
//                        style = MaterialTheme.typography.headlineSmall,
//                        color = MaterialTheme.colorScheme.primary
//                    )
//                    Spacer(modifier = Modifier.height(16.dp))
//                    Text(
//                        text = "Deskripsi Produk:",
//                        style = MaterialTheme.typography.titleMedium,
//                        fontWeight = FontWeight.SemiBold
//                    )
//                    Spacer(modifier = Modifier.height(4.dp))
//                    Text(
//                        text = product!!.description,
//                        style = MaterialTheme.typography.bodyLarge
//                    )
//                    // Tambahkan detail lain seperti rating, stok, dll. jika ada di Product model Anda
//                }
//            }
//        } else {
//            // State ketika product null tapi tidak ada error dan tidak loading
//            Box(
//                modifier = Modifier
//                    .fillMaxSize()
//                    .padding(paddingValues),
//                contentAlignment = Alignment.Center
//            ) {
//                Text("Produk tidak ditemukan.")
//            }
//        }
//    }
//}
//@Composable
//fun ProductDetailScreen(
//    productId: Int,
//    viewModel: ProductViewModel = hiltViewModel(),
//    navController: NavController
//) {
//    val context = LocalContext.current
//    val product by viewModel.getProductById(productId).collectAsState(initial = null)
//
//    val quantity = remember { mutableStateOf(1) }
//
//    if (product == null) {
//        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
//            CircularProgressIndicator()
//        }
//        return
//    }
//
//    // ✅ Smart cast ke non-nullable
//    val item = product!!
//
//    Box(modifier = Modifier.fillMaxSize()) {
//        LazyColumn(modifier = Modifier.fillMaxSize()) {
//            item {
//                Box(modifier = Modifier.fillMaxWidth()) {
//                    Image(
//                        painter = rememberAsyncImagePainter(item.imageUrl),
//                        contentDescription = item.name,
//                        modifier = Modifier
//                            .fillMaxWidth()
//                            .height(300.dp),
//                        contentScale = ContentScale.Crop
//                    )
//
//                    IconButton(
//                        onClick = { navController.popBackStack() },
//                        modifier = Modifier
//                            .padding(16.dp)
//                            .align(Alignment.TopStart)
//                            .background(Color.White.copy(alpha = 0.7f), shape = RoundedCornerShape(50))
//                            .size(36.dp)
//                    ) {
//                        Icon(
//                            imageVector = Icons.Default.ArrowBack,
//                            contentDescription = "Kembali",
//                            tint = Color.Black
//                        )
//                    }
//                }
//
//                Column(modifier = Modifier.padding(16.dp)) {
//                    Text(
//                        text = item.name,
//                        style = MaterialTheme.typography.titleLarge,
//                        fontWeight = FontWeight.Bold
//                    )
//
//                    Spacer(modifier = Modifier.height(8.dp))
//
//                    Text(
//                        text = item.price.toRupiah(),
//                        style = MaterialTheme.typography.bodyMedium,
//                        color = MaterialTheme.colorScheme.primary,
//                        fontWeight = FontWeight.SemiBold,
//                        fontSize = 20.sp
//                    )
//
//                    Spacer(modifier = Modifier.height(4.dp))
//
//                    Row(verticalAlignment = Alignment.CenterVertically) {
//                        Icon(
//                            imageVector = Icons.Default.Star,
//                            contentDescription = "Rating",
//                            tint = Color.Yellow,
//                            modifier = Modifier.size(18.dp)
//                        )
//                    }
//
//                    Spacer(modifier = Modifier.height(16.dp))
//
//                    Text(
//                        text = "Jumlah",
//                        style = MaterialTheme.typography.titleMedium,
//                        fontWeight = FontWeight.SemiBold
//                    )
//                    Spacer(modifier = Modifier.height(8.dp))
//
//                    Row(verticalAlignment = Alignment.CenterVertically) {
//                        IconButton(
//                            onClick = {
//                                if (quantity.value > 1) quantity.value--
//                            }
//                        ) {
//                            Icon(Icons.Default.Remove, contentDescription = "Kurangi")
//                        }
//
//                        Text(
//                            text = quantity.value.toString(),
//                            fontSize = 18.sp,
//                            modifier = Modifier.padding(horizontal = 16.dp)
//                        )
//
//                        IconButton(
//                            onClick = {
//                                quantity.value++
//                            }
//                        ) {
//                            Icon(Icons.Default.Add, contentDescription = "Tambah")
//                        }
//                    }
//
//                    Spacer(modifier = Modifier.height(80.dp))
//                }
//            }
//        }
//
//        Button(
//            onClick = {
//                Toast.makeText(
//                    context,
//                    "Berhasil tambah ${quantity.value} ke keranjang",
//                    Toast.LENGTH_SHORT
//                ).show()
//            },
//            modifier = Modifier
//                .fillMaxWidth()
//                .align(Alignment.BottomCenter)
//                .padding(16.dp),
//            shape = RoundedCornerShape(12.dp),
//            colors = ButtonDefaults.buttonColors(
//                containerColor = Color(0xFFFF1BA7),
//                contentColor = Color.White
//            )
//        ) {
//            Text("Beli Sekarang", fontSize = 18.sp)
//        }
//    }
//}
//
//fun Double.toRupiah(): String {
//    val formatter = NumberFormat.getCurrencyInstance(Locale("in", "ID"))
//    return formatter.format(this)
//}

//@OptIn(ExperimentalMaterial3Api::class)
//@Composable
//fun ProductDetailScreen(
//    productId: Int,
//    token: String,
//    viewModel: ProductViewModel = hiltViewModel(),
//    onBack: () -> Unit
//) {
//    val detail by viewModel.productDetail.collectAsState()
//    val isLoading by viewModel.isLoading.collectAsState()
//    val error by viewModel.detailError.collectAsState()
//
//    // Fetch saat pertama kali tampil
//    LaunchedEffect(productId) {
//        viewModel.getProductDetail(productId, token)
//    }
//
//    Scaffold(
//        topBar = {
//            TopAppBar(
//                title = { Text(text = detail?.name ?: "Product Detail") },
//                navigationIcon = {
//                    IconButton(onClick = onBack) {
//                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
//                    }
//                }
//            )
//        }
//    ) { padding ->
//        when {
//            isLoading -> {
//                Box(
//                    modifier = Modifier
//                        .fillMaxSize()
//                        .padding(padding),
//                    contentAlignment = Alignment.Center
//                ) {
//                    CircularProgressIndicator()
//                }
//            }
//
//            error != null -> {
//                Box(
//                    modifier = Modifier
//                        .fillMaxSize()
//                        .padding(padding),
//                    contentAlignment = Alignment.Center
//                ) {
//                    Text("Error: $error")
//                }
//            }
//
//            detail != null -> {
//                val product = detail!!
//                Column(
//                    modifier = Modifier
//                        .verticalScroll(rememberScrollState())
//                        .padding(16.dp)
//                        .padding(padding)
//                ) {
//                    Image(
//                        painter = rememberAsyncImagePainter(product.imageUrl),
//                        contentDescription = product.name,
//                        modifier = Modifier
//                            .fillMaxWidth()
//                            .height(250.dp),
//                        contentScale = ContentScale.Crop
//                    )
//
//                    Spacer(modifier = Modifier.height(16.dp))
//
//                    Text(product.name, style = MaterialTheme.typography.headlineSmall)
//                    Text(product.price.toRupiah(), color = MaterialTheme.colorScheme.primary)
//
//                    Spacer(modifier = Modifier.height(8.dp))
//
//                    Text(product.description ?: "No description available", style = MaterialTheme.typography.bodyMedium)
//                }
//            }
//
//            else -> {
//                Box(
//                    modifier = Modifier
//                        .fillMaxSize()
//                        .padding(padding),
//                    contentAlignment = Alignment.Center
//                ) {
//                    Text("Tidak ada data.")
//                }
//            }
//        }
//    }
//}

