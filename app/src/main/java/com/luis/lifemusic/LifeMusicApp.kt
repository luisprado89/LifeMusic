package com.luis.lifemusic

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.luis.lifemusic.navigation.AppNavHost

@Composable
fun LifeMusicApp(
    navController: NavHostController = rememberNavController()
) {
    AppNavHost(navController = navController)
}
