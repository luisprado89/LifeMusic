package com.luis.lifemusic.ui.home

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import com.luis.lifemusic.page.HomePage
import com.luis.lifemusic.ui.AppViewModelProvider

/**
 * HomeRoute conecta el HomeViewModel con la HomePage.
 */
@Composable
fun HomeRoute(
    onNavigateToList: () -> Unit,
    onNavigateToProfile: () -> Unit,
    onNavigateToDetail: (String) -> Unit, // Ahora es String
    onSessionExpired: () -> Unit,
    viewModel: HomeViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(uiState.hasActiveSession) {
        if (!uiState.hasActiveSession) {
            onSessionExpired()
        }
    }

    HomePage(
        // Pasamos las 3 listas de canciones
        recommendedSongs = uiState.recommendedSongs,
        newReleaseSongs = uiState.newReleaseSongs,
        popularSongs = uiState.popularSongs,

        isLoading = uiState.isLoading,
        errorMessage = uiState.errorMessage,
        
        // Los eventos se conectan directamente a las funciones del ViewModel
        onRetry = { /* Lógica de reintento si se necesita */ },
        onFavoriteClick = viewModel::addFavorite, // Conectamos el click del corazón
        onNavigateToDetail = onNavigateToDetail, // Pasamos el callback de navegación
        
        // Navegación a otras pantallas
        onNavigateToList = onNavigateToList,
        onNavigateToProfile = onNavigateToProfile
    )
}
