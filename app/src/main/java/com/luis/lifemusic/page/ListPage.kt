package com.luis.lifemusic.page

import android.content.res.Configuration
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.luis.lifemusic.component.MainScaffold
import com.luis.lifemusic.component.SongListItem
import com.luis.lifemusic.data.sampleSongs
import com.luis.lifemusic.navigation.NavigationDestination
import com.luis.lifemusic.ui.theme.LifeMusicTheme

// ✅ Destination (igual que en el otro proyecto)
object ListDestination : NavigationDestination {
    override val route: String = "list"
    override val title: String = "Todas las canciones"
}

@Composable
fun ListPage(
    onNavigateToDetail: (Int) -> Unit = {},
    onBackClick: () -> Unit = {}
) {
    MainScaffold(
        title = ListDestination.title,
        onBackClick = onBackClick
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(sampleSongs) { song ->
                // ✅ Click en toda la tarjeta -> navega por ID
                Surface(
                    modifier = Modifier.clickable { onNavigateToDetail(song.id) }
                ) {
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
