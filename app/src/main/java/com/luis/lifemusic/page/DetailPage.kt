package com.luis.lifemusic.page

import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.luis.lifemusic.R
import com.luis.lifemusic.data.Song
import com.luis.lifemusic.navigation.NavigationDestination
import com.luis.lifemusic.ui.theme.LifeMusicTheme
import com.luis.lifemusic.component.MainScaffold

/**
 * Destination de la pantalla de detalle.
 *
 * - route: nombre lógico usado en el NavHost
 * - title: título mostrado en el TopAppBar
 * - songIdArg: argumento necesario para identificar qué canción mostrar
 *
 * IMPORTANTE:
 * No navegamos usando el título, sino un id estable (songId).
 */
object DetailDestination : NavigationDestination {
    override val route = "detail"
    override val title = "Detalle de canción"

    const val songIdArg = "songId"
    val routeWithArgs = "$route/{$songIdArg}"
}

/**
 * DetailPage (UI pura).
 *
 * ✅ Regla MVVM:
 * - La pantalla NO mantiene estado local de favorito (no remember).
 * - Recibe todo desde DetailRoute/DetailViewModel:
 *   canción, favorito, loading/error y callbacks.
 */
@Composable
fun DetailPage(
    song: Song?,
    isFavorite: Boolean,
    isLoading: Boolean,
    errorMessage: String?,
    onRetry: () -> Unit,
    onFavoriteClick: () -> Unit,
    onBackClick: () -> Unit = {}
) {
    MainScaffold(
        title = DetailDestination.title,
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

        // ✅ Estado normal (si no hay loading/error, esperamos tener canción).
        val currentSong = song ?: return@MainScaffold

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Imagen del álbum
            Image(
                painter = painterResource(id = currentSong.imageRes),
                contentDescription = currentSong.title,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(260.dp)
                    .padding(bottom = 24.dp)
            )

            // Título de la canción
            Text(
                text = currentSong.title,
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )

            // Artista y álbum
            Text(
                text = "${currentSong.artist} • ${currentSong.album}",
                fontSize = 15.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Duración
            Surface(
                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.15f),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text(
                    text = "Duración: ${currentSong.duration}",
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp),
                    fontSize = 14.sp
                )
            }

            Spacer(modifier = Modifier.height(40.dp))

            // Botón favoritos (estado REAL desde ViewModel)
            Button(
                onClick = onFavoriteClick,
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (isFavorite) Color.Red else MaterialTheme.colorScheme.surfaceVariant,
                    contentColor = if (isFavorite) Color.White else MaterialTheme.colorScheme.onSurface
                ),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(55.dp)
            ) {
                Text(
                    text = if (isFavorite) "❤️ En favoritos" else "🤍 Añadir a favoritos",
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 16.sp
                )
            }
        }
    }
}

/**
 * Previews SOLO para diseño.
 */
@Preview(showBackground = true, name = "DetailPage - Light Mode")
@Composable
fun DetailPagePreviewLight() {
    LifeMusicTheme {
        DetailPage(
            song = Song(
                id = 1,
                imageRes = R.drawable.queen,
                title = "Bohemian Rhapsody",
                artist = "Queen",
                album = "A Night at the Opera",
                duration = "5:55"
            ),
            isFavorite = false,
            isLoading = false,
            errorMessage = null,
            onRetry = {},
            onFavoriteClick = {}
        )
    }
}

@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES, name = "DetailPage - Dark Mode")
@Composable
fun DetailPagePreviewDark() {
    LifeMusicTheme {
        DetailPage(
            song = Song(
                id = 1,
                imageRes = R.drawable.queen,
                title = "Bohemian Rhapsody",
                artist = "Queen",
                album = "A Night at the Opera",
                duration = "5:55"
            ),
            isFavorite = true,
            isLoading = false,
            errorMessage = null,
            onRetry = {},
            onFavoriteClick = {}
        )
    }
}
