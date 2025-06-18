package com.example.accesorismvvm.ui.CreateProduct

import android.Manifest
import android.net.Uri
import android.content.Context
import android.os.Build
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
//import androidx.databinding.tool.Context
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.accesorismvvm.data.Session.SessionManager
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import androidx.compose.ui.platform.LocalContext


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateProductScreen (
    navController: NavController,
    viewModel: CreateProductViewModel = hiltViewModel()
) {
    val productName by viewModel.productName.collectAsState()
    val productDescription by viewModel.productDescription.collectAsState()
    val productPrice by viewModel.productPrice.collectAsState()
    val productStock by viewModel.productStock.collectAsState()
    val selectedImageUri by viewModel.selectedImageUri.collectAsState() // Observe URI gambar
    val isLoading by viewModel.isLoading.collectAsState()
    val message by viewModel.message.collectAsState()
    val context = LocalContext.current

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (!isGranted) {
            Toast.makeText(context, "Izin akses media diperlukan", Toast.LENGTH_SHORT).show()
        }
    }

    LaunchedEffect(Unit) {
        val permission = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            Manifest.permission.READ_MEDIA_IMAGES
        } else {
            Manifest.permission.READ_EXTERNAL_STORAGE
        }

        permissionLauncher.launch(permission)
    }


    // Launcher untuk memilih gambar dari galeri
    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        viewModel.onImageSelected(uri)
    }

    LaunchedEffect(message) {
        message?.let {
            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
            if (it.contains("berhasil", ignoreCase = true)) {
                navController.popBackStack()
            }
            viewModel.clearMessage()
        }
    }

    Scaffold(
        topBar = { TopAppBar(title = { Text("Tambah Produk Baru") }) }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .padding(16.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            OutlinedTextField(
                value = productName,
                onValueChange = { viewModel.onProductNameChanged(it) },
                label = { Text("Nama Produk") },
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = productDescription,
                onValueChange = { viewModel.onProductDescriptionChanged(it) },
                label = { Text("Deskripsi Produk") },
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = productPrice,
                onValueChange = { viewModel.onProductPriceChanged(it) },
                label = { Text("Harga") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = productStock,
                onValueChange = { viewModel.onProductStockChanged(it) },
                label = { Text("Stok") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth()
            )

            // UI untuk pemilihan gambar
            Spacer(Modifier.height(8.dp))
            Text("Gambar Produk Utama:", style = MaterialTheme.typography.bodyLarge)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .border(1.dp, Color.Gray, RoundedCornerShape(8.dp))
                    .clip(RoundedCornerShape(8.dp))
                    .clickable { imagePickerLauncher.launch("image/*") }, // Membuka galeri
                contentAlignment = Alignment.Center
            ) {
                if (selectedImageUri != null) {
                    Image(
                        painter = rememberAsyncImagePainter(selectedImageUri),
                        contentDescription = "Selected Product Image",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                } else {
                    Text("Ketuk untuk memilih gambar", color = Color.Gray)
                }
            }
            Spacer(Modifier.height(16.dp))

            Button(
                onClick = {
                    val token = SessionManager(context).getToken()
                    val imageFile = selectedImageUri?.let { uri ->
                        // Konversi Uri ke File (ini perlu fungsi helper)
                        uriToFile(context, uri)
                    }

                    if (!token.isNullOrBlank()) {
                        viewModel.createProduct(token, imageFile)
                    } else {
                        Toast.makeText(context, "Sesi tidak valid, silakan login ulang.", Toast.LENGTH_SHORT).show()
                    }
                },
                enabled = !isLoading && selectedImageUri != null, // Button hanya aktif jika gambar sudah dipilih
                modifier = Modifier.fillMaxWidth()
            ) {
                if (isLoading) {
                    CircularProgressIndicator(color = MaterialTheme.colorScheme.onPrimary)
                } else {
                    Text("Simpan Produk")
                }
            }
        }
    }
}

// Fungsi Helper untuk mengonversi URI menjadi File sementara
// Ini diperlukan karena Retrofit Multipart memerlukan objek File atau RequestBody dari Stream.
// Simpan ini di file utilitas, atau di dalam AddProductScreen Composable jika Anda mau.
fun uriToFile(context: Context, uri: Uri): File? {
    val contentResolver = context.contentResolver
    val fileName = "temp_image_${System.currentTimeMillis()}"
    val tempFile = File(context.cacheDir, fileName)

    try {
        val inputStream: InputStream? = contentResolver.openInputStream(uri)
        val outputStream: FileOutputStream? = FileOutputStream(tempFile)

        inputStream?.use { input ->
            outputStream?.use { output ->
                input.copyTo(output)
            }
        }
        return tempFile
    } catch (e: Exception) {
        e.printStackTrace()
        Toast.makeText(context, "Gagal mengonversi gambar: ${e.message}", Toast.LENGTH_LONG).show()
        return null
    }
}