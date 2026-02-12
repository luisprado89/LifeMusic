package com.luis.lifemusic.data.local.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index

/**
 * Tabla de favoritos por usuario.
 *
 * ✅ Relación: (userId + songId) como clave compuesta
 * - Un usuario no puede marcar la misma canción como favorita 2 veces.
 *
 * ✅ songId es Int porque viene del modelo SongData (sampleSongs).
 * - Más adelante, si las canciones vienen de API/Room, se mantiene la idea de id estable.
 */
@Entity(
    tableName = "favorites",
    primaryKeys = ["user_id", "song_id"],
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
        Index(value = ["song_id"]),
    ]
)
data class FavoriteEntity(
    @ColumnInfo(name = "user_id")
    val userId: Long,

    @ColumnInfo(name = "song_id")
    val songId: Int
)
