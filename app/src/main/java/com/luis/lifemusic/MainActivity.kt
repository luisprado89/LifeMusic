package com.luis.lifemusic

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.luis.lifemusic.page.LoginPage
import com.luis.lifemusic.ui.theme.LifeMusicTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            LifeMusicTheme {
                LoginPage()
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    LifeMusicTheme {
        LoginPage()
    }
}