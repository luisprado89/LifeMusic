package com.luis.lifemusic.component

import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.luis.lifemusic.R
import com.luis.lifemusic.data.localsed.LocalSeedSong
import com.luis.lifemusic.ui.theme.LifeMusicTheme
import java.util.Locale
import java.util.concurrent.TimeUnit

@Composable
fun SongCard(
    song: LocalSeedSong,
    isFavorite: Boolean,
    onCardClick: () -> Unit,
    onFavoriteClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .width(180.dp)
            .wrapContentHeight()
            .clickable(onClick = onCardClick),
        shape = MaterialTheme.shapes.medium,
        elevation = CardDefaults.cardElevation(3.dp)
    ) {
        Column {
            val imageModifier = Modifier
                .fillMaxWidth()
                .height(120.dp)
                .clip(MaterialTheme.shapes.medium)

            if (!song.imageUrl.isNullOrBlank()) {
                AsyncImage(
                    model = song.imageUrl,
                    contentDescription = song.title,
                    contentScale = ContentScale.Crop,
                    modifier = imageModifier
                )
            } else if (song.imageRes != 0) {
                Image(
                    painter = painterResource(id = song.imageRes),
                    contentDescription = song.title,
                    contentScale = ContentScale.Crop,
                    modifier = imageModifier
                )
            } else {
                Box(
                    modifier = imageModifier
                        .background(MaterialTheme.colorScheme.surfaceVariant)
                )
            }

            Column(modifier = Modifier.padding(8.dp)) {
                Text(
                    text = song.title,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = song.artists.joinToString(),
                    fontSize = 13.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(4.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = formatDuration(song.durationMs),
                        fontSize = 12.sp
                    )
                    IconButton(onClick = onFavoriteClick) {
                        Icon(
                            imageVector = if (isFavorite) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
                            contentDescription = if (isFavorite) "Quitar de favoritos" else "Añadir a favoritos",
                            tint = if (isFavorite) Color.Red
                            else MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f),
                            modifier = Modifier.size(22.dp)
                        )
                    }
                }
            }
        }
    }
}

private fun formatDuration(millis: Int): String {
    val minutes = TimeUnit.MILLISECONDS.toMinutes(millis.toLong())
    val seconds = TimeUnit.MILLISECONDS.toSeconds(millis.toLong()) % 60
    return String.format(Locale.getDefault(), "%d:%02d", minutes, seconds)
}

private val previewSongApi = LocalSeedSong(
    spotifyId = "api_id",
    title = "Stairway to Heaven",
    artists = listOf("Led Zeppelin"),
    artistIds = listOf("36QJpDe2go2KgaRleHCDls"), // Campo añadido
    albumName = "Led Zeppelin IV",
    durationMs = 482000,
    popularity = 89,
    releaseDate = "1971-11-08",
    imageRes = 0,
    imageUrl = "https://i.scdn.co/image/ab67616d0000b273b5a53b73c4f74955b266e858"
)

private val previewSongLocal = LocalSeedSong(
    spotifyId = "local_id",
    title = "Bohemian Rhapsody",
    artists = listOf("Queen"),
    artistIds = listOf("1dfeR4HaWDbWqFHLkxsg1d"), // Campo añadido
    albumName = "A Night at the Opera",
    durationMs = 355000,
    popularity = 88,
    releaseDate = "1975-11-21",
    imageRes = R.drawable.queen,
    imageUrl = null
)

@Preview(showBackground = true, name = "SongCard - Local Image")
@Composable
fun SongCardPreviewLocal() {
    LifeMusicTheme {
        SongCard(
            song = previewSongLocal,
            isFavorite = true,
            onCardClick = {},
            onFavoriteClick = {}
        )
    }
}

@Preview(
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    name = "SongCard - API Image"
)
@Composable
fun SongCardPreviewApi() {
    LifeMusicTheme {
        SongCard(
            song = previewSongApi,
            isFavorite = false,
            onCardClick = {},
            onFavoriteClick = {}
        )
    }
}
