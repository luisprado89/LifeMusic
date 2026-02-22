package com.luis.lifemusic.ui.register

import android.util.Patterns
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.luis.lifemusic.data.localsed.localSeedSongs
import com.luis.lifemusic.data.repository.FavoritesRepository
import com.luis.lifemusic.data.repository.SessionRepository
import com.luis.lifemusic.data.repository.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class RegisterViewModel(
    private val userRepository: UserRepository,
    private val sessionRepository: SessionRepository,
    private val favoritesRepository: FavoritesRepository // Inyectamos el repositorio de favoritos
) : ViewModel() {

    private val _uiState = MutableStateFlow(RegisterUiState())
    val uiState: StateFlow<RegisterUiState> = _uiState

    // --- Eventos de UI (sin cambios) ---
    fun onDisplayNameChange(v: String) = _uiState.update { it.copy(displayName = v, errorMessage = null) }
    fun onEmailChange(v: String) = _uiState.update { it.copy(email = v, errorMessage = null) }
    fun onPasswordChange(v: String) = _uiState.update { it.copy(password = v, errorMessage = null) }
    fun onConfirmPasswordChange(v: String) = _uiState.update { it.copy(confirmPassword = v, errorMessage = null) }
    fun onBirthDateChange(v: Long?) = _uiState.update { it.copy(birthDate = v, errorMessage = null) }
    fun onSecurityQuestionChange(v: String) = _uiState.update { it.copy(securityQuestion = v, errorMessage = null) }
    fun onSecurityAnswerChange(v: String) = _uiState.update { it.copy(securityAnswer = v, errorMessage = null) }


    fun tryRegister(onResult: (Boolean) -> Unit) {
        val s = _uiState.value
        val displayName = s.displayName.trim()
        val email = s.email.trim().lowercase()
        val password = s.password
        val confirmPassword = s.confirmPassword
        val birthDate = s.birthDate
        val securityQuestion = s.securityQuestion.trim()
        val securityAnswer = s.securityAnswer.trim()

        // --- Validaciones (sin cambios) ---
        if (displayName.isBlank() || email.isBlank() || password.isBlank() || confirmPassword.isBlank() || securityQuestion.isBlank() || securityAnswer.isBlank()) {
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

        // -------- Registro y Siembra de Favoritos --------
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            try {
                // 1. Registrar usuario
                val userId = userRepository.register(
                    displayName = displayName, email = email, password = password,
                    birthDate = birthDate, securityQuestion = securityQuestion, securityAnswer = securityAnswer
                )

                // 2. ⭐ ¡NUEVO! Sembrar la cuenta con 6 favoritos aleatorios
                seedInitialFavorites(userId)

                // 3. Iniciar sesión automáticamente
                sessionRepository.setLoggedInUserId(userId)

                _uiState.update { it.copy(isLoading = false) }
                onResult(true)

            } catch (e: Exception) {
                _uiState.update { it.copy(isLoading = false, errorMessage = "Ese correo ya existe o ha ocurrido un error") }
                onResult(false)
            }
        }
    }

    /**
     * Selecciona 6 canciones aleatorias del catálogo local y las añade
     * a los favoritos del nuevo usuario.
     */
    private suspend fun seedInitialFavorites(userId: Long) {
        val randomFavoriteIds = localSeedSongs
            .shuffled() // Desordena la lista
            .take(6)    // Toma las primeras 6
            .map { it.spotifyId } // Extrae solo los IDs de Spotify

        if (randomFavoriteIds.isNotEmpty()) {
            favoritesRepository.addFavorites(userId, randomFavoriteIds)
        }
    }
}
