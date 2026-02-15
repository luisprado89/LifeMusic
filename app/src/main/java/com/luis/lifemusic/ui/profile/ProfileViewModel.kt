package com.luis.lifemusic.ui.profile

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
 * ✅ Regla de arquitectura:
 * - El ViewModel NO navega.
 * - El ViewModel expone estado (ProfileUiState) y acciones (funciones públicas).
 * - La navegación (volver a Login) se gestiona en Route/NavHost mediante hasActiveSession.
 */
class ProfileViewModel(
    private val userRepository: UserRepository,
    private val sessionRepository: SessionRepository
) : ViewModel() {

    /** Estado interno mutable (única fuente de verdad de la pantalla). */
    private val _uiState = MutableStateFlow(ProfileUiState(isLoading = true))

    /** Estado público inmutable observado desde ProfileRoute. */
    val uiState: StateFlow<ProfileUiState> = _uiState

    /** UserId de la sesión actual; se actualiza observando DataStore. */
    private var currentUserId: Long? = null

    /**
     * Job de observación del usuario:
     * - Cancelamos y recreamos cuando cambia el userId.
     * - Evita múltiples collectors simultáneos sobre Room.
     */
    private var observeUserJob: Job? = null

    init {
        observeSession()
    }

    /**
     * Observa DataStore para saber si hay sesión activa y, si existe,
     * suscribirse al usuario en Room.
     *
     * Reglas:
     * - userId == null  -> sesión inválida (guard) -> hasActiveSession = false
     * - userId != null  -> sesión válida -> observar el usuario de Room
     */
    private fun observeSession() {
        viewModelScope.launch {
            sessionRepository.sessionUserId.collectLatest { userId ->
                currentUserId = userId

                if (userId == null) {
                    // Sesión perdida: paramos observación y marcamos guard a false.
                    observeUserJob?.cancel()
                    _uiState.update {
                        it.copy(
                            hasActiveSession = false,
                            isLoading = false
                        )
                    }
                    return@collectLatest
                }

                // Sesión válida: activamos guard y cargamos el usuario desde Room.
                _uiState.update { it.copy(hasActiveSession = true, isLoading = true) }
                observeUser(userId)
            }
        }
    }

    /**
     * Observa en tiempo real el usuario actual desde Room.
     * Así, si el usuario cambia (updateProfile), la UI se actualiza automáticamente.
     */
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
                        verified = user.email.contains("@"),
                        isLoading = false,
                        errorMessage = null
                    )
                }
            }
        }
    }

    // ---------------------------
    // Eventos de UI (modo edición)
    // ---------------------------

    fun onEditClick() {
        _uiState.update { it.copy(isEditing = true, errorMessage = null) }
    }

    fun onCancelEdit() {
        _uiState.update { it.copy(isEditing = false, errorMessage = null) }

        /**
         * Al cancelar, recargamos el usuario para descartar cambios introducidos en UI.
         * (si el usuario editó name/email pero no guardó).
         */
        currentUserId?.let { observeUser(it) }
    }

    fun onNameChange(value: String) {
        _uiState.update { it.copy(name = value, errorMessage = null) }
    }

    fun onEmailChange(value: String) {
        _uiState.update { it.copy(email = value, errorMessage = null) }
    }

    /**
     * Guarda cambios del perfil (displayName/email) en Room.
     *
     * Validación mínima:
     * - El nombre no puede estar vacío.
     */
    fun onSaveChanges() {
        val userId = currentUserId ?: return
        val state = _uiState.value

        if (state.name.isBlank()) {
            _uiState.update { it.copy(errorMessage = "El nombre no puede estar vacío") }
            return
        }

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }

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
        }
    }

    /**
     * Logout:
     * - Limpia DataStore (sesión).
     * - ProfileRoute/NavHost reaccionan al guard (hasActiveSession = false)
     *   y vuelven a Login.
     */
    fun onLogoutClick() {
        viewModelScope.launch {
            sessionRepository.clearSession()
        }
    }
}
