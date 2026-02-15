package com.luis.lifemusic.ui.list

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import com.luis.lifemusic.page.ListPage
import com.luis.lifemusic.ui.AppViewModelProvider

/**
 * ListRoute
 *
 * Capa contenedora entre ListPage (UI pura)
 * y ListViewModel (lógica + estado).
 *
 * ✅ Responsabilidades:
 * - Obtener el ViewModel usando la Factory global.
 * - Observar el UiState (StateFlow).
 * - Traducir estado + eventos a la UI.
 * - Gestionar guard de sesión sin meter navegación en el ViewModel.
 *
 * ❌ El ViewModel NO navega.
 * ❌ La Page NO conoce repositorios.
 */
@Composable
fun ListRoute(
    onNavigateToDetail: (Int) -> Unit,
    onBackClick: () -> Unit,
    onSessionExpired: () -> Unit,
    viewModel: ListViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    // Observamos el estado emitido por el ViewModel.
    val uiState by viewModel.uiState.collectAsState()

    /**
     * Guard de sesión:
     * Si se pierde la sesión (userId null en DataStore),
     * notificamos al NavHost para redirigir a Login.
     */
    LaunchedEffect(uiState.hasActiveSession) {
        if (!uiState.hasActiveSession) {
            onSessionExpired()
        }
    }

    // Delegamos renderizado a la UI pura.
    ListPage(
        songs = uiState.songs,
        isLoading = uiState.isLoading,
        errorMessage = uiState.errorMessage,
        onRetry = viewModel::loadSongs,
        onNavigateToDetail = onNavigateToDetail,
        onBackClick = onBackClick
    )
}
