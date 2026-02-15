package com.luis.lifemusic.page

import android.content.res.Configuration
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
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
import com.luis.lifemusic.data.Song
import com.luis.lifemusic.data.sampleSongs
import com.luis.lifemusic.navigation.NavigationDestination
import com.luis.lifemusic.ui.theme.LifeMusicTheme

object HomeDestination : NavigationDestination {
    override val route = "home"
    override val title = "Explora tu música"
}

/**
 * HomePage (UI pura).
 *
 * ✅ Esta pantalla:
 * - No carga datos por su cuenta.
 * - Recibe estado y callbacks desde HomeRoute/HomeViewModel.
 * - Solo renderiza UI y emite eventos de navegación.
 */
@Composable
fun HomePage(
    recommendedSongs: List<Song>,
    newReleaseSongs: List<Song>,
    isLoading: Boolean,
    errorMessage: String?,
    onRetry: () -> Unit,
    onNavigateToList: () -> Unit = {},
    onNavigateToProfile: () -> Unit = {},
    onNavigateToDetail: (Int) -> Unit = {}
) {
    MainScaffold(
        title = HomeDestination.title,
        isHome = true,
        onListClick = onNavigateToList,
        onProfileClick = onNavigateToProfile
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
                .padding(16.dp)
        ) {

            item {
                Text(
                    text = "Recomendadas para ti",
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 18.sp,
                    modifier = Modifier.padding(vertical = 8.dp)
                )

                LazyRow(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    items(recommendedSongs) { song ->
                        Box(
                            modifier = Modifier.clickable {
                                onNavigateToDetail(song.id)
                            }
                        ) {
                            SongCard(
                                imageRes = song.imageRes,
                                title = song.title,
                                artist = song.artist,
                                duration = song.duration,
                                isFavorite = song.isFavorite
                            )
                        }
                    }
                }
            }

            item {
                Spacer(modifier = Modifier.height(24.dp))
                Text(
                    text = "Nuevos lanzamientos",
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 18.sp,
                    modifier = Modifier.padding(vertical = 8.dp)
                )

                LazyRow(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    items(newReleaseSongs) { song ->
                        Box(
                            modifier = Modifier.clickable {
                                onNavigateToDetail(song.id)
                            }
                        ) {
                            SongCard(
                                imageRes = song.imageRes,
                                title = song.title,
                                artist = song.artist,
                                duration = song.duration,
                                isFavorite = song.isFavorite
                            )
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true, name = "HomePage - Light Mode")
@Composable
fun HomePagePreviewLight() {
    LifeMusicTheme {
        HomePage(
            recommendedSongs = sampleSongs.take(3),
            newReleaseSongs = sampleSongs.takeLast(3),
            isLoading = false,
            errorMessage = null,
            onRetry = {},
            onNavigateToList = {},
            onNavigateToProfile = {},
            onNavigateToDetail = {}
        )
    }
}

@Preview(
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    name = "HomePage - Dark Mode"
)
@Composable
fun HomePagePreviewDark() {
    LifeMusicTheme {
        HomePage(
            recommendedSongs = sampleSongs.take(3),
            newReleaseSongs = sampleSongs.takeLast(3),
            isLoading = false,
            errorMessage = null,
            onRetry = {},
            onNavigateToList = {},
            onNavigateToProfile = {},
            onNavigateToDetail = {}
        )
    }
}

