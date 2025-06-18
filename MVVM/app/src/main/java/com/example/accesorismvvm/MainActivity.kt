package com.example.accesorismvvm

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.example.accesorismvvm.data.Session.SessionManager
import com.example.accesorismvvm.ui.theme.AccesorisMVVMTheme
import dagger.hilt.android.AndroidEntryPoint
import com.example.accesorismvvm.navigasi.AppNavHost
//import com.example.accesorismvvm.ui.product.ProductScreen
import javax.inject.Inject

//import com.example.accesorismvvm.ui.product.ProductScreen
@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var sessionManager: SessionManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        setContent {
            AccesorisMVVMTheme {
                val navController = rememberNavController()

                // Cek apakah user sudah login
                val userId = sessionManager.getUserId()
                val startDestination = if (userId != -1) {
                    "main" // Langsung ke main screen kalau sudah login
                } else {
                    "login" // Kalau belum login
                }

                Box(modifier = Modifier.fillMaxSize()) {
                    AppNavHost(navController = navController, startDestination = startDestination)
                }
            }
        }
    }
}

