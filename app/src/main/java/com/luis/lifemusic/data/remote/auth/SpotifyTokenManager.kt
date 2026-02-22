package com.luis.lifemusic.data.remote.auth

import android.util.Base64
import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext
import okhttp3.FormBody
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONObject

/**
 * ============================================================
 * SPOTIFY TOKEN MANAGER
 * ============================================================
 *
 * 🎯 RESPONSABILIDAD:
 * - Gestionar el token OAuth de Spotify.
 * - Obtenerlo automáticamente si no existe.
 * - Renovarlo cuando expira.
 * - Evitar múltiples peticiones concurrentes de renovación.
 *
 * 🔐 FLUJO:
 * - Usa Client Credentials Flow.
 * - Envía CLIENT_ID + CLIENT_SECRET en header Basic.
 * - Recibe access_token + expires_in.
 *
 * 🧠 OPTIMIZACIÓN:
 * - Cachea el token en memoria.
 * - Aplica margen de seguridad antes de expiración.
 * - Usa Mutex para evitar múltiples refresh simultáneos.
 *
 * 👉 El resto de la app SOLO debe llamar a:
 *    getValidToken()
 */
object SpotifyTokenManager {

    private const val TAG = "SpotifyTokenManager"

    /** Endpoint oficial para obtener token */
    private const val TOKEN_URL = "https://accounts.spotify.com/api/token"

    /** Margen de seguridad (1 minuto antes de expirar) */
    private const val TOKEN_SAFETY_MARGIN_MS = 60_000L

    /** Cliente HTTP interno */
    private val httpClient = OkHttpClient()

    /** Mutex para evitar renovaciones concurrentes */
    private val mutex = Mutex()

    /** Token actual en memoria */
    @Volatile
    private var accessToken: String? = null

    /** Momento exacto (epoch ms) en el que expira */
    @Volatile
    private var expiresAtEpochMs: Long = 0L

    /**
     * Devuelve un token válido.
     *
     * - Si el token sigue vigente → lo reutiliza.
     * - Si expiró → lo renueva automáticamente.
     */
    suspend fun getValidToken(): String? = withContext(Dispatchers.IO) {
        mutex.withLock {
            val now = System.currentTimeMillis()

            // Si el token existe y no ha expirado, reutilizarlo
            if (!accessToken.isNullOrBlank() && now < expiresAtEpochMs) {
                return@withLock accessToken
            }

            // Si no es válido, renovarlo
            refreshTokenLocked()
        }
    }

    /**
     * Obtiene un nuevo token desde Spotify.
     * ⚠️ Solo se ejecuta dentro del Mutex.
     */
    private fun refreshTokenLocked(): String? {

        if (!SpotifyCredentials.hasValidCredentials) {
            Log.e(TAG, "❌ Credenciales inválidas")
            return null
        }

        // Construcción del header Basic Auth
        val basic = Base64.encodeToString(
            "${SpotifyCredentials.CLIENT_ID}:${SpotifyCredentials.CLIENT_SECRET}".toByteArray(),
            Base64.NO_WRAP
        )

        val request = Request.Builder()
            .url(TOKEN_URL)
            .post(
                FormBody.Builder()
                    .add("grant_type", "client_credentials")
                    .build()
            )
            .header("Authorization", "Basic $basic")
            .header("Content-Type", "application/x-www-form-urlencoded")
            .build()

        return try {
            httpClient.newCall(request).execute().use { response ->

                val body = response.body?.string().orEmpty()

                if (!response.isSuccessful) {
                    Log.e(TAG, "❌ Error token HTTP ${response.code}: $body")
                    accessToken = null
                    expiresAtEpochMs = 0L
                    return null
                }

                val json = JSONObject(body)
                val token = json.optString("access_token")
                val expiresIn = json.optLong("expires_in", 0L)

                if (token.isBlank() || expiresIn <= 0L) {
                    Log.e(TAG, "❌ Respuesta token inválida: $body")
                    accessToken = null
                    expiresAtEpochMs = 0L
                    return null
                }

                // Guardamos el token en memoria
                accessToken = token

                // Calculamos expiración con margen de seguridad
                expiresAtEpochMs =
                    System.currentTimeMillis() +
                            (expiresIn * 1000L) -
                            TOKEN_SAFETY_MARGIN_MS

                Log.d(TAG, "✅ Token renovado correctamente. expiresIn=${expiresIn}s")

                token
            }

        } catch (e: Exception) {
            Log.e(TAG, "❌ Excepción obteniendo token", e)
            accessToken = null
            expiresAtEpochMs = 0L
            null
        }
    }
}