package com.luis.lifemusic.ui.home

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import com.luis.lifemusic.page.HomePage
import com.luis.lifemusic.ui.AppViewModelProvider

/**
 * ============================================================
 * HOME ROUTE
 * ============================================================
 *
 * 🎯 RESPONSABILIDAD:
 * - Conectar la UI (HomePage) con el estado del ViewModel (HomeViewModel).
 * - Gestionar efectos de navegación en base a estado (ej: sesión expirada).
 *
 * ✅ ARQUITECTURA:
 * - HomeRoute NO contiene lógica de negocio.
 * - Solo:
 *   1) Observa uiState (StateFlow)
 *   2) Dispara efectos (LaunchedEffect)
 *   3) Pasa callbacks a HomePage
 */
@Composable
fun HomeRoute(
    onNavigateToList: () -> Unit,
    onNavigateToProfile: () -> Unit,
    onNavigateToDetail: (String) -> Unit, // spotifyId
    onSessionExpired: () -> Unit,
    viewModel: HomeViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    // Observamos el estado del ViewModel
    val uiState by viewModel.uiState.collectAsState()

    /**
     * Si la sesión deja de estar activa, redirigimos a login
     * (o al flujo que tengas configurado en la navegación).
     */
    LaunchedEffect(uiState.hasActiveSession) {
        if (!uiState.hasActiveSession) {
            onSessionExpired()
        }
    }

    // Renderizamos la pantalla principal con sus datos y eventos
    HomePage(
        // Listas de canciones (3 secciones)
        recommendedSongs = uiState.recommendedSongs,
        newReleaseSongs = uiState.newReleaseSongs,
        popularSongs = uiState.popularSongs,

        // Estados globales
        isLoading = uiState.isLoading,
        errorMessage = uiState.errorMessage,

        // Mensajes informativos
        offlineNoticeMessage = uiState.offlineNoticeMessage,
        recommendedInfoMessage = uiState.recommendedInfoMessage,

        // Eventos conectados directamente al ViewModel
        onRetry = viewModel::refreshContent,
        onFavoriteClick = viewModel::addFavorite,
        onNavigateToDetail = onNavigateToDetail,

        // Navegación
        onNavigateToList = onNavigateToList,
        onNavigateToProfile = onNavigateToProfile
    )
}