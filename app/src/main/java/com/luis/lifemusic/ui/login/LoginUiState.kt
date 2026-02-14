package com.luis.lifemusic.ui.login

/**
 * Estado de la pantalla Login.
 *
 * ✅ UIState = “foto” de la pantalla
 */
data class LoginUiState(
    val username: String = "",
    val password: String = "",
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)
