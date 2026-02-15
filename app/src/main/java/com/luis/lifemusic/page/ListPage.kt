package com.luis.lifemusic.page

import android.content.res.Configuration
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.luis.lifemusic.component.MainScaffold
import com.luis.lifemusic.component.SongListItem
import com.luis.lifemusic.data.Song
import com.luis.lifemusic.data.sampleSongs
import com.luis.lifemusic.navigation.NavigationDestination
import com.luis.lifemusic.ui.theme.LifeMusicTheme

/**
 * Destination de la lista completa.
 */
object ListDestination : NavigationDestination {
    override val route: String = "list"
    override val title: String = "Todas las canciones"
}

/**
 * ListPage (UI pura).
 *
 * ✅ Principio MVVM:
 * - Esta pantalla NO carga datos por sí sola.
 * - Recibe estado (songs / loading / error) desde ListRoute/ListViewModel.
 * - Solo renderiza UI y emite eventos de navegación.
 */
@Composable
fun ListPage(
    songs: List<Song>,
    isLoading: Boolean,
    errorMessage: String?,
    onRetry: () -> Unit,
    onNavigateToDetail: (Int) -> Unit = {},
    onBackClick: () -> Unit = {}
) {
    MainScaffold(
        title = ListDestination.title,
        onBackClick = onBackClick
    ) { padding ->

        // 🔄 Estado de carga
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

        // ❌ Estado de error
        if (!errorMessage.isNullOrBlank()) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(16.dp),
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = errorMessage,
                    color = MaterialTheme.colorScheme.error
                )
                Spacer(modifier = Modifier.height(12.dp))
                Button(onClick = onRetry) {
                    Text("Reintentar")
                }
            }
            return@MainScaffold
        }

        // ✅ Estado normal
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(songs) { song ->
                /**
                 * Click en toda la tarjeta → navegar al detalle por ID.
                 * Nunca se navega pasando el objeto completo.
                 */
                Surface(
                    modifier = Modifier.clickable {
                        onNavigateToDetail(song.id)
                    }
                ) {
                    SongListItem(
                        imageRes = song.imageRes,
                        title = song.title,
                        artist = song.artist,
                        album = song.album,
                        duration = song.duration,
                        isFavorite = song.isFavorite
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
            songs = sampleSongs,
            isLoading = false,
            errorMessage = null,
            onRetry = {}
        )
    }
}

@Preview(
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    name = "ListPage - Dark Mode"
)
@Composable
fun ListPagePreviewDark() {
    LifeMusicTheme {
        ListPage(
            songs = sampleSongs,
            isLoading = false,
            errorMessage = null,
            onRetry = {}
        )
    }
}
