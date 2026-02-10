package com.luis.lifemusic.page

import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.luis.lifemusic.R
import com.luis.lifemusic.navigation.NavigationDestination
import com.luis.lifemusic.ui.theme.LifeMusicTheme

/**
 * ✅ Destino de navegación de la pantalla Login.
 *
 * ¿Por qué existe este object?
 * - Centraliza la ruta (route) y el título (title) de la pantalla.
 * - Evita "strings sueltos" repetidos por la app (más mantenible).
 * - Facilita construir el NavHost y referenciar esta pantalla de forma consistente.
 */
object LoginDestination : NavigationDestination {
    override val route = "login"
    override val title = "Login"
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginPage(
    username: String,
    password: String,
    onUsernameChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onLoginClick: () -> Unit = {},
    onGoToRegister: () -> Unit = {},
    onGoToRecover: () -> Unit = {}
) {
    /**
     * ⚠️ Importante (MVVM):
     * Esta pantalla NO guarda estado interno con remember.
     * Recibe username/password y callbacks desde fuera (ViewModel).
     *
     * Ventaja:
     * - UI pura (@Composable) sin lógica de negocio.
     * - El estado puede venir de un ViewModel con StateFlow, cumpliendo el enunciado del proyecto.
     */
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {

            // LOGO
            Image(
                painter = painterResource(id = R.drawable.logo_lm),
                contentDescription = "Logo LifeMusic",
                modifier = Modifier
                    .size(100.dp)
                    .padding(bottom = 12.dp)
            )

            // Título principal
            Text(
                text = "LifeMusic",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground
            )

            // Subtítulo
            Text(
                text = "Tu música para cada momento de la vida",
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f),
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(bottom = 24.dp)
            )

            // Sección de bienvenida
            Text(
                text = "Bienvenido",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            // Campo usuario o email
            OutlinedTextField(
                value = username,
                onValueChange = onUsernameChange,
                label = { Text("Nombre de usuario o correo electrónico") },
                leadingIcon = {
                    Icon(Icons.Default.Email, contentDescription = "Usuario")
                },
                singleLine = true,
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 12.dp)
            )

            // Campo contraseña
            OutlinedTextField(
                value = password,
                onValueChange = onPasswordChange,
                label = { Text("Contraseña") },
                leadingIcon = {
                    Icon(Icons.Default.Lock, contentDescription = "Contraseña")
                },
                visualTransformation = PasswordVisualTransformation(),
                singleLine = true,
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 20.dp)
            )

            // Botón iniciar sesión
            Button(
                onClick = onLoginClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                shape = RoundedCornerShape(10.dp)
            ) {
                Text(
                    text = "Iniciar sesión",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            // Recuperación contraseña
            Text(
                text = "¿Olvidaste tu contraseña?",
                color = MaterialTheme.colorScheme.primary,
                fontSize = 14.sp,
                modifier = Modifier
                    .padding(top = 16.dp)
                    .clickable { onGoToRecover() }
            )

            // Link registro
            Spacer(modifier = Modifier.height(12.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(text = "¿No tienes cuenta? ")
                Text(
                    text = "Regístrate",
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.clickable { onGoToRegister() }
                )
            }
        }
    }
}

/**
 * Previews con datos fake SOLO para diseño.
 * (Luego en MVVM estos valores vendrán del ViewModel)
 */
@Preview(showBackground = true, name = "LoginPage - Light Mode")
@Composable
fun LoginPagePreviewLight() {
    LifeMusicTheme {
        LoginPage(
            username = "",
            password = "",
            onUsernameChange = {},
            onPasswordChange = {}
        )
    }
}

@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES, name = "LoginPage - Dark Mode")
@Composable
fun LoginPagePreviewDark() {
    LifeMusicTheme {
        LoginPage(
            username = "luis@lifemusic.com",
            password = "1234",
            onUsernameChange = {},
            onPasswordChange = {}
        )
    }
}
