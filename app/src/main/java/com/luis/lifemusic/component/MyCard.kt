package com.luis.lifemusic.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.luis.lifemusic.R

data class MyCardData(
    val imageId: Int,
    val title: String,
    val subtitle: String
)

@Composable
fun MyCard(cardData: MyCardData) {
    Card(
        modifier = Modifier
            .width(180.dp)
            .height(230.dp)
            .padding(8.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        ),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(id = cardData.imageId),
                contentDescription = cardData.title,
                modifier = Modifier
                    .height(140.dp)
                    .fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = cardData.title,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = cardData.subtitle,
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}
