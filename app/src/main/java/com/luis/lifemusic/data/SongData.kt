package com.luis.lifemusic.data

import com.luis.lifemusic.R

data class Song(
    val id: Int,
    val imageRes: Int,
    val title: String,
    val artist: String,
    val album: String,
    val duration: String,
    val isFavorite: Boolean = false
)

val sampleSongs = listOf(
    Song(
        id = 1,
        imageRes = R.drawable.queen,
        title = "Bohemian Rhapsody",
        artist = "Queen",
        album = "A Night at the Opera",
        duration = "5:55",
        isFavorite = true
    ),
    Song(
        id = 2,
        imageRes = R.drawable.johnlennon,
        title = "Imagine",
        artist = "John Lennon",
        album = "Imagine",
        duration = "3:07"
    ),
    Song(
        id = 3,
        imageRes = R.drawable.hotelcalifornia,
        title = "Hotel California",
        artist = "Eagles",
        album = "Hotel California",
        duration = "6:30",
        isFavorite = true
    ),
    Song(
        id = 4,
        imageRes = R.drawable.ledzeppelin,
        title = "Stairway to Heaven",
        artist = "Led Zeppelin",
        album = "Led Zeppelin IV",
        duration = "8:02"
    ),
    Song(
        id = 5,
        imageRes = R.drawable.gunsnroses,
        title = "Sweet Child O' Mine",
        artist = "Guns N' Roses",
        album = "Appetite for Destruction",
        duration = "5:56",
        isFavorite = true
    ),

    // 🔹 Clásicos adicionales
    Song(6, R.drawable.queen, "Don’t Stop Me Now", "Queen", "Jazz", "3:29"),
    Song(7, R.drawable.johnlennon, "Jealous Guy", "John Lennon", "Imagine", "4:14", true),
    Song(8, R.drawable.ledzeppelin, "Kashmir", "Led Zeppelin", "Physical Graffiti", "8:37"),
    Song(9, R.drawable.gunsnroses, "November Rain", "Guns N' Roses", "Use Your Illusion I", "8:57"),
    Song(10, R.drawable.hotelcalifornia, "Life in the Fast Lane", "Eagles", "Hotel California", "4:46"),

    // 🔹 Rock moderno
    Song(11, R.drawable.queen, "Under Pressure", "Queen & David Bowie", "Hot Space", "4:08"),
    Song(12, R.drawable.gunsnroses, "Paradise City", "Guns N' Roses", "Appetite for Destruction", "6:45", true),
    Song(13, R.drawable.ledzeppelin, "Black Dog", "Led Zeppelin", "Led Zeppelin IV", "4:55"),
    Song(14, R.drawable.hotelcalifornia, "Desperado", "Eagles", "Desperado", "3:36"),
    Song(15, R.drawable.johnlennon, "Stand by Me", "John Lennon", "Rock 'n' Roll", "3:26", true),

    // 🔹 Extras para probar scroll largo
    Song(16, R.drawable.queen, "Somebody to Love", "Queen", "A Day at the Races", "4:56"),
    Song(17, R.drawable.gunsnroses, "Knockin' on Heaven’s Door", "Guns N' Roses", "Use Your Illusion II", "5:36"),
    Song(18, R.drawable.ledzeppelin, "Whole Lotta Love", "Led Zeppelin", "Led Zeppelin II", "5:34"),
    Song(19, R.drawable.hotelcalifornia, "Tequila Sunrise", "Eagles", "Desperado", "2:52"),
    Song(20, R.drawable.johnlennon, "Working Class Hero", "John Lennon", "Plastic Ono Band", "3:50")
)
