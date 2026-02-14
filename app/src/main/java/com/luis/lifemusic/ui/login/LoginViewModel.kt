package com.luis.lifemusic.ui.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.luis.lifemusic.data.repository.SessionRepository
import com.luis.lifemusic.data.repository.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/**
 * ViewModel de Login.
 *
 * Responsabilidades:
 * - Mantener LoginUiState con StateFlow.
 * - Validar campos.
 * - Ejecutar login en repositorio.
 * - Persistir sesión en DataStore (sessionRepository).
 *
 * Importante:
 * - No navega directamente.
 * - Informa resultado por callback para que la pantalla/NavHost decidan navegación.
 */
class LoginViewModel(
    private val userRepository: UserRepository,
    private val sessionRepository: SessionRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState: StateFlow<LoginUiState> = _uiState

    fun onUsernameChange(value: String) {
        _uiState.update { it.copy(username = value, errorMessage = null) }
    }

    fun onPasswordChange(value: String) {
        _uiState.update { it.copy(password = value, errorMessage = null) }
    }

    fun tryLogin(onResult: (Boolean) -> Unit) {
        val current = _uiState.value

        if (current.username.isBlank() || current.password.isBlank()) {
            _uiState.update { it.copy(errorMessage = "Rellena usuario y contraseña") }
            onResult(false)
            return
        }

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }

            val userId = userRepository.login(current.username, current.password)

            if (userId != null) {
                sessionRepository.setLoggedInUserId(userId)
                _uiState.update { it.copy(isLoading = false) }
                onResult(true)
            } else {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = "Credenciales incorrectas"
                    )
                }
                onResult(false)
            }
        }
    }
}
