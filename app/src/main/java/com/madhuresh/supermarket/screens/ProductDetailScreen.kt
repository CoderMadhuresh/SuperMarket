package com.madhuresh.supermarket.screens

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.HorizontalPagerIndicator
import com.google.accompanist.pager.rememberPagerState
import com.madhuresh.supermarket.viewModel.ProductViewModel

@OptIn(ExperimentalPagerApi::class)
@Composable
fun ProductDetailScreenContent(desc: String, image: String ,ingredients: String, manufacturer: String, name: String, price: Int, shelf: String, stock: Int, unit: String, images: List<String>, productViewModel: ProductViewModel = viewModel()
) {
    val pagerState = rememberPagerState()
    val scrollState = rememberScrollState()
    val addedState by remember { mutableStateOf(productViewModel.addedState) }
    val isAdded = addedState[name] ?: false

    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .weight(1f)
                .verticalScroll(scrollState)
                .padding(top = 16.dp, start = 16.dp, end = 16.dp)
        ) {
            Text(
                text = name,
                fontSize = 20.sp,
                color = if(isSystemInDarkTheme()) Color(78, 177, 118) else Color(78, 177, 118),
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            HorizontalPager(
                count = images.size,
                state = pagerState,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .clip(RoundedCornerShape(8.dp))
            ) { page ->
                AsyncImage(
                    model = images[page],
                    contentDescription = null,
                    contentScale = ContentScale.Fit,
                    modifier = Modifier.fillMaxSize()
                )
            }

            HorizontalPagerIndicator(
                pagerState = pagerState,
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(vertical = 8.dp),
                activeColor = Color.Black,
                inactiveColor = Color.Gray
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Price: ₹ $price",
                fontSize = 20.sp,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp)
            ) {
                Text(
                    text = "Stock: $stock",
                    fontSize = 16.sp,
                )
                Spacer(modifier = Modifier.padding(start = 16.dp))
                Text(
                    text = "Unit: $unit",
                    fontSize = 16.sp,
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Description",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .padding(bottom = 8.dp)
                    .fillMaxWidth()
            )
            Text(
                text = desc,
                fontSize = 14.sp,
                textAlign = TextAlign.Justify,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            Text(
                text = "Ingredients",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            Text(
                text = ingredients,
                fontSize = 14.sp,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            Text(
                text = "Manufacturer",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            Text(
                text = manufacturer,
                fontSize = 14.sp,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            Text(
                text = "Shelf Life",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            Text(
                text = shelf,
                fontSize = 14.sp,
                modifier = Modifier.padding(bottom = 16.dp)
            )

        }
        Button(
            onClick = {
                productViewModel.addToCart(
                    com.madhuresh.supermarket.dataModel.Product(
                        name = name,
                        desc = desc,
                        image = image,
                        ingredients = ingredients,
                        manufacturer = manufacturer,
                        price = price,
                        shelf = shelf,
                        stock = stock,
                        unit = unit,
                        images = images.associateWith { "" }
                    )
                )
            },
            colors = ButtonDefaults.buttonColors(
                containerColor = if (isSystemInDarkTheme()) Color(78, 177, 118) else Color(78, 177, 118)
            ),
            modifier = Modifier
                .fillMaxWidth()
                .height(80.dp)
                .padding(16.dp)
                .clip(RoundedCornerShape(8.dp))

        ) {
            Icon(
                Icons.Default.ShoppingCart,
                contentDescription = null,
                tint =  if (isSystemInDarkTheme()) Color.White else Color.White,
                modifier = Modifier.size(20.dp))
            Spacer(modifier = Modifier.padding(start = 10.dp))
            Text(
                text = if (isAdded) "Item Added" else "Add To Cart",
                fontWeight = FontWeight.SemiBold,
                fontSize = 18.sp,
                color = if (isSystemInDarkTheme()) Color.White else Color.White
            )
        }
    }
}