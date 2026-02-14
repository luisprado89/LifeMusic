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
 * ✅ Responsabilidad:
 * - Mantener estado (username/password/loading/error)
 * - Ejecutar login contra UserRepository
 * - Si es OK, guardar userId en SessionRepository (DataStore)
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

    /**
     * Devuelve true si el login fue correcto.
     * (La navegación NO la hace el VM; la hará la pantalla con callback)
     */
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
                    it.copy(isLoading = false, errorMessage = "Credenciales incorrectas")
                }
                onResult(false)
            }
        }
    }
}
