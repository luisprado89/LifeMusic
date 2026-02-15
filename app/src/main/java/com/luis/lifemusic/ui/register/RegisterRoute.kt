package com.luis.lifemusic.ui.register

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import com.luis.lifemusic.page.RegisterPage
import com.luis.lifemusic.ui.AppViewModelProvider

/**
 * RegisterRoute = contenedor que conecta RegisterViewModel con RegisterPage (UI pura).
 *
 * ✅ Responsabilidades:
 * - Obtener RegisterViewModel usando la Factory global.
 * - Observar RegisterUiState como única fuente de verdad.
 * - Conectar eventos de UI con funciones del ViewModel.
 * - Notificar al NavHost cuando el registro es exitoso.
 *
 * 🔐 Arquitectura:
 * - No contiene lógica de negocio.
 * - No navega directamente.
 * - Solo delega navegación al nivel superior (AppNavHost).
 */
@Composable
fun RegisterRoute(
    onRegisterSuccess: () -> Unit,
    onBackClick: () -> Unit,
    viewModel: RegisterViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    /**
     * Recogemos el estado observable del ViewModel.
     *
     * Este estado es la única fuente de verdad para la pantalla de registro,
     * evitando remember/local state para datos de negocio.
     */
    val uiState by viewModel.uiState.collectAsState()

    RegisterPage(
        // -------------------------
        // Estado
        // -------------------------
        displayName = uiState.displayName,
        email = uiState.email,
        birthDate = uiState.birthDate,
        password = uiState.password,
        confirmPassword = uiState.confirmPassword,
        securityQuestion = uiState.securityQuestion,
        securityAnswer = uiState.securityAnswer,
        isLoading = uiState.isLoading,
        errorMessage = uiState.errorMessage,

        // -------------------------
        // Eventos
        // -------------------------
        onDisplayNameChange = viewModel::onDisplayNameChange,
        onEmailChange = viewModel::onEmailChange,
        onBirthDateChange = viewModel::onBirthDateChange,
        onPasswordChange = viewModel::onPasswordChange,
        onConfirmPasswordChange = viewModel::onConfirmPasswordChange,
        onSecurityQuestionChange = viewModel::onSecurityQuestionChange,
        onSecurityAnswerChange = viewModel::onSecurityAnswerChange,

        onRegisterClick = {
            /**
             * El ViewModel valida y registra.
             *
             * Si el resultado es correcto, avisamos al nivel superior para
             * que AppNavHost navegue (sin acoplar la pantalla a rutas concretas).
             */
            viewModel.tryRegister { isSuccess ->
                if (isSuccess) {
                    onRegisterSuccess()
                }
            }
        },

        onBackClick = onBackClick
    )
}

