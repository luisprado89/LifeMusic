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
    Song(R.drawable.gunsnroses, "Sweet Child O' Mine", "Guns N' Roses", "Appetite for Destruction", "5:56", true),

    // 🔹 Clásicos adicionales
    Song(R.drawable.queen, "Don’t Stop Me Now", "Queen", "Jazz", "3:29"),
    Song(R.drawable.johnlennon, "Jealous Guy", "John Lennon", "Imagine", "4:14", true),
    Song(R.drawable.ledzeppelin, "Kashmir", "Led Zeppelin", "Physical Graffiti", "8:37"),
    Song(R.drawable.gunsnroses, "November Rain", "Guns N' Roses", "Use Your Illusion I", "8:57"),
    Song(R.drawable.hotelcalifornia, "Life in the Fast Lane", "Eagles", "Hotel California", "4:46"),

    // 🔹 Rock moderno
    Song(R.drawable.queen, "Under Pressure", "Queen & David Bowie", "Hot Space", "4:08"),
    Song(R.drawable.gunsnroses, "Paradise City", "Guns N' Roses", "Appetite for Destruction", "6:45", true),
    Song(R.drawable.ledzeppelin, "Black Dog", "Led Zeppelin", "Led Zeppelin IV", "4:55"),
    Song(R.drawable.hotelcalifornia, "Desperado", "Eagles", "Desperado", "3:36"),
    Song(R.drawable.johnlennon, "Stand by Me", "John Lennon", "Rock 'n' Roll", "3:26", true),

    // 🔹 Extras para probar scroll largo
    Song(R.drawable.queen, "Somebody to Love", "Queen", "A Day at the Races", "4:56"),
    Song(R.drawable.gunsnroses, "Knockin' on Heaven’s Door", "Guns N' Roses", "Use Your Illusion II", "5:36"),
    Song(R.drawable.ledzeppelin, "Whole Lotta Love", "Led Zeppelin", "Led Zeppelin II", "5:34"),
    Song(R.drawable.hotelcalifornia, "Tequila Sunrise", "Eagles", "Desperado", "2:52"),
    Song(R.drawable.johnlennon, "Working Class Hero", "John Lennon", "Plastic Ono Band", "3:50")
)
