package com.luis.lifemusic.data

import com.luis.lifemusic.R

data class Song(
    val imageRes: Int,
    val title: String,
    val artist: String,
    val album: String,
    val duration: String,
    val isFavorite: Boolean = false
)

val sampleSongs = listOf(
    Song(R.drawable.queen,
        "Bohemian Rhapsody",
        "Queen",
        "A Night at the Opera",
        "5:55",
        true),
    Song(R.drawable.johnlennon, "Imagine", "John Lennon", "Imagine", "3:07"),
    Song(R.drawable.hotelcalifornia, "Hotel California", "Eagles", "Hotel California", "6:30", true),
    Song(R.drawable.ledzeppelin, "Stairway to Heaven", "Led Zeppelin", "Led Zeppelin IV", "8:02"),
    Song(R.drawable.gunsnroses, "Sweet Child O' Mine", "Guns N' Roses", "Appetite for Destruction", "5:56", true)
)
