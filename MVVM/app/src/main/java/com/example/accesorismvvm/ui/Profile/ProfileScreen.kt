package com.example.accesorismvvm.ui.Profile

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
//import SessionManager
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
//import coil.compose.AsyncImage
import com.example.accesorismvvm.R
//import model.Profile
//import com.example.accessoriesbox.uriToFile
//import okhttp3.MediaType.Companion.toMediaTypeOrNull
//import okhttp3.MultipartBody
//import okhttp3.RequestBody.Companion.asRequestBody

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    navController: NavController,
    viewModel: ProfileViewModel = hiltViewModel(),

) {
    // Collect states dari ViewModel
    val userProfile by viewModel.userProfile.collectAsState() // Menggunakan userProfile (domain model)
    val usernameInput by viewModel.usernameInput.collectAsState()
    val editMode by viewModel.editMode.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val statusMessage by viewModel.statusMessage.collectAsState()
    val logoutEvent by viewModel.logoutEvent.collectAsState() // Menggunakan StateFlow

    val PinkColor = Color(0xFFFF1BA7)

    LaunchedEffect(userProfile) {
        Log.d("ProfileUI", "Data masuk ke UI: ${userProfile?.email} - ${userProfile?.username}")
    }


    LaunchedEffect(logoutEvent) {
        if (logoutEvent) {
            navController.navigate("login") {
                popUpTo("akun") { inclusive = true } // Pastikan "akun" adalah rute awal yang ingin di-pop
            }
            viewModel.clearLogoutEvent()
        }
    }

    Scaffold(
        containerColor = Color.White,
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "Profil Saya",
                        color = PinkColor,
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold
                    )
                },
                actions = {
                    if (editMode) {
                        // Tombol Save saat di mode edit
                        IconButton(onClick = { viewModel.saveUsername() }, enabled = !isLoading) {
                            Icon(Icons.Default.Save, contentDescription = "Simpan", tint = PinkColor)
                        }
                    } else {
                        // Tombol Edit saat tidak di mode edit
                        IconButton(onClick = { viewModel.toggleEditMode() }) {
                            Icon(Icons.Default.Edit, contentDescription = "Edit Profil", tint = PinkColor)
                        }
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = Color.White // Ganti ke warna yang kamu mau
                )
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .background(Color.White)
                .padding(horizontal = 16.dp, vertical = 8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Indikator loading
            if (isLoading) {
                LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
            }

            // Pesan status (sukses/error)
            statusMessage?.let { message ->
                Text(
                    text = message,
                    color = if (message.contains("Error") || message.contains("Gagal")) MaterialTheme.colorScheme.error else PinkColor,
                    modifier = Modifier.padding(vertical = 8.dp),
                    fontSize = 14.sp
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Box untuk foto profil (belum ada fungsi upload, hanya visual)
            Box(
                modifier = Modifier
                    .size(120.dp)
                    .clip(CircleShape)
                    .background(Color.LightGray.copy(alpha = 0.5f))
                    .clickable { /* TODO: Implementasi ganti foto profil */ },
                contentAlignment = Alignment.Center
            ) {
                // Contoh Icon default, ganti dengan Image jika punya URL foto profil
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = "Foto Profil",
                    modifier = Modifier.size(72.dp),
                    tint = Color.Gray
                )
//                Icon(
//                    painter = painterResource(id = R.drawable.camera), // Gunakan ikon kamera Anda
//                    contentDescription = "Ganti Foto",
//                    tint = PinkColor,
//                    modifier = Modifier
//                        .size(36.dp)
//                        .align(Alignment.BottomEnd)
//                        .offset(x = 8.dp, y = 8.dp) // Sesuaikan posisi ikon kamera
//                        .background(MaterialTheme.colorScheme.surface, shape = CircleShape)
//                        .padding(4.dp)
//                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Informasi Profil: Username (bisa diedit)
            // Menggunakan ProfileEditableInfoItem
            ProfileEditableInfoItem(
                label = "Username",
                value = usernameInput,
                onValueChange = { viewModel.onUsernameInputChange(it) },
                iconResId = R.drawable.user, // Ganti dengan ikon user Anda
                isEditable = editMode
            )

            // Informasi Profil: Email (tidak bisa diedit)
            userProfile?.email?.let { email ->
                ProfileInfoItem(
                    label = "Email",
                    value = email,
                    iconResId = R.drawable.email // Ganti dengan ikon email Anda
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Tombol Logout
            Button(
                onClick = { viewModel.logout() },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 32.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = PinkColor,
                    contentColor = Color.White
                )
            ) {
                Text("Logout", fontSize = 16.sp, fontWeight = FontWeight.Bold)
            }
        }
    }
}

// --- Komposable Baru untuk Item Informasi yang Bisa Diedit ---
@Composable
fun ProfileEditableInfoItem(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    iconResId: Int,
    isEditable: Boolean,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                painter = painterResource(id = iconResId),
                contentDescription = label,
                tint = Color(0xFFFF1BA7),
                modifier = Modifier
                    .size(40.dp)
                    .padding(end = 16.dp)
            )
            Column {
                Text(
                    text = label,
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                )
                if (isEditable) {
                    // TextField untuk mode edit
                    OutlinedTextField(
                        value = value,
                        onValueChange = onValueChange,
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Color(0xFFFF1BA7),
                            unfocusedBorderColor = Color.LightGray,
                            focusedLabelColor = Color(0xFFFF1BA7),
                            unfocusedLabelColor = MaterialTheme.colorScheme.onSurfaceVariant // Opsional: Warna label saat tidak fokus
                            // Anda bisa menambahkan warna lain seperti textColor, cursorColor, dll.
                        ),
                        // --- AKHIR PERBAIKAN ---
                        textStyle = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Medium)
                    )
                } else {
                    // Text untuk mode lihat
                    Text(
                        text = value,
                        style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Medium)
                    )
                }
            }
        }
    }
}


// --- ProfileInfoItem (untuk informasi yang tidak bisa diedit) ---
@Composable
fun ProfileInfoItem(
    label: String,
    value: String,
    iconResId: Int,
    modifier: Modifier = Modifier
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        Icon(
            painter = painterResource(id = iconResId),
            contentDescription = label,
            tint = Color(0xFFFF1BA7),
            modifier = Modifier
                .size(40.dp)
                .padding(end = 16.dp)
        )
        Column {
            Text(
                text = label,
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
            )
            Text(
                text = value,
                style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Medium)
            )
        }
    }
}

//@OptIn(ExperimentalMaterial3Api::class)
//@Composable
//fun ProfileScreen(
//    viewModel: ProfileViewModel = hiltViewModel(),
//    navController: NavController,
//    modifier: Modifier = Modifier,
//    logout: () -> Unit = {}  // Optional, karena ViewModel handle logout
//) {
//    val user by viewModel.user
//    val logoutEvent by viewModel.logoutEvent
//    val PinkColor = Color(0xFFFF1BA7)
//
//    LaunchedEffect(logoutEvent) {
//        if (logoutEvent) {
//            navController.navigate("login") {
//                popUpTo("akun") { inclusive = true }
//            }
//            viewModel.clearLogoutEvent()
//        }
//    }
//
//    Scaffold(
//        topBar = {
//            CenterAlignedTopAppBar(
//                title = {
//                    Text(
//                        text = "Profil Saya",
//                        color = PinkColor,
//                        fontSize = 22.sp,
//                        fontWeight = FontWeight.Bold
//                    )
//                }
//            )
//        }
//    ) { padding ->
//        Column(
//            modifier = modifier
//                .padding(padding)
//                .fillMaxSize()
//                .padding(16.dp),
//            horizontalAlignment = Alignment.CenterHorizontally
//        ) {
//            Box(
//                modifier = Modifier
//                    .padding(16.dp)
//                    .align(Alignment.CenterHorizontally)
//            ) {
//                Icon(
//                    painter = painterResource(id = R.drawable.camera),
//                    contentDescription = "Change Picture",
//                    tint = PinkColor,
//                    modifier = Modifier
//                        .size(36.dp)
//                        .align(Alignment.BottomEnd)
//                        .background(MaterialTheme.colorScheme.background, shape = CircleShape)
//                        .padding(4.dp)
//                )
//            }
//
//            user?.let {
//                ProfileInfoItem("Name", it.username, R.drawable.user)
//                ProfileInfoItem("Email", it.email, R.drawable.email)
//            } ?: run {
//                Text("Sedang memuat data pengguna...", color = Color.Gray)
//            }
//
//            Spacer(modifier = Modifier.height(24.dp))
//
//            Button(
//                onClick = { viewModel.logout() },
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .padding(32.dp),
//                colors = ButtonDefaults.buttonColors(
//                    containerColor = PinkColor,
//                    contentColor = Color.White
//                )
//            ) {
//                Text("Logout")
//            }
//        }
//    }
//}
//
//@Composable
//fun ProfileInfoItem(
//    label: String,
//    value: String,
//    iconResId: Int,
//    modifier: Modifier = Modifier
//) {
//    Row(
//        verticalAlignment = Alignment.CenterVertically,
//        modifier = modifier
//            .fillMaxWidth()
//            .padding(vertical = 8.dp)
//    ) {
//        Icon(
//            painter = painterResource(id = iconResId),
//            contentDescription = label,
//            tint = Color(0xFFFF1BA7),
//            modifier = Modifier
//                .size(40.dp)
//                .padding(end = 16.dp)
//        )
//        Column {
//            Text(
//                text = label,
//                style = MaterialTheme.typography.labelSmall,
//                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
//            )
//            Text(
//                text = value,
//                style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Medium)
//            )
//        }
//    }
//}
//
