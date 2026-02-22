package com.luis.lifemusic.data.remote.spotify.model

import com.google.gson.annotations.SerializedName

data class SpotifySearchResponse(
    val tracks: SpotifyPagingResult
)

data class SpotifyPagingResult(
    val href: String = "",
    val limit: Int = 0,
    val next: String? = null,
    val offset: Int = 0,
    val previous: String? = null,
    val total: Int = 0,              // ← ESTE ES EL QUE TE FALTA
    val items: List<TrackDto> = emptyList()
)

data class TrackDto(
    val id: String = "",
    val name: String = "",
    val artists: List<ArtistDto> = emptyList(),
    val album: AlbumDto = AlbumDto(),
    val popularity: Int = 0,
    @SerializedName("duration_ms")
    val durationMs: Int = 0
)

data class ArtistDto(
    val id: String = "",
    val name: String = ""
)

data class AlbumDto(
    val name: String = "",
    val images: List<ImageDto> = emptyList(),
    @SerializedName("release_date")
    val releaseDate: String = ""
)

data class ImageDto(
    val url: String = ""
)