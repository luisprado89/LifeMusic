package com.luis.lifemusic.data

import android.content.Context
import androidx.room.Room
import com.luis.lifemusic.data.local.LifeMusicDatabase
import com.luis.lifemusic.data.repository.*
// 🔥 IMPORTANTE: Importa el repositorio de Spotify
import com.luis.lifemusic.data.remote.spotify.api.SpotifyApiClient
import com.luis.lifemusic.data.remote.auth.SpotifyTokenManager

/**
 * AppContainer = contenedor manual de dependencias (sin Hilt/Koin).
 *
 * ✅ ¿Por qué existe?
 * - Centraliza la creación de dependencias globales (Room, DataStore, Retrofit...)
 * - Evita que cada pantalla/VM "invente" instancias por su cuenta.
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

    /**
     * Instancia única de Room.
     *
     * Nota:
     * - En DiscosFavoritos2 se hacía con singleton + companion object.
     * - Aquí lo centralizamos en AppContainer (mismo resultado, otra organización).
     */
    val database: LifeMusicDatabase by lazy {
        Room.databaseBuilder(
            applicationContext,
            LifeMusicDatabase::class.java,
            "lifemusic.db"
        ).build()
    }

    /** DAOs (acceso a tablas) */
    val userDao by lazy { database.userDao() }
    val favoriteDao by lazy { database.favoriteDao() }

    // ------------------------------------------------------------
    // 2) DATASTORE (Sesión)
    // ------------------------------------------------------------

    /**
     * Repositorio de sesión (DataStore).
     *
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
    // 3) SPOTIFY API (NUEVO)
    // ------------------------------------------------------------

    /**
     * Repositorio de Spotify para obtener canciones.
     * Se inicializa de forma perezosa (lazy) para no bloquear el arranque.
     */
    val spotifyRepository: SpotifyRepository by lazy {
        // Forzar la inicialización del cliente de Spotify
        // Esto hará que se obtenga el token la primera vez
        android.util.Log.d("AppContainer", "🎵 Inicializando SpotifyRepository...")

        // Acceder al apiService para forzar la creación de Retrofit
        val test = SpotifyApiClient.apiService

        SpotifyRepository()
    }

    // ------------------------------------------------------------
    // 4) RETROFIT (futuro)
    // ------------------------------------------------------------
    /**
     * Aquí irá:
     * - Retrofit builder
     * - Api services
     * - Repositorios remotos
     *
     * Ejemplo futuro:
     * val retrofit = Retrofit.Builder()...
     * val songsApi = retrofit.create(SongsApi::class.java)
     * val songsRepository = SongsRepositoryImpl(songsApi, favoriteDao)
     */
}