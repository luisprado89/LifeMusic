package com.luis.lifemusic.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.luis.lifemusic.camera.CameraScreen
import com.luis.lifemusic.page.CameraDestination
import com.luis.lifemusic.page.DetailDestination
import com.luis.lifemusic.page.HomeDestination
import com.luis.lifemusic.page.ListDestination
import com.luis.lifemusic.page.LoginDestination
import com.luis.lifemusic.page.ProfileDestination
import com.luis.lifemusic.page.RecoverDestination
import com.luis.lifemusic.page.RegisterDestination
import com.luis.lifemusic.ui.AppViewModelProvider
import com.luis.lifemusic.ui.detail.DetailRoute
import com.luis.lifemusic.ui.home.HomeRoute
import com.luis.lifemusic.ui.list.ListRoute
import com.luis.lifemusic.ui.login.LoginRoute
import com.luis.lifemusic.ui.profile.ProfileRoute
import com.luis.lifemusic.ui.profile.ProfileViewModel
import com.luis.lifemusic.ui.recover.RecoverRoute
import com.luis.lifemusic.ui.register.RegisterRoute

/**
 * AppNavHost centraliza toda la navegación de la app.
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
            RecoverRoute(
                onBackClick = { navController.popBackStack() }
            )
        }

        // ---------------------------
        // MAIN FLOW
        // ---------------------------

        composable(HomeDestination.route) {
            HomeRoute(
                onNavigateToList = { navController.navigate(ListDestination.route) },
                onNavigateToProfile = { navController.navigate(ProfileDestination.route) },
                onNavigateToDetail = { spotifyId -> // Ahora se espera un String
                    navController.navigate("${DetailDestination.route}/$spotifyId")
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
            ListRoute(
                onBackClick = { navController.popBackStack() },
                onNavigateToDetail = { spotifyId -> // Ahora se espera un String
                    navController.navigate("${DetailDestination.route}/$spotifyId")
                },
                onSessionExpired = {
                    navController.navigate(LoginDestination.route) {
                        popUpTo(0) { inclusive = true }
                        launchSingleTop = true
                    }
                }
            )
        }

        // DETAIL con argumento de tipo String
        composable(
            route = DetailDestination.routeWithArgs,
            arguments = listOf(
                // Corregido: Usa el argumento correcto (spotifyIdArg) y el tipo correcto (StringType)
                navArgument(DetailDestination.spotifyIdArg) { type = NavType.StringType }
            )
        ) {
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
            ProfileRoute(
                onBackClick = { navController.popBackStack() },
                onSessionExpired = {
                    navController.navigate(LoginDestination.route) {
                        popUpTo(0) { inclusive = true }
                        launchSingleTop = true
                    }
                },
                onNavigateToCamera = { navController.navigate(CameraDestination.route) }
            )
        }

        // ✅ Cámara (usa el MISMO ProfileViewModel para guardar photoUri)
        composable(CameraDestination.route) { backStackEntry ->
            val parentEntry = remember(backStackEntry) {
                navController.getBackStackEntry(ProfileDestination.route)
            }
            val profileViewModel: ProfileViewModel =
                viewModel(parentEntry, factory = AppViewModelProvider.Factory)

            CameraScreen(
                onBackClick = { navController.popBackStack() },
                onPhotoCaptured = { uriString ->
                    profileViewModel.onPhotoCaptured(uriString)
                    navController.popBackStack()
                }
            )
        }
    }
}
