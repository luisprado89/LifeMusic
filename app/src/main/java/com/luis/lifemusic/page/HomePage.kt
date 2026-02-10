package com.luis.lifemusic.page

import android.content.res.Configuration
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.luis.lifemusic.component.MainScaffold
import com.luis.lifemusic.component.SongCard
import com.luis.lifemusic.data.sampleSongs
import com.luis.lifemusic.navigation.NavigationDestination
import com.luis.lifemusic.ui.theme.LifeMusicTheme

/**
 * Destination de la Home.
 * Define:
 * - route: usada por Navigation Compose
 * - title: título mostrado en el TopAppBar
 */
object HomeDestination : NavigationDestination {
    override val route = "home"
    override val title = "Explora tu música"
}

@Composable
fun HomePage(
    onNavigateToList: () -> Unit = {},
    onNavigateToProfile: () -> Unit = {},

    /**
     * Callback de navegación al detalle.
     * Recibe el ID de la canción seleccionada.
     *
     * HomePage NO navega directamente.
     * Solo notifica que se quiere navegar.
     */
    onNavigateToDetail: (Int) -> Unit = {}
) {
    MainScaffold(
        title = HomeDestination.title,
        isHome = true,
        onListClick = onNavigateToList,
        onProfileClick = onNavigateToProfile
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
        ) {

            // =======================
            // Sección: Recomendadas
            // =======================
            item {
                Text(
                    text = "Recomendadas para ti",
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 18.sp,
                    modifier = Modifier.padding(vertical = 8.dp)
                )

                LazyRow(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    items(sampleSongs.take(3)) { song ->

                        /**
                         * ⚠️ IMPORTANTE
                         *
                         * SongCard es un composable SOLO visual,
                         * por lo que NO tiene onClick.
                         *
                         * Por eso se envuelve dentro de un Box clickable.
                         */
                        Box(
                            modifier = Modifier.clickable {

                                /**
                                 * Al hacer click:
                                 * - Se llama al callback onNavigateToDetail
                                 * - Se pasa el ID de la canción
                                 *
                                 * NO se pasa el título ni el objeto entero,
                                 * solo el ID, que es:
                                 * - estable
                                 * - único
                                 * - ideal para navegación
                                 */
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

            // =======================
            // Sección: Nuevos lanzamientos
            // =======================
            item {
                Spacer(modifier = Modifier.height(24.dp))
                Text(
                    text = "Nuevos lanzamientos",
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 18.sp,
                    modifier = Modifier.padding(vertical = 8.dp)
                )

                LazyRow(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    items(sampleSongs.takeLast(3)) { song ->
                        Box(
                            modifier = Modifier.clickable {
                                // Mismo comportamiento que arriba:
                                // click -> navegar al detalle con song.id
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
    LifeMusicTheme { HomePage() }
}

@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES, name = "HomePage - Dark Mode")
@Composable
fun HomePagePreviewDark() {
    LifeMusicTheme { HomePage() }
}
