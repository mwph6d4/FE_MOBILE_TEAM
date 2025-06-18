package com.example.accesorismvvm.ui.Keranjang

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectVerticalDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.navigation.NavController
import com.example.accesorismvvm.R

@Composable
fun KeranjangScreen(
    navController: NavController,
    modifier: Modifier = Modifier
) {
    val PinkColor = Color(0xFFFF1BA7)
    val LightGray = Color(0xFFF6F6F6)
    var itemDrag by remember { mutableStateOf(0f) }

    ConstraintLayout(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        val (topBar, product, checkout) = createRefs()

        // Header
        Row(
            modifier = Modifier
                .padding(20.dp)
                .fillMaxWidth()
                .constrainAs(topBar) {
                    top.linkTo(parent.top)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                },
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = "Keranjang Saya",
                    color = PinkColor,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "4 item",
                    color = Color.Gray,
                    fontSize = 14.sp
                )
            }
//            Icon(imageVector = Icons.Default.ShoppingCart, contentDescription = null, tint = PinkColor)
        }

        // Product List
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .constrainAs(product) {
                    top.linkTo(topBar.bottom)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                }
        ) {
            val selectedItems = remember { mutableStateListOf<Int>() }

            val products = listOf(
                Triple("Wireless Controller", "$79.99", R.drawable.srichand),
                Triple("Sport Shoes", "$100.25", R.drawable.wardah),
                Triple("Nike Pant", "$49.99", R.drawable.wardah2),
                Triple("Polygon Gloves", "$36.55", R.drawable.wardah2)
            )

            products.forEachIndexed { index, (title, price, image) ->
                val isSelected = index in selectedItems

                Card(
                    shape = RoundedCornerShape(16.dp),
                    elevation = CardDefaults.cardElevation(0.dp),
                    colors = CardDefaults.cardColors(containerColor = LightGray),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 6.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Checkbox(
                            checked = isSelected,
                            onCheckedChange = {
                                if (it) selectedItems.add(index) else selectedItems.remove(index)
                            },
                            colors = CheckboxDefaults.colors(checkedColor = PinkColor)
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Image(
                            painter = painterResource(id = image),
                            contentDescription = null,
                            modifier = Modifier
                                .size(70.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .background(Color.White)
                        )
                        Spacer(modifier = Modifier.width(16.dp))
                        Column(
                            modifier = Modifier
                                .weight(1f)
                        ) {
                            Text(text = title, fontWeight = FontWeight.Bold, fontSize = 15.sp)
                            Spacer(modifier = Modifier.height(6.dp))
                            Row {
                                Text(
                                    text = price,
                                    color = PinkColor,
                                    fontWeight = FontWeight.SemiBold,
                                    fontSize = 14.sp
                                )
                                Text(text = "  x1", color = Color.Gray, fontSize = 13.sp)
                            }
                        }
                        IconButton(onClick = { /* handle delete */ }) {
                            Icon(Icons.Default.Delete, contentDescription = null, tint = Color.Red)
                        }
                    }
                }
            }
        }

//        // Checkout Section
//        Column(
//            modifier = Modifier
//                .fillMaxWidth()
//                .background(Color.White, RoundedCornerShape(topStart = 30.dp, topEnd = 30.dp))
//                .shadow(2.dp)
//                .padding(20.dp)
//                .constrainAs(checkout) {
//                    bottom.linkTo(parent.bottom)
//                    start.linkTo(parent.start)
//                    end.linkTo(parent.end)
//                }
//        ) {
//            Row(
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .padding(bottom = 12.dp),
//                horizontalArrangement = Arrangement.SpaceBetween,
//                verticalAlignment = Alignment.CenterVertically
//            ) {
//                Row(verticalAlignment = Alignment.CenterVertically) {
//                    Icon(Icons.Default.Build, contentDescription = null, tint = PinkColor)
//                    Spacer(modifier = Modifier.width(8.dp))
//                    Text("Add Voucher Code", color = PinkColor)
//                }
//                Icon(Icons.Default.KeyboardArrowRight, contentDescription = null, tint = PinkColor)
//            }
//
//            Divider()
//
//            Row(
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .padding(top = 20.dp),
//                horizontalArrangement = Arrangement.SpaceBetween,
//                verticalAlignment = Alignment.CenterVertically
//            ) {
//                Column {
//                    Text("Total", fontSize = 14.sp)
//                    Text(
//                        text = "$266.78",
//                        fontSize = 22.sp,
//                        fontWeight = FontWeight.Bold,
//                        color = PinkColor
//                    )
//                }
//                Button(
//                    onClick = { /* handle checkout */ },
//                    shape = RoundedCornerShape(15.dp),
//                    colors = ButtonDefaults.buttonColors(containerColor = PinkColor),
//                    modifier = Modifier
//                        .height(48.dp)
//                        .defaultMinSize(minWidth = 120.dp)
//                ) {
//                    Text("Check Out", color = Color.White, fontSize = 16.sp)
//                }
//            }
//        }
    }
}
