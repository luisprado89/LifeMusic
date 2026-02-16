package com.luis.lifemusic.ui.login

/**
 * Estado inmutable de la pantalla Login.
 *
 * Reglas del proyecto:
 * - Login SOLO con email (email único en Room).
 * - No existe "username" como credencial.
 * - isLoading controla spinner/bloqueo UI.
 * - errorMessage muestra mensajes de validación o error.
 */
data class LoginUiState(
    val email: String = "",
    val password: String = "",
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)
