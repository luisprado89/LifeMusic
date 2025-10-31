package com.luis.lifemusic.component

import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.luis.lifemusic.R
import com.luis.lifemusic.ui.theme.LifeMusicTheme

@Composable
fun SongCard(
    imageRes: Int,
    title: String,
    artist: String,
    duration: String = "",
    isFavorite: Boolean = false
) {
    Card(
        modifier = Modifier
            .width(180.dp)
            .wrapContentHeight(),
        shape = MaterialTheme.shapes.medium,
        elevation = CardDefaults.cardElevation(3.dp)
    ) {
        Column(modifier = Modifier.padding(8.dp)) {
            Image(
                painter = painterResource(id = imageRes),
                contentDescription = title,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp)
            )
            Spacer(modifier = Modifier.height(6.dp))
            Text(title, fontWeight = FontWeight.Bold)
            Text(artist, fontSize = 13.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
            if (duration.isNotEmpty()) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(duration, fontSize = 12.sp)
                    Icon(
                        imageVector = if (isFavorite) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
                        contentDescription = if (isFavorite) "Favorito" else "No favorito",
                        tint = if (isFavorite)
                            Color.Red
                        else
                            MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f),
                        modifier = Modifier.size(18.dp)
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true, name = "SongCard - Light Mode")
@Composable
fun SongCardPreviewLight() {
    LifeMusicTheme {
        SongCard(
            imageRes = R.drawable.queen,
            title = "Bohemian Rhapsody",
            artist = "Queen",
            duration = "5:55",
            isFavorite = true
        )
    }
}

@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES, name = "SongCard - Dark Mode")
@Composable
fun SongCardPreviewDark() {
    LifeMusicTheme {
        SongCard(
            imageRes = R.drawable.johnlennon,
            title = "Imagine",
            artist = "John Lennon",
            duration = "3:07",
            isFavorite = false
        )
    }
}
