package com.luis.lifemusic.page

import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.luis.lifemusic.R
import com.luis.lifemusic.component.MainScaffold
import com.luis.lifemusic.data.localsed.LocalSeedSong
import com.luis.lifemusic.navigation.NavigationDestination
import com.luis.lifemusic.ui.theme.LifeMusicTheme
import java.util.Locale
import java.util.concurrent.TimeUnit

object DetailDestination : NavigationDestination {
    override val route = "detail"
    override val title = "Detalle de canción"
    const val spotifyIdArg = "spotifyId"
    val routeWithArgs = "$route/{$spotifyIdArg}"
}

@Composable
fun DetailPage(
    song: LocalSeedSong?,
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

        val currentSong = song ?: return@MainScaffold

        Column(
            modifier = Modifier.fillMaxSize().padding(padding).padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            val imageModifier = Modifier.size(260.dp).padding(bottom = 24.dp).clip(RoundedCornerShape(16.dp))
            if (!currentSong.imageUrl.isNullOrBlank()) {
                AsyncImage(model = currentSong.imageUrl, contentDescription = currentSong.title, contentScale = ContentScale.Crop, modifier = imageModifier)
            } else if (currentSong.imageRes != 0) {
                Image(painter = painterResource(id = currentSong.imageRes), contentDescription = currentSong.title, contentScale = ContentScale.Crop, modifier = imageModifier)
            } else {
                Box(modifier = imageModifier.background(MaterialTheme.colorScheme.surfaceVariant))
            }

            Text(text = currentSong.title, fontSize = 22.sp, fontWeight = FontWeight.Bold, textAlign = TextAlign.Center)
            Text(text = "${currentSong.artists.joinToString()} • ${currentSong.albumName}", fontSize = 15.sp, color = MaterialTheme.colorScheme.onSurfaceVariant, textAlign = TextAlign.Center)

            Spacer(modifier = Modifier.height(16.dp))

            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                InfoChip("Popularidad", "${currentSong.popularity}%")
                InfoChip("Duración", formatDuration(currentSong.durationMs))
                currentSong.releaseDate?.let { InfoChip("Lanzamiento", it) }
            }

            Spacer(modifier = Modifier.height(40.dp))

            Button(
                onClick = onFavoriteClick,
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (isFavorite) Color.Red else MaterialTheme.colorScheme.surfaceVariant,
                    contentColor = if (isFavorite) Color.White else MaterialTheme.colorScheme.onSurface
                ),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.fillMaxWidth().height(55.dp)
            ) {
                Text(text = if (isFavorite) "❤️ En favoritos" else "🤍 Añadir a favoritos", fontWeight = FontWeight.SemiBold, fontSize = 16.sp)
            }
        }
    }
}

@Composable
private fun InfoChip(label: String, value: String) {
    Surface(
        color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.1f),
        shape = RoundedCornerShape(8.dp)
    ) {
        Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp), horizontalAlignment = Alignment.CenterHorizontally) {
            Text(text = label, fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
            Text(text = value, fontSize = 14.sp, fontWeight = FontWeight.Medium)
        }
    }
}

private fun formatDuration(millis: Int): String {
    val minutes = TimeUnit.MILLISECONDS.toMinutes(millis.toLong())
    val seconds = TimeUnit.MILLISECONDS.toSeconds(millis.toLong()) % 60
    return String.format(Locale.getDefault(), "%d:%02d", minutes, seconds)
}

private val previewSong = LocalSeedSong(
    spotifyId = "preview_id",
    title = "Bohemian Rhapsody",
    artists = listOf("Queen"),
    artistIds = listOf("1dfeR4HaWDbWqFHLkxsg1d"), // Corregido
    albumName = "A Night at the Opera",
    durationMs = 355000,
    popularity = 88,
    releaseDate = "1975-11-21",
    imageRes = R.drawable.queen,
    imageUrl = null
)

@Preview(showBackground = true, name = "DetailPage - Light Mode")
@Composable
fun DetailPagePreviewLight() {
    LifeMusicTheme {
        DetailPage(song = previewSong, isFavorite = false, isLoading = false, errorMessage = null, onRetry = {}, onFavoriteClick = {}, onBackClick = {})
    }
}
