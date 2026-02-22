package com.luis.lifemusic.data

import android.content.Context
import androidx.room.Room
import com.luis.lifemusic.data.local.LifeMusicDatabase
import com.luis.lifemusic.data.remote.spotify.api.SpotifyApiClient
import com.luis.lifemusic.data.repository.*
import com.luis.lifemusic.data.seed.DemoDataSeeder
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

/**
 * AppContainer = contenedor manual de dependencias (sin Hilt/Koin).
 *
 * ✅ ¿Por qué existe?
 * - Centraliza la creación de dependencias globales (Room, DataStore, Retrofit...)
 * - Evita que cada pantalla/VM cree instancias por su cuenta.
 *
 * ✅ Regla didáctica (MVVM):
 * - La UI NO crea Room/DataStore/Retrofit.
 * - Los ViewModels dependen de repositorios, no de DAOs directamente.
 */
class AppContainer(appContext: Context) {

    /**
     * Usamos applicationContext (no el context de Activity).
     * 👉 Evita fugas de memoria y es seguro para singletons.
     */
    private val applicationContext: Context = appContext.applicationContext

    // ------------------------------------------------------------
    // 0) SCOPE DE APLICACIÓN (para tareas "fire-and-forget")
    /**
     * Scope de aplicación para tareas "one-shot" como seeds.
     * - SupervisorJob para que si falla el seed no tumbe el resto.
     * - Dispatchers.IO porque tocamos Room.
     */
    private val appScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)


    // ------------------------------------------------------------
    // 1) ROOM (Base de datos local)
    // ------------------------------------------------------------

    private val database: LifeMusicDatabase by lazy {
        Room.databaseBuilder(
            applicationContext,
            LifeMusicDatabase::class.java,
            "lifemusic_database"
        ).build()
    }

    private val userDao = database.userDao()
    private val favoriteDao = database.favoriteDao()

    // ------------------------------------------------------------
    // 2) REPOSITORIOS LOCALES (Room + DataStore)
    // ------------------------------------------------------------

    /**
     * Guarda SOLO el userId logueado.
     * - null => no hay sesión
     * - Long => userId activo
     */
    val sessionRepository: SessionRepository by lazy {
        SessionRepositoryImpl(applicationContext)
    }

    val userRepository: UserRepository by lazy {
        UserRepositoryImpl(userDao)
    }

    val favoritesRepository: FavoritesRepository by lazy {
        FavoritesRepositoryImpl(favoriteDao)
    }

    // ------------------------------------------------------------
    // 3) RETROFIT (Spotify) + REPOSITORIO REMOTO
    // ------------------------------------------------------------

    /**
     * SpotifyApiClient contiene el Retrofit + ApiService.
     * Este repositorio usa ese ApiService para cargar datos online.
     */
    val spotifyRepository: SpotifyRepository by lazy {
        SpotifyRepository(apiService = SpotifyApiClient.apiService)
    }

// ------------------------------------------------------------
    // 4) DEMO SEED (2 usuarios + 6 favoritos)
    // ------------------------------------------------------------

    init {
        /**
         * Se ejecuta en segundo plano al crear el contenedor.
         * ✅ No bloquea el arranque de la app.
         * ✅ Idempotente: no duplica usuarios ni favoritos.
         */
        appScope.launch {
            try {
                DemoDataSeeder(
                    userDao = userDao,
                    favoriteDao = favoriteDao
                ).seedIfNeeded()
            } catch (_: Exception) {
                // Si algo falla en el seed, no queremos romper la app.
                // En un proyecto real loggearíamos el error.
            }
        }
    }
}