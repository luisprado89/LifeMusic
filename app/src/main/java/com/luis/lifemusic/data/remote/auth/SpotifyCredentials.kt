package com.luis.lifemusic.data.remote.auth

/**
 * CREDENCIALES DE SPOTIFY API
 *
 * ✅ MODO DESARROLLO:
 *   - Las credenciales son FIJAS (nunca cambian)
 *   - El token se renueva automáticamente cada hora
 *   - Válido para hasta 25 usuarios (alumno + profesor + compañeros)
 *
 * ⚠️ IMPORTANTE:
 *   - Este archivo SE SUBE a GitHub (son credenciales de desarrollo)
 *   - Así tu profesor puede clonar y TODO FUNCIONA
 *   - En producción NUNCA se hace esto, pero para educativo es perfecto
 */
object SpotifyCredentials {

    // 🔐 TUS CREDENCIALES REALES (de la imagen)
    const val CLIENT_ID = "8ca33619ade24215a8127be9b9b3c2c1"
    const val CLIENT_SECRET = "e684a27dfebf48a0980f061013d54ebe"

    /**
     * Verifica que las credenciales son válidas
     * (solo comprueba que no están vacías)
     */
    val hasValidCredentials: Boolean =
        CLIENT_ID.isNotBlank() &&
                CLIENT_SECRET.isNotBlank()
}