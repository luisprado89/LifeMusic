package com.luis.lifemusic.ui.list

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import com.luis.lifemusic.page.ListPage
import com.luis.lifemusic.ui.AppViewModelProvider

/**
 * ListRoute conecta el ListViewModel con la ListPage.
 * Ahora gestiona la lista de favoritos.
 */
@Composable
fun ListRoute(
    onNavigateToDetail: (String) -> Unit, // Ahora es String
    onBackClick: () -> Unit,
    onSessionExpired: () -> Unit,
    viewModel: ListViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(uiState.hasActiveSession) {
        if (!uiState.hasActiveSession) {
            onSessionExpired()
        }
    }

    ListPage(
        // Pasamos la lista de favoritos y los callbacks correctos
        favoriteSongs = uiState.favoriteSongs,
        isLoading = uiState.isLoading,
        errorMessage = uiState.errorMessage,
        onFavoriteClick = viewModel::removeFavorite, // Conectamos el click del corazón para eliminar
        onNavigateToDetail = onNavigateToDetail,
        onBackClick = onBackClick
    )
}
