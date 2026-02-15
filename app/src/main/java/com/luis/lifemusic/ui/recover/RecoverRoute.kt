package com.luis.lifemusic.ui.recover

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import com.luis.lifemusic.page.RecoverPasswordPage
import com.luis.lifemusic.ui.AppViewModelProvider

/**
 * RecoverRoute = puente entre RecoverViewModel y RecoverPasswordPage (UI pura).
 *
 * ✅ Hace de capa contenedora:
 * - Obtiene ViewModel con la Factory global.
 * - Observa uiState.
 * - Conecta acciones de UI con funciones del ViewModel.
 */
@Composable
fun RecoverRoute(
    onBackClick: () -> Unit,
    viewModel: RecoverViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val uiState by viewModel.uiState.collectAsState()

    RecoverPasswordPage(
        username = uiState.username,
        securityQuestion = uiState.securityQuestion,
        securityAnswer = uiState.securityAnswer,
        newPassword = uiState.newPassword,
        isQuestionLoaded = uiState.isQuestionLoaded,
        isLoading = uiState.isLoading,
        onUsernameChange = viewModel::onUsernameChange,
        onSecurityAnswerChange = viewModel::onSecurityAnswerChange,
        onNewPasswordChange = viewModel::onNewPasswordChange,
        onSearchUserClick = viewModel::searchUser,
        onResetPasswordClick = {
            // No navegamos automáticamente: mostramos feedback en la misma pantalla.
            viewModel.resetPassword(onResult = {})
        },
        errorMessage = uiState.errorMessage,
        successMessage = uiState.successMessage,
        onBackClick = onBackClick
    )
}
