package com.luis.lifemusic.ui.home

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import com.luis.lifemusic.page.HomePage
import com.luis.lifemusic.ui.AppViewModelProvider

/**
 * HomeRoute = capa contenedora entre HomePage (UI pura)
 * y HomeViewModel (lógica + estado).
 *
 * ✅ Responsabilidades:
 * - Obtener el ViewModel mediante la Factory global.
 * - Observar el UiState (StateFlow).
 * - Pasar estado y callbacks a HomePage.
 * - Gestionar eventos de sesión (expirada / no válida).
 *
 * ❌ No navega directamente.
 * - Solo emite eventos hacia el NavHost mediante callbacks.
 */
@Composable
fun HomeRoute(
    onNavigateToList: () -> Unit,
    onNavigateToProfile: () -> Unit,
    onNavigateToDetail: (Int) -> Unit,
    onSessionExpired: () -> Unit,
    viewModel: HomeViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    // Observamos el estado del ViewModel.
    val uiState by viewModel.uiState.collectAsState()

    /**
     * Guard de sesión:
     * Si el usuario pierde la sesión (DataStore → sessionUserId = null),
     * notificamos al NavHost para que vuelva a Login
     * limpiando el back stack.
     */
    LaunchedEffect(uiState.hasActiveSession) {
        if (!uiState.hasActiveSession) {
            onSessionExpired()
        }
    }

    // Delegamos renderizado a la UI pura.
    HomePage(
        recommendedSongs = uiState.recommendedSongs,
        newReleaseSongs = uiState.newReleaseSongs,
        isLoading = uiState.isLoading,
        errorMessage = uiState.errorMessage,
        onRetry = viewModel::loadHome,
        onNavigateToList = onNavigateToList,
        onNavigateToProfile = onNavigateToProfile,
        onNavigateToDetail = onNavigateToDetail
    )
}
