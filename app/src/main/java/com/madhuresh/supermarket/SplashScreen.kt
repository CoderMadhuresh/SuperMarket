package com.madhuresh.supermarket

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.madhuresh.supermarket.ui.theme.SuperMarketTheme

@SuppressLint("CustomSplashScreen")
class SplashScreen : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SuperMarketTheme {
                Surface(
                    color = if (isSystemInDarkTheme()) Color.Black else Color.White,
                    modifier = Modifier.fillMaxSize()
                ) {
                    SplashScreenContent()

                    window.navigationBarColor = if (isSystemInDarkTheme()) Color.Black.toArgb() else Color.White.toArgb()
                }
            }
        }
        Handler(Looper.getMainLooper()).postDelayed({
            val intent = Intent(this, MainScreen::class.java)
            startActivity(intent)
            finish()
        }, 2000)
    }
}

@Composable
fun SplashScreenContent() {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier.fillMaxSize()
    ) {
        Image(
            painter = painterResource(id = R.drawable.logo),
            contentDescription = "Logo",
            modifier = Modifier
                .width(60.dp)
                .height(60.dp)
        )
        Spacer(modifier = Modifier.padding(top = 15.dp))
        Text(
            text = "Super Market",
            color = MaterialTheme.colorScheme.onBackground,
            fontWeight = FontWeight.Bold,
            fontSize = 25.sp
        )
    }
}

@Preview(showBackground = true)
@Composable
fun SplashScreenPreview(){
    SuperMarketTheme {
        SplashScreenContent()
    }
}