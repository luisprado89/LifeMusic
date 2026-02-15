package com.luis.lifemusic.ui.recover

/**
 * Estado inmutable de la pantalla de recuperación.
 *
 * ✅ Incluye:
 * - Datos que escribe el usuario.
 * - Datos cargados desde repositorio (pregunta de seguridad).
 * - Estado de carga y mensajes de error/éxito para la UI.
 */
data class RecoverUiState(
    val username: String = "",
    val securityQuestion: String = "",
    val securityAnswer: String = "",
    val newPassword: String = "",
    val isQuestionLoaded: Boolean = false,
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val successMessage: String? = null
)
