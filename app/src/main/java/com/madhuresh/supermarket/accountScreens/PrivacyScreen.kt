package com.madhuresh.supermarket.accountScreens

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.madhuresh.supermarket.ui.theme.SuperMarketTheme

@Composable
fun PrivacyScreen(){
    SuperMarketTheme {
        Surface(
            color = if (isSystemInDarkTheme()) Color.Black else Color.White,
            modifier = Modifier.fillMaxSize()
        ) {
            Text(text = "Privacy")
        }
    }
}