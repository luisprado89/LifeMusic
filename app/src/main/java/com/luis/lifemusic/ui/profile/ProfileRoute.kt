package com.luis.lifemusic.ui.profile

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import com.luis.lifemusic.page.ProfilePage
import com.luis.lifemusic.ui.AppViewModelProvider

/**
 * ProfileRoute = capa contenedora entre ProfilePage (UI pura) y ProfileViewModel.
 *
 * ✅ Responsabilidades:
 * - Conectar la UI con el estado expuesto por ProfileViewModel.
 * - Delegar eventos de usuario (editar, guardar, logout).
 * - Aplicar guard de sesión (DataStore).
 *
 * 🔒 Guard de sesión:
 * - Si hasActiveSession pasa a false,
 *   delegamos en NavHost para volver a Login y limpiar back stack.
 *
 * 📌 Regla de arquitectura:
 * - ProfilePage no conoce repositorios.
 * - ProfilePage no conoce DataStore.
 * - ProfilePage no conoce navegación.
 * - Solo recibe estado y callbacks.
 */
@Composable
fun ProfileRoute(
    onBackClick: () -> Unit,
    onSessionExpired: () -> Unit,
    onNavigateToCamera: () -> Unit,
    viewModel: ProfileViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(uiState.hasActiveSession) {
        if (!uiState.hasActiveSession) onSessionExpired()
    }

    ProfilePage(
        name = uiState.name,
        email = uiState.email,
        birthDate = uiState.birthDate,
        verified = uiState.verified,
        photoUri = uiState.photoUri,
        isEditing = uiState.isEditing,
        isLoading = uiState.isLoading,
        errorMessage = uiState.errorMessage,
        onNameChange = viewModel::onNameChange,
        onEmailChange = viewModel::onEmailChange,
        onEditClick = viewModel::onEditClick,
        onCancelEdit = viewModel::onCancelEdit,
        onSaveChanges = viewModel::onSaveChanges,
        onChangePhotoClick = onNavigateToCamera,
        onLogoutClick = viewModel::onLogoutClick,
        onBackClick = onBackClick
    )
}