package com.luis.lifemusic.page

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.luis.lifemusic.component.MainScaffold
import com.luis.lifemusic.component.SongListItem
import com.luis.lifemusic.data.localsed.LocalSeedSong
import com.luis.lifemusic.data.localsed.localSeedSongs
import com.luis.lifemusic.navigation.NavigationDestination
import com.luis.lifemusic.ui.theme.LifeMusicTheme

/**
 * Destino para la pantalla "Mis Favoritos".
 */
object ListDestination : NavigationDestination {
    override val route: String = "list"
    override val title: String = "Mis Favoritos"
}

/**
 * ListPage (UI pura), muestra la lista de favoritos del usuario.
 *
 * ✅ OFFLINE:
 * - Si faltan favoritos remotos (porque no hay conexión), mostramos un aviso
 *   con missingRemoteCount, sin ocultar los locales.
 */
@Composable
fun ListPage(
    favoriteSongs: List<LocalSeedSong>,
    isLoading: Boolean,
    errorMessage: String?,
    missingRemoteCount: Int, // ✅ NUEVO
    onFavoriteClick: (String) -> Unit, // spotifyId
    onNavigateToDetail: (String) -> Unit, // spotifyId
    onBackClick: () -> Unit = {}
) {
    MainScaffold(
        title = ListDestination.title,
        onBackClick = onBackClick
    ) { padding ->

        if (isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
            return@MainScaffold
        }

        if (!errorMessage.isNullOrBlank()) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(16.dp),
                verticalArrangement = Arrangement.Center
            ) {
                Text(text = errorMessage, color = MaterialTheme.colorScheme.error)
                Spacer(modifier = Modifier.height(12.dp))
                Button(onClick = { /* onRetry si lo implementas */ }) { Text("Reintentar") }
            }
            return@MainScaffold
        }

        if (favoriteSongs.isEmpty() && missingRemoteCount == 0) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentAlignment = Alignment.Center
            ) {
                Text("Aún no tienes canciones favoritas.", textAlign = TextAlign.Center)
            }
            return@MainScaffold
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
        ) {
            if (missingRemoteCount > 0) {
                Text(
                    text = "Tienes $missingRemoteCount favoritos añadidos desde internet, pero ahora no hay conexión para mostrarlos.",
                    color = MaterialTheme.colorScheme.tertiary,
                    modifier = Modifier.padding(bottom = 12.dp)
                )
            }

            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(favoriteSongs, key = { it.spotifyId }) { song ->
                    SongListItem(
                        song = song,
                        isFavorite = true,
                        onItemClick = { onNavigateToDetail(song.spotifyId) },
                        onFavoriteClick = { onFavoriteClick(song.spotifyId) }
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true, name = "ListPage - Light Mode")
@Composable
fun ListPagePreviewLight() {
    LifeMusicTheme {
        ListPage(
            favoriteSongs = localSeedSongs.take(5),
            isLoading = false,
            errorMessage = null,
            missingRemoteCount = 2,
            onFavoriteClick = {},
            onNavigateToDetail = {},
            onBackClick = {}
        )
    }
}

@Preview(showBackground = true, name = "ListPage - Empty")
@Composable
fun ListPagePreviewEmpty() {
    LifeMusicTheme {
        ListPage(
            favoriteSongs = emptyList(),
            isLoading = false,
            errorMessage = null,
            missingRemoteCount = 0,
            onFavoriteClick = {},
            onNavigateToDetail = {},
            onBackClick = {}
        )
    }
}