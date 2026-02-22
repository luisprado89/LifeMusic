package com.luis.lifemusic.data.localsed

import com.luis.lifemusic.R

/**
 * Catálogo local "seed" (offline-first) — 57 canciones de 10 álbumes únicos.
 *
 * 🎯 OBJETIVO ARQUITECTÓNICO
 * - La app debe poder mostrar canciones SIN conexión.
 * - Cada artista aporta un único álbum con múltiples canciones.
 * - Este catálogo es el “fallback” inicial para Home/List/Detail.
 * - Cada item guarda spotifyId REAL para que cuando llegue Retrofit + Spotify:
 *   podamos filtrar por spotifyId y evitar repetidos.
 *
 * 🔑 CLAVE: spotifyId debe coincidir con Spotify Track.id
 * Ejemplo:
 * https://open.spotify.com/track/3z8h0TU7ReDPLIbEnYhWZb
 * → spotifyId = "3z8h0TU7ReDPLIbEnYhWZb"
 *
 * ✅ DATOS (Spotify)
 * - spotifyId: Track.id (REAL)
 * - title: Track.name
 * - artists: Track.artists[].name
 * - artistIds: Track.artists[].id (REAL)
 * - albumName: Track.album.name
 * - durationMs: Track.duration_ms
 * - popularity: Track.popularity (si no existe en endpoint usado, se deja inventada como se indicó)
 * - releaseDate: Track.album.release_date
 *
 * ✅ IMPORTANTE
 * - Este catálogo local NO se actualiza ni se “reemplaza” con online.
 * - Online se añade aparte y se filtra para no repetir IDs.
 */

data class LocalSeedSong(

    /** 🎵 SPOTIFY API: Track.id (EL ID REAL DE LA CANCIÓN EN SPOTIFY) */
    val spotifyId: String,

    /**
     * 🖼️ LOCAL ONLY: Recurso drawable
     * El comentario contiene el enlace al álbum en Spotify para que descargues la portada REAL.
     */
    val imageRes: Int,

    /** 🎵 SPOTIFY API: Track.name */
    val title: String,

    /** 🎵 SPOTIFY API: Track.artists[].name */
    val artists: List<String>,

    /** 🎵 SPOTIFY API: Track.artists[].id */
    val artistIds: List<String>,

    /** 🎵 SPOTIFY API: Track.album.name */
    val albumName: String,

    /** 🎵 SPOTIFY API: Track.duration_ms */
    val durationMs: Int,

    /** 🎵 SPOTIFY API: Track.popularity (0..100) */
    val popularity: Int,

    /**
     * Spotify API: Track.album.release_date
     * Puede venir como "YYYY", "YYYY-MM" o "YYYY-MM-DD".
     */
    val releaseDate: String?,

    /** 🌐 URL de Spotify (si hay conexión) */
    val imageUrl: String? = null
)

val localSeedSongs: List<LocalSeedSong> = listOf(
    // =========================================================
    // 1. QUEEN - A NIGHT AT THE OPERA (2011 Remaster)
    // AlbumId: 1GbtB4zTqAsyfZEsm1RZfx
    // ArtistId: 1dfeR4HaWDbWqFHLkxsg1d
    // =========================================================
    LocalSeedSong(
        spotifyId = "4u7EnebtmKWzUH433cf5Qv", // Bohemian Rhapsody - Remastered 2011
        imageRes = R.drawable.a_night_at_the_opera,
        title = "Bohemian Rhapsody",
        artists = listOf("Queen"),
        artistIds = listOf("1dfeR4HaWDbWqFHLkxsg1d"),
        albumName = "A Night At The Opera (2011 Remaster)",
        durationMs = 354_320,
        popularity = 90,
        releaseDate = "1975-11-21"
    ),
    LocalSeedSong(
        spotifyId = "4vhVDkSx9RSb2k6mWFMYNI", // You're My Best Friend - Remastered 2011
        imageRes = R.drawable.a_night_at_the_opera,
        title = "You're My Best Friend",
        artists = listOf("Queen"),
        artistIds = listOf("1dfeR4HaWDbWqFHLkxsg1d"),
        albumName = "A Night At The Opera (2011 Remaster)",
        durationMs = 170_786,
        popularity = 78,
        releaseDate = "1975-11-21"
    ),
    LocalSeedSong(
        spotifyId = "2BlNyI35idBaI6BN6WGZeQ", // Love Of My Life - Remastered 2011
        imageRes = R.drawable.a_night_at_the_opera,
        title = "Love Of My Life",
        artists = listOf("Queen"),
        artistIds = listOf("1dfeR4HaWDbWqFHLkxsg1d"),
        albumName = "A Night At The Opera (2011 Remaster)",
        durationMs = 217_386,
        popularity = 75,
        releaseDate = "1975-11-21"
    ),
    LocalSeedSong(
        spotifyId = "6aNP9GlBi3VHPXl7w3Qjr9", // '39 - Remastered 2011
        imageRes = R.drawable.a_night_at_the_opera,
        title = "'39",
        artists = listOf("Queen"),
        artistIds = listOf("1dfeR4HaWDbWqFHLkxsg1d"),
        albumName = "A Night At The Opera (2011 Remaster)",
        durationMs = 210_800,
        popularity = 68,
        releaseDate = "1975-11-21"
    ),
    LocalSeedSong(
        spotifyId = "0VU7avDE4OZ36oVmLPSEZA", // Seaside Rendezvous - Remastered 2011
        imageRes = R.drawable.a_night_at_the_opera,
        title = "Seaside Rendezvous",
        artists = listOf("Queen"),
        artistIds = listOf("1dfeR4HaWDbWqFHLkxsg1d"),
        albumName = "A Night At The Opera (2011 Remaster)",
        durationMs = 134_120,
        popularity = 62,
        releaseDate = "1975-11-21"
    ),

    // =========================================================
    // 2. LED ZEPPELIN - LED ZEPPELIN IV (Remaster) (5 canciones)
    // AlbumId: 5EyIDBAqhnlkAHqvPRwdbX
    // ArtistId: 36QJpDe2go2KgaRleHCDTp
    // =========================================================
    LocalSeedSong(
        spotifyId = "0RO9W1xJoUEpq5MEelddFb", // Stairway to Heaven - Remaster (track 4)
        imageRes = R.drawable.led_zeppelin_iv,
        title = "Stairway to Heaven",
        artists = listOf("Led Zeppelin"),
        artistIds = listOf("36QJpDe2go2KgaRleHCDTp"),
        albumName = "Led Zeppelin IV (Remaster)",
        durationMs = 482_830,
        popularity = 86,
        releaseDate = "1971-11-08"
    ),
    LocalSeedSong(
        spotifyId = "0D58ERdLBDRgT86BPnH8ps", // Black Dog - Remaster (track 1)
        imageRes = R.drawable.led_zeppelin_iv,
        title = "Black Dog",
        artists = listOf("Led Zeppelin"),
        artistIds = listOf("36QJpDe2go2KgaRleHCDTp"),
        albumName = "Led Zeppelin IV (Remaster)",
        durationMs = 295_386,
        popularity = 78,
        releaseDate = "1971-11-08"
    ),
    LocalSeedSong(
        spotifyId = "7dWfTdoKjUip1fAW7cdcIX", // Rock and Roll - Remaster (track 2)
        imageRes = R.drawable.led_zeppelin_iv,
        title = "Rock and Roll",
        artists = listOf("Led Zeppelin"),
        artistIds = listOf("36QJpDe2go2KgaRleHCDTp"),
        albumName = "Led Zeppelin IV (Remaster)",
        durationMs = 220_560,
        popularity = 79,
        releaseDate = "1971-11-08"
    ),
    LocalSeedSong(
        spotifyId = "1YVc2NJBwOtAebQiSUbt5T", // Going to California - Remaster (track 7)
        imageRes = R.drawable.led_zeppelin_iv,
        title = "Going to California",
        artists = listOf("Led Zeppelin"),
        artistIds = listOf("36QJpDe2go2KgaRleHCDTp"),
        albumName = "Led Zeppelin IV (Remaster)",
        durationMs = 212_160,
        popularity = 72,
        releaseDate = "1971-11-08"
    ),
    LocalSeedSong(
        spotifyId = "7psfcbjfnXwYIezHstNa1a", // When the Levee Breaks - Remaster (track 8)
        imageRes = R.drawable.led_zeppelin_iv,
        title = "When the Levee Breaks",
        artists = listOf("Led Zeppelin"),
        artistIds = listOf("36QJpDe2go2KgaRleHCDTp"),
        albumName = "Led Zeppelin IV (Remaster)",
        durationMs = 428_851,
        popularity = 76,
        releaseDate = "1971-11-08"
    ),

    // =========================================================
    // 3. GUNS N' ROSES - APPETITE FOR DESTRUCTION (5 canciones)
    // AlbumId: 3I9Z1nDCL4E0cP62flcbI5
    // ArtistId: 3qm84nBOXUEQ2vnTfUTTFC
    // =========================================================
    LocalSeedSong(
        spotifyId = "7o2CTH4ctstm8TNelqjb51", // Sweet Child O' Mine (linked_from.id)
        imageRes = R.drawable.appetite_for_destruction,
        title = "Sweet Child O' Mine",
        artists = listOf("Guns N' Roses"),
        artistIds = listOf("3qm84nBOXUEQ2vnTfUTTFC"),
        albumName = "Appetite For Destruction",
        durationMs = 354_520,
        popularity = 87,
        releaseDate = "1987-07-21"
    ),
    LocalSeedSong(
        spotifyId = "3YBZIN3rekqsKxbJc9FZko", // Paradise City (linked_from.id)
        imageRes = R.drawable.appetite_for_destruction,
        title = "Paradise City",
        artists = listOf("Guns N' Roses"),
        artistIds = listOf("3qm84nBOXUEQ2vnTfUTTFC"),
        albumName = "Appetite For Destruction",
        durationMs = 405_640,
        popularity = 82,
        releaseDate = "1987-07-21"
    ),
    LocalSeedSong(
        spotifyId = "0bVtevEgtDIeRjCJbK3Lmv", // Welcome To The Jungle (linked_from.id)
        imageRes = R.drawable.appetite_for_destruction,
        title = "Welcome To The Jungle",
        artists = listOf("Guns N' Roses"),
        artistIds = listOf("3qm84nBOXUEQ2vnTfUTTFC"),
        albumName = "Appetite For Destruction",
        durationMs = 272_026,
        popularity = 84,
        releaseDate = "1987-07-21"
    ),
    LocalSeedSong(
        spotifyId = "2vNw57KPaYDzkyPxXYUORX", // Nightrain (linked_from.id)
        imageRes = R.drawable.appetite_for_destruction,
        title = "Nightrain",
        artists = listOf("Guns N' Roses"),
        artistIds = listOf("3qm84nBOXUEQ2vnTfUTTFC"),
        albumName = "Appetite For Destruction",
        durationMs = 266_173,
        popularity = 72,
        releaseDate = "1987-07-21"
    ),
    LocalSeedSong(
        spotifyId = "4DnEyHNO8MdhFYFrDq73BV", // Mr. Brownstone (linked_from.id)
        imageRes = R.drawable.appetite_for_destruction,
        title = "Mr. Brownstone",
        artists = listOf("Guns N' Roses"),
        artistIds = listOf("3qm84nBOXUEQ2vnTfUTTFC"),
        albumName = "Appetite For Destruction",
        durationMs = 225_706,
        popularity = 70,
        releaseDate = "1987-07-21"
    ),

    // =========================================================
    // 4. NIRVANA - NEVERMIND (13 canciones)
    // ArtistId: 6olE6TJLqED3rqDCT0FyPh
    // =========================================================
    LocalSeedSong(
        spotifyId = "4CeeEOM32jQcH3eN9Q2dGj",
        imageRes = R.drawable.nevermind,
        title = "Smells Like Teen Spirit",
        artists = listOf("Nirvana"),
        artistIds = listOf("6olE6TJLqED3rqDCT0FyPh"),
        albumName = "Nevermind (Remastered)",
        durationMs = 301_920,
        popularity = 95,
        releaseDate = "1991-09-26"
    ),
    LocalSeedSong(
        spotifyId = "2mvffzYUJ9Ld9xhsF5DUjU",
        imageRes = R.drawable.nevermind,
        title = "In Bloom",
        artists = listOf("Nirvana"),
        artistIds = listOf("6olE6TJLqED3rqDCT0FyPh"),
        albumName = "Nevermind (Remastered)",
        durationMs = 255_080,
        popularity = 88,
        releaseDate = "1991-09-26"
    ),
    LocalSeedSong(
        spotifyId = "2RsAajgo0g7bMCHxwH3Sk0",
        imageRes = R.drawable.nevermind,
        title = "Come As You Are",
        artists = listOf("Nirvana"),
        artistIds = listOf("6olE6TJLqED3rqDCT0FyPh"),
        albumName = "Nevermind (Remastered)",
        durationMs = 218_920,
        popularity = 92,
        releaseDate = "1991-09-26"
    ),
    LocalSeedSong(
        spotifyId = "3PyPpABRA2bTGhNwPd66H6",
        imageRes = R.drawable.nevermind,
        title = "Breed",
        artists = listOf("Nirvana"),
        artistIds = listOf("6olE6TJLqED3rqDCT0FyPh"),
        albumName = "Nevermind (Remastered)",
        durationMs = 184_040,
        popularity = 80,
        releaseDate = "1991-09-26"
    ),
    LocalSeedSong(
        spotifyId = "5vHLwhxxlGzmClMcxRRFPr",
        imageRes = R.drawable.nevermind,
        title = "Lithium",
        artists = listOf("Nirvana"),
        artistIds = listOf("6olE6TJLqED3rqDCT0FyPh"),
        albumName = "Nevermind (Remastered)",
        durationMs = 257_053,
        popularity = 90,
        releaseDate = "1991-09-26"
    ),
    LocalSeedSong(
        spotifyId = "2SJ38LDlkNjwWSUq98r4Q5",
        imageRes = R.drawable.nevermind,
        title = "Polly",
        artists = listOf("Nirvana"),
        artistIds = listOf("6olE6TJLqED3rqDCT0FyPh"),
        albumName = "Nevermind (Remastered)",
        durationMs = 173_853,
        popularity = 78,
        releaseDate = "1991-09-26"
    ),
    LocalSeedSong(
        spotifyId = "6oZYLz0vZy8HhqwioRYtTp",
        imageRes = R.drawable.nevermind,
        title = "Territorial Pissings",
        artists = listOf("Nirvana"),
        artistIds = listOf("6olE6TJLqED3rqDCT0FyPh"),
        albumName = "Nevermind (Remastered)",
        durationMs = 142_946,
        popularity = 74,
        releaseDate = "1991-09-26"
    ),
    LocalSeedSong(
        spotifyId = "0bTLGlCqwZXwJGWGE2Dywg",
        imageRes = R.drawable.nevermind,
        title = "Drain You",
        artists = listOf("Nirvana"),
        artistIds = listOf("6olE6TJLqED3rqDCT0FyPh"),
        albumName = "Nevermind (Remastered)",
        durationMs = 223_880,
        popularity = 86,
        releaseDate = "1991-09-26"
    ),
    LocalSeedSong(
        spotifyId = "1o5jmMhhk2UZ9YP1X5fXfj",
        imageRes = R.drawable.nevermind,
        title = "Lounge Act",
        artists = listOf("Nirvana"),
        artistIds = listOf("6olE6TJLqED3rqDCT0FyPh"),
        albumName = "Nevermind (Remastered)",
        durationMs = 156_426,
        popularity = 76,
        releaseDate = "1991-09-26"
    ),
    LocalSeedSong(
        spotifyId = "0w7k0ZhHzkfEBsDmwp49xA",
        imageRes = R.drawable.nevermind,
        title = "Stay Away",
        artists = listOf("Nirvana"),
        artistIds = listOf("6olE6TJLqED3rqDCT0FyPh"),
        albumName = "Nevermind (Remastered)",
        durationMs = 211_440,
        popularity = 73,
        releaseDate = "1991-09-26"
    ),
    LocalSeedSong(
        spotifyId = "2xIlEsioAFODKpsVgVNrrQ",
        imageRes = R.drawable.nevermind,
        title = "On A Plain",
        artists = listOf("Nirvana"),
        artistIds = listOf("6olE6TJLqED3rqDCT0FyPh"),
        albumName = "Nevermind (Remastered)",
        durationMs = 194_426,
        popularity = 75,
        releaseDate = "1991-09-26"
    ),
    LocalSeedSong(
        spotifyId = "4gHnSNHs8RyVukKoWdS99f",
        imageRes = R.drawable.nevermind,
        title = "Something In The Way",
        artists = listOf("Nirvana"),
        artistIds = listOf("6olE6TJLqED3rqDCT0FyPh"),
        albumName = "Nevermind (Remastered)",
        durationMs = 232_146,
        popularity = 89,
        releaseDate = "1991-09-26"
    ),
    LocalSeedSong(
        spotifyId = "5KV9bNW1Ta4Z4PdHgu84TA",
        imageRes = R.drawable.nevermind,
        title = "Endless, Nameless",
        artists = listOf("Nirvana"),
        artistIds = listOf("6olE6TJLqED3rqDCT0FyPh"),
        albumName = "Nevermind (Remastered)",
        durationMs = 403_293,
        popularity = 60,
        releaseDate = "1991-09-26"
    ),

    // =========================================================
    // 5. U2 - THE JOSHUA TREE (5 canciones)
    // ArtistId: 51Blml2LZPmy7TTiAg47vQ
    // =========================================================
    LocalSeedSong(
        spotifyId = "2x45xqISlmmDJqxOqr8BuS",
        imageRes = R.drawable.the_joshua_tree,
        title = "With Or Without You - Remastered 2007",
        artists = listOf("U2"),
        artistIds = listOf("51Blml2LZPmy7TTiAg47vQ"),
        albumName = "The Joshua Tree",
        durationMs = 295_520,
        popularity = 86,
        releaseDate = "1987-03-09"
    ),
    LocalSeedSong(
        spotifyId = "3MRQ3CSjoiV1HFil8ykM9M",
        imageRes = R.drawable.the_joshua_tree,
        title = "I Still Haven't Found What I'm Looking For - Remastered 2007",
        artists = listOf("U2"),
        artistIds = listOf("51Blml2LZPmy7TTiAg47vQ"),
        albumName = "The Joshua Tree",
        durationMs = 277_480,
        popularity = 84,
        releaseDate = "1987-03-09"
    ),
    LocalSeedSong(
        spotifyId = "7h1YqA5MZrRxmkUFpukRcp",
        imageRes = R.drawable.the_joshua_tree,
        title = "Where The Streets Have No Name - Remastered",
        artists = listOf("U2"),
        artistIds = listOf("51Blml2LZPmy7TTiAg47vQ"),
        albumName = "The Joshua Tree",
        durationMs = 336_613,
        popularity = 82,
        releaseDate = "1987-03-09"
    ),
    LocalSeedSong(
        spotifyId = "1C6Tmo58WMtnAPdxYz9qCz",
        imageRes = R.drawable.the_joshua_tree,
        title = "Bullet The Blue Sky - Remastered 2007",
        artists = listOf("U2"),
        artistIds = listOf("51Blml2LZPmy7TTiAg47vQ"),
        albumName = "The Joshua Tree",
        durationMs = 271_546,
        popularity = 71,
        releaseDate = "1987-03-09"
    ),
    LocalSeedSong(
        spotifyId = "741AUHHkjuWe9wSNIhmApn",
        imageRes = R.drawable.the_joshua_tree,
        title = "Running To Stand Still - Remastered 2007",
        artists = listOf("U2"),
        artistIds = listOf("51Blml2LZPmy7TTiAg47vQ"),
        albumName = "The Joshua Tree",
        durationMs = 257_186,
        popularity = 68,
        releaseDate = "1987-03-09"
    ),

    // =========================================================
    // 6. PINK FLOYD - THE DARK SIDE OF THE MOON (5 canciones)
    // ArtistId: 0k17h0D3J5VfsdmQ1iZtE9
    // =========================================================
    LocalSeedSong(
        spotifyId = "0vFOzaXqZHahrZp6enQwQb",
        imageRes = R.drawable.dark_side_of_the_moon,
        title = "Money",
        artists = listOf("Pink Floyd"),
        artistIds = listOf("0k17h0D3J5VfsdmQ1iZtE9"),
        albumName = "The Dark Side of the Moon",
        durationMs = 380_080,
        popularity = 84,
        releaseDate = "1973-03-01"
    ),
    LocalSeedSong(
        spotifyId = "3TO7bbrUKrOSPGRTB5MeCz",
        imageRes = R.drawable.dark_side_of_the_moon,
        title = "Time",
        artists = listOf("Pink Floyd"),
        artistIds = listOf("0k17h0D3J5VfsdmQ1iZtE9"),
        albumName = "The Dark Side of the Moon",
        durationMs = 422_853,
        popularity = 82,
        releaseDate = "1973-03-01"
    ),
    LocalSeedSong(
        spotifyId = "1TKTiKp3zbNgrBH2IwSwIx",
        imageRes = R.drawable.dark_side_of_the_moon,
        title = "Us and Them",
        artists = listOf("Pink Floyd"),
        artistIds = listOf("0k17h0D3J5VfsdmQ1iZtE9"),
        albumName = "The Dark Side of the Moon",
        durationMs = 472_626,
        popularity = 76,
        releaseDate = "1973-03-01"
    ),
    LocalSeedSong(
        spotifyId = "05uGBKRCuePsf43Hfm0JwX",
        imageRes = R.drawable.dark_side_of_the_moon,
        title = "Brain Damage",
        artists = listOf("Pink Floyd"),
        artistIds = listOf("0k17h0D3J5VfsdmQ1iZtE9"),
        albumName = "The Dark Side of the Moon",
        durationMs = 230_493,
        popularity = 73,
        releaseDate = "1973-03-01"
    ),
    LocalSeedSong(
        spotifyId = "1tDWVeCR9oWGX8d5J9rswk",
        imageRes = R.drawable.dark_side_of_the_moon,
        title = "Eclipse",
        artists = listOf("Pink Floyd"),
        artistIds = listOf("0k17h0D3J5VfsdmQ1iZtE9"),
        albumName = "The Dark Side of the Moon",
        durationMs = 126_720,
        popularity = 71,
        releaseDate = "1973-03-01"
    ),

    // =========================================================
    // 7. THE BEATLES - ABBEY ROAD (5 canciones)
    // ArtistId: 3WrFJ7ztbogyGnTHbHJFl2
    // =========================================================
    LocalSeedSong(
        spotifyId = "2EqlS6tkEnglzr7tkKAAYD",
        imageRes = R.drawable.abbey_road,
        title = "Come Together",
        artists = listOf("The Beatles"),
        artistIds = listOf("3WrFJ7ztbogyGnTHbHJFl2"),
        albumName = "Abbey Road",
        durationMs = 259_946,
        popularity = 89,
        releaseDate = "1969-09-26"
    ),
    LocalSeedSong(
        spotifyId = "0pNeVovbiZHkulpGeOx1Gj",
        imageRes = R.drawable.abbey_road,
        title = "Something",
        artists = listOf("The Beatles"),
        artistIds = listOf("3WrFJ7ztbogyGnTHbHJFl2"),
        albumName = "Abbey Road",
        durationMs = 182_293,
        popularity = 84,
        releaseDate = "1969-09-26"
    ),
    LocalSeedSong(
        spotifyId = "6dGnYIeXmHdcikdzNNDMm2",
        imageRes = R.drawable.abbey_road,
        title = "Here Comes the Sun",
        artists = listOf("The Beatles"),
        artistIds = listOf("3WrFJ7ztbogyGnTHbHJFl2"),
        albumName = "Abbey Road",
        durationMs = 185_733,
        popularity = 92,
        releaseDate = "1969-09-26"
    ),
    LocalSeedSong(
        spotifyId = "2mxByJWOajjiVsLWjNXvDJ",
        imageRes = R.drawable.abbey_road,
        title = "Oh! Darling",
        artists = listOf("The Beatles"),
        artistIds = listOf("3WrFJ7ztbogyGnTHbHJFl2"),
        albumName = "Abbey Road",
        durationMs = 207_240,
        popularity = 71,
        releaseDate = "1969-09-26"
    ),
    LocalSeedSong(
        spotifyId = "0suLngfo7rJoetk7Ub6N8l",
        imageRes = R.drawable.abbey_road,
        title = "Octopus's Garden",
        artists = listOf("The Beatles"),
        artistIds = listOf("3WrFJ7ztbogyGnTHbHJFl2"),
        albumName = "Abbey Road",
        durationMs = 170_720,
        popularity = 66,
        releaseDate = "1969-09-26"
    ),

    // =========================================================
    // 8. MICHAEL JACKSON - THRILLER (5 canciones)
    // ArtistId: 3fMbdgg4jU18AjLCKBhRSm
    // =========================================================
    LocalSeedSong(
        spotifyId = "7J1uxwnxfQLu4APicE5Rnj",
        imageRes = R.drawable.thriller,
        title = "Billie Jean",
        artists = listOf("Michael Jackson"),
        artistIds = listOf("3fMbdgg4jU18AjLCKBhRSm"),
        albumName = "Thriller",
        durationMs = 293_802,
        popularity = 91,
        releaseDate = "1982-11-29"
    ),
    LocalSeedSong(
        spotifyId = "3BovdzfaX4jb5KFQwoPfAw",
        imageRes = R.drawable.thriller,
        title = "Beat It",
        artists = listOf("Michael Jackson"),
        artistIds = listOf("3fMbdgg4jU18AjLCKBhRSm"),
        albumName = "Thriller",
        durationMs = 258_296,
        popularity = 88,
        releaseDate = "1982-11-29"
    ),
    LocalSeedSong(
        spotifyId = "2LlQb7Uoj1kKyGhlkBf9aC",
        imageRes = R.drawable.thriller,
        title = "Thriller",
        artists = listOf("Michael Jackson"),
        artistIds = listOf("3fMbdgg4jU18AjLCKBhRSm"),
        albumName = "Thriller",
        durationMs = 358_807,
        popularity = 86,
        releaseDate = "1982-11-29"
    ),
    LocalSeedSong(
        spotifyId = "1hu2s7qkm5bo03eODpRQO3",
        imageRes = R.drawable.thriller,
        title = "Wanna Be Startin' Somethin'",
        artists = listOf("Michael Jackson"),
        artistIds = listOf("3fMbdgg4jU18AjLCKBhRSm"),
        albumName = "Thriller",
        durationMs = 363_400,
        popularity = 79,
        releaseDate = "1982-11-29"
    ),
    LocalSeedSong(
        spotifyId = "4cgjA7B4fJBHyB9Ya2bu0t",
        imageRes = R.drawable.thriller,
        title = "Human Nature",
        artists = listOf("Michael Jackson"),
        artistIds = listOf("3fMbdgg4jU18AjLCKBhRSm"),
        albumName = "Thriller",
        durationMs = 245_837,
        popularity = 77,
        releaseDate = "1982-11-29"
    ),

    // =========================================================
    // 9. PRINCE - PURPLE RAIN (4 canciones)
    // ArtistId: 5a2EaR3hamoenG9rDuVn8j
    // =========================================================
    LocalSeedSong(
        spotifyId = "1uvyZBs4IZYRebHIB1747m",
        imageRes = R.drawable.purple_rain,
        title = "Purple Rain",
        artists = listOf("Prince"),
        artistIds = listOf("5a2EaR3hamoenG9rDuVn8j"),
        albumName = "Purple Rain",
        durationMs = 521_866,
        popularity = 85,
        releaseDate = "1984-06-25"
    ),
    LocalSeedSong(
        spotifyId = "6sby78fghipoXHQLeeZFFH",
        imageRes = R.drawable.purple_rain,
        title = "When Doves Cry",
        artists = listOf("Prince"),
        artistIds = listOf("5a2EaR3hamoenG9rDuVn8j"),
        albumName = "Purple Rain",
        durationMs = 354_133,
        popularity = 83,
        releaseDate = "1984-06-25"
    ),
    LocalSeedSong(
        spotifyId = "6FMIVQPZg9cmMY8hPxAacD",
        imageRes = R.drawable.purple_rain,
        title = "Let's Go Crazy",
        artists = listOf("Prince"),
        artistIds = listOf("5a2EaR3hamoenG9rDuVn8j"),
        albumName = "Purple Rain",
        durationMs = 279_240,
        popularity = 74,
        releaseDate = "1984-06-25"
    ),
    LocalSeedSong(
        spotifyId = "3QszJeuSyyZQmD9pY1tqpo",
        imageRes = R.drawable.purple_rain,
        title = "I Would Die 4 U",
        artists = listOf("Prince"),
        artistIds = listOf("5a2EaR3hamoenG9rDuVn8j"),
        albumName = "Purple Rain",
        durationMs = 169_666,
        popularity = 68,
        releaseDate = "1984-06-25"
    ),

    // =========================================================
    // 10. ED SHEERAN - ÷ (DIVIDE) (10 canciones)
    // ArtistId: 6eUKZXaKkcviH0Ku9w2n3V
    // =========================================================
    LocalSeedSong(
        spotifyId = "7oolFzHipTMg2nL7shhdz2",
        imageRes = R.drawable.divide,
        title = "Eraser",
        artists = listOf("Ed Sheeran"),
        artistIds = listOf("6eUKZXaKkcviH0Ku9w2n3V"),
        albumName = "÷ (Divide)",
        durationMs = 227_426,
        popularity = 78,
        releaseDate = "2017-03-03"
    ),
    LocalSeedSong(
        spotifyId = "6PCUP3dWmTjcTtXY02oFdT",
        imageRes = R.drawable.divide,
        title = "Castle on the Hill",
        artists = listOf("Ed Sheeran"),
        artistIds = listOf("6eUKZXaKkcviH0Ku9w2n3V"),
        albumName = "÷ (Divide)",
        durationMs = 261_153,
        popularity = 87,
        releaseDate = "2017-03-03"
    ),
    LocalSeedSong(
        spotifyId = "51ChrwmUPDJvedPQnIU8Ls",
        imageRes = R.drawable.divide,
        title = "Dive",
        artists = listOf("Ed Sheeran"),
        artistIds = listOf("6eUKZXaKkcviH0Ku9w2n3V"),
        albumName = "÷ (Divide)",
        durationMs = 238_440,
        popularity = 80,
        releaseDate = "2017-03-03"
    ),
    LocalSeedSong(
        spotifyId = "7qiZfU4dY1lWllzX7mPBI3",
        imageRes = R.drawable.divide,
        title = "Shape of You",
        artists = listOf("Ed Sheeran"),
        artistIds = listOf("6eUKZXaKkcviH0Ku9w2n3V"),
        albumName = "÷ (Divide)",
        durationMs = 233_712,
        popularity = 95,
        releaseDate = "2017-03-03"
    ),
    LocalSeedSong(
        spotifyId = "0tgVpDi06FyKpA1z0VMD4v",
        imageRes = R.drawable.divide,
        title = "Perfect",
        artists = listOf("Ed Sheeran"),
        artistIds = listOf("6eUKZXaKkcviH0Ku9w2n3V"),
        albumName = "÷ (Divide)",
        durationMs = 263_400,
        popularity = 92,
        releaseDate = "2017-03-03"
    ),
    LocalSeedSong(
        spotifyId = "0afhq8XCExXpqazXczTSve",
        imageRes = R.drawable.divide,
        title = "Galway Girl",
        artists = listOf("Ed Sheeran"),
        artistIds = listOf("6eUKZXaKkcviH0Ku9w2n3V"),
        albumName = "÷ (Divide)",
        durationMs = 170_826,
        popularity = 84,
        releaseDate = "2017-03-03"
    ),
    LocalSeedSong(
        spotifyId = "2RttW7RAu5nOAfq6YFvApB",
        imageRes = R.drawable.divide,
        title = "Happier",
        artists = listOf("Ed Sheeran"),
        artistIds = listOf("6eUKZXaKkcviH0Ku9w2n3V"),
        albumName = "÷ (Divide)",
        durationMs = 207_520,
        popularity = 83,
        releaseDate = "2017-03-03"
    ),
    LocalSeedSong(
        spotifyId = "5HDPtsnyb3maFmPL8LLUTG",
        imageRes = R.drawable.divide,
        title = "New Man",
        artists = listOf("Ed Sheeran"),
        artistIds = listOf("6eUKZXaKkcviH0Ku9w2n3V"),
        albumName = "÷ (Divide)",
        durationMs = 189_280,
        popularity = 79,
        releaseDate = "2017-03-03"
    ),
    LocalSeedSong(
        spotifyId = "2dfHh7ECGxfNqZTQno09Vk",
        imageRes = R.drawable.divide,
        title = "Hearts Don't Break Around Here",
        artists = listOf("Ed Sheeran"),
        artistIds = listOf("6eUKZXaKkcviH0Ku9w2n3V"),
        albumName = "÷ (Divide)",
        durationMs = 248_413,
        popularity = 81,
        releaseDate = "2017-03-03"
    ),
    LocalSeedSong(
        spotifyId = "2pJZ1v8HezrAoZ0Fhzby92",
        imageRes = R.drawable.divide,
        title = "What Do I Know?",
        artists = listOf("Ed Sheeran"),
        artistIds = listOf("6eUKZXaKkcviH0Ku9w2n3V"),
        albumName = "÷ (Divide)",
        durationMs = 237_333,
        popularity = 77,
        releaseDate = "2017-03-03"
    ),
)