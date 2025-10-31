package com.luis.lifemusic.page

import android.content.res.Configuration
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.luis.lifemusic.R
import com.luis.lifemusic.component.MainScaffold
import com.luis.lifemusic.component.SongListItem
import com.luis.lifemusic.ui.theme.LifeMusicTheme

@Composable
fun ListPage() {
    MainScaffold(
        title = "Todas las canciones",
        isHome = false,
        onListClick = { /* TODO */ },
        onProfileClick = { /* TODO */ }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(5) { index ->
                when (index) {
                    0 -> SongListItem(
                        imageRes = R.drawable.queen,
                        title = "Bohemian Rhapsody",
                        artist = "Queen",
                        album = "A Night at the Opera",
                        duration = "5:55",
                        isFavorite = true
                    )
                    1 -> SongListItem(
                        imageRes = R.drawable.johnlennon,
                        title = "Imagine",
                        artist = "John Lennon",
                        album = "Imagine",
                        duration = "3:07"
                    )
                    2 -> SongListItem(
                        imageRes = R.drawable.hotelcalifornia,
                        title = "Hotel California",
                        artist = "Eagles",
                        album = "Hotel California",
                        duration = "6:30",
                        isFavorite = true
                    )
                    3 -> SongListItem(
                        imageRes = R.drawable.ledzeppelin,
                        title = "Stairway to Heaven",
                        artist = "Led Zeppelin",
                        album = "Led Zeppelin IV",
                        duration = "8:02"
                    )
                    4 -> SongListItem(
                        imageRes = R.drawable.gunsnroses,
                        title = "Sweet Child O' Mine",
                        artist = "Guns N' Roses",
                        album = "Appetite for Destruction",
                        duration = "5:56",
                        isFavorite = true
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true, name = "ListPage - Light Mode")
@Composable
fun ListPagePreviewLight() {
    LifeMusicTheme {
        ListPage()
    }
}

@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES, name = "ListPage - Dark Mode")
@Composable
fun ListPagePreviewDark() {
    LifeMusicTheme {
        ListPage()
    }
}
