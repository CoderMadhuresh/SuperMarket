package com.madhuresh.supermarket.accountScreens

import android.app.Activity
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.madhuresh.supermarket.dataModel.UserOrder
import com.madhuresh.supermarket.ui.theme.SuperMarketTheme
import com.madhuresh.supermarket.viewModel.AuthViewModel
import com.madhuresh.supermarket.viewModel.OrderViewModel

@Composable
fun OrderScreen(
    authViewModel: AuthViewModel = viewModel(),
    orderViewModel: OrderViewModel = viewModel()
) {
    val context = LocalContext.current
    val user = authViewModel.user.collectAsState().value
    LaunchedEffect(user) {
        val window = (context as? Activity)?.window
        val statusBarColor = Color(78, 177, 118).toArgb()
        window?.statusBarColor = statusBarColor

        user?.uid?.let { userId ->
            orderViewModel.fetchOrders(userId)
        }
    }
    SuperMarketTheme {
        Surface(
            color = if (isSystemInDarkTheme()) Color(78, 177, 118) else Color(78, 177, 118),
            modifier = Modifier.fillMaxSize()
        ) {
            user?.uid?.let { userId ->
                OrderScreenContent(orderViewModel, userId)
            }
        }
    }
}

@Composable
fun OrderScreenContent(viewModel: OrderViewModel, userId: String) {
    val ordersSnapshot = viewModel.ordersSnapshot.collectAsState().value

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "Your Orders",
            fontSize = 25.sp,
            color = if (isSystemInDarkTheme()) Color.White else Color.White,
            fontWeight = FontWeight.SemiBold,
        )

        Spacer(modifier = Modifier.height(16.dp))

        if (ordersSnapshot.isEmpty()) {
            Text(
                text = "You haven't ordered anything\nPlease Explore for products",
                fontSize = 18.sp,
                textAlign = TextAlign.Center,
                color = if (isSystemInDarkTheme()) Color.White else Color.White,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier
                    .fillMaxWidth()
            )
        }
        else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
            ) {
                ordersSnapshot.forEach { (orderId, order) ->
                    OrderItem(orderId = orderId, order = order, userId = userId, viewModel = viewModel)
                    Spacer(modifier = Modifier.height(16.dp))
                }
            }
        }
    }
}

@Composable
fun OrderItem(orderId: String, order: UserOrder, userId: String, viewModel: OrderViewModel) {
    val context = LocalContext.current
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White, RoundedCornerShape(15.dp))
            .padding(10.dp)
    ) {
        Text(
            text = "Order Id : $orderId",
            fontSize = 16.sp,
            color = if (isSystemInDarkTheme()) Color(78, 177, 118) else Color(78, 177, 118),
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier
                .fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = "Date & Time : ${order.dateTime}",
            fontSize = 16.sp,
            color = if (isSystemInDarkTheme()) Color.Black else Color.Black,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier
                .fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = "Order Status : ${order.orderStatus}",
            fontSize = 16.sp,
            color = if (isSystemInDarkTheme()) Color.Black else Color.Black,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier
                .fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = "Total Price : ₹${order.totalPrice}",
            fontSize = 16.sp,
            color = if (isSystemInDarkTheme()) Color.Black else Color.Black,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier
                .fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = "Products : ",
            fontSize = 16.sp,
            color = if (isSystemInDarkTheme()) Color.Black else Color.Black,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier
                .fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(4.dp))
        order.products.forEach { product ->
            Text(
                text = "${product.quantity} x ${product.name} = ₹${product.price}",
                fontSize = 16.sp,
                color = if (isSystemInDarkTheme()) Color(78, 177, 118) else Color(78, 177, 118),
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier
                    .fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(4.dp))
        }
        if (order.orderStatus != "Cancelled") {
            Button(
                onClick = {
                    viewModel.updateOrderStatus(orderId, userId, "Cancelled", context)
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (isSystemInDarkTheme()) Color(78, 177, 118) else Color(78, 177, 118)
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(IntrinsicSize.Max)
                    .padding(top = 8.dp)
                    .clip(RoundedCornerShape(8.dp))
            ) {
                Text(
                    text = "Cancel Order",
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 20.sp,
                    color = if (isSystemInDarkTheme()) Color.White else Color.White
                )
            }
        }
    }
}