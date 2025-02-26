package com.madhuresh.supermarket.accountScreens

import android.app.Activity
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.madhuresh.supermarket.ui.theme.SuperMarketTheme
import com.madhuresh.supermarket.viewModel.AuthViewModel

@Composable
fun MyProfileScreen(){

    val context = LocalContext.current

    LaunchedEffect(Unit) {
        val window = (context as? Activity)?.window
        val statusBarColor = Color(78, 177, 118).toArgb()
        window?.statusBarColor = statusBarColor
    }

    SuperMarketTheme {
        Surface(
            color = if (isSystemInDarkTheme()) Color(78, 177, 118) else Color(78, 177, 118),
            modifier = Modifier.fillMaxSize()
        ) {
            MyProfileScreenContent(AuthViewModel())
        }
    }
}

@Composable
fun MyProfileScreenContent(authViewModel: AuthViewModel) {
    val user by authViewModel.user.collectAsState()
    var isEditing by remember { mutableStateOf(false) }
    var newAddress by remember { mutableStateOf(user?.address ?: "") }
    val context = LocalContext.current

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .verticalScroll(rememberScrollState())
            .fillMaxSize()
            .padding(16.dp)
    ) {

        Text(
            text = "My Profile",
            fontSize = 25.sp,
            color = if (isSystemInDarkTheme()) Color.White else Color.White,
            fontWeight = FontWeight.SemiBold,
        )

        Card(
            shape = RoundedCornerShape(15.dp),
            modifier = Modifier
                .fillMaxWidth()
                .height(IntrinsicSize.Max)
                .padding(top = 16.dp)
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .background(if (isSystemInDarkTheme()) Color.White else Color.White)
            ) {
                Text(
                    text = "Name",
                    fontSize = 20.sp,
                    color = if (isSystemInDarkTheme()) Color(78, 177, 118) else Color(78, 177, 118),
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 20.dp, top = 20.dp)
                )
                Card(
                    shape = RoundedCornerShape(30.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = if (isSystemInDarkTheme()) Color(243, 243, 243) else Color(243, 243, 243),
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(IntrinsicSize.Max)
                        .padding(10.dp)
                ) {
                    Text(
                        text = user?.name ?: "",
                        fontSize = 18.sp,
                        color = if (isSystemInDarkTheme()) Color.Black else Color.Black,
                        fontWeight = FontWeight.Normal,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(15.dp)
                    )
                }

                Text(
                    text = "Email",
                    fontSize = 20.sp,
                    color = if (isSystemInDarkTheme()) Color(78, 177, 118) else Color(78, 177, 118),
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 20.dp, top = 20.dp)
                )
                Card(
                    shape = RoundedCornerShape(30.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = if (isSystemInDarkTheme()) Color(243, 243, 243) else Color(243, 243, 243),
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(IntrinsicSize.Max)
                        .padding(10.dp)
                ) {
                    Text(
                        text = user?.email ?: "",
                        fontSize = 18.sp,
                        color = if (isSystemInDarkTheme()) Color.Black else Color.Black,
                        fontWeight = FontWeight.Normal,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(15.dp)
                    )
                }

                Text(
                    text = "Phone",
                    fontSize = 20.sp,
                    color = if (isSystemInDarkTheme()) Color(78, 177, 118) else Color(78, 177, 118),
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 20.dp, top = 20.dp)
                )
                Card(
                    shape = RoundedCornerShape(30.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = if (isSystemInDarkTheme()) Color(243, 243, 243) else Color(243, 243, 243),
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(IntrinsicSize.Max)
                        .padding(10.dp)
                ) {
                    Text(
                        text = user?.phone ?: "",
                        fontSize = 18.sp,
                        color = if (isSystemInDarkTheme()) Color.Black else Color.Black,
                        fontWeight = FontWeight.Normal,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(15.dp)
                    )
                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    Text(
                        text = "Address",
                        fontSize = 20.sp,
                        color = if (isSystemInDarkTheme()) Color(78, 177, 118) else Color(78, 177, 118),
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier
                            .padding(start = 20.dp, top = 20.dp)
                    )
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = null,
                        tint = if (isSystemInDarkTheme()) Color(78, 177, 118) else Color(78, 177, 118),
                        modifier = Modifier
                            .padding(start = 10.dp, top = 20.dp)
                            .clickable {
                                isEditing = true
                            }
                    )
                }

                if (isEditing) {
                    TextField(
                        value = newAddress,
                        onValueChange = { newAddress = it },
                        shape = RoundedCornerShape(30.dp),
                        placeholder = { Text(text = "Address") },
                        leadingIcon = { Icon(imageVector = Icons.Default.LocationOn, contentDescription = null) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(IntrinsicSize.Max)
                            .padding(15.dp),
                        colors = TextFieldDefaults.colors(
                            focusedTextColor = if (isSystemInDarkTheme()) Color.Black else Color.Black,
                            unfocusedTextColor = if (isSystemInDarkTheme()) Color.Black else Color.Black,
                            unfocusedLeadingIconColor = if (isSystemInDarkTheme()) Color.Black else Color.Black,
                            focusedLeadingIconColor = if (isSystemInDarkTheme()) Color.Black else Color.Black,
                            unfocusedPlaceholderColor = if (isSystemInDarkTheme()) Color.Black else Color.Black,
                            focusedPlaceholderColor = if (isSystemInDarkTheme()) Color.Black else Color.Black,
                            unfocusedContainerColor = if (isSystemInDarkTheme()) Color(243, 243, 243) else Color(243, 243, 243),
                            focusedContainerColor = if (isSystemInDarkTheme()) Color(243, 243, 243) else Color(243, 243, 243),
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent
                        )
                    )
                    Button(
                        onClick = {
                            if (newAddress.isBlank()) {
                                Toast.makeText(context, "Address cannot be empty", Toast.LENGTH_SHORT).show()
                            } else {
                                authViewModel.updateAddress(newAddress, context = context)
                                isEditing = false
                            }
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (isSystemInDarkTheme()) Color(78, 177, 118) else Color(78, 177, 118)
                        )
                    ) {
                        Text(text = "Save")
                    }
                } else {
                    Card(
                        shape = RoundedCornerShape(30.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = if (isSystemInDarkTheme()) Color(243, 243, 243) else Color(243, 243, 243),
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(IntrinsicSize.Max)
                            .padding(10.dp)
                    ) {
                        Text(
                            text = user?.address ?: "",
                            fontSize = 18.sp,
                            color = if (isSystemInDarkTheme()) Color.Black else Color.Black,
                            fontWeight = FontWeight.Normal,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(15.dp)
                        )
                    }
                }
            }
        }
    }
}