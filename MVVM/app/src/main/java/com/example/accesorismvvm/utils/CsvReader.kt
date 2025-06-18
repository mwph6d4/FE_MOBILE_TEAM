//package com.example.accesorismvvm.utils
//
//
//
//import com.example.accesorismvvm.data.entityDAO.ProductEntity
//import com.google.android.gms.analytics.ecommerce.Product
//import java.io.BufferedReader
//import java.io.InputStreamReader
//import android.content.Context
//
//object CsvReader {
//    fun readProductsFromCsv(context: Context, fileName: String): List<ProductEntity> {
//        val productList = mutableListOf<ProductEntity>()
//        try {
//            val inputStream = context.assets.open(fileName)
//            val reader = BufferedReader(InputStreamReader(inputStream))
//            reader.readLine() // Skip header row
//            var line: String?
//            while (reader.readLine().also { line = it } != null) {
//                // Perhatikan bahwa data CSV Anda mungkin memiliki koma di dalam link URL,
//                // sehingga split(",") sederhana mungkin tidak cukup.
//                // Untuk contoh ini, saya asumsikan koma hanya sebagai delimiter utama.
//                // Jika URL memiliki koma, Anda mungkin perlu solusi parsing CSV yang lebih robust,
//                // seperti library Apache Commons CSV atau manual parsing yang lebih cermat.
//                val tokens = line?.split(",")
//
//                // Harusnya ada 8 kolom sesuai header:
//                // Link Produk,Link Gambar,Nama Toko,Lokasi,Nama Produk,Harga,Rating,Review
//                if (tokens != null && tokens.size == 8) {
//                    try {
//                        val productLink = tokens[0].trim()
//                        val imageLink = tokens[1].trim()
//                        val storeName = tokens[2].trim()
//                        val location = tokens[3].trim()
//                        val productName = tokens[4].trim()
//
//                        // Parsing Harga: Menghilangkan "Rp " dan koma (jika ada)
//                        val priceString = tokens[5].trim()
//                        val price = priceString.replace("Rp ", "").replace(".", "").toLong() // Menghilangkan "Rp " dan titik ribuan
//
//                        val rating = tokens[6].trim().toDouble()
//                        val reviewCount = tokens[7].trim().toInt()
//
//                        productList.add(
//                            ProductEntity(
//                                productLink = productLink,
//                                imageLink = imageLink,
//                                storeName = storeName,
//                                location = location,
//                                productName = productName,
//                                price = price,
//                                rating = rating,
//                                reviewCount = reviewCount
//                            )
//                        )
//                    } catch (e: NumberFormatException) {
//                        // Log errors for invalid numbers
//                        System.err.println("Error parsing number in line: $line - ${e.message}")
//                    } catch (e: IndexOutOfBoundsException) {
//                        // Log errors if not enough columns are found
//                        System.err.println("Error parsing line: $line - Not enough columns: ${e.message}")
//                    }
//                } else {
//                    System.err.println("Skipping malformed line (wrong number of tokens): $line")
//                }
//            }
//            reader.close()
//        } catch (e: Exception) {
//            e.printStackTrace()
//        }
//        return productList
//    }
//}
