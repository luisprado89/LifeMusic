package com.luis.lifemusic.component


import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.font.FontWeight

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
        shape = MaterialTheme.shapes.medium
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

                    // Icono favorito
                    Icon(
                        imageVector = if (isFavorite) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
                        contentDescription = if (isFavorite) "Favorito" else "No favorito",
                        tint = if (isFavorite)
                            Color.Red//rojo puro e igual en modo claro/oscuro.
                        else  //color gris suave, se adapta automáticamente al tema (oscuro o claro).
                            MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f),
                        modifier = Modifier.size(18.dp)
                    )

                }
            }
        }
    }
}
