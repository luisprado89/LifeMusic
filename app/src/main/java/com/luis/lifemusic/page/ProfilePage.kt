package com.luis.lifemusic.page

import android.content.res.Configuration
import coil.compose.AsyncImage
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.MarkEmailRead
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
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
import com.luis.lifemusic.navigation.NavigationDestination
import com.luis.lifemusic.ui.theme.LifeMusicTheme
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * Destination de la pantalla de Perfil.
 *
 * ✅ Centraliza route y title.
 * - Evita strings sueltos.
 * - Usado por AppNavHost.
 */
object ProfileDestination : NavigationDestination {
    override val route = "profile"
    override val title = "Mi Perfil"
}

/**
 * Extensión para formatear birthDate (epoch millis).
 */
private fun Long.toBirthDateText(): String {
    if (this <= 0L) return "Sin definir"
    return SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        .format(Date(this))
}

/**
 * ProfilePage (UI pura).
 *
 * ✅ Regla MVVM:
 * - NO consulta Room.
 * - NO toca DataStore.
 * - NO navega.
 * - Todo el estado entra por parámetros.
 * - Todas las acciones salen por callbacks.
 *
 * username visual = email.substringBefore("@")
 */
@Composable
fun ProfilePage(
    name: String,
    email: String,
    birthDate: Long,
    verified: Boolean,
    photoUri: String?,
    isEditing: Boolean,
    isLoading: Boolean,
    errorMessage: String?,
    onNameChange: (String) -> Unit,
    onEmailChange: (String) -> Unit,
    onEditClick: () -> Unit,
    onCancelEdit: () -> Unit,
    onSaveChanges: () -> Unit,
    onChangePhotoClick: () -> Unit,
    onBackClick: () -> Unit = {},
    onLogoutClick: () -> Unit = {}
) {
    MainScaffold(
        title = ProfileDestination.title,
        onBackClick = onBackClick
    ) { padding ->

        if (isLoading) {
            Box(
                modifier = Modifier.fillMaxSize().padding(padding),
                contentAlignment = Alignment.Center
            ) { CircularProgressIndicator() }
            return@MainScaffold
        }

        val usernameVisual = email.substringBefore("@").ifBlank { "usuario" }
        val scrollState = rememberScrollState()
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(24.dp)
                .verticalScroll(scrollState)
                .navigationBarsPadding(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {

            // Avatar: si hay foto -> AsyncImage, si no -> inicial
            Box(
                modifier = Modifier.size(120.dp).clip(CircleShape).background(Color(0xFF53E88B)),
                contentAlignment = Alignment.Center
            ) {
                if (!photoUri.isNullOrBlank()) {
                    AsyncImage(
                        model = photoUri,
                        contentDescription = "Foto de perfil",
                        modifier = Modifier.fillMaxSize().clip(CircleShape)
                    )
                } else {
                    Text(
                        text = name.firstOrNull()?.uppercase() ?: "U",
                        fontWeight = FontWeight.Bold,
                        fontSize = 48.sp,
                        color = Color.Black.copy(alpha = 0.6f)
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(text = name, fontWeight = FontWeight.Bold, fontSize = 20.sp)
            Text(text = usernameVisual, fontSize = 14.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)

            Spacer(modifier = Modifier.height(12.dp))

            if (!errorMessage.isNullOrBlank()) {
                Text(
                    text = errorMessage,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(12.dp))
            }

            Spacer(modifier = Modifier.height(12.dp))

            if (!isEditing) {

                ProfileInfoItem(Icons.Default.Person, "Nombre completo", name)
                Spacer(modifier = Modifier.height(12.dp))

                ProfileInfoItem(Icons.Default.Email, "Correo electrónico", email)
                Spacer(modifier = Modifier.height(12.dp))

                ProfileInfoItem(Icons.Default.CalendarMonth, "Fecha de nacimiento", birthDate.toBirthDateText())
                Spacer(modifier = Modifier.height(12.dp))

                ProfileInfoItem(Icons.Default.MarkEmailRead, "Correo verificado", if (verified) "Sí" else "No")

                Spacer(modifier = Modifier.height(40.dp))

                Button(
                    onClick = onEditClick,
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth().height(55.dp)
                ) { Text("✏️ Editar perfil", fontWeight = FontWeight.SemiBold) }

                Spacer(modifier = Modifier.height(16.dp))

                OutlinedButton(
                    onClick = onLogoutClick,
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth().height(55.dp),
                    border = BorderStroke(2.dp, MaterialTheme.colorScheme.primary)
                ) { Text("Cerrar sesión", fontWeight = FontWeight.SemiBold) }

            } else {

                // ✅ botón cámara solo en edición
                OutlinedButton(
                    onClick = onChangePhotoClick,
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth().height(55.dp)
                ) { Text("📸 Cambiar foto", fontWeight = FontWeight.SemiBold) }

                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    value = name,
                    onValueChange = onNameChange,
                    label = { Text("Nombre completo") },
                    leadingIcon = { Icon(Icons.Default.Person, contentDescription = null) },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    value = email,
                    onValueChange = onEmailChange,
                    label = { Text("Correo electrónico") },
                    leadingIcon = { Icon(Icons.Default.Email, contentDescription = null) },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(16.dp))

                ProfileInfoItem(Icons.Default.CalendarMonth, "Fecha de nacimiento", birthDate.toBirthDateText())
                Spacer(modifier = Modifier.height(12.dp))

                ProfileInfoItem(Icons.Default.MarkEmailRead, "Correo verificado", if (verified) "Sí" else "No")
                Spacer(modifier = Modifier.height(40.dp))

                Button(
                    onClick = onSaveChanges,
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth().height(55.dp)
                ) { Text("💾 Guardar cambios", fontWeight = FontWeight.SemiBold) }

                Spacer(modifier = Modifier.height(16.dp))

                OutlinedButton(
                    onClick = onCancelEdit,
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth().height(55.dp)
                ) { Text("Cancelar", fontWeight = FontWeight.SemiBold) }
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
            Icon(imageVector = icon, contentDescription = label, tint = MaterialTheme.colorScheme.onSurfaceVariant)
            Spacer(modifier = Modifier.width(12.dp))
            Column {
                Text(text = label, fontSize = 13.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                Text(text = value, fontSize = 15.sp, fontWeight = FontWeight.Medium)
            }
        }
    }
}
@Preview(showBackground = true, name = "Profile - Light")
@Composable
fun ProfilePreviewLight() {
    LifeMusicTheme {
        ProfilePage(
            name = "Luis",
            email = "luis@lifemusic.com",
            birthDate = 946684800000,
            verified = true,
            photoUri = null,
            isEditing = false,
            isLoading = false,
            errorMessage = null,
            onNameChange = {},
            onEmailChange = {},
            onEditClick = {},
            onCancelEdit = {},
            onSaveChanges = {},
        onChangePhotoClick = {},
        onBackClick = {},
        onLogoutClick = {}
        )
    }
}

@Preview(
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    name = "Profile - Dark"
)
@Composable
fun ProfilePreviewDark() {
    LifeMusicTheme {
        ProfilePage(
            name = "Luis",
            email = "luis@lifemusic.com",
            birthDate = 946684800000,
            verified = true,
            photoUri = null,
            isEditing = false,
            isLoading = false,
            errorMessage = null,
            onNameChange = {},
            onEmailChange = {},
            onEditClick = {},
            onCancelEdit = {},
            onSaveChanges = {},
            onChangePhotoClick = {},
            onBackClick = {},
            onLogoutClick = {}
        )
    }
}
