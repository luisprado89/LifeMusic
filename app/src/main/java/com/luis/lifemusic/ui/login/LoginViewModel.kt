package com.luis.lifemusic.ui.login

import android.util.Patterns
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.luis.lifemusic.data.repository.SessionRepository
import com.luis.lifemusic.data.repository.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/**
 * LoginViewModel
 *
 * ✅ Objetivo:
 * - Gestionar el estado de Login (LoginUiState) con StateFlow.
 * - Validar inputs antes de consultar Room.
 * - Ejecutar login SOLO por email usando UserRepository.
 * - Persistir sesión en DataStore (SessionRepository) guardando el userId.
 *
 * ✅ Reglas del proyecto:
 * - Login SOLO con email (email único en Room).
 * - No existe "username" como credencial.
 * - No navega: devuelve el resultado por callback y la navegación la decide AppNavHost.
 */
class LoginViewModel(
    private val userRepository: UserRepository,
    private val sessionRepository: SessionRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState: StateFlow<LoginUiState> = _uiState

    fun onEmailChange(value: String) {
        _uiState.update { it.copy(email = value, errorMessage = null) }
    }

    fun onPasswordChange(value: String) {
        _uiState.update { it.copy(password = value, errorMessage = null) }
    }

    fun tryLogin(onResult: (Boolean) -> Unit) {
        val current = _uiState.value

        if (current.email.isBlank() || current.password.isBlank()) {
            _uiState.update { it.copy(errorMessage = "Rellena correo y contraseña") }
            onResult(false)
            return
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(current.email.trim()).matches()) {
            _uiState.update { it.copy(errorMessage = "Introduce un correo válido") }
            onResult(false)
            return
        }

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }

            val userId = userRepository.login(
                email = current.email.trim().lowercase(),
                password = current.password
            )

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