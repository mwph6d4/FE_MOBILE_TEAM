package com.example.accesorismvvm.ui.Pesanan

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

@Composable
fun PesananScreen(navController: NavController) {
    val tabs = listOf("Belum Bayar", "Dikemas", "Dikirim", "Selesai")
    var selectedTabIndex by remember { mutableStateOf(0) }

    val activeColor = Color(0xFFFF1BA7)
    val backgroundColor = Color.White // warna latar yang seragam

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundColor)
    ) {
        // Judul
        Text(
            text = "Pesanan Saya",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = activeColor,
            modifier = Modifier
                .padding(start = 20.dp, top = 24.dp, bottom = 12.dp)
        )

        // Tab
        ScrollableTabRow(
            selectedTabIndex = selectedTabIndex,
            modifier = Modifier
                .fillMaxWidth()
                .background(backgroundColor), // pastikan tab row juga pakai bg sama
            edgePadding = 16.dp,
            contentColor = activeColor,
            indicator = { tabPositions ->
                TabRowDefaults.Indicator(
                    Modifier.tabIndicatorOffset(tabPositions[selectedTabIndex]),
                    color = activeColor,
                    height = 3.dp
                )
            },
            containerColor = backgroundColor // warna latar tab
        ) {
            tabs.forEachIndexed { index, title ->
                Tab(
                    selected = selectedTabIndex == index,
                    onClick = { selectedTabIndex = index },
                    selectedContentColor = activeColor,
                    unselectedContentColor = Color.Gray,
                    text = { Text(text = title) }
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        when (selectedTabIndex) {
            0 -> TabContent("Belum Bayar", backgroundColor)
            1 -> TabContent("Dikemas", backgroundColor)
            2 -> TabContent("Dikirim", backgroundColor)
            3 -> TabContent("Selesai", backgroundColor)
        }
    }
}

@Composable
fun TabContent(kategori: String, backgroundColor: Color) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundColor) // pastikan konten juga punya bg yang sama
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "Menampilkan produk kategori: $kategori",
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium
        )
    }
}
