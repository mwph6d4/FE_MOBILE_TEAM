package com.example.accesorismvvm.navigasi

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Assignment
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.Store
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable

sealed class BottomNavItem(val route: String, val icon: ImageVector, val label: String, val color: Color) {
    object Home : BottomNavItem("home", Icons.Default.Home, "Beranda", Color(0xFFFF1BA7))
    object Pesanan : BottomNavItem("pesanan", Icons.Default.Assignment, "Pesanan", Color(0xFFFF1BA7))
    object Akun : BottomNavItem("akun", Icons.Default.AccountCircle, "Akun", Color(0xFFFF1BA7))
    object Keranjang : BottomNavItem("keranjang", Icons.Default.ShoppingCart, "Keranjang", Color(0xFFFF1BA7))
    object Toko  : BottomNavItem("toko", Icons.Default.Store, "Toko", Color(0xFFFF1BA7))
}

//@Composable
//fun AppNavHost(navController: NavHostController) {
//    NavHost(navController = navController, startDestination = "start") {
//        composable("start") { StartScreen(navController) }
//        composable("login") { LoginScreen(navController) }
//        composable("register") { RegisterScreen(navController) }
//        composable("main") { MainScreen(navController) }
//    }
//}
