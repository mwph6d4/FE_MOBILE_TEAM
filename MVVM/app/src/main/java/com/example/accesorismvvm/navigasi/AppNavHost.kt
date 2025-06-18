package com.example.accesorismvvm.navigasi

import ProductDetailScreen
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.accesorismvvm.data.Session.SessionManager
import com.example.accesorismvvm.ui.CreateBrand.CreateBrandScreen
import com.example.accesorismvvm.ui.CreateProduct.CreateProductScreen
import com.example.accesorismvvm.ui.Login.LoginScreen
import com.example.accesorismvvm.ui.Main.MainScreen
import com.example.accesorismvvm.ui.Register.RegisterScreen
import com.example.accesorismvvm.ui.Search.SearchScreen

@Composable
fun AppNavHost(
    navController: NavHostController,
    startDestination: String
) {
    val context = LocalContext.current
    val sessionManager = remember { SessionManager(context) } // Ambil instance SessionManager
    val token = remember { sessionManager.getToken() } ?: ""  // Ambil token dari SharedPreferences

    NavHost(navController = navController, startDestination = startDestination) {
        composable(Screen.Register.route) { RegisterScreen(navController) }
        composable(Screen.Login.route) { LoginScreen(navController) }
        composable(Screen.main.route) { MainScreen(navController) }
        composable(Screen.addProduct.route) { CreateProductScreen(navController) }
//        composable("product_detail/{productId}") { backStackEntry ->
//            val productId = backStackEntry.arguments?.getString("productId")?.toIntOrNull() ?: return@composable
//
//            ProductDetailScreen(
//                productId = productId,
//                token = token, // Pakai token dari sessionManager
//                onBack = { navController.popBackStack() }
//            )
//        }

        composable("product_detail/{productId}") { backStackEntry ->
            val context = LocalContext.current
            val sessionManager = remember { SessionManager(context) }
            val token = sessionManager.getToken()
            val productId = backStackEntry.arguments?.getString("productId")?.toIntOrNull() ?: return@composable

            ProductDetailScreen(
                productId = productId,
                token = token ?: "",
                onBack = { navController.popBackStack() }
            )
        }


        composable("search_screen") {
            SearchScreen(navController = navController)
        }

    }
}
