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
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyHorizontalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.luis.lifemusic.component.MainScaffold
import com.luis.lifemusic.component.SongCard
import com.luis.lifemusic.data.localsed.LocalSeedSong
import com.luis.lifemusic.data.localsed.localSeedSongs
import com.luis.lifemusic.navigation.NavigationDestination
import com.luis.lifemusic.ui.theme.LifeMusicTheme

object HomeDestination : NavigationDestination {
    override val route = "home"
    override val title = "Explora tu música"
}

@Composable
fun HomePage(
    recommendedSongs: List<LocalSeedSong>,
    newReleaseSongs: List<LocalSeedSong>,
    popularSongs: List<LocalSeedSong>,
    isLoading: Boolean,
    errorMessage: String?,
    onRetry: () -> Unit,
    onFavoriteClick: (String) -> Unit, // spotifyId
    onNavigateToDetail: (String) -> Unit, // spotifyId
    onNavigateToList: () -> Unit = {},
    onNavigateToProfile: () -> Unit = {},
) {
    MainScaffold(
        title = HomeDestination.title,
        isHome = true,
        onListClick = onNavigateToList,
        onProfileClick = onNavigateToProfile
    ) { padding ->

        if (isLoading) {
            Box(modifier = Modifier.fillMaxSize().padding(padding), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
            return@MainScaffold
        }

        if (!errorMessage.isNullOrBlank()) {
            Column(
                modifier = Modifier.fillMaxSize().padding(padding).padding(16.dp),
                verticalArrangement = Arrangement.Center
            ) {
                Text(text = errorMessage, color = MaterialTheme.colorScheme.error)
                Spacer(modifier = Modifier.height(12.dp))
                Button(onClick = onRetry) { Text("Reintentar") }
            }
            return@MainScaffold
        }

        LazyColumn(modifier = Modifier.fillMaxSize().padding(padding).padding(horizontal = 16.dp)) {
            item { Spacer(modifier = Modifier.height(16.dp)) }

            item {
                SongSection(
                    title = "Recomendadas para ti",
                    songs = recommendedSongs,
                    onFavoriteClick = onFavoriteClick,
                    onNavigateToDetail = onNavigateToDetail
                )
            }

            item { Spacer(modifier = Modifier.height(24.dp)) }

            item {
                SongSection(
                    title = "Nuevos Lanzamientos",
                    songs = newReleaseSongs,
                    onFavoriteClick = onFavoriteClick,
                    onNavigateToDetail = onNavigateToDetail
                )
            }
            
            item { Spacer(modifier = Modifier.height(24.dp)) }

            item {
                SongSection(
                    title = "Más Populares",
                    songs = popularSongs,
                    onFavoriteClick = onFavoriteClick,
                    onNavigateToDetail = onNavigateToDetail
                )
            }
        }
    }
}

/**
 * Sección que muestra una parrilla de 2 filas con scroll horizontal unificado.
 */
@Composable
private fun SongSection(
    title: String,
    songs: List<LocalSeedSong>,
    onFavoriteClick: (String) -> Unit,
    onNavigateToDetail: (String) -> Unit
) {
    Column {
        Text(
            text = title,
            fontWeight = FontWeight.SemiBold,
            fontSize = 18.sp,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        LazyHorizontalGrid(
            rows = GridCells.Fixed(2),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            // La altura debe ser suficiente para 2 tarjetas + el espaciado vertical.
            // Una SongCard mide aprox 220dp de alto. (220 * 2) + 12 = 452
            modifier = Modifier.height(460.dp)
        ) {
            items(songs, key = { it.spotifyId }) { song ->
                SongCard(
                    song = song,
                    isFavorite = false,
                    onFavoriteClick = { onFavoriteClick(song.spotifyId) },
                    onCardClick = { onNavigateToDetail(song.spotifyId) }
                )
            }
        }
    }
}


@Preview(showBackground = true, name = "HomePage - Light Mode")
@Composable
fun HomePagePreviewLight() {
    LifeMusicTheme {
        HomePage(
            recommendedSongs = localSeedSongs.take(12),
            newReleaseSongs = localSeedSongs.takeLast(12),
            popularSongs = localSeedSongs.shuffled().take(12),
            isLoading = false,
            errorMessage = null,
            onRetry = {},
            onNavigateToList = {},
            onNavigateToProfile = {},
            onNavigateToDetail = {},
            onFavoriteClick = {}
        )
    }
}
