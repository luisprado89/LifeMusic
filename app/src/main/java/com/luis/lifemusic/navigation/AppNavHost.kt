package com.luis.lifemusic.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.luis.lifemusic.page.*
import com.luis.lifemusic.ui.detail.DetailRoute
import com.luis.lifemusic.ui.home.HomeRoute
import com.luis.lifemusic.ui.list.ListRoute
import com.luis.lifemusic.ui.login.LoginRoute
import com.luis.lifemusic.ui.profile.ProfileRoute
import com.luis.lifemusic.ui.recover.RecoverRoute
import com.luis.lifemusic.ui.register.RegisterRoute

/**
 * AppNavHost centraliza toda la navegación de la app.
 *
 * ✅ Regla:
 * - Navegamos por IDs estables (songId) y NO por títulos.
 *
 * ✅ Arquitectura:
 * - Las pantallas "Page" son UI pura.
 * - Las pantallas "Route" conectan UI con ViewModels (MVVM).
 * - El guard de sesión se aplica en cada Route (hasActiveSession).
 *
 * 🔜 Evolución:
 * - Sustituir sampleSongs por repositorios reales (Room/Retrofit).
 * - Manteniendo la inyección desde AppViewModelProvider.
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
             * LoginRoute:
             * - Conecta LoginPage (UI pura) con LoginViewModel.
             * - La navegación se resuelve aquí en NavHost (no en el ViewModel).
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
            /**
             * RegisterRoute:
             * - Conecta RegisterPage (UI pura) con RegisterViewModel.
             * - Tras registro exitoso, navegamos a Home limpiando back stack.
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
             * - Conecta RecoverPasswordPage (UI pura) con RecoverViewModel.
             * - Gestiona recuperación por pregunta de seguridad.
             */
            RecoverRoute(
                onBackClick = { navController.popBackStack() }
            )
        }

        // ---------------------------
        // MAIN FLOW
        // ---------------------------

        composable(HomeDestination.route) {
            /**
             * HomeRoute:
             * - Conecta HomePage (UI pura) con HomeViewModel.
             * - Incluye guard de sesión:
             *   si se pierde la sesión, vuelve a Login limpiando el back stack.
             */
            HomeRoute(
                onNavigateToList = { navController.navigate(ListDestination.route) },
                onNavigateToProfile = { navController.navigate(ProfileDestination.route) },
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

        composable(ListDestination.route) {
            /**
             * ListRoute:
             * - Conecta ListPage (UI pura) con ListViewModel.
             * - Incluye guard de sesión:
             *   si se pierde la sesión, vuelve a Login limpiando el back stack.
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
        ) {
            /**
             * DetailRoute:
             * - Conecta DetailPage (UI pura) con DetailViewModel.
             * - Lee songId desde argumentos (SavedStateHandle).
             * - Gestiona favoritos reales (Room) por usuario.
             * - Incluye guard de sesión:
             *   si se pierde la sesión, vuelve a Login limpiando el back stack.
             */
            DetailRoute(
                onBackClick = { navController.popBackStack() },
                onSessionExpired = {
                    navController.navigate(LoginDestination.route) {
                        popUpTo(0) { inclusive = true }
                        launchSingleTop = true
                    }
                }
            )
        }

        composable(ProfileDestination.route) {
            /**
             * ProfileRoute:
             * - Conecta ProfilePage (UI pura) con ProfileViewModel.
             * - Permite editar nombre/email y guardar en Room.
             * - Logout: lo ejecuta el ViewModel limpiando DataStore.
             *   Cuando eso ocurre, el guard de sesión detecta session inválida y
             *   se dispara onSessionExpired (redirección a Login).
             */
            ProfileRoute(
                onBackClick = { navController.popBackStack() },
                onSessionExpired = {
                    navController.navigate(LoginDestination.route) {
                        popUpTo(0) { inclusive = true }
                        launchSingleTop = true
                    }
                }
            )
        }
    }
}
