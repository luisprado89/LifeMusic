package com.luis.lifemusic.data

import android.content.Context
import androidx.room.Room
import com.luis.lifemusic.data.local.LifeMusicDatabase
import com.luis.lifemusic.data.remote.spotify.api.SpotifyApiClient
import com.luis.lifemusic.data.repository.*

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
    // 4) RETROFIT (futuro: otros endpoints / otras APIs)
    // ------------------------------------------------------------
    /**
     * Aquí irán otros clientes Retrofit / servicios / repos remotos
     * si añades más APIs además de Spotify.
     */
}