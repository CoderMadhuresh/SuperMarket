package com.madhuresh.supermarket.bottomNavScreens

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.HorizontalPagerIndicator
import com.google.accompanist.pager.rememberPagerState
import com.madhuresh.supermarket.R
import com.madhuresh.supermarket.dataModel.Product
import com.madhuresh.supermarket.ui.theme.SuperMarketTheme
import com.madhuresh.supermarket.viewModel.HomeFilterProductViewModel
import com.madhuresh.supermarket.viewModel.HomeProductViewModel
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

@Composable
fun HomeScreen(navController: NavController){
    SuperMarketTheme {
        Surface(
            color = if (isSystemInDarkTheme()) Color.Black else Color.White,
            modifier = Modifier.fillMaxSize()
        ) {
            HomeScreenContent(navController)
        }
    }
}

@OptIn(ExperimentalPagerApi::class)
@Composable
fun HomeScreenContent(navController: NavController) {
    val textState = remember { mutableStateOf(TextFieldValue()) }
    val pagerState = rememberPagerState()
    val productViewModel: HomeProductViewModel = viewModel()
    val products by productViewModel.products.collectAsState()

    val homeFilterProductViewModel: HomeFilterProductViewModel = viewModel()
    val filteredProducts by homeFilterProductViewModel.products.collectAsState()

    val searchQuery = textState.value.text.lowercase()

    val imageList = listOf(
        R.drawable.image1,
        R.drawable.image2,
        R.drawable.image3
    )

    Column(modifier = Modifier
        .fillMaxSize()
        .padding(15.dp)
    ) {

        Column(modifier = Modifier
            .height(110.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()) {
                Image(
                    painter = painterResource(id = R.drawable.logo),
                    contentDescription = "Logo",
                    modifier = Modifier
                        .width(40.dp)
                        .height(40.dp)
                )
                Spacer(modifier = Modifier.padding(start = 20.dp))
                Text(
                    text = "Super Market",
                    color = if (isSystemInDarkTheme()) Color.White else Color.Black,
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp
                )
            }
            TextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(IntrinsicSize.Max)
                    .padding(top = 15.dp),
                shape = RoundedCornerShape(30.dp),
                value = textState.value,
                onValueChange = { textState.value = it },
                placeholder = { Text(text = "Search Store") },
                leadingIcon = { Icon(imageVector = Icons.Default.Search, contentDescription = null) },
                singleLine = true,
                maxLines = 1,
                colors = TextFieldDefaults.colors(
                    focusedTextColor = if(isSystemInDarkTheme()) Color.Black else Color.Black,
                    unfocusedTextColor = if(isSystemInDarkTheme()) Color.Black else Color.Black,
                    unfocusedLeadingIconColor = if(isSystemInDarkTheme()) Color.Black else Color.Black,
                    focusedLeadingIconColor = if(isSystemInDarkTheme()) Color.Black else Color.Black,
                    unfocusedPlaceholderColor = if(isSystemInDarkTheme()) Color.Black else Color.Black,
                    focusedPlaceholderColor = if(isSystemInDarkTheme()) Color.Black else Color.Black,
                    unfocusedContainerColor = if(isSystemInDarkTheme()) Color(243, 243, 243) else Color(243, 243, 243),
                    focusedContainerColor = if(isSystemInDarkTheme()) Color(243, 243, 243) else Color(243, 243, 243),
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent
                )
            )
        }

        Spacer(modifier = Modifier.padding(top = 5.dp))

        Column(modifier = Modifier
            .fillMaxHeight()
        ) {

            if(searchQuery.isEmpty()){
                Column(modifier = Modifier
                    .verticalScroll(rememberScrollState())
                ) {
                    Box(modifier = Modifier
                        .fillMaxWidth()
                    ) {
                        HorizontalPager(
                            count = imageList.size,
                            state = pagerState,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(200.dp)
                        ) { page ->
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .clip(RoundedCornerShape(16.dp))
                            ) {
                                Image(
                                    painter = painterResource(id = imageList[page]),
                                    contentDescription = "Slider Image",
                                    modifier = Modifier.fillMaxSize()
                                )
                            }
                        }
                        HorizontalPagerIndicator(
                            pagerState = pagerState,
                            modifier = Modifier
                                .align(Alignment.BottomCenter)
                                .padding(5.dp)
                        )
                    }

                    products.forEach { (category, productMap) ->
                        Text(
                            text = category,
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp,
                            modifier = Modifier.padding(top = 15.dp)
                        )

                        LazyRow(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 8.dp)
                        ) {
                            items(productMap.values.toList()) { product ->
                                ProductCard(product = product, navController)
                            }
                        }
                    }
                }
            }
            else {
                val filteredList = filteredProducts.filter { product ->
                    product.name.lowercase().contains(searchQuery) ||
                            product.desc.lowercase().contains(searchQuery)
                }

                if (filteredList.isNotEmpty()) {
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(2),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 8.dp)
                    ) {
                        items(filteredList) { product ->
                            ProductCard(product = product, navController)
                        }
                    }
                } else {
                    Text(
                        text = "No products found for \"$searchQuery\"",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(top = 16.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun ProductCard(product: Product, navController: NavController) {
    val painter = rememberAsyncImagePainter(model = product.image)

    Card(
        modifier = Modifier
            .padding(8.dp)
            .clickable {
                navController.navigate(
                    "productDetail/${product.desc}/${
                        URLEncoder.encode(
                            product.image,
                            StandardCharsets.UTF_8.name()
                        )
                    }/${product.ingredients}/${product.manufacturer}/${product.name}/${product.price}/${product.shelf}/${product.stock}/${product.unit}/${
                        URLEncoder.encode(
                            product.images.values.joinToString(","),
                            StandardCharsets.UTF_8.name()
                        )
                    }"
                )
            }
            .border(width = 2.dp, color = Color(78, 177, 118), shape = RoundedCornerShape(15.dp))
            .width(140.dp)
            .height(180.dp),
        shape = RoundedCornerShape(15.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.Transparent
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        ) {
            Image(
                painter = painter,
                contentDescription = product.name,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(80.dp)
                    .clip(RoundedCornerShape(8.dp))
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = product.name,
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp,
                modifier = Modifier.padding(start = 5.dp)
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "₹${product.price} | ${product.stock} left",
                fontSize = 14.sp,
                modifier = Modifier.padding(start = 5.dp)
            )
        }
    }
}