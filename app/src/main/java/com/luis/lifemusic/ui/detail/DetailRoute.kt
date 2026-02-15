package com.luis.lifemusic.ui.detail

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import com.luis.lifemusic.page.DetailPage
import com.luis.lifemusic.ui.AppViewModelProvider

/**
 * DetailRoute
 *
 * Capa contenedora entre DetailPage (UI pura)
 * y DetailViewModel (lógica + estado).
 *
 * ✅ Responsabilidades:
 * - Obtener el ViewModel desde la Factory global.
 * - Observar el UiState (StateFlow).
 * - Traducir estado + eventos a la UI.
 * - Gestionar guard de sesión sin mezclar navegación en el ViewModel.
 *
 * ❌ El ViewModel NO navega.
 * ❌ La Page NO conoce repositorios.
 */
@Composable
fun DetailRoute(
    onBackClick: () -> Unit,
    onSessionExpired: () -> Unit,
    viewModel: DetailViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    // Observamos el estado emitido por el ViewModel.
    val uiState by viewModel.uiState.collectAsState()

    /**
     * Guard de sesión:
     * Si la sesión deja de ser válida (userId null en DataStore),
     * avisamos al NavHost para volver a Login.
     */
    LaunchedEffect(uiState.hasActiveSession) {
        if (!uiState.hasActiveSession) {
            onSessionExpired()
        }
    }

    // Delegamos renderizado a la UI pura.
    DetailPage(
        song = uiState.song,
        isFavorite = uiState.isFavorite,
        isLoading = uiState.isLoading,
        errorMessage = uiState.errorMessage,
        onRetry = viewModel::loadDetail,
        onFavoriteClick = viewModel::onFavoriteClick,
        onBackClick = onBackClick
    )
}
