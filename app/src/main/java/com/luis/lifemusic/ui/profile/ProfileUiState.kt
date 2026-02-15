package com.luis.lifemusic.ui.profile

/**
 * Estado inmutable de la pantalla Profile.
 *
 * 📌 Arquitectura:
 * - Este estado es la única fuente de verdad para ProfilePage.
 * - La UI nunca modifica datos directamente.
 * - Todo cambio pasa por ProfileViewModel.
 *
 * ✅ Contiene:
 * - Datos del usuario (name, email).
 * - verified: estado visual de cuenta verificada.
 * - memberSince: dato informativo mostrado en pantalla.
 * - isEditing: controla si la UI está en modo edición.
 * - isLoading/errorMessage: feedback visual.
 * - hasActiveSession: guard de sesión (DataStore).
 *
 * 🔒 Guard de sesión:
 * - true  -> se permite mostrar Profile.
 * - false -> NavHost debe redirigir a Login.
 */
data class ProfileUiState(
    val name: String = "",
    val email: String = "",
    val verified: Boolean = false,
    val memberSince: String = "Enero 2023",
    val isEditing: Boolean = false,
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val hasActiveSession: Boolean = true
)
