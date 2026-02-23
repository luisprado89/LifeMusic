package com.luis.lifemusic.ui.profile

/**
 * Estado inmutable de la pantalla Profile.
 *
 * ✅ Reglas del proyecto:
 * - Login SOLO con email (email único en Room).
 * - "Username visual" se deriva del email (antes del @) en la UI (no se guarda en BD).
 * - displayName es el nombre completo editable (si lo mantienes editable).
 *
 * ⚠️ Nota:
 * - birthDate se guarda como Long (epoch millis).
 */
data class ProfileUiState(
    val name: String = "",
    val email: String = "",
    val birthDate: Long = 0L,
    val verified: Boolean = false,
    val photoUri: String? = null,
    val isEditing: Boolean = false,
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val hasActiveSession: Boolean = true
)
