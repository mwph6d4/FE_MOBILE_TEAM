package com.example.accesorismvvm.ui.Register

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.accesorismvvm.R
import com.example.accesorismvvm.navigasi.Screen


@Composable
fun RegisterScreen(navController: NavController,viewModel: RegisterViewModel = hiltViewModel()) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(viewModel.message) {
        if (viewModel.message.isNotEmpty()) {
            Toast.makeText(context, viewModel.message, Toast.LENGTH_SHORT).show()
            if (viewModel.message == "Registrasi berhasil!") {
                navController.navigate(Screen.Login.route)
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 32.dp, vertical = 24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally

    ) {
        Image(
            painter = painterResource(id = R.drawable.logofix),
            contentDescription = "Login image",
            modifier = Modifier.size(170.dp)

        )
        Spacer(modifier = Modifier.height(24.dp))
        Text(
            text = "Selamat Datang",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(24.dp))

        OutlinedTextField(
            value = viewModel.username,
            onValueChange = { viewModel.username = it },
            label = { Text("Username") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = viewModel.email,
            onValueChange = { viewModel.email = it },
            label = { Text("Email") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = viewModel.password,
            onValueChange = { viewModel.password = it },
            label = { Text("Password") },
            modifier = Modifier.fillMaxWidth(),
            visualTransformation = PasswordVisualTransformation()
        )

        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = { viewModel.register()


                         },
            modifier = Modifier
                .fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFFFF1BA7),
                contentColor = Color.White

            )

        ) {
            Text("Register")
        }
        if (viewModel.message.isNotEmpty()) {
            Text(viewModel.message, color = Color.Red)
        }

        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "Sudah punya akun? Masuk",
            modifier = Modifier
                .clickable { navController.navigate(Screen.Login.route) }
                .padding(top = 16.dp),
            color = Color.Gray
        )
//



    }
}


//@Composable
//fun RegisterScreen(navController: NavController) {
//    var name by remember { mutableStateOf("") }
//    var email by remember { mutableStateOf("") }
//    var password by remember { mutableStateOf("") }
//    var isLoading by remember { mutableStateOf(false) }
//    var message by remember { mutableStateOf<String?>(null) }
//
//    val context = LocalContext.current
//    val coroutineScope = rememberCoroutineScope()
//
//    Column(
//        modifier = Modifier
//            .fillMaxSize()
//            .padding(horizontal = 32.dp, vertical = 24.dp),
//        verticalArrangement = Arrangement.Center,
//        horizontalAlignment = Alignment.CenterHorizontally
//    ) {
//        Image(
//            painter = painterResource(id = R.drawable.gambarlogin),
//            contentDescription = "Login image",
//            modifier = Modifier.size(270.dp)
//        )
//
//        Text(
//            text = "Selamat Datang",
//            fontSize = 28.sp,
//            fontWeight = FontWeight.Bold
//        )
//
//        Spacer(modifier = Modifier.height(24.dp))
//
//        OutlinedTextField(
//            value = name,
//            onValueChange = { name = it },
//            label = { Text("Nama Lengkap") },
//            modifier = Modifier.fillMaxWidth()
//        )
//
//        Spacer(modifier = Modifier.height(8.dp))
//
//        OutlinedTextField(
//            value = email,
//            onValueChange = { email = it },
//            label = { Text("Alamat Email") },
//            modifier = Modifier.fillMaxWidth()
//        )
//
//        Spacer(modifier = Modifier.height(8.dp))
//
//        OutlinedTextField(
//            value = password,
//            onValueChange = { password = it },
//            label = { Text("Kata Sandi") },
//            modifier = Modifier.fillMaxWidth(),
//            visualTransformation = PasswordVisualTransformation()
//        )
//
//        Spacer(modifier = Modifier.height(16.dp))
//
//        Button(
//            onClick = {
//                coroutineScope.launch {
//                    isLoading = true
//
//                    val request = RegisterRequest(name,email, password) // Sesuaikan dengan server (tidak ada "name")
//                    try {
//                        val response = RetrofitInstance.api.register(request)
//                        if (response.isSuccessful) {
//                            message = "Pendaftaran berhasil. Silakan login."
//                            navController.navigate("login")
//                        } else {
//                            message = "Pendaftaran gagal: ${response.errorBody()?.string()}"
//                        }
//                    } catch (e: Exception) {
//                        message = "Terjadi kesalahan: ${e.localizedMessage}"
//                    } finally {
//                        isLoading = false
//                    }
//                }
//                // Logika untuk menangani pendaftaran
//            },
//            modifier = Modifier.fillMaxWidth(),
//            colors = ButtonDefaults.buttonColors(
//                containerColor = Color(0xFFFF1BA7),
//                contentColor = Color.White
//            )
//        ) {
//            Text(if (isLoading) "Memproses..." else "Daftar")
//        }
//
//        Spacer(modifier = Modifier.height(16.dp))
//
//        message?.let {
//            Text(
//                text = it,
//                color = if (it.contains("berhasil", ignoreCase = true)) Color.Green else Color.Red,
//                modifier = Modifier.padding(8.dp)
//            )
//        }
//
//        Text(
//            text = "Sudah punya akun? Masuk",
//            modifier = Modifier
//                .clickable { navController.navigate("login") }
//                .padding(top = 16.dp),
//            color = Color.Gray
//        )
//    }
//
//
//}
//
//@Preview(showBackground = true)
//@Composable
//fun RegisterScreenPreview() {
//// Preview komponen RegisterScreen
//    RegisterScreen(navController = rememberNavController())
//}
