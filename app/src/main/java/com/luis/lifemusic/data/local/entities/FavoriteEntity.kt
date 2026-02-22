package com.luis.lifemusic.data.local.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index

/**
 * Tabla de favoritos por usuario.
 *
 * ✅ Almacena el ID de Spotify (String) de la canción, no un Int local.
 * - Esto permite que los favoritos sean consistentes entre datos locales y de la API.
 */
@Entity(
    tableName = "favorites",
    // La clave primaria compuesta asegura que un usuario no puede marcar la misma canción dos veces.
    primaryKeys = ["user_id", "song_spotify_id"],
    foreignKeys = [
        ForeignKey(
            entity = UserEntity::class,
            parentColumns = ["id"],
            childColumns = ["user_id"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [
        Index(value = ["user_id"]),
        Index(value = ["song_spotify_id"]),
    ]
)
data class FavoriteEntity(
    @ColumnInfo(name = "user_id")
    val userId: Long,

    // Cambiamos de Int a String para guardar el spotifyId.
    @ColumnInfo(name = "song_spotify_id")
    val songSpotifyId: String
)
