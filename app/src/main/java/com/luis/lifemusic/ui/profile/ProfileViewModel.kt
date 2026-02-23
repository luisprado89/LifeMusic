package com.luis.lifemusic.ui.profile

import android.database.sqlite.SQLiteConstraintException
import android.util.Patterns
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.luis.lifemusic.data.repository.SessionRepository
import com.luis.lifemusic.data.repository.UserRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/**
 * ProfileViewModel
 *
 * ✅ Responsabilidades:
 * - Obtener el userId actual desde DataStore (SessionRepository).
 * - Observar en tiempo real el usuario de Room (UserRepository.observeUser).
 * - Gestionar el modo edición (isEditing) y los cambios de name/email.
 * - Guardar cambios en Room usando userRepository.updateProfile.
 * - Logout limpiando la sesión (sessionRepository.clearSession).
 *
 * ✅ Reglas de la app:
 * - Login SOLO con email.
 * - Email es único en BD (índice UNIQUE).
 * - displayName es el nombre completo editable.
 * - El "username visual" se deriva del email en la UI (substringBefore("@")).
 *
 * ✅ Regla de arquitectura:
 * - El ViewModel NO navega.
 * - El ViewModel expone estado (ProfileUiState) y acciones (funciones públicas).
 * - La navegación (volver a Login) se gestiona en Route/NavHost mediante hasActiveSession.
 */

class ProfileViewModel(
    private val userRepository: UserRepository,
    private val sessionRepository: SessionRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(ProfileUiState(isLoading = true))
    val uiState: StateFlow<ProfileUiState> = _uiState

    private var currentUserId: Long? = null
    private var observeUserJob: Job? = null

    init {
        observeSession()
    }

    private fun observeSession() {
        viewModelScope.launch {
            sessionRepository.sessionUserId.collectLatest { userId ->
                currentUserId = userId

                if (userId == null) {
                    observeUserJob?.cancel()
                    _uiState.update { it.copy(hasActiveSession = false, isLoading = false) }
                    return@collectLatest
                }

                _uiState.update { it.copy(hasActiveSession = true, isLoading = true) }
                observeUser(userId)
            }
        }
    }

    private fun observeUser(userId: Long) {
        observeUserJob?.cancel()
        observeUserJob = viewModelScope.launch {
            userRepository.observeUser(userId).collectLatest { user ->
                if (user == null) {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            errorMessage = "No se pudo cargar el perfil"
                        )
                    }
                    return@collectLatest
                }

                _uiState.update {
                    it.copy(
                        name = user.displayName,
                        email = user.email,
                        birthDate = user.birthDate,
                        verified = user.email.contains("@"),
                        photoUri = user.photoUri,
                        isLoading = false,
                        errorMessage = null
                    )
                }
            }
        }
    }

    fun onEditClick() {
        _uiState.update { it.copy(isEditing = true, errorMessage = null) }
    }

    fun onCancelEdit() {
        _uiState.update { it.copy(isEditing = false, errorMessage = null) }
        currentUserId?.let { observeUser(it) }
    }

    fun onNameChange(value: String) {
        _uiState.update { it.copy(name = value, errorMessage = null) }
    }

    fun onEmailChange(value: String) {
        _uiState.update { it.copy(email = value, errorMessage = null) }
    }

    fun onSaveChanges() {
        val userId = currentUserId ?: return
        val state = _uiState.value

        if (state.name.isBlank()) {
            _uiState.update { it.copy(errorMessage = "El nombre no puede estar vacío") }
            return
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(state.email.trim()).matches()) {
            _uiState.update { it.copy(errorMessage = "Introduce un correo válido") }
            return
        }

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }

            try {
                userRepository.updateProfile(
                    userId = userId,
                    displayName = state.name,
                    email = state.email
                )

                _uiState.update {
                    it.copy(
                        isLoading = false,
                        isEditing = false,
                        verified = state.email.contains("@")
                    )
                }
            } catch (_: SQLiteConstraintException) {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = "Ese correo ya existe"
                    )
                }
            } catch (_: Exception) {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = "No se pudo guardar el perfil"
                    )
                }
            }
        }
    }

    // ✅ Se llama desde CameraScreen al capturar
    fun onPhotoCaptured(uriString: String) {
        val userId = currentUserId ?: return

        viewModelScope.launch {
            runCatching {
                userRepository.updatePhoto(userId = userId, photoUri = uriString)
            }.onFailure {
                _uiState.update { it.copy(errorMessage = "No se pudo guardar la foto") }
            }
        }
    }

    fun onLogoutClick() {
        viewModelScope.launch { sessionRepository.clearSession() }
    }
}