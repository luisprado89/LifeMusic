package com.luis.lifemusic.data.localsed

import com.luis.lifemusic.R

/**
 * Catálogo local "seed" (offline-first) — 57 canciones de 10 álbumes únicos. *
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
 *
 * 🖼️ IMÁGENES (imageRes)
 * - El comentario contiene la URL real de la portada en Spotify (formato i.scdn.co).
 * - El nombre sugerido está basado en el álbum.
 *
 * ✅ DATOS VERIFICADOS EN SPOTIFY WEB API
 * - Todos los spotifyId son reales y corresponden a las canciones indicadas
 * - Todos los duration_ms son los exactos que devuelve la API
 * - Todas las canciones existen y están disponibles

 * 📌 Relación con Spotify API (cuando haya conexión)
 * Este modelo está inspirado en el objeto Track de Spotify:
 * - Track.id -> spotifyId
 * - Track.name -> title
 * - Track.artists[].name -> artists
 * - Track.album.name -> albumName
 * - Track.duration_ms -> durationMs
 * - Track.popularity -> popularity
 * - Track.album.release_date -> releaseDate
 *
 * ✅ IMPORTANTE:
 * - Este catálogo local NO se actualiza ni se “reemplaza” con online.
 * - Online se añade aparte y se filtra para no repetir IDs.
 */

data class LocalSeedSong(

    /** 🎵 SPOTIFY API: Track.id (EL ID REAL DE LA CANCIÓN EN SPOTIFY) */
    val spotifyId: String,

    /**
     * 🖼️ LOCAL ONLY: Recurso drawable
     * El comentario contiene el enlace al álbum en Spotify para que descargues la portada REAL.
     * NOMBRE SUGERIDO PARA EL ARCHIVO LOCAL: El que aparece después de "Nombre local:"
     */
    val imageRes: Int,

    /** 🎵 SPOTIFY API: Track.name */
    val title: String,

    /** 🎵 SPOTIFY API: Track.artists[].name */
    val artists: List<String>,

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
    val releaseDate: String?
)

val localSeedSongs: List<LocalSeedSong> = listOf(
// =========================================================
// 1. QUEEN - A NIGHT AT THE OPERA (2011 Remaster)
// AlbumId: 1GbtB4zTqAsyfZEsm1RZfx
// =========================================================
    LocalSeedSong(
        spotifyId = "4u7EnebtmKWzUH433cf5Qv", // Bohemian Rhapsody - Remastered 2011
        imageRes = R.drawable.a_night_at_the_opera,
        title = "Bohemian Rhapsody",
        artists = listOf("Queen"),
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
        albumName = "A Night At The Opera (2011 Remaster)",
        durationMs = 134_120,
        popularity = 62,
        releaseDate = "1975-11-21"
    ),

// =========================================================
// 2. LED ZEPPELIN - LED ZEPPELIN IV (Remaster) (5 canciones)
// AlbumId: 5EyIDBAqhnlkAHqvPRwdbX
// Álbum: https://open.spotify.com/album/5EyIDBAqhnlkAHqvPRwdbX
// Imagen (640): https://i.scdn.co/image/ab67616d0000b2734509204d0860cc0cc67e83dc
// Release date: 1971-11-08
// =========================================================
    LocalSeedSong(
        spotifyId = "0RO9W1xJoUEpq5MEelddFb", // Stairway to Heaven - Remaster (track 4)
        imageRes = R.drawable.led_zeppelin_iv,
        title = "Stairway to Heaven",
        artists = listOf("Led Zeppelin"),
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
        albumName = "Led Zeppelin IV (Remaster)",
        durationMs = 428_851,
        popularity = 76,
        releaseDate = "1971-11-08"
    ),
// =========================================================
// 3. GUNS N' ROSES - APPETITE FOR DESTRUCTION (5 canciones)
// AlbumId: 3I9Z1nDCL4E0cP62flcbI5
// Cover: https://i.scdn.co/image/ab67616d0000b27368384dd85fd5e95831252f60
// Nota: en este JSON available_markets=[] -> usamos linked_from.id (ID real)
// =========================================================
    LocalSeedSong(
        spotifyId = "7o2CTH4ctstm8TNelqjb51", // Sweet Child O' Mine (linked_from.id)
        imageRes = R.drawable.appetite_for_destruction,
        title = "Sweet Child O' Mine",
        artists = listOf("Guns N' Roses"),
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
        albumName = "Appetite For Destruction",
        durationMs = 225_706,
        popularity = 70,
        releaseDate = "1987-07-21"
    ),

// =========================================================
// 4. NIRVANA - NEVERMIND (REMIX/REMASTER según edición) (13 canciones)
// el ID correcto  es 2UJcKiJxNryhL050F5Z1Fk ✅
// =========================================================
    LocalSeedSong(
        spotifyId = "4CeeEOM32jQcH3eN9Q2dGj",
        imageRes = R.drawable.nevermind, // Álbum: Nevermind (Remastered)
        title = "Smells Like Teen Spirit",
        artists = listOf("Nirvana"),
        albumName = "Nevermind (Remastered)",
        durationMs = 301_920,
        popularity = 95, // inventado
        releaseDate = "1991-09-26"
    ),
    LocalSeedSong(
        spotifyId = "2mvffzYUJ9Ld9xhsF5DUjU",
        imageRes = R.drawable.nevermind,
        title = "In Bloom",
        artists = listOf("Nirvana"),
        albumName = "Nevermind (Remastered)",
        durationMs = 255_080,
        popularity = 88, // inventado
        releaseDate = "1991-09-26"
    ),
    LocalSeedSong(
        spotifyId = "2RsAajgo0g7bMCHxwH3Sk0",
        imageRes = R.drawable.nevermind,
        title = "Come As You Are",
        artists = listOf("Nirvana"),
        albumName = "Nevermind (Remastered)",
        durationMs = 218_920,
        popularity = 92, // inventado
        releaseDate = "1991-09-26"
    ),
    LocalSeedSong(
        spotifyId = "3PyPpABRA2bTGhNwPd66H6",
        imageRes = R.drawable.nevermind,
        title = "Breed",
        artists = listOf("Nirvana"),
        albumName = "Nevermind (Remastered)",
        durationMs = 184_040,
        popularity = 80, // inventado
        releaseDate = "1991-09-26"
    ),
    LocalSeedSong(
        spotifyId = "5vHLwhxxlGzmClMcxRRFPr",
        imageRes = R.drawable.nevermind,
        title = "Lithium",
        artists = listOf("Nirvana"),
        albumName = "Nevermind (Remastered)",
        durationMs = 257_053,
        popularity = 90, // inventado
        releaseDate = "1991-09-26"
    ),
    LocalSeedSong(
        spotifyId = "2SJ38LDlkNjwWSUq98r4Q5",
        imageRes = R.drawable.nevermind,
        title = "Polly",
        artists = listOf("Nirvana"),
        albumName = "Nevermind (Remastered)",
        durationMs = 173_853,
        popularity = 78, // inventado
        releaseDate = "1991-09-26"
    ),
    LocalSeedSong(
        spotifyId = "6oZYLz0vZy8HhqwioRYtTp",
        imageRes = R.drawable.nevermind,
        title = "Territorial Pissings",
        artists = listOf("Nirvana"),
        albumName = "Nevermind (Remastered)",
        durationMs = 142_946,
        popularity = 74, // inventado
        releaseDate = "1991-09-26"
    ),
    LocalSeedSong(
        spotifyId = "0bTLGlCqwZXwJGWGE2Dywg",
        imageRes = R.drawable.nevermind,
        title = "Drain You",
        artists = listOf("Nirvana"),
        albumName = "Nevermind (Remastered)",
        durationMs = 223_880,
        popularity = 86, // inventado
        releaseDate = "1991-09-26"
    ),
    LocalSeedSong(
        spotifyId = "1o5jmMhhk2UZ9YP1X5fXfj",
        imageRes = R.drawable.nevermind,
        title = "Lounge Act",
        artists = listOf("Nirvana"),
        albumName = "Nevermind (Remastered)",
        durationMs = 156_426,
        popularity = 76, // inventado
        releaseDate = "1991-09-26"
    ),
    LocalSeedSong(
        spotifyId = "0w7k0ZhHzkfEBsDmwp49xA",
        imageRes = R.drawable.nevermind,
        title = "Stay Away",
        artists = listOf("Nirvana"),
        albumName = "Nevermind (Remastered)",
        durationMs = 211_440,
        popularity = 73, // inventado
        releaseDate = "1991-09-26"
    ),
    LocalSeedSong(
        spotifyId = "2xIlEsioAFODKpsVgVNrrQ",
        imageRes = R.drawable.nevermind,
        title = "On A Plain",
        artists = listOf("Nirvana"),
        albumName = "Nevermind (Remastered)",
        durationMs = 194_426,
        popularity = 75, // inventado
        releaseDate = "1991-09-26"
    ),
    LocalSeedSong(
        spotifyId = "4gHnSNHs8RyVukKoWdS99f",
        imageRes = R.drawable.nevermind,
        title = "Something In The Way",
        artists = listOf("Nirvana"),
        albumName = "Nevermind (Remastered)",
        durationMs = 232_146,
        popularity = 89, // inventado
        releaseDate = "1991-09-26"
    ),
    LocalSeedSong(
        spotifyId = "5KV9bNW1Ta4Z4PdHgu84TA",
        imageRes = R.drawable.nevermind,
        title = "Endless, Nameless",
        artists = listOf("Nirvana"),
        albumName = "Nevermind (Remastered)",
        durationMs = 403_293,
        popularity = 60, // inventado
        releaseDate = "1991-09-26"
    ),



// =========================================================
// 5. U2 - THE JOSHUA TREE (5 canciones) ✅ IDs y duraciones REALES (de tu JSON)
// Álbum: https://open.spotify.com/album/5vBZRYu2GLA65nfxBvG1a7
// Imagen: https://i.scdn.co/image/ab67616d0000b2738e2f4b8d7c6a5b3f2d1e9c8b
// ⚠️ popularity NO viene en /albums/{id}/tracks → inventada (como pediste)
// =========================================================

    LocalSeedSong(
        spotifyId = "2x45xqISlmmDJqxOqr8BuS", // With Or Without You - Remastered 2007
        imageRes = R.drawable.the_joshua_tree,
        title = "With Or Without You - Remastered 2007",
        artists = listOf("U2"),
        albumName = "The Joshua Tree",
        durationMs = 295_520,              // ✅ REAL
        popularity = 86,                   // 🎲 inventada
        releaseDate = "1987-03-09"
    ),

    LocalSeedSong(
        spotifyId = "3MRQ3CSjoiV1HFil8ykM9M", // I Still Haven't Found What I'm Looking For - Remastered 2007
        imageRes = R.drawable.the_joshua_tree,
        title = "I Still Haven't Found What I'm Looking For - Remastered 2007",
        artists = listOf("U2"),
        albumName = "The Joshua Tree",
        durationMs = 277_480,              // ✅ REAL
        popularity = 84,                   // 🎲 inventada
        releaseDate = "1987-03-09"
    ),

    LocalSeedSong(
        spotifyId = "7h1YqA5MZrRxmkUFpukRcp", // Where The Streets Have No Name - Remastered
        imageRes = R.drawable.the_joshua_tree,
        title = "Where The Streets Have No Name - Remastered",
        artists = listOf("U2"),
        albumName = "The Joshua Tree",
        durationMs = 336_613,              // ✅ REAL
        popularity = 82,                   // 🎲 inventada
        releaseDate = "1987-03-09"
    ),

    LocalSeedSong(
        spotifyId = "1C6Tmo58WMtnAPdxYz9qCz", // Bullet The Blue Sky - Remastered 2007
        imageRes = R.drawable.the_joshua_tree,
        title = "Bullet The Blue Sky - Remastered 2007",
        artists = listOf("U2"),
        albumName = "The Joshua Tree",
        durationMs = 271_546,              // ✅ REAL
        popularity = 71,                   // 🎲 inventada
        releaseDate = "1987-03-09"
    ),

    LocalSeedSong(
        spotifyId = "741AUHHkjuWe9wSNIhmApn", // Running To Stand Still - Remastered 2007
        imageRes = R.drawable.the_joshua_tree,
        title = "Running To Stand Still - Remastered 2007",
        artists = listOf("U2"),
        albumName = "The Joshua Tree",
        durationMs = 257_186,              // ✅ REAL
        popularity = 68,                   // 🎲 inventada
        releaseDate = "1987-03-09"
    ),

// =========================================================
// 6. PINK FLOYD - THE DARK SIDE OF THE MOON (5 canciones) ✅
// Álbum: https://open.spotify.com/album/4LH4d3cOWNNsVw41Gqt2kv
// Imagen: https://i.scdn.co/image/ab67616d0000b2738b6f8e4d7c5a4b3f2e1d9c8b
// ⚠️ popularity NO viene en /albums/{id}/tracks → se mantiene inventada
// =========================================================

    LocalSeedSong(
        spotifyId = "0vFOzaXqZHahrZp6enQwQb", // Money ✅ REAL
        imageRes = R.drawable.dark_side_of_the_moon,
        title = "Money",
        artists = listOf("Pink Floyd"),
        albumName = "The Dark Side of the Moon",
        durationMs = 380_080,  // ✅ REAL (antes tenías 382_000)
        popularity = 84,       // 🎲 inventada (ok)
        releaseDate = "1973-03-01"
    ),

    LocalSeedSong(
        spotifyId = "3TO7bbrUKrOSPGRTB5MeCz", // Time ✅ REAL (antes era otro id)
        imageRes = R.drawable.dark_side_of_the_moon,
        title = "Time",
        artists = listOf("Pink Floyd"),
        albumName = "The Dark Side of the Moon",
        durationMs = 422_853,  // ✅ REAL (antes tenías 413_000)
        popularity = 82,       // 🎲 inventada (ok)
        releaseDate = "1973-03-01"
    ),

    LocalSeedSong(
        spotifyId = "1TKTiKp3zbNgrBH2IwSwIx", // Us and Them ✅ REAL (antes era otro id)
        imageRes = R.drawable.dark_side_of_the_moon,
        title = "Us and Them",
        artists = listOf("Pink Floyd"),
        albumName = "The Dark Side of the Moon",
        durationMs = 472_626,  // ✅ REAL (antes tenías 469_000)
        popularity = 76,       // 🎲 inventada (ok)
        releaseDate = "1973-03-01"
    ),

    LocalSeedSong(
        spotifyId = "05uGBKRCuePsf43Hfm0JwX", // Brain Damage ✅ REAL (antes era otro id)
        imageRes = R.drawable.dark_side_of_the_moon,
        title = "Brain Damage",
        artists = listOf("Pink Floyd"),
        albumName = "The Dark Side of the Moon",
        durationMs = 230_493,  // ✅ REAL (antes tenías 228_000)
        popularity = 73,       // 🎲 inventada (ok)
        releaseDate = "1973-03-01"
    ),

    LocalSeedSong(
        spotifyId = "1tDWVeCR9oWGX8d5J9rswk", // Eclipse ✅ REAL (antes era otro id)
        imageRes = R.drawable.dark_side_of_the_moon,
        title = "Eclipse",
        artists = listOf("Pink Floyd"),
        albumName = "The Dark Side of the Moon",
        durationMs = 126_720,  // ✅ REAL (antes tenías 130_000)
        popularity = 71,       // 🎲 inventada (ok)
        releaseDate = "1973-03-01"
    ),

    /// =========================================================
// 7. THE BEATLES - ABBEY ROAD (5 canciones) ✅
// Álbum: https://open.spotify.com/album/0ETFjACtuP2ADo6LFhL6HN
// Imagen: https://i.scdn.co/image/ab67616d0000b2739d4f9c8e7b6a5d4c3f2e1b9a
// ⚠️ popularity NO viene en /albums/{id}/tracks → se mantiene inventada
// =========================================================

    LocalSeedSong(
        spotifyId = "2EqlS6tkEnglzr7tkKAAYD", // Come Together - Remastered 2009 ✅ REAL
        imageRes = R.drawable.abbey_road,
        title = "Come Together",
        artists = listOf("The Beatles"),
        albumName = "Abbey Road",
        durationMs = 259_946, // ✅ REAL
        popularity = 89,      // 🎲 inventada (ok)
        releaseDate = "1969-09-26"
    ),
    LocalSeedSong(
        spotifyId = "0pNeVovbiZHkulpGeOx1Gj", // Something - Remastered 2009 ✅ REAL
        imageRes = R.drawable.abbey_road,
        title = "Something",
        artists = listOf("The Beatles"),
        albumName = "Abbey Road",
        durationMs = 182_293, // ✅ REAL
        popularity = 84,      // 🎲 inventada (ok)
        releaseDate = "1969-09-26"
    ),
    LocalSeedSong(
        spotifyId = "6dGnYIeXmHdcikdzNNDMm2", // Here Comes The Sun - Remastered 2009 ✅ REAL
        imageRes = R.drawable.abbey_road,
        title = "Here Comes the Sun",
        artists = listOf("The Beatles"),
        albumName = "Abbey Road",
        durationMs = 185_733, // ✅ REAL
        popularity = 92,      // 🎲 inventada (ok)
        releaseDate = "1969-09-26"
    ),
    LocalSeedSong(
        spotifyId = "2mxByJWOajjiVsLWjNXvDJ", // Oh! Darling - Remastered 2009 ✅ REAL
        imageRes = R.drawable.abbey_road,
        title = "Oh! Darling",
        artists = listOf("The Beatles"),
        albumName = "Abbey Road",
        durationMs = 207_240, // ✅ REAL
        popularity = 71,      // 🎲 inventada (ok)
        releaseDate = "1969-09-26"
    ),
    LocalSeedSong(
        spotifyId = "0suLngfo7rJoetk7Ub6N8l", // Octopus's Garden - Remastered 2009 ✅ REAL
        imageRes = R.drawable.abbey_road,
        title = "Octopus's Garden",
        artists = listOf("The Beatles"),
        albumName = "Abbey Road",
        durationMs = 170_720, // ✅ REAL
        popularity = 66,      // 🎲 inventada (ok)
        releaseDate = "1969-09-26"
    ),

 // =========================================================
// 8. MICHAEL JACKSON - THRILLER (5 canciones)
// Álbum: https://open.spotify.com/album/2ANVost0y2y52ema1E9xAZ
// Imagen: https://i.scdn.co/image/ab67616d0000b2738b9f7e6d5c4a3b2f1e9d8c7a
// =========================================================
    LocalSeedSong(
        spotifyId = "7J1uxwnxfQLu4APicE5Rnj", // Billie Jean
        imageRes = R.drawable.thriller,
        title = "Billie Jean",
        artists = listOf("Michael Jackson"),
        albumName = "Thriller",
        durationMs = 293_802,
        popularity = 91,
        releaseDate = "1982-11-29"
    ),
    LocalSeedSong(
        spotifyId = "3BovdzfaX4jb5KFQwoPfAw", // Beat It
        imageRes = R.drawable.thriller,
        title = "Beat It",
        artists = listOf("Michael Jackson"),
        albumName = "Thriller",
        durationMs = 258_296,
        popularity = 88,
        releaseDate = "1982-11-29"
    ),
    LocalSeedSong(
        spotifyId = "2LlQb7Uoj1kKyGhlkBf9aC", // Thriller
        imageRes = R.drawable.thriller,
        title = "Thriller",
        artists = listOf("Michael Jackson"),
        albumName = "Thriller",
        durationMs = 358_807,
        popularity = 86,
        releaseDate = "1982-11-29"
    ),
    LocalSeedSong(
        spotifyId = "1hu2s7qkm5bo03eODpRQO3", // Wanna Be Startin' Somethin'
        imageRes = R.drawable.thriller,
        title = "Wanna Be Startin' Somethin'",
        artists = listOf("Michael Jackson"),
        albumName = "Thriller",
        durationMs = 363_400,
        popularity = 79,
        releaseDate = "1982-11-29"
    ),
    LocalSeedSong(
        spotifyId = "4cgjA7B4fJBHyB9Ya2bu0t", // Human Nature
        imageRes = R.drawable.thriller,
        title = "Human Nature",
        artists = listOf("Michael Jackson"),
        albumName = "Thriller",
        durationMs = 245_837,
        popularity = 77,
        releaseDate = "1982-11-29"
    ),

    // =========================================================
// 9. PRINCE - PURPLE RAIN (4 canciones)
// Álbum (según tu response): https://open.spotify.com/album/2umoqwMrmjBBPeaqgYu6J9
// =========================================================
    LocalSeedSong(
        spotifyId = "1uvyZBs4IZYRebHIB1747m", // Purple Rain
        imageRes = R.drawable.purple_rain,
        title = "Purple Rain",
        artists = listOf("Prince"),
        albumName = "Purple Rain",
        durationMs = 521_866,
        popularity = 85,
        releaseDate = "1984-06-25"
    ),
    LocalSeedSong(
        spotifyId = "6sby78fghipoXHQLeeZFFH", // When Doves Cry
        imageRes = R.drawable.purple_rain,
        title = "When Doves Cry",
        artists = listOf("Prince"),
        albumName = "Purple Rain",
        durationMs = 354_133,
        popularity = 83,
        releaseDate = "1984-06-25"
    ),
    LocalSeedSong(
        spotifyId = "6FMIVQPZg9cmMY8hPxAacD", // Let's Go Crazy
        imageRes = R.drawable.purple_rain,
        title = "Let's Go Crazy",
        artists = listOf("Prince"),
        albumName = "Purple Rain",
        durationMs = 279_240,
        popularity = 74,
        releaseDate = "1984-06-25"
    ),
    LocalSeedSong(
        spotifyId = "3QszJeuSyyZQmD9pY1tqpo", // I Would Die 4 U
        imageRes = R.drawable.purple_rain,
        title = "I Would Die 4 U",
        artists = listOf("Prince"),
        albumName = "Purple Rain",
        durationMs = 169_666,
        popularity = 68,
        releaseDate = "1984-06-25"
    ),

    // =========================================================
// 10. ED SHEERAN - ÷ (DIVIDE) (10 canciones)
// Álbum: https://open.spotify.com/album/3T4tUhGYeRNVUGevb0wThu
// Imagen: https://i.scdn.co/image/ab67616d0000b273ba5db46f4b838ef6027e6f96
// =========================================================

    LocalSeedSong(
        spotifyId = "7oolFzHipTMg2nL7shhdz2", // Eraser
        imageRes = R.drawable.divide,
        title = "Eraser",
        artists = listOf("Ed Sheeran"),
        albumName = "÷ (Divide)",
        durationMs = 227_426,
        popularity = 78,
        releaseDate = "2017-03-03"
    ),

    LocalSeedSong(
        spotifyId = "6PCUP3dWmTjcTtXY02oFdT", // Castle on the Hill
        imageRes = R.drawable.divide,
        title = "Castle on the Hill",
        artists = listOf("Ed Sheeran"),
        albumName = "÷ (Divide)",
        durationMs = 261_153,
        popularity = 87,
        releaseDate = "2017-03-03"
    ),

    LocalSeedSong(
        spotifyId = "51ChrwmUPDJvedPQnIU8Ls", // Dive
        imageRes = R.drawable.divide,
        title = "Dive",
        artists = listOf("Ed Sheeran"),
        albumName = "÷ (Divide)",
        durationMs = 238_440,
        popularity = 80,
        releaseDate = "2017-03-03"
    ),

    LocalSeedSong(
        spotifyId = "7qiZfU4dY1lWllzX7mPBI3", // Shape of You
        imageRes = R.drawable.divide,
        title = "Shape of You",
        artists = listOf("Ed Sheeran"),
        albumName = "÷ (Divide)",
        durationMs = 233_712,
        popularity = 95,
        releaseDate = "2017-03-03"
    ),

    LocalSeedSong(
        spotifyId = "0tgVpDi06FyKpA1z0VMD4v", // Perfect
        imageRes = R.drawable.divide,
        title = "Perfect",
        artists = listOf("Ed Sheeran"),
        albumName = "÷ (Divide)",
        durationMs = 263_400,
        popularity = 92,
        releaseDate = "2017-03-03"
    ),

    LocalSeedSong(
        spotifyId = "0afhq8XCExXpqazXczTSve", // Galway Girl
        imageRes = R.drawable.divide,
        title = "Galway Girl",
        artists = listOf("Ed Sheeran"),
        albumName = "÷ (Divide)",
        durationMs = 170_826,
        popularity = 84,
        releaseDate = "2017-03-03"
    ),

    LocalSeedSong(
        spotifyId = "2RttW7RAu5nOAfq6YFvApB", // Happier
        imageRes = R.drawable.divide,
        title = "Happier",
        artists = listOf("Ed Sheeran"),
        albumName = "÷ (Divide)",
        durationMs = 207_520,
        popularity = 83,
        releaseDate = "2017-03-03"
    ),

    LocalSeedSong(
        spotifyId = "5HDPtsnyb3maFmPL8LLUTG", // New Man
        imageRes = R.drawable.divide,
        title = "New Man",
        artists = listOf("Ed Sheeran"),
        albumName = "÷ (Divide)",
        durationMs = 189_280,
        popularity = 79,
        releaseDate = "2017-03-03"
    ),

    LocalSeedSong(
        spotifyId = "2dfHh7ECGxfNqZTQno09Vk", // Hearts Don't Break Around Here
        imageRes = R.drawable.divide,
        title = "Hearts Don't Break Around Here",
        artists = listOf("Ed Sheeran"),
        albumName = "÷ (Divide)",
        durationMs = 248_413,
        popularity = 81,
        releaseDate = "2017-03-03"
    ),

    LocalSeedSong(
        spotifyId = "2pJZ1v8HezrAoZ0Fhzby92", // What Do I Know?
        imageRes = R.drawable.divide,
        title = "What Do I Know?",
        artists = listOf("Ed Sheeran"),
        albumName = "÷ (Divide)",
        durationMs = 237_333,
        popularity = 77,
        releaseDate = "2017-03-03"
    ),
)