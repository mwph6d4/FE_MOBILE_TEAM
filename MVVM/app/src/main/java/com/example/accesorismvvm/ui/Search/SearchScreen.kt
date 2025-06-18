package com.example.accesorismvvm.ui.Search

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Sort
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.accesorismvvm.ui.Home.ProductCardShopee
import com.example.accesorismvvm.ui.product.ProductViewModel
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(
    viewModel: SearchViewModel = hiltViewModel(),
    navController: NavController
) {
    val searchQuery = viewModel.query
    val searchResults = viewModel.searchResults
    val isLoading = viewModel.isLoading
    val isError = viewModel.isError

    val DarkText = Color.Black
    val LightGray = Color(0xFFE0E0E0)

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Pencarian Produk",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Normal,
                        color = DarkText
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Kembali", tint = DarkText)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.White
                )
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp)
                .background(Color.White)
        ) {
            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = searchQuery,
                onValueChange = {
                    viewModel.onQueryChange(it)
                    viewModel.performSearch()
                },
                placeholder = { Text("Cari produk...") },
                modifier = Modifier.fillMaxWidth(),
                leadingIcon = {
                    Icon(Icons.Default.Search, contentDescription = "Search", tint = DarkText)
                },
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = DarkText,
                    unfocusedBorderColor = LightGray,
                    cursorColor = DarkText
                )
            )

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    "Hasil pencarian:",
                    fontSize = 16.sp,
                    color = DarkText
                )

                DropdownMenuSort(
                    selected = viewModel.sortOrder,
                    onSortSelected = { viewModel.onSortOrderChange(it) },
                    textColor = DarkText,
                    borderColor = LightGray
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            when {
                isLoading -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator(color = DarkText)
                    }
                }

                isError -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text("Gagal memuat hasil. Coba lagi.", color = DarkText)
                    }
                }

                searchResults.isEmpty() -> {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(top = 32.dp),
                        contentAlignment = Alignment.TopCenter
                    ) {
                        Text(
                            text = "Tidak ada produk ditemukan.",
                            style = MaterialTheme.typography.bodyMedium,
                            textAlign = TextAlign.Center,
                            color = DarkText
                        )
                    }
                }

                else -> {
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(2),
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(bottom = 16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(searchResults) { product ->
                            ProductCardShopee(
                                product = product,
                                navController = navController,
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                    }
                }
            }
        }
    }
}
@Composable
fun DropdownMenuSort(
    selected: String,
    onSortSelected: (String) -> Unit,
    textColor: Color,
    borderColor: Color
) {
    var expanded by remember { mutableStateOf(false) }
    val options = listOf("asc", "desc")

    Box {
        OutlinedButton(
            onClick = { expanded = true },
            border = BorderStroke(1.dp, borderColor),
            colors = ButtonDefaults.outlinedButtonColors(
                contentColor = textColor
            )
        ) {
            Icon(Icons.Default.Sort, contentDescription = null)
            Spacer(modifier = Modifier.width(4.dp))
            Text(if (selected == "asc") "Termurah" else "Termahal")
            Icon(Icons.Default.ArrowDropDown, contentDescription = null)
        }

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            options.forEach { option ->
                DropdownMenuItem(
                    text = { Text(if (option == "asc") "Termurah" else "Termahal") },
                    onClick = {
                        onSortSelected(option)
                        expanded = false
                    }
                )
            }
        }
    }
}



//
//@OptIn(ExperimentalMaterial3Api::class)
//@Composable
//fun SearchScreen(
//    viewModel: ProductViewModel = hiltViewModel(),
//    navController: NavController
//) {
//    val searchQuery by viewModel.searchQuery.collectAsState()
//    val productList by viewModel.filteredProducts.collectAsState()
//    val priceFilter by viewModel.priceFilter.collectAsState()

//    Scaffold(
//        topBar = {
//            TopAppBar(
//                title = { Text("Pencarian Produk") },
//                navigationIcon = {
//                    IconButton(onClick = { navController.popBackStack() }) {
//                        Icon(Icons.Default.ArrowBack, contentDescription = "Kembali")
//                    }
//                }
//            )
//        }
//    ) { innerPadding ->
//        Column(
//            modifier = Modifier
//                .fillMaxSize()
//                .padding(innerPadding)
//        ) {
//            TextField(
//                value = searchQuery,
//                onValueChange = { viewModel.onSearchQueryChanged(it) },
//                placeholder = { Text("Cari produk...") },
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .padding(16.dp)
//            )
//
//            // 🔽 Baris filter
//            FilterRow(
//                selected = priceFilter,
//                onSelect = { viewModel.setPriceFilter(it) }
//            )
//
//            Spacer(modifier = Modifier.height(8.dp))
//
//            // 🚫 Jika hasil kosong
//            if (productList.isEmpty()) {
//                Box(
//                    modifier = Modifier
//                        .fillMaxSize()
//                        .padding(top = 32.dp),
//                    contentAlignment = Alignment.TopCenter
//                ) {
//                    Text(
//                        text = "Tidak ada produk ditemukan.",
//                        style = MaterialTheme.typography.bodyMedium,
//                        textAlign = TextAlign.Center
//                    )
//                }
//            } else {
//                // ✅ Produk dalam grid 2 kolom
//                LazyVerticalGrid(
//                    columns = GridCells.Fixed(2),
//                    modifier = Modifier.fillMaxSize(),
//                    contentPadding = PaddingValues(8.dp),
//                    verticalArrangement = Arrangement.spacedBy(8.dp),
//                    horizontalArrangement = Arrangement.spacedBy(8.dp)
//                ) {
//                    items(productList) { product ->
//                        ProductCardShopee(
//                            product = product,
//                            navController = navController,
//                            modifier = Modifier.fillMaxWidth()
//                        )
//                    }
//                }
//            }
//        }
//    }
//}

//@Composable
//fun FilterRow(
//    selected: ProductViewModel.PriceFilter,
//    onSelect: (ProductViewModel.PriceFilter) -> Unit
//) {
//    Row(
//        modifier = Modifier
//            .fillMaxWidth()
//            .padding(horizontal = 16.dp),
//        horizontalArrangement = Arrangement.spacedBy(8.dp)
//    ) {
//        FilterChip("Termurah", selected == ProductViewModel.PriceFilter.TERMURAH) {
//            onSelect(ProductViewModel.PriceFilter.TERMURAH)
//        }
//        FilterChip("Termahal", selected == ProductViewModel.PriceFilter.TERMAHAL) {
//            onSelect(ProductViewModel.PriceFilter.TERMAHAL)
//        }
//        FilterChip("Reset", selected == ProductViewModel.PriceFilter.NONE) {
//            onSelect(ProductViewModel.PriceFilter.NONE)
//        }
//    }
//}

//@Composable
//fun FilterChip(text: String, selected: Boolean, onClick: () -> Unit) {
//    AssistChip(
//        onClick = onClick,
//        label = { Text(text) },
//        colors = AssistChipDefaults.assistChipColors(
//            containerColor = if (selected) Color(0xFFFF1BA7) else MaterialTheme.colorScheme.surfaceVariant,
//            labelColor = if (selected) Color.White else MaterialTheme.colorScheme.onSurface)
//    )
//}
