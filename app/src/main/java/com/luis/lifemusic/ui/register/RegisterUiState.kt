package com.luis.lifemusic.ui.register


data class RegisterUiState(
    val username: String = "",
    val password: String = "",
    val confirmPassword: String = "",
    val securityQuestion: String = "",
    val securityAnswer: String = "",
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)
