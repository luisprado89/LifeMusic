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
import com.luis.lifemusic.ui.login.LoginRoute

/**
 * AppNavHost centraliza toda la navegación de la app.
 *
 * ✅ Regla:
 * - Navegamos por IDs estables (songId) y NO por títulos.
 *
 * ✅ Estado:
 * - De momento seguimos con sampleSongs para pintar UI.
 * - Siguiente paso: terminar ViewModels + repos (Room/DataStore/Retrofit)
 *   manteniendo la inyección desde AppViewModelProvider.
 */
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

        // ---------------------------
        // AUTH FLOW
        // ---------------------------

        composable(LoginDestination.route) {
            /**
             * Login ya conectado a ViewModel (Route pattern).
             * La navegación sigue en NavHost (no en el ViewModel).
             */
            LoginRoute(
                onLoginSuccess = {
                    navController.navigate(HomeDestination.route) {
                        popUpTo(LoginDestination.route) { inclusive = true }
                        launchSingleTop = true
                    }
                },
                onGoToRegister = { navController.navigate(RegisterDestination.route) },
                onGoToRecover = { navController.navigate(RecoverDestination.route) }
            )
        }

        composable(RegisterDestination.route) {
            RegisterPage(
                onBackClick = { navController.popBackStack() },
                onRegisterClick = {
                    navController.navigate(HomeDestination.route) {
                        popUpTo(LoginDestination.route) { inclusive = true }
                        launchSingleTop = true
                    }
                }
            )
        }

        composable(RecoverDestination.route) {
            RecoverPasswordPage(
                onBackClick = { navController.popBackStack() }
            )
        }

        // ---------------------------
        // MAIN FLOW
        // ---------------------------

        composable(HomeDestination.route) {
            HomePage(
                onNavigateToList = { navController.navigate(ListDestination.route) },
                onNavigateToProfile = { navController.navigate(ProfileDestination.route) },
                onNavigateToDetail = { songId ->
                    navController.navigate("${DetailDestination.route}/$songId")
                }
            )
        }

        composable(ListDestination.route) {
            ListPage(
                onBackClick = { navController.popBackStack() },
                onNavigateToDetail = { songId ->
                    navController.navigate("${DetailDestination.route}/$songId")
                }
            )
        }

        // DETAIL con argumento
        composable(
            route = DetailDestination.routeWithArgs,
            arguments = listOf(
                navArgument(DetailDestination.songIdArg) { type = NavType.IntType }
            )
        ) { backStackEntry ->
            val songId = backStackEntry.arguments?.getInt(DetailDestination.songIdArg)

            val song = sampleSongs.firstOrNull { it.id == songId } ?: sampleSongs.first()

            DetailPage(
                songId = song.id,
                imageRes = song.imageRes,
                title = song.title,
                artist = song.artist,
                album = song.album,
                duration = song.duration,
                isFavoriteInitial = song.isFavorite,
                onBackClick = { navController.popBackStack() }
            )
        }

        composable(ProfileDestination.route) {
            ProfilePage(
                onBackClick = { navController.popBackStack() },
                onLogoutClick = {
                    navController.navigate(LoginDestination.route) {
                        popUpTo(0) { inclusive = true }
                        launchSingleTop = true
                    }
                }
            )
        }
    }
}
