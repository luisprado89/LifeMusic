package com.luis.lifemusic.ui.login

/**
 * Estado inmutable de la pantalla Login.
 *
 * - username/password: datos de entrada.
 * - isLoading: controla bloqueo visual y spinner.
 * - errorMessage: mensaje user-friendly si falla validación/login.
 */
data class LoginUiState(
    val username: String = "",
    val password: String = "",
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)
