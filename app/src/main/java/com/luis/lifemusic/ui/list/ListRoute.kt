package com.luis.lifemusic.ui.list

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import com.luis.lifemusic.page.ListPage
import com.luis.lifemusic.ui.AppViewModelProvider

@Composable
fun ListRoute(
    onBackClick: () -> Unit,
    onNavigateToDetail: (String) -> Unit,
    onSessionExpired: () -> Unit,
    viewModel: ListViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(uiState.hasActiveSession) {
        if (!uiState.hasActiveSession) onSessionExpired()
    }

    ListPage(
        favoriteSongs = uiState.favoriteSongs,
        isLoading = uiState.isLoading,
        errorMessage = uiState.errorMessage,
        missingRemoteCount = uiState.missingRemoteCount, // ✅ NUEVO
        onFavoriteClick = viewModel::toggleFavorite,
        onNavigateToDetail = onNavigateToDetail,
        onBackClick = onBackClick
    )
}