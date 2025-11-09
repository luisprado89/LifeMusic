package com.luis.lifemusic.page

import android.content.res.Configuration
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.luis.lifemusic.component.MainScaffold
import com.luis.lifemusic.component.SongListItem
import com.luis.lifemusic.data.sampleSongs
import com.luis.lifemusic.ui.theme.LifeMusicTheme

@Composable
fun ListPage() {
    MainScaffold(
        title = "Todas las canciones"
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(sampleSongs) { song ->
                SongListItem(
                    imageRes = song.imageRes,
                    title = song.title,
                    artist = song.artist,
                    album = song.album,
                    duration = song.duration,
                    isFavorite = song.isFavorite
                )
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
