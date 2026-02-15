package com.luis.lifemusic.ui.recover

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.luis.lifemusic.data.repository.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/**
 * ViewModel de Recover.
 *
 * ✅ Responsabilidades:
 * - Gestionar el RecoverUiState.
 * - Buscar cuenta y cargar pregunta de seguridad.
 * - Validar respuesta + actualizar contraseña vía UserRepository.
 *
 * ✅ Regla:
 * - No navega directamente.
 * - Devuelve resultado por callback para que la Route/NavHost decidan navegación.
 */
class RecoverViewModel(
    private val userRepository: UserRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(RecoverUiState())
    val uiState: StateFlow<RecoverUiState> = _uiState

    fun onUsernameChange(value: String) {
        _uiState.update {
            it.copy(
                username = value,
                errorMessage = null,
                successMessage = null,
                // Si cambia usuario, reiniciamos el paso 2.
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

    fun searchUser() {
        val username = _uiState.value.username.trim()
        if (username.isBlank()) {
            _uiState.update { it.copy(errorMessage = "Introduce un usuario", successMessage = null) }
            return
        }

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null, successMessage = null) }

            val question = userRepository.getSecurityQuestion(username)
            if (question.isNullOrBlank()) {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        isQuestionLoaded = false,
                        securityQuestion = "",
                        errorMessage = "No existe una cuenta con ese usuario"
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

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null, successMessage = null) }

            val ok = userRepository.resetPassword(
                username = state.username,
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
