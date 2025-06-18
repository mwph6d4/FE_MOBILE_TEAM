package com.example.accesorismvvm.ui.CreateBrand

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SegmentedButtonDefaults.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.accesorismvvm.ui.Search.SearchViewModel
import androidx.compose.material3.Icon
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.remember
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import com.example.accesorismvvm.data.Session.SessionManager
import com.example.accesorismvvm.ui.Home.ProductCardShopee
import com.example.accesorismvvm.data.remote.response.ProductDto
import com.example.accesorismvvm.domain.model.Product
import com.example.accesorismvvm.data.mapper.toDomain// <-- ini penting!

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateBrandScreen(
    navController: NavController,
    viewModel: CreateBrandViewModel = hiltViewModel()
) {
    val brandName by viewModel.brandName.collectAsState()
    val brandDescription by viewModel.brandDescription.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val message by viewModel.message.collectAsState()
    val hasStore by viewModel.hasStore.collectAsState()
    val existingBrandName by viewModel.existingBrandName.collectAsState()

    // >>> Gunakan SATU StateFlow yang baru
    val myBrandProducts by viewModel.myBrandProducts.collectAsState()


    val context = LocalContext.current
    val sessionManager = remember { SessionManager(context) }
    val token = sessionManager.getToken()
    val pinkColor = Color(0xFFFF1BA7)

    LaunchedEffect(Unit) {
        if (!token.isNullOrBlank()) {
            viewModel.checkBrand(token)
        }
    }

    LaunchedEffect(message) {
        message?.let {
            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
            if (it.contains("berhasil", ignoreCase = true)) {
                // Ini akan memicu checkBrand lagi di ViewModel yang sudah diperbarui
                // dan juga akan popBackStack setelah proses selesai.
                navController.popBackStack()
            }
            viewModel.clearMessage()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Brand",
                        fontWeight = FontWeight.Bold,
                        color = pinkColor
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
            )
        },
        containerColor = Color.White
    ) { padding ->

        when (hasStore) {
            null -> {
                Box(
                    modifier = Modifier
                        .padding(padding)
                        .fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = pinkColor)
                }
            }

            true -> {
                Column(
                    modifier = Modifier
                        .padding(padding)
                        .fillMaxSize()
                        .padding(16.dp)
                ) {
                    Text(
                        text = "Kamu sudah memiliki brand!",
                        color = pinkColor,
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = "Nama Brand: $existingBrandName",
                        fontSize = 16.sp
                    )

                    Spacer(modifier = Modifier.height(20.dp))

                    // >>> Hapus baris ini: val offlineBrandProducts by viewModel.offlineBrandProducts.collectAsState()
                    // >>> Hapus juga logika if (brandProducts.isNotEmpty()) ... else ...
                    Button(
                        onClick = {
                            navController.navigate("addProduct") // pastikan ini sesuai dengan route Compose kamu
                        },
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = pinkColor,
                            contentColor = Color.White
                        )
                    ) {
                        Text("Tambah Produk", fontWeight = FontWeight.Bold)
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    if (myBrandProducts.isEmpty()) { // Gunakan myBrandProducts langsung
                        Text("Belum ada produk dari brand kamu.", color = Color.Gray)
                    } else {
                        LazyColumn {
                            items(myBrandProducts) { product -> // Gunakan myBrandProducts
                                ProductCardShopee(
                                    product = product,
                                    navController = navController,
                                    modifier = Modifier.padding(bottom = 12.dp)
                                )
                            }
                        }
                    }
                }
            }
            false -> {
                Column(
                    modifier = Modifier
                        .padding(padding)
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                        .fillMaxSize(),
                    verticalArrangement = Arrangement.Top,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // ... (UI untuk membuat brand) ...
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(
                                color = Color(0xFFFFF3F8),
                                shape = RoundedCornerShape(12.dp)
                            )
                            .padding(16.dp)
                    ) {
                        Text(
                            text = "Anda belum mempunyai brand! Buat brand baru sekarang.",
                            color = pinkColor,
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 14.sp,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    OutlinedTextField(
                        value = brandName,
                        onValueChange = { viewModel.onBrandNameChanged(it) },
                        label = { Text("Nama Brand") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    OutlinedTextField(
                        value = brandDescription,
                        onValueChange = { viewModel.onBrandDescriptionChanged(it) },
                        label = { Text("Deskripsi Brand") },
                        modifier = Modifier.fillMaxWidth(),
                        maxLines = 5
                    )

                    Spacer(modifier = Modifier.height(20.dp))

                    Button(
                        onClick = {
                            if (!token.isNullOrBlank()) {
                                viewModel.createBrand(token)
                            } else {
                                Toast.makeText(context, "Token tidak tersedia", Toast.LENGTH_SHORT).show()
                            }
                        },
                        enabled = !isLoading,
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = pinkColor,
                            contentColor = Color.White
                        )
                    ) {
                        Text("Buat Brand", fontWeight = FontWeight.Bold)
                    }

                    if (isLoading) {
                        Spacer(modifier = Modifier.height(16.dp))
                        CircularProgressIndicator(color = pinkColor)
                    }
                }
            }
        }
    }
}

//@OptIn(ExperimentalMaterial3Api::class)
//@Composable
//fun CreateBrandScreen(
//    navController: NavController,
//    viewModel: CreateBrandViewModel = hiltViewModel()
//) {
//    val brandName by viewModel.brandName.collectAsState()
//    val brandDescription by viewModel.brandDescription.collectAsState()
//    val isLoading by viewModel.isLoading.collectAsState()
//    val message by viewModel.message.collectAsState()
//    val hasStore by viewModel.hasStore.collectAsState()
//    val existingBrandName by viewModel.existingBrandName.collectAsState()
//    val brandProducts by viewModel.brandProducts.collectAsState()
//
//
//    val context = LocalContext.current
//    val sessionManager = remember { SessionManager(context) }
//    val token = sessionManager.getToken()
//    val pinkColor = Color(0xFFFF1BA7)
//
//    LaunchedEffect(Unit) {
//        if (!token.isNullOrBlank()) {
//            viewModel.checkBrand(token)
//        }
//    }
//
//    LaunchedEffect(message) {
//        message?.let {
//            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
//            if (it.contains("berhasil", ignoreCase = true)) {
//                navController.popBackStack()
//            }
//            viewModel.clearMessage()
//        }
//    }
//
//    Scaffold(
//        topBar = {
//            TopAppBar(
//                title = {
//                    Text(
//                        text = "Brand",
//                        fontWeight = FontWeight.Bold,
//                        color = pinkColor
//                    )
//                },
////                navigationIcon = {
////                    IconButton(onClick = { navController.popBackStack() }) {
////                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
////                    }
////                },
//                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
//            )
//        },
//        containerColor = Color.White
//    ) { padding ->
//
//        when (hasStore) {
//            null -> {
//                Box(
//                    modifier = Modifier
//                        .padding(padding)
//                        .fillMaxSize(),
//                    contentAlignment = Alignment.Center
//                ) {
//                    CircularProgressIndicator(color = pinkColor)
//                }
//            }
//
//            true -> {
//                Column(
//                    modifier = Modifier
//                        .padding(padding)
//                        .fillMaxSize()
//                        .padding(16.dp)
//                ) {
//                    Text(
//                        text = "Kamu sudah memiliki brand!",
//                        color = pinkColor,
//                        fontWeight = FontWeight.Bold,
//                        fontSize = 20.sp
//                    )
//
//                    Spacer(modifier = Modifier.height(12.dp))
//
//                    Text(
//                        text = "Nama Brand: $existingBrandName",
//                        fontSize = 16.sp
//                    )
//
//                    Spacer(modifier = Modifier.height(20.dp))
//
//                    val offlineBrandProducts by viewModel.offlineBrandProducts.collectAsState()
//
//                    val allProducts = if (brandProducts.isNotEmpty()) {
//                        brandProducts.map { it.toDomain() }
//                    } else {
//                        offlineBrandProducts
//                    }
//
//                    if (allProducts.isEmpty()) {
//                        Text("Belum ada produk dari brand kamu.", color = Color.Gray)
//                    } else {
//                        LazyColumn {
//                            items(allProducts) { product ->
//                                ProductCardShopee(
//                                    product = product,
//                                    navController = navController,
//                                    modifier = Modifier.padding(bottom = 12.dp)
//                                )
//                            }
//                        }
//                    }
//
//                }
//            }
//
//
//
//
//            false -> {
//                Column(
//                    modifier = Modifier
//                        .padding(padding)
//                        .padding(horizontal = 16.dp, vertical = 8.dp)
//                        .fillMaxSize(),
//                    verticalArrangement = Arrangement.Top,
//                    horizontalAlignment = Alignment.CenterHorizontally
//                ) {
//                    // Box Informasi
//                    Box(
//                        modifier = Modifier
//                            .fillMaxWidth()
//                            .background(
//                                color = Color(0xFFFFF3F8),
//                                shape = RoundedCornerShape(12.dp)
//                            )
//                            .padding(16.dp)
//                    ) {
//                        Text(
//                            text = "Anda belum mempunyai brand! Buat brand baru sekarang.",
//                            color = pinkColor,
//                            fontWeight = FontWeight.SemiBold,
//                            fontSize = 14.sp,
//                            textAlign = TextAlign.Center,
//                            modifier = Modifier.fillMaxWidth()
//                        )
//                    }
//
//                    Spacer(modifier = Modifier.height(20.dp))
//
//                    OutlinedTextField(
//                        value = brandName,
//                        onValueChange = { viewModel.onBrandNameChanged(it) },
//                        label = { Text("Nama Brand") },
//                        singleLine = true,
//                        modifier = Modifier.fillMaxWidth()
//                    )
//
//                    Spacer(modifier = Modifier.height(12.dp))
//
//                    OutlinedTextField(
//                        value = brandDescription,
//                        onValueChange = { viewModel.onBrandDescriptionChanged(it) },
//                        label = { Text("Deskripsi Brand") },
//                        modifier = Modifier.fillMaxWidth(),
//                        maxLines = 5
//                    )
//
//                    Spacer(modifier = Modifier.height(20.dp))
//
//                    Button(
//                        onClick = {
//                            if (!token.isNullOrBlank()) {
//                                viewModel.createBrand(token)
//                            } else {
//                                Toast.makeText(context, "Token tidak tersedia", Toast.LENGTH_SHORT).show()
//                            }
//                        },
//                        enabled = !isLoading,
//                        modifier = Modifier.fillMaxWidth(),
//                        colors = ButtonDefaults.buttonColors(
//                            containerColor = pinkColor,
//                            contentColor = Color.White
//                        )
//                    ) {
//                        Text("Buat Brand", fontWeight = FontWeight.Bold)
//                    }
//
//                    if (isLoading) {
//                        Spacer(modifier = Modifier.height(16.dp))
//                        CircularProgressIndicator(color = pinkColor)
//                    }
//                }
//            }
//        }
//    }
//}