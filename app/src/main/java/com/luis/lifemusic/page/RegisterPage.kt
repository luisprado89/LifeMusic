package com.luis.lifemusic.page

import android.content.res.Configuration
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.QuestionMark
import androidx.compose.material.icons.filled.Security
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.luis.lifemusic.component.MainScaffold
import com.luis.lifemusic.navigation.NavigationDestination
import com.luis.lifemusic.ui.theme.LifeMusicTheme

/**
 * Destination de registro.
 * - route: se usa en AppNavHost para navegar a esta pantalla.
 * - title: título mostrado en la TopAppBar (MainScaffold).
 */
object RegisterDestination : NavigationDestination {
    override val route = "register"
    override val title = "Crear cuenta"
}

/**
 * RegisterPage (UI pura).
 *
 * ✅ Importante:
 * - No guardamos estado con remember.
 * - Todo el estado entra por parámetros y se actualiza con callbacks.
 * - El ViewModel (más adelante) validará:
 *   - si el correo/usuario ya existe en Room
 *   - si password == confirmPassword
 *   - y guardará la pregunta/respuesta para recuperación ficticia.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterPage(
    username: String,
    password: String,
    confirmPassword: String,
    securityQuestion: String,
    securityAnswer: String,
    onUsernameChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onConfirmPasswordChange: (String) -> Unit,
    onSecurityQuestionChange: (String) -> Unit,
    onSecurityAnswerChange: (String) -> Unit,
    onRegisterClick: () -> Unit = {},
    onBackClick: () -> Unit = {}
) {
    // ✅ Regla simple para UX (sin persistencia aquí):
    // activamos botón si hay datos y las contraseñas coinciden.
    // (En MVVM esto también se controlará desde el ViewModel.)
    val canRegister =
        username.isNotBlank() &&
                password.isNotBlank() &&
                confirmPassword.isNotBlank() &&
                password == confirmPassword &&
                securityQuestion.isNotBlank() &&
                securityAnswer.isNotBlank()

    MainScaffold(
        // ✅ Título consistente con la navegación (sin hardcode).
        title = RegisterDestination.title,
        onBackClick = onBackClick
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {

            Text(
                text = "Crea tu cuenta para guardar tus favoritos",
                style = MaterialTheme.typography.bodyMedium
            )

            OutlinedTextField(
                value = username,
                onValueChange = onUsernameChange,
                label = { Text("Usuario o correo (ficticio)") },
                leadingIcon = { Icon(Icons.Default.Email, contentDescription = null) },
                singleLine = true,
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = password,
                onValueChange = onPasswordChange,
                label = { Text("Contraseña") },
                leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null) },
                singleLine = true,
                visualTransformation = PasswordVisualTransformation(),
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = confirmPassword,
                onValueChange = onConfirmPasswordChange,
                label = { Text("Confirmar contraseña") },
                leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null) },
                singleLine = true,
                visualTransformation = PasswordVisualTransformation(),
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = "Recuperación de contraseña (sin email real)",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.SemiBold
            )

            OutlinedTextField(
                value = securityQuestion,
                onValueChange = onSecurityQuestionChange,
                label = { Text("Pregunta de seguridad") },
                leadingIcon = { Icon(Icons.Default.QuestionMark, contentDescription = null) },
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = securityAnswer,
                onValueChange = onSecurityAnswerChange,
                label = { Text("Respuesta") },
                leadingIcon = { Icon(Icons.Default.Security, contentDescription = null) },
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(10.dp))

            Button(
                onClick = onRegisterClick,
                enabled = canRegister, // ✅ evita “registro vacío” en UI
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("Registrarme", fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Preview(showBackground = true, name = "RegisterPage - Light")
@Composable
fun RegisterPagePreviewLight() {
    LifeMusicTheme {
        RegisterPage(
            username = "",
            password = "",
            confirmPassword = "",
            securityQuestion = "¿Cómo se llamaba tu primera mascota?",
            securityAnswer = "",
            onUsernameChange = {},
            onPasswordChange = {},
            onConfirmPasswordChange = {},
            onSecurityQuestionChange = {},
            onSecurityAnswerChange = {}
        )
    }
}

@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES, name = "RegisterPage - Dark")
@Composable
fun RegisterPagePreviewDark() {
    LifeMusicTheme {
        RegisterPage(
            username = "luis@lifemusic.com",
            password = "1234",
            confirmPassword = "1234",
            securityQuestion = "¿En qué ciudad naciste?",
            securityAnswer = "Vigo",
            onUsernameChange = {},
            onPasswordChange = {},
            onConfirmPasswordChange = {},
            onSecurityQuestionChange = {},
            onSecurityAnswerChange = {}
        )
    }
}
