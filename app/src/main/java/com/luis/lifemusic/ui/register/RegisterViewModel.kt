package com.luis.lifemusic.ui.register

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
 * RegisterViewModel
 *
 * ✅ Objetivo:
 * - Gestionar el estado de registro (RegisterUiState).
 * - Validar campos obligatorios antes de ir a Room.
 * - Registrar usuario en Room a través de UserRepository.
 * - Crear sesión (DataStore) tras registro exitoso.
 *
 * ✅ Reglas de tu app:
 * - Login SOLO con email (email único en DB).
 * - Nombre completo obligatorio (displayName).
 * - Fecha de nacimiento obligatoria (birthDate).
 * - Confirm password es SOLO UI (no se guarda).
 * - Recuperación: pregunta + respuesta obligatorias.
 */
class RegisterViewModel(
    private val userRepository: UserRepository,
    private val sessionRepository: SessionRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(RegisterUiState())
    val uiState: StateFlow<RegisterUiState> = _uiState

    // -----------------------------
    // Eventos UI (inputs)
    // -----------------------------

    fun onDisplayNameChange(v: String) =
        _uiState.update { it.copy(displayName = v, errorMessage = null) }

    fun onEmailChange(v: String) =
        _uiState.update { it.copy(email = v, errorMessage = null) }

    fun onPasswordChange(v: String) =
        _uiState.update { it.copy(password = v, errorMessage = null) }

    fun onConfirmPasswordChange(v: String) =
        _uiState.update { it.copy(confirmPassword = v, errorMessage = null) }

    /**
     * birthDate se guarda como Long (epoch millis).
     * - Obligatoria (no puede ser null).
     */
    fun onBirthDateChange(v: Long?) =
        _uiState.update { it.copy(birthDate = v, errorMessage = null) }

    fun onSecurityQuestionChange(v: String) =
        _uiState.update { it.copy(securityQuestion = v, errorMessage = null) }

    fun onSecurityAnswerChange(v: String) =
        _uiState.update { it.copy(securityAnswer = v, errorMessage = null) }

    // -----------------------------
    // Acción principal
    // -----------------------------

    fun tryRegister(onResult: (Boolean) -> Unit) {
        val s = _uiState.value

        val displayName = s.displayName.trim()
        val email = s.email.trim().lowercase()
        val password = s.password
        val confirmPassword = s.confirmPassword
        val birthDate = s.birthDate
        val securityQuestion = s.securityQuestion.trim()
        val securityAnswer = s.securityAnswer.trim()

        // -------- Validaciones --------

        if (displayName.isBlank() ||
            email.isBlank() ||
            password.isBlank() ||
            confirmPassword.isBlank() ||
            securityQuestion.isBlank() ||
            securityAnswer.isBlank()
        ) {
            _uiState.update { it.copy(errorMessage = "Completa todos los campos obligatorios") }
            onResult(false)
            return
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            _uiState.update { it.copy(errorMessage = "Introduce un correo válido") }
            onResult(false)
            return
        }

        if (password.length < 6) {
            _uiState.update { it.copy(errorMessage = "La contraseña debe tener al menos 6 caracteres") }
            onResult(false)
            return
        }

        if (password != confirmPassword) {
            _uiState.update { it.copy(errorMessage = "Las contraseñas no coinciden") }
            onResult(false)
            return
        }

        if (birthDate == null) {
            _uiState.update { it.copy(errorMessage = "Selecciona tu fecha de nacimiento") }
            onResult(false)
            return
        }

        // -------- Registro --------

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            try {
                val userId = userRepository.register(
                    displayName = displayName,
                    email = email,
                    password = password,
                    birthDate = birthDate,
                    securityQuestion = securityQuestion,
                    securityAnswer = securityAnswer
                )

                // ✅ Auto-login: guardamos sesión en DataStore
                sessionRepository.setLoggedInUserId(userId)

                _uiState.update { it.copy(isLoading = false) }
                onResult(true)

            } catch (_: Exception) {
                // Con índice UNIQUE(email), aquí saltará si ya existe ese correo.
                _uiState.update {
                    it.copy(isLoading = false, errorMessage = "Ese correo ya existe")
                }
                onResult(false)
            }
        }
    }
}
