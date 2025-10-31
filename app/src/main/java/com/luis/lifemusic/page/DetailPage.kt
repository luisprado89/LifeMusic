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
import com.luis.lifemusic.ui.theme.LifeMusicTheme

@Composable
fun DetailPage(
    imageRes: Int = R.drawable.queen,
    title: String = "Bohemian Rhapsody",
    artist: String = "Queen",
    album: String = "A Night at the Opera",
    duration: String = "5:55",
    isFavoriteInitial: Boolean = false,
    onBackClick: () -> Unit = {},
    onReturnToList: () -> Unit = {}
) {
    var isFavorite by remember { mutableStateOf(isFavoriteInitial) }

    MainScaffold(
        title = "Detalle de canción",
        onBackClick = onBackClick
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
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

            // Información principal
            Text(
                text = title,
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )
            Text(
                text = "$artist • $album",
                fontSize = 15.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Duración con fondo gris
            Surface(
                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.15f),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text(
                    text = "Duración: $duration",
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp),
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Spacer(modifier = Modifier.height(40.dp))

            // Botón "Me gusta"
            Button(
                onClick = { isFavorite = !isFavorite },
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
                    text = if (isFavorite) "❤️ Me gusta" else "🤍 Me gusta",
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 16.sp
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Botón "Volver al listado"
            OutlinedButton(
                onClick = onReturnToList,
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(55.dp)
            ) {
                Text(
                    text = "Volver al listado",
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 16.sp
                )
            }
        }
    }
}

@Preview(showBackground = true, name = "DetailPage - Light Mode")
@Composable
fun DetailPagePreviewLight() {
    LifeMusicTheme {
        DetailPage()
    }
}

@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES, name = "DetailPage - Dark Mode")
@Composable
fun DetailPagePreviewDark() {
    LifeMusicTheme {
        DetailPage()
    }
}
