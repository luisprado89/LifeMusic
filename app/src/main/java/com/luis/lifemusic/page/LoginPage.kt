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
 * Destino de navegación para Login.
 * Se mantiene aquí para centralizar route/title y evitar strings sueltos.
 */
object LoginDestination : NavigationDestination {
    override val route = "login"
    override val title = "Login"
}

/**
 * Pantalla de Login 100% UI.
 *
 * Reglas que cumple:
 * - No usa remember para estado de producción.
 * - Todo estado viene por parámetros (desde ViewModel).
 * - Solo emite eventos mediante callbacks.
 */
@Composable
fun LoginPage(
    username: String,
    password: String,
    isLoading: Boolean,
    errorMessage: String?,
    onUsernameChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onLoginClick: () -> Unit = {},
    onGoToRegister: () -> Unit = {},
    onGoToRecover: () -> Unit = {}
) {
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
            // Logo de app
            Image(
                painter = painterResource(id = R.drawable.logo_lm),
                contentDescription = "Logo LifeMusic",
                modifier = Modifier
                    .size(100.dp)
                    .padding(bottom = 12.dp)
            )

            Text(
                text = "LifeMusic",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground
            )

            Text(
                text = "Tu música para cada momento de la vida",
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f),
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(bottom = 24.dp)
            )

            Text(
                text = "Bienvenido",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            // Campo usuario/email
            OutlinedTextField(
                value = username,
                onValueChange = onUsernameChange,
                label = { Text("Nombre de usuario o correo electrónico") },
                leadingIcon = { Icon(Icons.Default.Email, contentDescription = "Usuario") },
                singleLine = true,
                enabled = !isLoading, // Bloqueamos edición durante login
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
                leadingIcon = { Icon(Icons.Default.Lock, contentDescription = "Contraseña") },
                visualTransformation = PasswordVisualTransformation(),
                singleLine = true,
                enabled = !isLoading, // Bloqueamos edición durante login
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp)
            )

            // Mensaje de error del ViewModel (si existe)
            if (!errorMessage.isNullOrBlank()) {
                Text(
                    text = errorMessage,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 12.dp)
                )
            } else {
                Spacer(modifier = Modifier.height(12.dp))
            }

            // Botón login + loading
            Button(
                onClick = onLoginClick,
                enabled = !isLoading, // Evita doble click mientras carga
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                shape = RoundedCornerShape(10.dp)
            ) {
                if (isLoading) {
                    CircularProgressIndicator(
                        strokeWidth = 2.dp,
                        modifier = Modifier.size(18.dp)
                    )
                } else {
                    Text(
                        text = "Iniciar sesión",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Text(
                text = "¿Olvidaste tu contraseña?",
                color = MaterialTheme.colorScheme.primary,
                fontSize = 14.sp,
                modifier = Modifier
                    .padding(top = 16.dp)
                    .clickable(enabled = !isLoading) { onGoToRecover() }
            )

            Spacer(modifier = Modifier.height(12.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(text = "¿No tienes cuenta? ")
                Text(
                    text = "Regístrate",
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.clickable(enabled = !isLoading) { onGoToRegister() }
                )
            }
        }
    }
}

/**
 * Preview solo visual (datos fake, sin ViewModel real).
 */
@Preview(showBackground = true, name = "LoginPage - Light Mode")
@Composable
fun LoginPagePreviewLight() {
    LifeMusicTheme {
        LoginPage(
            username = "",
            password = "",
            isLoading = false,
            errorMessage = null,
            onUsernameChange = {},
            onPasswordChange = {}
        )
    }
}

@Preview(
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    name = "LoginPage - Dark Mode"
)
@Composable
fun LoginPagePreviewDark() {
    LifeMusicTheme {
        LoginPage(
            username = "luis@lifemusic.com",
            password = "1234",
            isLoading = false,
            errorMessage = "Credenciales incorrectas",
            onUsernameChange = {},
            onPasswordChange = {}
        )
    }
}
