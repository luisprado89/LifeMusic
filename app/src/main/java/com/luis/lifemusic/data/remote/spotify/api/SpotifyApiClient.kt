package com.luis.lifemusic.data.remote.spotify.api

import android.util.Log
import com.luis.lifemusic.data.remote.auth.SpotifyTokenManager
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

/**
 * ============================================================
 * SPOTIFY API CLIENT
 * ============================================================
 *
 * 🎯 RESPONSABILIDAD:
 * - Configurar Retrofit.
 * - Configurar OkHttp.
 * - Añadir automáticamente el token Bearer en cada request.
 *
 * 🔐 AUTENTICACIÓN:
 * - Usa SpotifyTokenManager.
 * - Añade header Authorization: Bearer <token>.
 *
 * 🧠 DISEÑO:
 * - Singleton (object).
 * - Inicialización lazy.
 * - Interceptor centralizado.
 *
 * 👉 El resto de la app SOLO usa:
 *    SpotifyApiClient.apiService
 */
object SpotifyApiClient {

    private const val TAG = "SpotifyApiClient"
    private const val BASE_URL = "https://api.spotify.com/v1/"

    /**
     * Interceptor que:
     * 1️⃣ Obtiene token válido
     * 2️⃣ Añade Authorization Bearer
     * 3️⃣ Añade Accept JSON
     */
    private val authInterceptor = Interceptor { chain ->

        val originalRequest = chain.request()

        // ⚠️ runBlocking aquí es seguro porque el interceptor ya corre en hilo IO
        val token = runBlocking {
            SpotifyTokenManager.getValidToken()
        }

        val newRequest = originalRequest.newBuilder().apply {

            if (!token.isNullOrBlank()) {
                header("Authorization", "Bearer $token")
            }

            header("Accept", "application/json")

        }.build()

        Log.d(TAG, "➡️ ${newRequest.method} ${newRequest.url}")

        chain.proceed(newRequest)
    }

    /**
     * Cliente HTTP configurado
     */
    private val okHttpClient: OkHttpClient by lazy {
        OkHttpClient.Builder()
            .addInterceptor(authInterceptor)
//            .addInterceptor(
//                HttpLoggingInterceptor()
//                    .setLevel(HttpLoggingInterceptor.Level.BASIC)
                    .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
            )
            .connectTimeout(15, TimeUnit.SECONDS)
            .readTimeout(20, TimeUnit.SECONDS)
            .build()
    }

    /**
     * Instancia Retrofit
     */
    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    /**
     * Servicio principal de la API de Spotify
     */
    val apiService: SpotifyApiService by lazy {
        retrofit.create(SpotifyApiService::class.java)
    }
}