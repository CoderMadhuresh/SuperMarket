package com.madhuresh.supermarket.accountScreens

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.madhuresh.supermarket.ui.theme.SuperMarketTheme

@Composable
fun SettingScreen() {
    // State to track the theme mode
    val theme = isSystemInDarkTheme()
    var darkTheme by remember { mutableStateOf(theme) }

    SuperMarketTheme(darkTheme = darkTheme) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Button(onClick = { darkTheme = !darkTheme }) {
                Text(text = if (darkTheme) "Switch to Light Theme" else "Switch to Dark Theme")
            }
            Spacer(modifier = Modifier.height(16.dp))
            Text(text = "Settings")
        }
    }
}