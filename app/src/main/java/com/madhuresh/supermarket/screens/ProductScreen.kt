package com.madhuresh.supermarket.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.madhuresh.supermarket.dataModel.Product
import com.madhuresh.supermarket.viewModel.ProductViewModel
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

@Composable
fun ProductScreenContent(categoryName: String?, viewModel: ProductViewModel = viewModel(), navController: NavController) {
    val products by viewModel.products.collectAsState()
    val cartItems by remember { mutableStateOf(viewModel.cartItems) }
    val textState = remember { mutableStateOf(TextFieldValue()) }
    val searchText = textState.value.text
    val addedState by remember { mutableStateOf(viewModel.addedState) }

    LaunchedEffect(categoryName) {
        categoryName?.let {
            viewModel.fetchProductsByCategory(it)
        }
    }

    LaunchedEffect(cartItems) {
        viewModel.loadCartItems()
    }

    val filteredProducts = if (searchText.isBlank()) {
        products
    } else {
        products.filter { product ->
            product.name.contains(searchText, ignoreCase = true)
        }
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
            .padding(8.dp)
    ) {
        Text(
            text = "Find Products",
            fontSize = 20.sp,
            color = if (isSystemInDarkTheme()) Color.White else Color.Black,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.padding(top = 15.dp)
        )

        Spacer(modifier = Modifier.height(8.dp))

        TextField(
            modifier = Modifier
                .fillMaxWidth()
                .height(60.dp)
                .padding(start = 10.dp, end = 10.dp, bottom = 5.dp),
            shape = RoundedCornerShape(30.dp),
            value = textState.value,
            onValueChange = { textState.value = it },
            placeholder = { Text(text = "Search Products") },
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

        Spacer(modifier = Modifier.height(5.dp))

        if (products.isEmpty()) {
            Text(
                text = "Loading Products...",
                fontSize = 16.sp,
                color = if (isSystemInDarkTheme()) Color.White else Color.Black,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.padding(16.dp)
            )
        } else if (filteredProducts.isEmpty()) {
            Text(
                text = "No Products Found",
                fontSize = 16.sp,
                color = if (isSystemInDarkTheme()) Color.White else Color.Black,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.padding(16.dp)
            )
        } else {
            LazyVerticalGrid(
                columns = GridCells.Fixed(1),
                contentPadding = PaddingValues(8.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                items(filteredProducts) { product ->
                    ProductCard(
                        product = product,
                        onAddClick = {
                            viewModel.addToCart(product)
                        },
                        onClick = {
                            navController.navigate("productDetail/${product.desc}/${URLEncoder.encode(product.image, StandardCharsets.UTF_8.name())}/${product.ingredients}/${product.manufacturer}/${product.name}/${product.price}/${product.shelf}/${product.stock}/${product.unit}/${URLEncoder.encode(product.images.values.joinToString(","), StandardCharsets.UTF_8.name())}")
                        },
                        isAdded = addedState[product.name] ?: false
                    )
                }
            }
        }
    }
}

@Composable
fun ProductCard(
    product: Product,
    onAddClick: () -> Unit,
    onClick: () -> Unit,
    isAdded: Boolean
) {

    Card(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
            .clickable { onClick() }
            .height(IntrinsicSize.Max),
        shape = RoundedCornerShape(10.dp),
        border = BorderStroke(3.dp, if (isSystemInDarkTheme()) Color(78, 177, 118) else Color(78, 177, 118)),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isSystemInDarkTheme()) Color.White else Color.White
        )
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp)
        ) {
            androidx.compose.foundation.Image(
                painter = rememberAsyncImagePainter(product.image),
                contentDescription = product.name,
                modifier = Modifier
                    .size(130.dp, 160.dp)
                    .clip(RoundedCornerShape(10.dp)),
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.width(8.dp))

            Column(
                modifier = Modifier
                    .fillMaxHeight()
                    .weight(1f)
                    .padding(end = 8.dp)
            ) {
                Text(
                    text = product.name,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    color = if (isSystemInDarkTheme()) Color.Black else Color.Black
                )
                Text(
                    text = product.unit,
                    fontSize = 16.sp,
                    color = if (isSystemInDarkTheme()) Color.Black else Color.Black
                )
                Spacer(modifier = Modifier.height(5.dp))

                Text(
                    text = "Price: ₹${product.price}",
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    color = if (isSystemInDarkTheme()) Color.Black else Color.Black
                )
                Text(
                    text = "Stock: ${product.stock}",
                    fontSize = 16.sp,
                    color = if (isSystemInDarkTheme()) Color.Black else Color.Black
                )

                Spacer(modifier = Modifier.weight(1f))

                Button(
                    onClick = {
                        onAddClick()
                    },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (isSystemInDarkTheme()) Color(78, 177, 118) else Color(78, 177, 118)
                    )
                ) {
                    Text(
                        text = if (isAdded) "Item Added" else "Add",
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 18.sp,
                        color = if (isSystemInDarkTheme()) Color.White else Color.White
                    )
                }
            }
        }
    }
}