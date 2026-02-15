package com.luis.lifemusic.page

import android.content.res.Configuration
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
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

/**
 * Destination de la pantalla de Perfil.
 *
 * ✅ Por qué existe:
 * - Centraliza "route" y "title" para navegación.
 * - Evita strings sueltos repartidos por el proyecto.
 * - AppNavHost usa ProfileDestination.route para navegar a esta pantalla.
 */
object ProfileDestination : NavigationDestination {
    override val route = "profile"
    override val title = "Mi Perfil"
}

/**
 * ProfilePage (UI pura).
 *
 * ✅ Regla MVVM:
 * - La pantalla NO consulta Room, NO toca DataStore y NO tiene lógica de negocio.
 * - Todo el estado entra por parámetros (ProfileUiState).
 * - Todas las acciones salen por callbacks (eventos hacia el ViewModel).
 *
 * ✅ Estados visuales:
 * - isLoading: muestra indicador de progreso.
 * - errorMessage: muestra feedback de error.
 *
 * Nota:
 * - El modo edición (isEditing) viene controlado por el ViewModel.
 */
@Composable
fun ProfilePage(
    name: String,
    email: String,
    verified: Boolean,
    memberSince: String,
    isEditing: Boolean,
    isLoading: Boolean,
    errorMessage: String?,
    onNameChange: (String) -> Unit,
    onEmailChange: (String) -> Unit,
    onEditClick: () -> Unit,
    onCancelEdit: () -> Unit,
    onSaveChanges: () -> Unit,
    onBackClick: () -> Unit = {},
    onLogoutClick: () -> Unit = {}
) {
    MainScaffold(
        // Usamos el title centralizado en el Destination para consistencia.
        title = ProfileDestination.title,
        onBackClick = onBackClick
    ) { padding ->

        // Estado de carga: mostramos progreso centrado.
        if (isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
            return@MainScaffold
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {

            // Avatar circular con inicial del nombre.
            Box(
                modifier = Modifier
                    .size(120.dp)
                    .clip(CircleShape)
                    .background(Color(0xFF53E88B)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = name.firstOrNull()?.uppercase() ?: "L",
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

            Spacer(modifier = Modifier.height(12.dp))

            // Feedback de error (si existe).
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
                // ===========================
                // MODO VISUALIZACIÓN
                // ===========================
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
                    onClick = onEditClick,
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
                // ===========================
                // MODO EDICIÓN
                // ===========================
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

                ProfileInfoItem(
                    Icons.Default.MarkEmailRead,
                    "Correo verificado",
                    if (verified) "Sí" else "No"
                )
                Spacer(modifier = Modifier.height(12.dp))
                ProfileInfoItem(Icons.Default.CalendarMonth, "Miembro desde", memberSince)

                Spacer(modifier = Modifier.height(40.dp))

                Button(
                    onClick = onSaveChanges,
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(55.dp)
                ) {
                    Text("💾 Guardar cambios", fontWeight = FontWeight.SemiBold)
                }

                Spacer(modifier = Modifier.height(16.dp))

                OutlinedButton(
                    onClick = onCancelEdit,
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

/**
 * Item reutilizable para mostrar información del perfil.
 */
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
        ProfilePage(
            name = "Luis",
            email = "luis@lifemusic.com",
            verified = true,
            memberSince = "Enero 2023",
            isEditing = false,
            isLoading = false,
            errorMessage = null,
            onNameChange = {},
            onEmailChange = {},
            onEditClick = {},
            onCancelEdit = {},
            onSaveChanges = {}
        )
    }
}

@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES, name = "ProfilePage - Dark Mode")
@Composable
fun ProfilePagePreviewDark() {
    LifeMusicTheme {
        ProfilePage(
            name = "Luis",
            email = "luis@lifemusic.com",
            verified = true,
            memberSince = "Enero 2023",
            isEditing = false,
            isLoading = false,
            errorMessage = null,
            onNameChange = {},
            onEmailChange = {},
            onEditClick = {},
            onCancelEdit = {},
            onSaveChanges = {}
        )
    }
}
