package com.luis.lifemusic.ui.detail

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import com.luis.lifemusic.page.DetailPage
import com.luis.lifemusic.ui.AppViewModelProvider

/**
 * DetailRoute conecta el DetailViewModel con la DetailPage.
 */
@Composable
fun DetailRoute(
    onBackClick: () -> Unit,
    onSessionExpired: () -> Unit,
    viewModel: DetailViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(uiState.hasActiveSession) {
        if (!uiState.hasActiveSession) {
            onSessionExpired()
        }
    }

    DetailPage(
        song = uiState.song,
        isFavorite = uiState.isFavorite,
        isLoading = uiState.isLoading,
        errorMessage = uiState.errorMessage,
        // Corregido: La carga es automática, onRetry podría re-disparar la observación si fuera necesario.
        onRetry = { /* Lógica de reintento si se implementa */ }, 
        // Corregido: El evento ahora se llama toggleFavorite.
        onFavoriteClick = viewModel::toggleFavorite,
        onBackClick = onBackClick
    )
}
