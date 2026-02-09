package com.luis.lifemusic.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.luis.lifemusic.data.sampleSongs
import com.luis.lifemusic.page.*

@Composable
fun AppNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = LoginDestination.route,
        modifier = modifier
    ) {
        // LOGIN
        composable(LoginDestination.route) {
            LoginPage(
                onLoginClick = { navController.navigate(HomeDestination.route) },
                onRegisterClick = { navController.navigate(RegisterDestination.route) },
                onRecoverClick = { navController.navigate(RecoverDestination.route) }
            )
        }

        // REGISTER
        composable(RegisterDestination.route) {
            RegisterPage(
                onBackClick = { navController.popBackStack() },
                onRegisterSuccess = { navController.navigate(HomeDestination.route) }
            )
        }

        // RECOVER
        composable(RecoverDestination.route) {
            RecoverPasswordPage(
                onBackClick = { navController.popBackStack() }
            )
        }

        // HOME
        composable(HomeDestination.route) {
            HomePage(
                onNavigateToList = { navController.navigate(ListDestination.route) },
                onNavigateToProfile = { navController.navigate(ProfileDestination.route) },
                onNavigateToDetail = { songId ->
                    navController.navigate("${DetailDestination.route}/$songId")
                }
            )
        }

        // LIST
        composable(ListDestination.route) {
            ListPage(
                onBackClick = { navController.popBackStack() },
                onNavigateToDetail = { songId ->
                    navController.navigate("${DetailDestination.route}/$songId")
                }
            )
        }

        // DETAIL (con argumento songId)
        composable(
            route = DetailDestination.routeWithArgs,
            arguments = listOf(navArgument(DetailDestination.songIdArg) { type = NavType.IntType })
        ) { backStackEntry ->
            val songId = backStackEntry.arguments?.getInt(DetailDestination.songIdArg) ?: 1
            val song = sampleSongs.firstOrNull { it.id == songId } ?: sampleSongs.first()

            DetailPage(
                imageRes = song.imageRes,
                title = song.title,
                artist = song.artist,
                album = song.album,
                duration = song.duration,
                isFavoriteInitial = song.isFavorite,
                onBackClick = { navController.popBackStack() },
                onReturnToList = { navController.popBackStack() }
            )
        }

        // PROFILE
        composable(ProfileDestination.route) {
            ProfilePage(
                onBackClick = { navController.popBackStack() },
                onLogoutClick = {
                    navController.navigate(LoginDestination.route) {
                        popUpTo(HomeDestination.route) { inclusive = true }
                        launchSingleTop = true
                    }
                }
            )
        }
    }
}
