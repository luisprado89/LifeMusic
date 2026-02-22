package com.luis.lifemusic.data.remote.spotify.api

import android.util.Log
import com.luis.lifemusic.data.remote.auth.SpotifyTokenManager
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

/**
 * Cliente de red para la API de Spotify.
 * Es un singleton y se encarga de la autenticación y configuración de Retrofit.
 */
object SpotifyApiClient {

    private const val TAG = "SpotifyApiClient"
    private const val BASE_URL = "https://api.spotify.com/v1/"

    // Variable para saber si ya se inicializó
    private var isInitialized = false

    // Inicialización tardía (lazy) de los componentes
    private val authInterceptor by lazy {
        Log.d(TAG, "⚙️ Creando authInterceptor por primera vez")
        Interceptor { chain ->
            val original: Request = chain.request()
            Log.d(TAG, "🔑 Interceptor ejecutándose para: ${original.url}")

            val token = try {
                runBlocking {
                    SpotifyTokenManager.getValidToken()
                }
            } catch (e: Exception) {
                Log.e(TAG, "❌ Error obteniendo token", e)
                null
            }

            if (token.isNullOrBlank()) {
                Log.e(TAG, "❌ TOKEN ES NULO O VACÍO")
            } else {
                Log.d(TAG, "✅ Token OK: ${token.take(15)}...")
            }

            val newRequest = original.newBuilder().apply {
                if (!token.isNullOrBlank()) {
                    header("Authorization", "Bearer $token")
                }
                header("Accept", "application/json")
            }.build()

            chain.proceed(newRequest)
        }
    }

    private val okHttpClient by lazy {
        Log.d(TAG, "⚙️ Creando OkHttpClient")
        OkHttpClient.Builder()
            .addInterceptor(authInterceptor)
            .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
            .connectTimeout(15, TimeUnit.SECONDS)
            .readTimeout(20, TimeUnit.SECONDS)
            .build()
    }

    private val retrofit by lazy {
        Log.d(TAG, "⚙️ Creando Retrofit")
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build()
    }

    val apiService: SpotifyApiService by lazy {
        Log.d(TAG, "⚙️ Creando apiService")
        retrofit.create(SpotifyApiService::class.java)
    }

    /**
     * Método para forzar la inicialización temprana
     */
    fun initialize() {
        if (!isInitialized) {
            Log.d(TAG, "🚀 Inicializando SpotifyApiClient...")
            // Acceder a las propiedades lazy para forzar su creación
            val test = apiService
            isInitialized = true
            Log.d(TAG, "✅ SpotifyApiClient inicializado correctamente")
        }
    }
}