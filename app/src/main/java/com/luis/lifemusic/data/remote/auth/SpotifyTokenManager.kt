package com.luis.lifemusic.data.remote.auth

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
 * Gestor de tokens de Spotify auto-suficiente.
 * Se encarga de obtener y renovar el token automáticamente.
 */
object SpotifyTokenManager {

    private const val TAG = "SpotifyTokenManager"
    private val client = OkHttpClient()
    private val mutex = Mutex()

    private var currentToken: String? = null
    private var tokenExpiration: Long = 0

    /**
     * Obtiene un token válido, renovándolo si es necesario.
     * Esta es la única función que el resto de la app debe llamar.
     */
    suspend fun getValidToken(): String? = withContext(Dispatchers.IO) {
        mutex.withLock {
            if (isTokenValid()) {
                Log.d(TAG, "Usando token existente en caché: ${currentToken?.take(10)}...")
                return@withLock currentToken
            }

            Log.d(TAG, "Token no válido o expirado. Pidiendo uno nuevo...")
            fetchNewToken()
        }
    }

    private fun isTokenValid(): Boolean {
        return !currentToken.isNullOrBlank() && System.currentTimeMillis() < tokenExpiration
    }

    /**
     * Llama a la API de cuentas de Spotify para obtener un nuevo token de acceso.
     */
    private fun fetchNewToken(): String? {
        if (!SpotifyCredentials.hasValidCredentials) {
            Log.e(TAG, "❌ CREDENCIALES INVÁLIDAS:")
            Log.e(TAG, "   CLIENT_ID: '${SpotifyCredentials.CLIENT_ID}'")
            Log.e(TAG, "   CLIENT_SECRET: '${SpotifyCredentials.CLIENT_SECRET}'")
            Log.e(TAG, "   hasValidCredentials: ${SpotifyCredentials.hasValidCredentials}")
            return null
        }

        Log.d(TAG, "Intentando obtener token con:")
        Log.d(TAG, "  CLIENT_ID: ${SpotifyCredentials.CLIENT_ID.take(5)}...")
        Log.d(TAG, "  CLIENT_SECRET: ${SpotifyCredentials.CLIENT_SECRET.take(5)}...")

        val requestBody = FormBody.Builder()
            .add("grant_type", "client_credentials")
            .add("client_id", SpotifyCredentials.CLIENT_ID)
            .add("client_secret", SpotifyCredentials.CLIENT_SECRET)
            .build()

        val request = Request.Builder()
            .url("https://accounts.spotify.com/api/token")
            .post(requestBody)
            .addHeader("Content-Type", "application/x-www-form-urlencoded")
            .build()

        return try {
            val response = client.newCall(request).execute()
            val responseBody = response.body?.string()

            Log.d(TAG, "Código de respuesta: ${response.code}")

            if (response.isSuccessful) {
                val json = JSONObject(responseBody ?: "")
                val accessToken = json.getString("access_token")
                val expiresIn = json.getLong("expires_in")

                tokenExpiration = System.currentTimeMillis() + (expiresIn * 1000L) - 60000
                currentToken = accessToken

                Log.d(TAG, "✅ Token obtenido con éxito. Expira en ${expiresIn}s")
                Log.d(TAG, "   Token (primeros 20): ${accessToken.take(20)}...")
                accessToken
            } else {
                Log.e(TAG, "❌ Error al obtener el token: ${response.code}")
                Log.e(TAG, "   Cuerpo: $responseBody")
                Log.e(TAG, "   Headers: ${response.headers}")
                null
            }
        } catch (e: Exception) {
            Log.e(TAG, "❌ Excepción al obtener el token", e)
            null
        }
    }
}