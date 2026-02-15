package com.luis.lifemusic.ui.register


import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import com.luis.lifemusic.page.RegisterPage
import com.luis.lifemusic.ui.AppViewModelProvider

/**
 * RegisterRoute = contenedor que conecta ViewModel con la UI pura de RegisterPage.
 *
 * ✅ Responsabilidades de esta Route:
 * - Obtener RegisterViewModel usando la Factory global.
 * - Observar RegisterUiState.
 * - Pasar estado y eventos a RegisterPage.
 * - Comunicar el éxito de registro al NavHost mediante callback.
 *
 * ✅ Importante:
 * - Aquí NO hay lógica de navegación con NavController.
 * - La navegación real la decide AppNavHost (nivel superior).
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
        username = uiState.username,
        password = uiState.password,
        confirmPassword = uiState.confirmPassword,
        securityQuestion = uiState.securityQuestion,
        securityAnswer = uiState.securityAnswer,
        onUsernameChange = viewModel::onUsernameChange,
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
                if (isSuccess) onRegisterSuccess()
            }
        },
        onBackClick = onBackClick
    )
}
