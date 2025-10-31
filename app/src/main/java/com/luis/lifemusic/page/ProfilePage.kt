package com.luis.lifemusic.page

import android.content.res.Configuration
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.luis.lifemusic.component.MainScaffold
import com.luis.lifemusic.ui.theme.LifeMusicTheme

@Composable
fun ProfilePage(
    initialName: String = "Luis",
    initialEmail: String = "luis@lifemusic.com",
    verified: Boolean = true,
    memberSince: String = "Enero 2023",
    onBackClick: () -> Unit = {},
    onLogoutClick: () -> Unit = {}
) {
    var name by remember { mutableStateOf(initialName) }
    var email by remember { mutableStateOf(initialEmail) }
    var isEditing by remember { mutableStateOf(false) }

    MainScaffold(
        title = "Mi Perfil",
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
            // Imagen de perfil circular
            Box(
                modifier = Modifier
                    .size(120.dp)
                    .clip(CircleShape)
                    .background(Color(0xFF53E88B)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = name.first().uppercase(),
                    fontWeight = FontWeight.Bold,
                    fontSize = 48.sp,
                    color = Color.Black.copy(alpha = 0.6f)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(text = name, fontWeight = FontWeight.Bold, fontSize = 20.sp)
            Text(
                text = email,
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(24.dp))

            if (!isEditing) {
                // 🔹 Modo visualización
                ProfileInfoItem(Icons.Default.Person, "Nombre completo", name)
                Spacer(modifier = Modifier.height(12.dp))
                ProfileInfoItem(Icons.Default.Email, "Correo electrónico", email)
                Spacer(modifier = Modifier.height(12.dp))
                ProfileInfoItem(
                    Icons.Default.MarkEmailRead,
                    "Correo verificado",
                    if (verified) "Sí" else "No"
                )
                Spacer(modifier = Modifier.height(12.dp))
                ProfileInfoItem(Icons.Default.CalendarMonth, "Miembro desde", memberSince)

                Spacer(modifier = Modifier.height(40.dp))

                Button(
                    onClick = { isEditing = true },
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(55.dp)
                ) {
                    Text("✏️ Editar perfil", fontWeight = FontWeight.SemiBold)
                }

                Spacer(modifier = Modifier.height(16.dp))

                OutlinedButton(
                    onClick = onLogoutClick,
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(55.dp),
                    border = BorderStroke(2.dp, MaterialTheme.colorScheme.primary)
                ) {
                    Text("Cerrar sesión", fontWeight = FontWeight.SemiBold)
                }

            } else {
                // 🔹 Modo edición
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Nombre completo") },
                    leadingIcon = { Icon(Icons.Default.Person, contentDescription = null) },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
                    label = { Text("Correo electrónico") },
                    leadingIcon = { Icon(Icons.Default.Email, contentDescription = null) },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(16.dp))

                ProfileInfoItem(
                    Icons.Default.MarkEmailRead,
                    "Correo verificado",
                    if (verified) "Sí" else "No"
                )
                Spacer(modifier = Modifier.height(12.dp))
                ProfileInfoItem(Icons.Default.CalendarMonth, "Miembro desde", memberSince)

                Spacer(modifier = Modifier.height(40.dp))

                Button(
                    onClick = { isEditing = false },
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(55.dp)
                ) {
                    Text("💾 Guardar cambios", fontWeight = FontWeight.SemiBold)
                }

                Spacer(modifier = Modifier.height(16.dp))

                OutlinedButton(
                    onClick = { isEditing = false },
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(55.dp)
                ) {
                    Text("Cancelar", fontWeight = FontWeight.SemiBold)
                }
            }
        }
    }
}

@Composable
fun ProfileInfoItem(icon: ImageVector, label: String, value: String) {
    Surface(
        color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.1f),
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(12.dp)
        ) {
            Icon(icon, contentDescription = label, tint = MaterialTheme.colorScheme.onSurfaceVariant)
            Spacer(modifier = Modifier.width(12.dp))
            Column {
                Text(label, fontSize = 13.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                Text(value, fontSize = 15.sp, fontWeight = FontWeight.Medium)
            }
        }
    }
}

@Preview(showBackground = true, name = "ProfilePage - Light Mode")
@Composable
fun ProfilePagePreviewLight() {
    LifeMusicTheme {
        ProfilePage()
    }
}

@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES, name = "ProfilePage - Dark Mode")
@Composable
fun ProfilePagePreviewDark() {
    LifeMusicTheme {
        ProfilePage()
    }
}
