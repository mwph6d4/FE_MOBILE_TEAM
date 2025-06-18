package com.example.accesorismvvm.navigasi

sealed class Screen(val route: String) {
    object Register : Screen("register")
    object Login : Screen("login")
    object main : Screen("main")
    object addProduct : Screen("addProduct")// optional
}
