package com.luis.lifemusic.ui.recover

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import com.luis.lifemusic.page.RecoverPasswordPage
import com.luis.lifemusic.ui.AppViewModelProvider

/**
 * RecoverRoute
 *
 * 🔗 Puente entre RecoverViewModel y RecoverPasswordPage (UI pura).
 *
 * ✅ Responsabilidades:
 * - Obtener RecoverViewModel usando la Factory global.
 * - Observar RecoverUiState como única fuente de verdad.
 * - Conectar eventos de la UI con funciones del ViewModel.
 *
 * 🧠 Arquitectura:
 * - No contiene lógica de negocio.
 * - No navega directamente.
 * - Solo actúa como capa intermedia entre UI y ViewModel.
 */
@Composable
fun RecoverRoute(
    onBackClick: () -> Unit,
    viewModel: RecoverViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    // Observamos el estado expuesto por el ViewModel
    val uiState by viewModel.uiState.collectAsState()

    RecoverPasswordPage(
        // -------------------------
        // Estado
        // -------------------------
        email = uiState.email,
        securityQuestion = uiState.securityQuestion,
        securityAnswer = uiState.securityAnswer,
        newPassword = uiState.newPassword,
        isQuestionLoaded = uiState.isQuestionLoaded,
        isLoading = uiState.isLoading,
        errorMessage = uiState.errorMessage,
        successMessage = uiState.successMessage,

        // -------------------------
        // Eventos
        // -------------------------
        onEmailChange = viewModel::onEmailChange,
        onSecurityAnswerChange = viewModel::onSecurityAnswerChange,
        onNewPasswordChange = viewModel::onNewPasswordChange,

        onSearchUserClick = viewModel::searchUser,

        onResetPasswordClick = {
            /**
             * No navegamos automáticamente.
             * Mostramos feedback en la misma pantalla.
             *
             * Si más adelante quieres que tras éxito vuelva a Login,
             * aquí sería el lugar correcto para hacerlo mediante callback.
             */
            viewModel.resetPassword(onResult = {})
        },

        onBackClick = onBackClick
    )
}
