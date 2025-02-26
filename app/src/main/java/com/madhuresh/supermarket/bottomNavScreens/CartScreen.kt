package com.madhuresh.supermarket.bottomNavScreens

import android.content.Context
import android.os.Environment
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
//noinspection UsingMaterialAndMaterial3Libraries
import androidx.compose.material.ModalBottomSheetLayout
//noinspection UsingMaterialAndMaterial3Libraries
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.google.firebase.database.FirebaseDatabase
import com.itextpdf.text.Document
import com.itextpdf.text.DocumentException
import com.itextpdf.text.Paragraph
import com.itextpdf.text.pdf.PdfWriter
import com.madhuresh.supermarket.R
import com.madhuresh.supermarket.dataModel.Order
import com.madhuresh.supermarket.dataModel.OrderProduct
import com.madhuresh.supermarket.dataModel.Product
import com.madhuresh.supermarket.dataModel.UserOrder
import com.madhuresh.supermarket.ui.theme.SuperMarketTheme
import com.madhuresh.supermarket.viewModel.AuthViewModel
import com.madhuresh.supermarket.viewModel.ProductViewModel
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun CartScreen(viewModel: ProductViewModel ) {
    SuperMarketTheme {
        Surface(
            color = if (isSystemInDarkTheme()) Color.Black else Color.White,
            modifier = Modifier.fillMaxSize()
        ) {
            CartScreenContent(viewModel, AuthViewModel())
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun CartScreenContent(viewModel: ProductViewModel, authViewModel: AuthViewModel) {
    val cartItems by remember { mutableStateOf(viewModel.cartItems) }
    val isAuthenticated by authViewModel.isAuthenticated.collectAsState()
    val context = LocalContext.current
    val sheetState = androidx.compose.material.rememberModalBottomSheetState(initialValue = ModalBottomSheetValue.Hidden)
    val scope = rememberCoroutineScope()
    var cartQuantities by remember { mutableStateOf(cartItems.associate { it.name to 1 }) } // Default quantity 1
    val user by authViewModel.user.collectAsState()
    var showSuccessDialog by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        viewModel.loadCartItems()
    }

    fun updateQuantity(productName: String, newQuantity: Int) {
        cartQuantities = cartQuantities.toMutableMap().apply {
            this[productName] = newQuantity
        }
    }

    fun showCheckoutSummary() {
        if (isAuthenticated) {
            scope.launch {
                sheetState.show()
            }
        } else {
            Toast.makeText(
                context,
                "Not Signed In\nGo to 'Account' For Register",
                Toast.LENGTH_LONG
            ).show()
        }
    }

    fun generateAndSavePdfInvoice(context: Context, order: Order) {
        val document = Document()
        try {
            val directory = File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS), "SuperMarket")
            if (!directory.exists()) {
                directory.mkdirs()
            }
            val file = File(directory, "Invoice_${System.currentTimeMillis()}.pdf")

            PdfWriter.getInstance(document, FileOutputStream(file))

            document.open()
            document.add(Paragraph("Invoice"))
            document.add(Paragraph("Date: ${order.dateTime}"))
            document.add(Paragraph("Total Price: ₹${order.totalPrice}"))
            document.add(Paragraph(""))

            for (product in order.products) {
                document.add(Paragraph("Product: ${product.name}"))
                document.add(Paragraph("Quantity: ${product.quantity}"))
                document.add(Paragraph("Price: ₹${product.price}"))
                document.add(Paragraph(""))
            }

            document.close()

            Toast.makeText(context, "Invoice saved to ${file.absolutePath}", Toast.LENGTH_LONG).show()
        } catch (e: DocumentException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    fun placeOrder() {
        if (isAuthenticated) {
            val userId = user?.uid ?: return
            val date = SimpleDateFormat("dd-MM-yyyy HH:mm:ss", Locale.getDefault()).format(Date())

            val orderProducts = cartItems.map { product ->
                OrderProduct(
                    name = product.name,
                    quantity = cartQuantities[product.name] ?: 1,
                    price = product.price
                )
            }
            val order = Order(
                userId = userId,
                products = orderProducts,
                address = user?.address ?: "Address not available",
                totalPrice = orderProducts.sumOf { it.price * (cartQuantities[it.name] ?: 1) },
                dateTime = date,
                orderStatus = "Processing"
            )
            val database = FirebaseDatabase.getInstance()
            val ordersRef = database.getReference("orders")
            val orderId = ordersRef.push().key ?: return

            ordersRef.child(orderId).setValue(order).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val userOrder = UserOrder(
                        dateTime = date,
                        products = orderProducts,
                        totalPrice = order.totalPrice,
                        orderStatus = "Processing"
                    )
                    val userOrdersRef = database.getReference("users").child(userId).child("orders")
                    userOrdersRef.child(orderId).setValue(userOrder).addOnCompleteListener { userOrderTask ->
                        if (userOrderTask.isSuccessful) {
                            viewModel.clearCart()
                            showSuccessDialog = true
                            generateAndSavePdfInvoice(context, order)
                        } else {
                            Toast.makeText(context, "Failed to save order details", Toast.LENGTH_LONG).show()
                        }
                    }
                }
                else {
                    Toast.makeText(context, "Failed to place order", Toast.LENGTH_LONG).show()
                }
            }

            scope.launch {
                sheetState.hide()
            }
        }
        else {
            Toast.makeText(context, "Not Signed In\nGo to 'Account' For Register", Toast.LENGTH_LONG).show()
        }
    }

    ModalBottomSheetLayout(
        sheetState = sheetState,
        sheetContent = {
            CartSummaryBottomSheet(
                cartItems = cartItems,
                quantities = cartQuantities,
                userAddress = user?.address ?: "Address not available",
                onPlaceOrder = {
                    scope.launch {
                        placeOrder()
                    }
                }
            )
        }
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp)
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(bottom = 80.dp)
            ) {
                Text(
                    text = "Your Cart",
                    fontSize = 20.sp,
                    color = if (isSystemInDarkTheme()) Color.White else Color.Black,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.padding(top = 15.dp)
                )

                Spacer(modifier = Modifier.height(8.dp))

                if (cartItems.isEmpty()) {
                    Text(
                        text = "No items in the cart\nPlease Explore for Products",
                        fontSize = 16.sp,
                        color = if (isSystemInDarkTheme()) Color.White else Color.Black,
                        fontWeight = FontWeight.SemiBold,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(16.dp)
                    )
                } else {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize()
                    ) {
                        items(cartItems) { product ->
                            CartItemCard(
                                product = product,
                                quantity = cartQuantities[product.name] ?: 1,
                                onQuantityChange = { newQuantity ->
                                    updateQuantity(product.name, newQuantity)
                                },
                                onRemove = {
                                    viewModel.removeFromCart(product)
                                    updateQuantity(product.name, 1) // Reset quantity after removal
                                }
                            )
                        }
                    }
                }
            }

            if (cartItems.isNotEmpty()) {
                Button(
                    onClick = { showCheckoutSummary() },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (isSystemInDarkTheme()) Color(78, 177, 118) else Color(78, 177, 118)
                    ),
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .fillMaxWidth()
                        .height(70.dp)
                        .padding(8.dp)
                        .clip(RoundedCornerShape(8.dp))
                ) {
                    Text(
                        text = "Checkout",
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 18.sp,
                        color = if (isSystemInDarkTheme()) Color.White else Color.White
                    )
                }
            }
        }

        if (showSuccessDialog) {
            OrderSuccessDialog(onDismiss = { showSuccessDialog = false })
        }

    }
}

@Composable
fun CartItemCard(product: Product, quantity: Int, onQuantityChange: (Int) -> Unit, onRemove: () -> Unit) {
    val adjustedQuantity = when {
        quantity > product.stock -> product.stock
        quantity < 1 -> 1
        else -> quantity
    }

    fun increment() {
        onQuantityChange((adjustedQuantity + 1).coerceAtMost(product.stock))
    }

    fun decrement() {
        onQuantityChange((adjustedQuantity - 1).coerceAtLeast(1))
    }

    val buttonColor = if (isSystemInDarkTheme()) Color(78, 177, 118) else Color(78, 177, 118)

    Card(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
            .height(IntrinsicSize.Min),
        shape = RoundedCornerShape(10.dp),
        border = BorderStroke(3.dp, if (isSystemInDarkTheme()) Color(78, 177, 118) else Color(78, 177, 118)),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isSystemInDarkTheme()) Color.White else Color.White
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            ) {
                Image(
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
                        text = "Price: ₹${product.price * adjustedQuantity}",
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        color = if (isSystemInDarkTheme()) Color.Black else Color.Black
                    )
                    Text(
                        text = "Stock: ${product.stock}",
                        fontSize = 16.sp,
                        color = if (isSystemInDarkTheme()) Color.Black else Color.Black
                    )
                    Button(
                        onClick = {
                            onRemove()
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 8.dp),
                        shape = RoundedCornerShape(8.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (isSystemInDarkTheme()) Color(78, 177, 118) else Color(78, 177, 118)
                        )
                    ) {
                        Text(
                            text = "Remove From Cart",
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 14.sp,
                            color = if (isSystemInDarkTheme()) Color.White else Color.White
                        )
                    }
                }
            }

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(5.dp)
            ) {
                Button(
                    onClick = { decrement() },
                    enabled = adjustedQuantity > 1,
                    modifier = Modifier.size(60.dp, 50.dp),
                    shape = RoundedCornerShape(50),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = buttonColor
                    )
                ) {
                    Text(
                        text = "-",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.ExtraBold
                    )
                }

                Spacer(modifier = Modifier.width(8.dp))

                Text(
                    text = "Quantity : $adjustedQuantity",
                    fontSize = 20.sp,
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier
                        .align(Alignment.CenterVertically)
                        .padding(horizontal = 16.dp)
                        .width(120.dp)
                )

                Spacer(modifier = Modifier.width(8.dp))

                Button(
                    onClick = { increment() },
                    enabled = adjustedQuantity < product.stock,
                    modifier = Modifier.size(60.dp, 50.dp),
                    shape = RoundedCornerShape(50),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = buttonColor
                    )
                ) {
                    Text(
                        text = "+",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.ExtraBold
                    )
                }
            }
        }
    }
}

@Composable
fun CartSummaryBottomSheet(cartItems: List<Product>, quantities: Map<String, Int>, userAddress: String, onPlaceOrder: () -> Unit) {
    val totalPrice = cartItems.sumOf { it.price * (quantities[it.name] ?: 1) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Text(
            text = "Order Summary",
            fontWeight = FontWeight.Bold,
            fontSize = 20.sp,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
        ) {
            LazyColumn(
                modifier = Modifier.fillMaxWidth()
            ) {
                items(cartItems) { product ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 5.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = product.name,
                        )
                        Text(
                            text = "₹${product.price * (quantities[product.name] ?: 1)}"
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(15.dp))

        Text(
            text = "Delivery Address: $userAddress",
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        Text(
            text = "Payment Mode: Cash On Delivery",
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        Text(
            text = "Total Price: ₹$totalPrice",
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        Button(
            onClick = onPlaceOrder,
            modifier = Modifier
                .height(50.dp)
                .fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(78, 177, 118)
            )
        ) {
            Text(
                text = "Place Order",
                fontWeight = FontWeight.SemiBold,
                fontSize = 18.sp
            )
        }
    }
}

@Composable
fun OrderSuccessDialog(onDismiss: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(text = "Order Successful")
        },
        text = {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Image(
                    painter = painterResource(id = R.drawable.order),
                    contentDescription = "Order Successful",
                    modifier = Modifier.size(180.dp, 180.dp),
                    contentScale = ContentScale.Crop
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "Your order has been accepted.",
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "You can track your order in the 'Order' section in 'Accounts' tab.",
                    fontSize = 15.sp,
                    textAlign = TextAlign.Center
                )
            }
        },
        confirmButton = {
            Button(
                onClick = onDismiss,
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(78, 177, 118)
                )
            ) {
                Text("OK")
            }
        },
        modifier = Modifier.padding(16.dp)
    )
}

@Preview(showBackground = true)
@Composable
fun SomePreview(){
    SuperMarketTheme {
        OrderSuccessDialog {}
    }
}