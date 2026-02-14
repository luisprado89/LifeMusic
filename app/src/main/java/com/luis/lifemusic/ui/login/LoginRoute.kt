package com.luis.lifemusic.ui.login

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.runtime.collectAsState
import com.luis.lifemusic.page.LoginPage
import com.luis.lifemusic.ui.AppViewModelProvider

/**
 * LoginRoute = puente entre UI pura y ViewModel.
 *
 * - Obtiene LoginViewModel con factory global.
 * - Observa uiState.
 * - Pasa datos/eventos a LoginPage.
 * - Expone callbacks de navegación al nivel superior.
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
        username = uiState.username,
        password = uiState.password,
        isLoading = uiState.isLoading,
        errorMessage = uiState.errorMessage,
        onUsernameChange = viewModel::onUsernameChange,
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
