package com.luis.lifemusic.ui.recover

import android.util.Patterns
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.luis.lifemusic.data.repository.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/**
 * RecoverViewModel
 *
 * ✅ Responsabilidades:
 * - Gestionar el RecoverUiState.
 * - Paso 1: buscar cuenta por email y cargar pregunta de seguridad.
 * - Paso 2: validar respuesta + actualizar contraseña vía UserRepository.
 *
 * ✅ Reglas del proyecto:
 * - Recuperación SOLO por email (mismo identificador que login).
 * - No navega directamente: devuelve resultado por callback.
 */
class RecoverViewModel(
    private val userRepository: UserRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(RecoverUiState())
    val uiState: StateFlow<RecoverUiState> = _uiState

    // -----------------------------
    // Eventos UI
    // -----------------------------

    fun onEmailChange(value: String) {
        _uiState.update {
            it.copy(
                email = value,
                errorMessage = null,
                successMessage = null,
                // Si cambia el email reiniciamos el paso 2.
                isQuestionLoaded = false,
                securityQuestion = "",
                securityAnswer = "",
                newPassword = ""
            )
        }
    }

    fun onSecurityAnswerChange(value: String) {
        _uiState.update { it.copy(securityAnswer = value, errorMessage = null, successMessage = null) }
    }

    fun onNewPasswordChange(value: String) {
        _uiState.update { it.copy(newPassword = value, errorMessage = null, successMessage = null) }
    }

    // -----------------------------
    // Paso 1: Buscar cuenta
    // -----------------------------

    fun searchUser() {
        val email = _uiState.value.email.trim()

        if (email.isBlank()) {
            _uiState.update { it.copy(errorMessage = "Introduce un correo", successMessage = null) }
            return
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            _uiState.update { it.copy(errorMessage = "Introduce un correo válido", successMessage = null) }
            return
        }

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null, successMessage = null) }

            val question = userRepository.getSecurityQuestion(email)

            if (question.isNullOrBlank()) {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        isQuestionLoaded = false,
                        securityQuestion = "",
                        errorMessage = "No existe una cuenta con ese correo"
                    )
                }
            } else {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        isQuestionLoaded = true,
                        securityQuestion = question,
                        errorMessage = null
                    )
                }
            }
        }
    }

    // -----------------------------
    // Paso 2: Reset de contraseña
    // -----------------------------

    fun resetPassword(onResult: (Boolean) -> Unit) {
        val state = _uiState.value

        if (!state.isQuestionLoaded) {
            _uiState.update { it.copy(errorMessage = "Primero busca la cuenta") }
            onResult(false)
            return
        }

        if (state.securityAnswer.isBlank() || state.newPassword.isBlank()) {
            _uiState.update { it.copy(errorMessage = "Completa respuesta y nueva contraseña") }
            onResult(false)
            return
        }

        if (state.newPassword.length < 6) {
            _uiState.update { it.copy(errorMessage = "La nueva contraseña debe tener al menos 6 caracteres") }
            onResult(false)
            return
        }

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null, successMessage = null) }

            val ok = userRepository.resetPassword(
                email = state.email.trim().lowercase(),
                securityAnswer = state.securityAnswer,
                newPassword = state.newPassword
            )

            if (ok) {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        successMessage = "Contraseña actualizada correctamente",
                        // Limpiamos campos sensibles tras éxito.
                        securityAnswer = "",
                        newPassword = ""
                    )
                }
                onResult(true)
            } else {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = "La respuesta de seguridad no es correcta"
                    )
                }
                onResult(false)
            }
        }
    }
}
