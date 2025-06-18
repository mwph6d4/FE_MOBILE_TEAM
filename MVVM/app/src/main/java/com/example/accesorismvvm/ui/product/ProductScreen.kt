//package com.example.accesorismvvm.ui.product
//
//import androidx.compose.foundation.Image
//import androidx.compose.foundation.layout.*
//import androidx.compose.foundation.lazy.grid.GridCells
//import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
//import androidx.compose.foundation.lazy.grid.items
//import androidx.compose.material.icons.Icons
//import androidx.compose.material.icons.automirrored.filled.ArrowBack
//import androidx.compose.material.icons.filled.Star
//import androidx.compose.material3.*
//import androidx.compose.runtime.Composable
//import androidx.compose.runtime.collectAsState
//import androidx.compose.runtime.getValue
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.layout.ContentScale
//import androidx.compose.ui.text.font.FontWeight
//import androidx.compose.ui.text.style.TextOverflow
//import androidx.compose.ui.unit.dp
//import androidx.hilt.navigation.compose.hiltViewModel
//import coil.compose.rememberAsyncImagePainter
//import com.example.accesorismvvm.domain.model.Product
//import com.example.accesorismvvm.ui.Detail.ProductDetailViewModel
//import com.example.accesorismvvm.ui.Home.toRupiah
//import java.text.NumberFormat
//import java.util.Locale
//
//
//@OptIn(ExperimentalMaterial3Api::class)
//@Composable
//fun ProductScreen(viewModel: ProductViewModel = hiltViewModel()) {
//    val products by viewModel.produk.collectAsState()
//    val isLoading by viewModel.isLoading.collectAsState()
//
//    Scaffold(
//        topBar = {
//            TopAppBar(title = { Text("Product Catalog") })
//        }
//    ) { padding ->
//        when {
//            isLoading -> Box(
//                modifier = Modifier
//                    .fillMaxSize()
//                    .padding(padding),
//                contentAlignment = Alignment.Center
//            ) {
//                CircularProgressIndicator()
//            }
//
//            products.isEmpty() -> Box(
//                modifier = Modifier
//                    .fillMaxSize()
//                    .padding(padding),
//                contentAlignment = Alignment.Center
//            ) {
//                Text("No products found.")
//            }
//
//            else -> LazyVerticalGrid(
//                columns = GridCells.Fixed(2),
//                contentPadding = PaddingValues(8.dp),
//                verticalArrangement = Arrangement.spacedBy(8.dp),
//                horizontalArrangement = Arrangement.spacedBy(8.dp),
//                modifier = Modifier.padding(padding)
//            ) {
//                items(products) { product ->
//                    ProductCard(product = product)
//                }
//            }
//        }
//    }
//}
//
//@OptIn(ExperimentalMaterial3Api::class)
//@Composable
//fun ProductCard(product: Product) {
//    Card(
//        modifier = Modifier
//            .fillMaxWidth()
//            .height(250.dp), // Sesuaikan tinggi kartu
//        onClick = { /* Handle klik produk, misalnya navigasi ke detail produk */ }
//    ) {
//        Column(
//            modifier = Modifier.fillMaxSize()
//        ) {
//            Image(
//                painter = rememberAsyncImagePainter(product.imageUrl),
//                contentDescription = product.name,
//                modifier = Modifier
//                    .height(120.dp)
//                    .fillMaxWidth(),
//                contentScale = ContentScale.Crop
//            )
//            Spacer(modifier = Modifier.height(8.dp))
//            Column(
//                modifier = Modifier
//                    .padding(horizontal = 8.dp)
//                    .fillMaxHeight(),
//                verticalArrangement = Arrangement.SpaceBetween
//            ) {
//                Text(
//                    text = product.name,
//                    style = MaterialTheme.typography.bodyLarge,
//                    fontWeight = FontWeight.Bold,
//                    maxLines = 2,
//                    overflow = TextOverflow.Ellipsis
//                )
//                Spacer(modifier = Modifier.height(4.dp))
//
//                Text(
//                    text = product.price.toRupiah(),
//                    style = MaterialTheme.typography.bodyMedium,
//                    color = MaterialTheme.colorScheme.primary
//                )
//
//                Spacer(modifier = Modifier.height(4.dp))
//                Row(
//                    verticalAlignment = Alignment.CenterVertically
//                ) {
//                    Icon(
//                        imageVector = Icons.Default.Star, // Perlu ikon bintang
//                        contentDescription = "Rating",
//                        tint = Color.Yellow, // Warna bintang
//                        modifier = Modifier.size(16.dp)
//                    )
////                    Text(
////                        text = "${product.rating} (${product.reviewCount})",
////                        style = MaterialTheme.typography.bodySmall,
////                        modifier = Modifier.padding(start = 4.dp)
////                    )
//                }
//            }
//        }
//    }
//}