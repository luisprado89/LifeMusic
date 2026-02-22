package com.luis.lifemusic.data.seed

import com.luis.lifemusic.data.local.dao.FavoriteDao
import com.luis.lifemusic.data.local.dao.UserDao
import com.luis.lifemusic.data.local.entities.FavoriteEntity
import com.luis.lifemusic.data.local.entities.UserEntity
import com.luis.lifemusic.data.localsed.localSeedSongs
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * ============================================================
 * DEMO DATA SEEDER
 * ============================================================
 *
 * 🎯 OBJETIVO:
 * - Crear 2 usuarios demo al arrancar la app (si no existen).
 * - Insertar 6 favoritos aleatorios (del catálogo LOCAL) a cada uno.
 *
 * ✅ IMPORTANTE:
 * - Los favoritos salen de localSeedSongs -> funcionan OFFLINE.
 * - Es idempotente: si ya existen los usuarios o ya tienen favoritos, no duplica.
 */
class DemoDataSeeder(
    private val userDao: UserDao,
    private val favoriteDao: FavoriteDao
) {

    /**
     * Ejecuta el seed si es necesario.
     * - Crea los 2 usuarios demo si NO existen.
     * - Asegura 6 favoritos para cada uno si aún no tienen.
     */
    suspend fun seedIfNeeded() = withContext(Dispatchers.IO) {

        val demoUsers = listOf(
            DemoUser(
                email = "profe@lifemusic.com",
                password = "1234567",
                displayName = "Profesor Demo",
                birthDate = 315532800000L, // 1980-01-01 aprox (UTC millis)
                securityQuestion = "Color favorito",
                securityAnswer = "azul"
            ),
            DemoUser(
                email = "alumno@lifemusic.com",
                password = "1234567",
                displayName = "Alumno Demo",
                birthDate = 946684800000L, // 2000-01-01 aprox (UTC millis)
                securityQuestion = "Nombre de tu mascota",
                securityAnswer = "luna"
            )
        )

        // 1) Crear usuarios si no existen y obtener sus IDs
        val demoUserIds: List<Long> = demoUsers.map { demo ->
            val existing = userDao.findByEmail(demo.email.trim().lowercase())
            if (existing != null) {
                existing.id
            } else {
                userDao.insert(
                    UserEntity(
                        email = demo.email.trim().lowercase(),
                        password = demo.password,
                        displayName = demo.displayName.trim(),
                        birthDate = demo.birthDate,
                        securityQuestion = demo.securityQuestion.trim(),
                        securityAnswer = demo.securityAnswer.trim(),
                        photoUri = null
                    )
                )
            }
        }

        // 2) Para cada usuario demo: asegurar 6 favoritos (solo si aún no tiene)
        demoUserIds.forEach { userId ->
            val currentFavIds = favoriteDao.getFavoriteSongIdsOnce(userId)
            if (currentFavIds.isNotEmpty()) return@forEach

            val randomIds = localSeedSongs
                .shuffled()
                .take(6)
                .map { it.spotifyId }
                .distinct()

            if (randomIds.isEmpty()) return@forEach

            val entities = randomIds.map { id ->
                FavoriteEntity(userId = userId, songSpotifyId = id)
            }
            favoriteDao.addFavorites(entities)
        }
    }

    private data class DemoUser(
        val email: String,
        val password: String,
        val displayName: String,
        val birthDate: Long,
        val securityQuestion: String,
        val securityAnswer: String
    )
}

/**
 * ============================================================
 * EXTENSIÓN mínima para FavoriteDao (consulta puntual)
 * ============================================================
 *
 * 🎯 La pantalla observa con Flow, pero para seed necesitamos "una lectura única".
 * Como no quiero tocar tu arquitectura con repos, lo resolvemos aquí:
 *
 * ✅ Requiere que FavoriteDao tenga una query suspend simple.
 * Te dejo abajo el cambio exacto para FavoriteDao.
 */
private suspend fun FavoriteDao.getFavoriteSongIdsOnce(userId: Long): List<String> {
    // Esta función usa el método que vamos a añadir en FavoriteDao:
    return getFavoriteSongIdsOnceInternal(userId)
}