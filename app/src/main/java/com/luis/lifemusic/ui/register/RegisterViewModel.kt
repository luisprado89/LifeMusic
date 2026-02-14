package com.luis.lifemusic.ui.register


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.luis.lifemusic.data.repository.SessionRepository
import com.luis.lifemusic.data.repository.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class RegisterViewModel(
    private val userRepository: UserRepository,
    private val sessionRepository: SessionRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(RegisterUiState())
    val uiState: StateFlow<RegisterUiState> = _uiState

    fun onUsernameChange(v: String) = _uiState.update { it.copy(username = v, errorMessage = null) }
    fun onPasswordChange(v: String) = _uiState.update { it.copy(password = v, errorMessage = null) }
    fun onConfirmPasswordChange(v: String) = _uiState.update { it.copy(confirmPassword = v, errorMessage = null) }
    fun onSecurityQuestionChange(v: String) = _uiState.update { it.copy(securityQuestion = v, errorMessage = null) }
    fun onSecurityAnswerChange(v: String) = _uiState.update { it.copy(securityAnswer = v, errorMessage = null) }

    fun tryRegister(onResult: (Boolean) -> Unit) {
        val s = _uiState.value

        if (s.username.isBlank() || s.password.isBlank() || s.confirmPassword.isBlank()) {
            _uiState.update { it.copy(errorMessage = "Completa todos los campos") }
            onResult(false); return
        }
        if (s.password != s.confirmPassword) {
            _uiState.update { it.copy(errorMessage = "Las contraseñas no coinciden") }
            onResult(false); return
        }
        if (s.securityQuestion.isBlank() || s.securityAnswer.isBlank()) {
            _uiState.update { it.copy(errorMessage = "Completa la recuperación") }
            onResult(false); return
        }

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            try {
                val userId = userRepository.register(
                    username = s.username,
                    password = s.password,
                    securityQuestion = s.securityQuestion,
                    securityAnswer = s.securityAnswer
                )
                // Auto-login tras registrar (opcional pero cómodo)
                sessionRepository.setLoggedInUserId(userId)
                _uiState.update { it.copy(isLoading = false) }
                onResult(true)
            } catch (e: Exception) {
                // Si el username es único, aquí suele saltar por constraint
                _uiState.update {
                    it.copy(isLoading = false, errorMessage = "Ese usuario ya existe")
                }
                onResult(false)
            }
        }
    }
}
