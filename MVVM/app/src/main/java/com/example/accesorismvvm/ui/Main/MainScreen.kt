package com.example.accesorismvvm.ui.Main

import android.net.http.SslCertificate.saveState
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialogDefaults.containerColor
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
//import androidx.savedstate.savedState
import com.example.accesorismvvm.ui.Home.HomeScreen
import com.example.accesorismvvm.ui.Keranjang.KeranjangScreen
import com.example.accesorismvvm.ui.Pesanan.PesananScreen
import com.example.accesorismvvm.ui.Profile.ProfileScreen
import com.example.accesorismvvm.navigasi.BottomNavItem
import com.example.accesorismvvm.ui.CreateBrand.CreateBrandScreen
import com.example.accesorismvvm.ui.Search.SearchScreen

@Composable
fun MainScreen(navController: NavHostController) {
    val bottomNavController = rememberNavController()
    val items = listOf(
        BottomNavItem.Home,
        BottomNavItem.Pesanan,
        BottomNavItem.Keranjang,
        BottomNavItem.Toko,
        BottomNavItem.Akun
    )

    Scaffold(
        bottomBar = {
            NavigationBar (
                containerColor = Color(0xFFF0F0F0)
            ){
                val navBackStackEntry by bottomNavController.currentBackStackEntryAsState()
                val currentRoute = navBackStackEntry?.destination?.route

                items.forEach { item ->
                    val selected = currentRoute == item.route

                    NavigationBarItem(
                        icon = {
                            Icon(
                                imageVector = item.icon,
                                contentDescription = item.label
                            )
                        },
                        label = { Text(item.label) },
                        selected = selected,
                        onClick = {
                            if (!selected) {
                                bottomNavController.navigate(item.route) {
                                    popUpTo(bottomNavController.graph.startDestinationRoute ?: "") {
                                        saveState = true
                                    }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            }
                        },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = item.color,           // <- pink
                            selectedTextColor = item.color,
                            indicatorColor = item.color.copy(alpha = 0.1f),
                            unselectedIconColor = Color.Gray,
                            unselectedTextColor = Color.Gray
                        )
                    )
                }
            }
        }
    ) { paddingValues ->
        NavHost(
            navController = bottomNavController,
            startDestination = BottomNavItem.Home.route,
            modifier = Modifier.padding(paddingValues)
        ) {
            composable(BottomNavItem.Home.route) {
                HomeScreen(navController = navController)
            }

            composable(BottomNavItem.Pesanan.route) {
                PesananScreen(navController = navController)
            }
            composable(BottomNavItem.Keranjang.route) {
                KeranjangScreen(navController = navController)
            }
            composable(BottomNavItem.Toko.route) {
                CreateBrandScreen(
                    navController = navController
                )
            }
            composable(BottomNavItem.Akun.route) {
                ProfileScreen(
                    navController = navController
                )
            }
        }
    }
}
