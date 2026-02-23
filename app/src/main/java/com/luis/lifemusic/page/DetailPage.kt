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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.ui.text.style.TextOverflow
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
                Button(onClick = onRetry) { Text("Reintentar") }
            }
            return@MainScaffold
        }

        val currentSong = song ?: return@MainScaffold
        val scrollState = rememberScrollState()
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(scrollState)
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            val imageModifier = Modifier
                .size(260.dp)
                .padding(bottom = 24.dp)
                .clip(RoundedCornerShape(16.dp))

            if (!currentSong.imageUrl.isNullOrBlank()) {
                AsyncImage(
                    model = currentSong.imageUrl,
                    contentDescription = currentSong.title,
                    contentScale = ContentScale.Crop,
                    modifier = imageModifier
                )
            } else if (currentSong.imageRes != 0) {
                Image(
                    painter = painterResource(id = currentSong.imageRes),
                    contentDescription = currentSong.title,
                    contentScale = ContentScale.Crop,
                    modifier = imageModifier
                )
            } else {
                Box(modifier = imageModifier.background(MaterialTheme.colorScheme.surfaceVariant))
            }

            Text(
                text = currentSong.title,
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )
            Text(
                text = "${currentSong.artists.joinToString()} • ${currentSong.albumName}",
                fontSize = 15.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(16.dp))

            // ✅ Chips responsivos: mismo ancho + sin partir textos
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {

                currentSong.releaseDate?.let {
                    InfoChip(
                        modifier = Modifier
                            .fillMaxWidth(0.8f),
                        label = "📅 Lanzamiento",
                        value = formatEuropeanDate(it)
                    )
                }

                InfoChip(
                    modifier = Modifier
                        .fillMaxWidth(0.8f),
                    label = "📈 Popularidad",
                    value = "${currentSong.popularity}%"
                )

                InfoChip(
                    modifier = Modifier
                        .fillMaxWidth(0.8f),
                    label = "⏱ Duración",
                    value = formatDuration(currentSong.durationMs)
                )
            }

            Spacer(modifier = Modifier.height(40.dp))

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

@Composable
private fun InfoChip(
    modifier: Modifier = Modifier,
    label: String,
    value: String
) {
    Surface(
        modifier = modifier,
        color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.1f),
        shape = RoundedCornerShape(8.dp)
    ) {
        Column(
            // ✅ Un poco menos padding para ganar espacio en móviles pequeños
            modifier = Modifier
                .fillMaxWidth() // Importante: ocupa todo el ancho que el padre le asigna
                .padding(horizontal = 12.dp, vertical = 8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = label,
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                maxLines = 1,
                softWrap = false,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = value,
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                maxLines = 1,
                softWrap = false,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

private fun formatDuration(millis: Int): String {
    val minutes = TimeUnit.MILLISECONDS.toMinutes(millis.toLong())
    val seconds = TimeUnit.MILLISECONDS.toSeconds(millis.toLong()) % 60
    return String.format(Locale.getDefault(), "%d:%02d", minutes, seconds)
}

private fun formatEuropeanDate(date: String?): String {
    if (date.isNullOrBlank()) return ""

    return try {
        val parts = date.split("-")

        when (parts.size) {
            3 -> { // yyyy-MM-dd
                val year = parts[0]
                val month = parts[1]
                val day = parts[2]
                "$day-$month-$year"
            }
            2 -> { // yyyy-MM
                val year = parts[0]
                val month = parts[1]
                "$month-$year"
            }
            1 -> { // yyyy
                parts[0]
            }
            else -> date
        }
    } catch (e: Exception) {
        date
    }
}

private val previewSong = LocalSeedSong(
    spotifyId = "preview_id",
    title = "Bohemian Rhapsody",
    artists = listOf("Queen"),
    artistIds = listOf("1dfeR4HaWDbWqFHLkxsg1d"),
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
        DetailPage(
            song = previewSong,
            isFavorite = false,
            isLoading = false,
            errorMessage = null,
            onRetry = {},
            onFavoriteClick = {},
            onBackClick = {}
        )
    }
}