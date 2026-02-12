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

/**
 * AppNavHost centraliza toda la navegación de la app.
 *
 * ✅ Regla que seguimos:
 * - Navegamos por IDs estables (songId) y NO por títulos (title) para evitar errores
 *   si hay canciones repetidas o el texto cambia.
 *
 * ✅ Importante (estado):
 * - De momento usamos sampleSongs para mostrar pantallas.
 * - Más adelante: AppNavHost no debería depender de sampleSongs,
 *   sino de ViewModels + Room/Retrofit.
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
            LoginPage(
                onLoginClick = {
                    // ✅ Opción: evitamos volver a Login con back si ya estás dentro
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
                    // ✅ En el futuro: aquí el VM validará y guardará usuario en Room.
                    // Si el registro es OK, entramos a Home.
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
                // El reset real se conectará luego con Room (usuario + pregunta/respuesta)
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
                    // ✅ Navegación por ID: detail/{songId}
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

        /**
         * Detail usa un argumento obligatorio songId:
         * routeWithArgs = "detail/{songId}"
         */
        composable(
            route = DetailDestination.routeWithArgs,
            arguments = listOf(
                navArgument(DetailDestination.songIdArg) { type = NavType.IntType }
            )
        ) { backStackEntry ->

            // ✅ Recuperamos el songId desde argumentos
            val songId = backStackEntry.arguments?.getInt(DetailDestination.songIdArg)

            // ✅ De momento buscamos en sampleSongs (temporal).
            // Luego: esto vendrá de Room / ViewModel.
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
                // Si quieres un botón "Volver al listado", NO hace falta otra acción:
                // popBackStack() ya vuelve a la pantalla anterior.
            )
        }

        composable(ProfileDestination.route) {
            ProfilePage(
                onBackClick = { navController.popBackStack() },
                onLogoutClick = {
                    // ✅ Opción 2 (la que te gusta): navegación limpia al hacer logout.
                    // Elimina el backstack y vuelve a Login como nueva raíz.
                    navController.navigate(LoginDestination.route) {
                        popUpTo(0) { inclusive = true } // borra TODO el stack
                        launchSingleTop = true
                    }
                }
            )
        }
    }
}
