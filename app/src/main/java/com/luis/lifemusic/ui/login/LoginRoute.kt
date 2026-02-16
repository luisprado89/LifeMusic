package com.luis.lifemusic.ui.login

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.collectAsState
import androidx.lifecycle.viewmodel.compose.viewModel
import com.luis.lifemusic.page.LoginPage
import com.luis.lifemusic.ui.AppViewModelProvider

/**
 * LoginRoute = puente entre UI pura y ViewModel.
 *
 * Responsabilidades:
 * - Obtener LoginViewModel con la Factory global.
 * - Observar LoginUiState como única fuente de verdad.
 * - Conectar eventos UI con funciones del ViewModel.
 * - Notificar al NavHost cuando el login es exitoso.
 *
 * Arquitectura:
 * - No contiene lógica de negocio.
 * - No navega directamente.
 * - Solo delega navegación al nivel superior.
 */
@Composable
fun LoginRoute(
    onLoginSuccess: () -> Unit,
    onGoToRegister: () -> Unit,
    onGoToRecover: () -> Unit,
    viewModel: LoginViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val uiState by viewModel.uiState.collectAsState()

    LoginPage(
        // Estado
        email = uiState.email,
        password = uiState.password,
        isLoading = uiState.isLoading,
        errorMessage = uiState.errorMessage,

        // Eventos
        onEmailChange = viewModel::onEmailChange,
        onPasswordChange = viewModel::onPasswordChange,
        onLoginClick = {
            viewModel.tryLogin { isSuccess ->
                if (isSuccess) onLoginSuccess()
            }
        },
        onGoToRegister = onGoToRegister,
        onGoToRecover = onGoToRecover
    )
}
