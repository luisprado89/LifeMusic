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
import com.luis.lifemusic.ui.home.HomeRoute
import com.luis.lifemusic.ui.list.ListRoute
import com.luis.lifemusic.ui.login.LoginRoute
import com.luis.lifemusic.ui.recover.RecoverRoute
import com.luis.lifemusic.ui.register.RegisterRoute

/**
 * AppNavHost centraliza toda la navegación de la app.
 *
 * ✅ Regla:
 * - Navegamos por IDs estables (songId) y NO por títulos.
 *
 * ✅ Estado actual:
 * - Auth (Login/Register/Recover) usa Route pattern (ViewModel + UiState).
 * - Main (Home/List) también usa Route pattern con guard de sesión.
 * - Detail/Profile siguen siendo Pages (de momento).
 *
 * 🔜 Siguiente paso:
 * - Terminar ViewModels + repos (Room/DataStore/Retrofit)
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

        // ===========================
        // AUTH FLOW
        // ===========================

        composable(LoginDestination.route) {
            /**
             * LoginRoute:
             * - Obtiene su ViewModel internamente (Factory global).
             * - Emite eventos hacia el NavHost para navegar.
             * - El ViewModel NO navega.
             */
            LoginRoute(
                onLoginSuccess = {
                    navController.navigate(HomeDestination.route) {
                        popUpTo(LoginDestination.route) { inclusive = true }
                        launchSingleTop = true
                    }
                },
                onGoToRegister = {
                    navController.navigate(RegisterDestination.route)
                },
                onGoToRecover = {
                    navController.navigate(RecoverDestination.route)
                }
            )
        }

        composable(RegisterDestination.route) {
            /**
             * RegisterRoute:
             * - Conecta UI + ViewModel.
             * - En éxito notifica al NavHost para navegar.
             */
            RegisterRoute(
                onBackClick = { navController.popBackStack() },
                onRegisterSuccess = {
                    navController.navigate(HomeDestination.route) {
                        popUpTo(LoginDestination.route) { inclusive = true }
                        launchSingleTop = true
                    }
                }
            )
        }

        composable(RecoverDestination.route) {
            /**
             * RecoverRoute:
             * - Controla recuperación de contraseña con estado desde ViewModel.
             * - La navegación se resuelve aquí (NavHost), no en el VM.
             */
            RecoverRoute(
                onBackClick = { navController.popBackStack() }
            )
        }

        // ===========================
        // MAIN FLOW
        // ===========================

        composable(HomeDestination.route) {
            /**
             * HomeRoute:
             * - Conecta HomePage (UI pura) con HomeViewModel.
             * - Incluye guard de sesión: si se pierde sesión, avisa al NavHost.
             */
            HomeRoute(
                onNavigateToList = { navController.navigate(ListDestination.route) },
                onNavigateToProfile = { navController.navigate(ProfileDestination.route) },
                onNavigateToDetail = { songId ->
                    navController.navigate("${DetailDestination.route}/$songId")
                },
                onSessionExpired = {
                    // Si no hay sesión activa, volvemos a Login y limpiamos el back stack.
                    navController.navigate(LoginDestination.route) {
                        popUpTo(0) { inclusive = true }
                        launchSingleTop = true
                    }
                }
            )
        }

        composable(ListDestination.route) {
            /**
             * ListRoute:
             * - Conecta ListPage (UI pura) con ListViewModel.
             * - Incluye guard de sesión: si se pierde la sesión, vuelve a Login.
             */
            ListRoute(
                onBackClick = { navController.popBackStack() },
                onNavigateToDetail = { songId ->
                    navController.navigate("${DetailDestination.route}/$songId")
                },
                onSessionExpired = {
                    navController.navigate(LoginDestination.route) {
                        popUpTo(0) { inclusive = true }
                        launchSingleTop = true
                    }
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

            // De momento seguimos con sampleSongs para esta pantalla.
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
