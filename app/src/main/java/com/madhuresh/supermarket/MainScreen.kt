package com.madhuresh.supermarket

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.madhuresh.supermarket.accountScreens.AboutScreen
import com.madhuresh.supermarket.accountScreens.HelpScreen
import com.madhuresh.supermarket.accountScreens.MyProfileScreen
import com.madhuresh.supermarket.accountScreens.OrderScreen
import com.madhuresh.supermarket.accountScreens.PrivacyScreen
import com.madhuresh.supermarket.accountScreens.SettingScreen
import com.madhuresh.supermarket.bottomNavScreens.AccountScreen
import com.madhuresh.supermarket.bottomNavScreens.CartScreen
import com.madhuresh.supermarket.bottomNavScreens.ExploreScreen
import com.madhuresh.supermarket.bottomNavScreens.HomeScreen
import com.madhuresh.supermarket.screens.ProductDetailScreenContent
import com.madhuresh.supermarket.screens.ProductScreenContent
import com.madhuresh.supermarket.ui.theme.SuperMarketTheme
import com.madhuresh.supermarket.viewModel.ProductViewModel
import kotlinx.coroutines.delay

class MainScreen : ComponentActivity() {

    private lateinit var productViewModel: ProductViewModel
    private lateinit var connectivityObserver: ConnectivityObserver

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        productViewModel = ViewModelProvider(this, ProductViewModelFactory(application))[ProductViewModel::class.java]
        connectivityObserver = ConnectivityObserver(applicationContext)

        setContent {
            SuperMarketTheme {
                Surface(
                    color = if (isSystemInDarkTheme()) Color.Black else Color.White,
                    modifier = Modifier.fillMaxSize()
                ) {
                    val isConnected by connectivityObserver.isConnected.observeAsState(initial = true)
                    var showDialog by remember { mutableStateOf(!isConnected) }

                    LaunchedEffect(isConnected) {
                        if (!isConnected) {
                            delay(2000)
                            showDialog = true
                        }
                    }

                    if (!isConnected && showDialog) {
                        NoInternetDialog(
                            context = applicationContext,
                            onRetry = {
                                showDialog = false
                            }
                        )
                    }

                    BottomAppBar(productViewModel)
                    window.navigationBarColor = if (isSystemInDarkTheme()) Color(78, 177, 118).toArgb() else Color(78, 177, 118).toArgb()
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        connectivityObserver.unregister()
    }

}


@Composable
fun BottomAppBar(viewModel: ProductViewModel){
    val navigationController = rememberNavController()
    val selected = remember {
        mutableStateOf(Icons.Default.Home)
    }

    Scaffold(
        bottomBar = {
            androidx.compose.material3.BottomAppBar(
                containerColor = Color(78, 177, 118)
            ) {
                IconButton(
                    onClick = {
                        selected.value = Icons.Default.Home
                        navigationController.navigate(Screens.Home.screen) {
                            popUpTo(0)
                        }
                    },
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxSize(),
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Icon(
                            Icons.Default.Home,
                            contentDescription = null,
                            modifier = Modifier.size(30.dp),
                            tint = if (selected.value == Icons.Default.Home) Color.Black else Color.White
                        )
                        if (selected.value == Icons.Default.Home) {
                            Text(
                                text = "Home",
                                color = Color.Black,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }
                }

                IconButton(
                    onClick = {
                        selected.value = Icons.Default.Search
                        navigationController.navigate(Screens.Explore.screen) {
                            popUpTo(0)
                        }
                    },
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxSize()
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Icon(
                            Icons.Default.Search,
                            contentDescription = null,
                            modifier = Modifier.size(30.dp),
                            tint = if (selected.value == Icons.Default.Search) Color.Black else Color.White
                        )
                        if (selected.value == Icons.Default.Search) {
                            Text(
                                text = "Explore",
                                color = Color.Black,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }
                }

                IconButton(
                    onClick = {
                        selected.value = Icons.Default.ShoppingCart
                        navigationController.navigate(Screens.Cart.screen) {
                            popUpTo(0)
                        }
                    },
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxSize()
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Icon(
                            Icons.Default.ShoppingCart,
                            contentDescription = null,
                            modifier = Modifier.size(30.dp),
                            tint = if (selected.value == Icons.Default.ShoppingCart) Color.Black else Color.White
                        )
                        if (selected.value == Icons.Default.ShoppingCart) {
                            Text(
                                text = "Cart",
                                color = Color.Black,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }
                }

                IconButton(
                    onClick = {
                        selected.value = Icons.Default.AccountCircle
                        navigationController.navigate(Screens.Account.screen) {
                            popUpTo(0)
                        }
                    },
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxSize()
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Icon(
                            Icons.Default.AccountCircle,
                            contentDescription = null,
                            modifier = Modifier.size(30.dp),
                            tint = if (selected.value == Icons.Default.AccountCircle) Color.Black else Color.White
                        )
                        if (selected.value == Icons.Default.AccountCircle) {
                            Text(
                                text = "Account",
                                color = Color.Black,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }
                }
            }
        }
    ) {paddingValues ->
        NavHost(navController = navigationController,
            startDestination = Screens.Home.screen,
            modifier = Modifier.padding(paddingValues)){
            composable(Screens.Home.screen){ HomeScreen(navigationController)}
            composable(Screens.Explore.screen){ ExploreScreen(navigationController) }
            composable(Screens.Cart.screen){ CartScreen(viewModel = viewModel) }
            composable(Screens.Account.screen){ AccountScreen(navigationController) }
            composable("product/{categoryName}") { backStackEntry ->
                val categoryName = backStackEntry.arguments?.getString("categoryName")
                ProductScreenContent(categoryName, navController = navigationController)
            }
            composable("productDetail/{desc}/{image}/{ingredients}/{manufacturer}/{name}/{price}/{shelf}/{stock}/{unit}/{images}") { backStackEntry ->
                val desc = backStackEntry.arguments?.getString("desc") ?: ""
                val image = backStackEntry.arguments?.getString("image") ?: ""
                val ingredients = backStackEntry.arguments?.getString("ingredients") ?: ""
                val manufacturer = backStackEntry.arguments?.getString("manufacturer") ?: ""
                val name = backStackEntry.arguments?.getString("name") ?: ""
                val price = backStackEntry.arguments?.getString("price")?.toIntOrNull() ?: 0
                val shelf = backStackEntry.arguments?.getString("shelf") ?: ""
                val stock = backStackEntry.arguments?.getString("stock")?.toIntOrNull() ?: 0
                val unit = backStackEntry.arguments?.getString("unit") ?: ""
                val images = backStackEntry.arguments?.getString("images")?.split(",") ?: listOf()
                ProductDetailScreenContent(desc, image, ingredients, manufacturer, name, price, shelf, stock, unit, images
                )
            }
            composable("myProfile"){ MyProfileScreen() }
            composable("order"){ OrderScreen() }
            composable("setting"){ SettingScreen() }
            composable("help"){ HelpScreen() }
            composable("about"){ AboutScreen() }
            composable("privacy"){ PrivacyScreen() }
        }
    }
}