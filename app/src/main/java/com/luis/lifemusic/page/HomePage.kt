package com.luis.lifemusic.page

import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.luis.lifemusic.R
import com.luis.lifemusic.ui.theme.LifeMusicTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomePage() {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Explora tu música", fontWeight = FontWeight.Bold) },
                actions = {
                    IconButton(onClick = { /* TODO: acción lista */ }) {
                        Icon(Icons.AutoMirrored.Filled.List, contentDescription = "Lista")
                    }
                    IconButton(onClick = { /* TODO: acción perfil */ }) {
                        Icon(Icons.Default.Person, contentDescription = "Perfil")
                    }
                }
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
        ) {
            // Sección 1: Recomendadas
            item {
                Text(
                    text = "Recomendadas para ti",
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 18.sp,
                    modifier = Modifier.padding(vertical = 8.dp)
                )
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    item {
                        SongCard(
                            imageRes = R.drawable.queen,
                            title = "Bohemian Rhapsody",
                            artist = "Queen",
                            duration = "5:55",
                            isFavorite = true
                        )
                    }
                    item {
                        SongCard(
                            imageRes = R.drawable.johnlennon,
                            title = "Imagine",
                            artist = "John Lennon",
                            duration = "3:07"
                        )
                    }
                }
            }

            // Sección 2: Nuevos lanzamientos
            item {
                Spacer(modifier = Modifier.height(24.dp))
                Text(
                    text = "Nuevos lanzamientos",
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 18.sp,
                    modifier = Modifier.padding(vertical = 8.dp)
                )
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    item {
                        SongCard(
                            imageRes = R.drawable.ledzeppelin,
                            title = "Stairway to Heaven",
                            artist = "Led Zeppelin"
                        )
                    }
                    item {
                        SongCard(
                            imageRes = R.drawable.gunsnroses,
                            title = "Sweet Child O' Mine",
                            artist = "Guns N' Roses"
                        )
                    }
                }
            }
        }
    }
}

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
        Column(
            modifier = Modifier.padding(8.dp)
        ) {
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
                    if (isFavorite) {
                        Icon(
                            imageVector = Icons.Filled.Favorite,
                            contentDescription = "Favorito",
                            tint = MaterialTheme.colorScheme.error,
                            modifier = Modifier.size(16.dp)
                        )
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
        HomePage()
    }
}

@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES, name = "HomePage - Dark Mode")
@Composable
fun HomePagePreviewDark() {
    LifeMusicTheme {
        HomePage()
    }
}
