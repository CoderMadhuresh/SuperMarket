package com.madhuresh.supermarket.bottomNavScreens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.compose.rememberAsyncImagePainter
import com.madhuresh.supermarket.ui.theme.SuperMarketTheme
import com.madhuresh.supermarket.viewModel.ExploreViewModel

@Composable
fun ExploreScreen(navigationController: NavController){
    SuperMarketTheme {
        Surface(
            color = if (isSystemInDarkTheme()) Color.Black else Color.White,
            modifier = Modifier.fillMaxSize()
        ) {
            ExploreScreenContent(navigationController = navigationController)
        }
    }
}

@Composable
fun ExploreScreenContent(
    navigationController: NavController,
    viewModel: ExploreViewModel = androidx.lifecycle.viewmodel.compose.viewModel()
) {
    val categories by viewModel.categories.collectAsState()
    val textState = remember { mutableStateOf(TextFieldValue()) }
    val searchText = textState.value.text
    val filteredCategories = if (searchText.isBlank()) {
        categories
    } else {
        categories.filterKeys { it.contains(searchText, ignoreCase = true) }
    }
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxSize()
    ) {
        Text(
            text = "Search By Categories",
            fontSize = 20.sp,
            color = if (isSystemInDarkTheme()) Color.White else Color.Black,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.padding(top = 15.dp)
        )

        Spacer(modifier = Modifier.padding(8.dp))

        TextField(
            modifier = Modifier
                .fillMaxWidth()
                .height(60.dp)
                .padding(start = 10.dp, end = 10.dp, bottom = 5.dp),
            shape = RoundedCornerShape(30.dp),
            value = textState.value,
            onValueChange = { textState.value = it },
            placeholder = { Text(text = "Search Categories") },
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
        if (categories.isEmpty()) {
            Text(
                text = "Loading Categories...",
                fontSize = 16.sp,
                color = if (isSystemInDarkTheme()) Color.White else Color.Black,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.padding(16.dp)
            )
        } else if (filteredCategories.isEmpty()) {
            Text(
                text = "No Categories Found",
                fontSize = 16.sp,
                color = if (isSystemInDarkTheme()) Color.White else Color.Black,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.padding(16.dp)
            )
        } else {
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                contentPadding = PaddingValues(8.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                items(filteredCategories.keys.size) { index ->
                    val categoryName = filteredCategories.keys.elementAt(index)
                    val category = filteredCategories[categoryName]
                    CategoryCard(
                        categoryName = categoryName,
                        imageUrl = category?.categoryImage ?: "",
                        onClick = {
                            navigationController.navigate("product/$categoryName")
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun CategoryCard(categoryName: String, imageUrl: String, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .padding(8.dp)
            .clickable { onClick() }
            .fillMaxWidth()
            .aspectRatio(1f)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.background(Color(if (isSystemInDarkTheme()) 0xFF4EB176 else 0xFF4EB176))
        ) {
            Image(
                painter = rememberAsyncImagePainter(imageUrl),
                contentDescription = categoryName,
                contentScale = ContentScale.FillBounds,
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            )
            Text(
                text = categoryName,
                fontSize = 16.sp,
                color = if(isSystemInDarkTheme()) Color.White else Color.Black,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.padding(8.dp)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ExploreScreenPreview(){
    SuperMarketTheme {
        ExploreScreenContent(rememberNavController())
    }
}