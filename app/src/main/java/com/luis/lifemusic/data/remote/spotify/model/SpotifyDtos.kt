package com.luis.lifemusic.data.remote.spotify.model

import com.google.gson.annotations.SerializedName

/**
 * Representa la respuesta de más alto nivel del endpoint de búsqueda de Spotify.
 * La respuesta JSON tiene un objeto "tracks" que contiene los resultados.
 */
data class SpotifySearchResponse(
    val tracks: SpotifySearchResult
)

/**
 * Representa el objeto anidado "tracks" que contiene la lista de items (canciones).
 */
data class SpotifySearchResult(
    val items: List<TrackDto>
)

/**
 * Data Transfer Object (DTO) para una canción individual de la API de Spotify.
 */
data class TrackDto(
    val id: String,
    val name: String,
    val artists: List<ArtistDto>,
    val album: AlbumDto,
    val popularity: Int,
    @SerializedName("duration_ms")
    val durationMs: Int
)

/**
 * DTO para un artista. Incluye el ID, que es crucial para futuras recomendaciones.
 */
data class ArtistDto(
    val id: String,
    val name: String
)

/**
 * DTO para un álbum.
 */
data class AlbumDto(
    val name: String,
    val images: List<ImageDto>,
    @SerializedName("release_date")
    val releaseDate: String
)

/**
 * DTO para una imagen de la carátula del álbum.
 */
data class ImageDto(
    val url: String
)
