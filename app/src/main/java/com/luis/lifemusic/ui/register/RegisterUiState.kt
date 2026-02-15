package com.luis.lifemusic.ui.register

/**
 * Estado inmutable de la pantalla Register.
 *
 * ✅ Reglas del proyecto:
 * - Login SOLO con email (único en Room).
 * - displayName obligatorio (nombre completo real).
 * - birthDate obligatorio (Long epoch millis).
 * - confirmPassword solo se usa para validación UI.
 * - securityQuestion + securityAnswer obligatorias.
 *
 * 🔒 Nota:
 * - password se guarda en texto plano por simplicidad didáctica.
 *   En un proyecto real debería almacenarse con hash.
 */
data class RegisterUiState(

    // Datos personales
    val displayName: String = "",
    val email: String = "",
    val birthDate: Long? = null,   // Obligatoria

    // Credenciales
    val password: String = "",
    val confirmPassword: String = "",

    // Recuperación
    val securityQuestion: String = "",
    val securityAnswer: String = "",

    // Estado UI
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)
