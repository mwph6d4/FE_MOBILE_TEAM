package com.example.accesorismvvm.ui.Home

import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.accesorismvvm.R
import com.example.accesorismvvm.domain.model.Product
import com.example.accesorismvvm.ui.product.ProductViewModel
import androidx.compose.ui.draw.clip

import java.nio.file.WatchEvent
import java.text.NumberFormat
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    navController: NavController,
    viewModel: ProductViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val products by viewModel.products.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()


    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ){
        LazyColumn(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item {
                // ✅ Banner
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(120.dp)
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.logofix2),
                        contentDescription = "Banner Promo",
                        contentScale = ContentScale.Crop, // atau FillWidth jika ingin tidak terpotong
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp) // Padding kiri, kanan, atas, bawah
                            .clip(RoundedCornerShape(16.dp))
                    )
                }
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                        .height(48.dp),
                    shape = RoundedCornerShape(24.dp),
                    border = BorderStroke(1.dp, Color(0xFFE0E0E0)), // Tambahkan border halus abu muda
                    colors = CardDefaults.cardColors(
                        containerColor = Color.White // Putih biar matching dengan screen
                    ),
                    onClick = { navController.navigate("search_screen") }
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = 16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.outline_search_24),
                            contentDescription = "Search",
                            tint = Color(0xFF9E9E9E) // Abu-abu netral
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            "Cari produk di AccessoriesBox",
                            color = Color(0xFF9E9E9E), // Sama seperti icon
                            fontSize = 16.sp
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))
            }

            // ✅ Product Grid (2 columns)
            items(products.chunked(2)) { rowItems ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 12.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    rowItems.forEach { product ->
                        ProductCardShopee(
                            product = product,
                            navController = navController,
                            modifier = Modifier.weight(1f)
                        )
                    }
                    if (rowItems.size == 1) {
                        Spacer(modifier = Modifier.weight(1f))
                    }
                }
            }
            item {
                Spacer(modifier = Modifier.height(32.dp))
            }
        }
    }
}

fun Double.toRupiah(): String {
    val format = NumberFormat.getCurrencyInstance(Locale("in", "ID"))
    return format.format(this)
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductCardShopee(
    product: Product,
    navController: NavController,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .height(230.dp)
            .border(1.dp, Color(0xFFE0E0E0), RoundedCornerShape(16.dp)),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        elevation = CardDefaults.cardElevation(0.dp),
        onClick = {
            navController.navigate("product_detail/${product.id}")
        }
    ) {
        Column {
            // ✅ Gambar Produk
            Image(
                painter = rememberAsyncImagePainter(product.imageUrl),
                contentDescription = product.name,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(130.dp)
                    .clip(RoundedCornerShape(topStart = 12.dp, topEnd = 12.dp))
            )

            // ✅ Info Produk
            Column(
                modifier = Modifier
                    .padding(12.dp)
            ) {
                // Nama Produk
                Text(
                    text = product.name,
                    style = MaterialTheme.typography.titleSmall.copy( // lebih kecil dari bodyLarge
                        fontWeight = FontWeight.Normal // tidak terlalu tebal
                    ),
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )

                // Harga Produk (lebih besar)
                Text(
                    text = product.price.toRupiah(),
                    style = MaterialTheme.typography.titleMedium.copy( // lebih besar dari bodyMedium
                        color = Color(0xFFFF1BA7),
                        fontWeight = FontWeight.SemiBold
                    ),
                    modifier = Modifier.padding(top = 6.dp)
                )


            }
        }
    }
}
