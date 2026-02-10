package com.luis.lifemusic.page

import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.luis.lifemusic.R
import com.luis.lifemusic.component.MainScaffold
import com.luis.lifemusic.navigation.NavigationDestination
import com.luis.lifemusic.ui.theme.LifeMusicTheme

/**
 * Destination de la pantalla de detalle.
 *
 * - route: nombre lógico usado en el NavHost
 * - title: título mostrado en el TopAppBar
 * - songIdArg: argumento necesario para identificar qué canción mostrar
 *
 * IMPORTANTE:
 * No navegamos usando el título, sino un id estable (songId).
 * Esto es fundamental para:
 *  - Navegación segura
 *  - Futuro uso con ViewModel + Room
 */
object DetailDestination : NavigationDestination {
    override val route = "detail"
    override val title = "Detalle de canción"

    const val songIdArg = "songId"
    val routeWithArgs = "$route/{$songIdArg}"
}

@Composable
fun DetailPage(
    songId: Int,
    imageRes: Int,
    title: String,
    artist: String,
    album: String,
    duration: String,
    isFavoriteInitial: Boolean = false,
    onBackClick: () -> Unit = {}
) {
    /*
     * 🔴 ESTADO LOCAL TEMPORAL (IMPORTANTE)
     *
     * Este estado se usa SOLO para que la UI sea interactiva mientras:
     *  - No existe todavía un ViewModel
     *  - No existe todavía Room
     *
     * Cuando se implemente MVVM:
     *  - `isFavorite` vendrá del UiState del ViewModel
     *  - Este remember desaparecerá
     *  - El botón llamará a un evento del ViewModel
     */
    var isFavorite by remember { mutableStateOf(isFavoriteInitial) }

    MainScaffold(
        title = DetailDestination.title,
        onBackClick = onBackClick
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            // Imagen del álbum
            Image(
                painter = painterResource(id = imageRes),
                contentDescription = title,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(260.dp)
                    .padding(bottom = 24.dp)
            )

            // Título de la canción
            Text(
                text = title,
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )

            // Artista y álbum
            Text(
                text = "$artist • $album",
                fontSize = 15.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Duración
            Surface(
                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.15f),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text(
                    text = "Duración: $duration",
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp),
                    fontSize = 14.sp
                )
            }

            Spacer(modifier = Modifier.height(40.dp))

            // Botón favoritos (estado LOCAL por ahora)
            Button(
                onClick = {
                    // 🔹 Cambia solo el estado visual por ahora
                    isFavorite = !isFavorite
                },
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

/**
 * Previews SOLO para diseño.
 * El songId aquí no se usa realmente.
 */
@Preview(showBackground = true, name = "DetailPage - Light Mode")
@Composable
fun DetailPagePreviewLight() {
    LifeMusicTheme {
        DetailPage(
            songId = 1,
            imageRes = R.drawable.queen,
            title = "Bohemian Rhapsody",
            artist = "Queen",
            album = "A Night at the Opera",
            duration = "5:55"
        )
    }
}

@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES, name = "DetailPage - Dark Mode")
@Composable
fun DetailPagePreviewDark() {
    LifeMusicTheme {
        DetailPage(
            songId = 1,
            imageRes = R.drawable.queen,
            title = "Bohemian Rhapsody",
            artist = "Queen",
            album = "A Night at the Opera",
            duration = "5:55"
        )
    }
}
