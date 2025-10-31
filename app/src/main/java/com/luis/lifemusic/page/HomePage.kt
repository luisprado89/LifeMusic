package com.luis.lifemusic.page

import android.content.res.Configuration
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.luis.lifemusic.R
import com.luis.lifemusic.component.MainScaffold
import com.luis.lifemusic.component.SongCard
import com.luis.lifemusic.ui.theme.LifeMusicTheme

@Composable
fun HomePage() {
    MainScaffold(
        title = "Explora tu música",
        isHome = true,
        onListClick = { /* TODO: acción lista */ },
        onProfileClick = { /* TODO: acción perfil */ }
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
                LazyRow(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
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
                            duration = "3:07",
                            isFavorite = false
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
                LazyRow(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
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
