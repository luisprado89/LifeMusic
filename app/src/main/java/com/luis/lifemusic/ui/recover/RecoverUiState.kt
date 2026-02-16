package com.luis.lifemusic.ui.recover

/**
 * Estado inmutable de la pantalla Recover.
 *
 * ✅ Flujo en 2 pasos:
 *
 * Paso 1:
 * - El usuario introduce su email.
 * - Se busca la cuenta y se carga la pregunta de seguridad.
 *
 * Paso 2:
 * - Se muestra la pregunta.
 * - El usuario introduce la respuesta + nueva contraseña.
 *
 * 🔐 Reglas del proyecto:
 * - Recuperación SOLO por email (igual que login).
 * - isQuestionLoaded controla si estamos en el paso 1 o paso 2.
 * - errorMessage y successMessage son feedback visual para la UI.
 */
data class RecoverUiState(

    // Paso 1: Identificación por email
    val email: String = "",

    // Paso 2: Recuperación
    val securityQuestion: String = "",
    val securityAnswer: String = "",
    val newPassword: String = "",

    // Control de flujo
    val isQuestionLoaded: Boolean = false,

    // Estado UI
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val successMessage: String? = null
)
