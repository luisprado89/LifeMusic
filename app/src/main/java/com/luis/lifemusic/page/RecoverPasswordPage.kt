package com.luis.lifemusic.page

import android.content.res.Configuration
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.QuestionMark
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
 * Destination de recuperación.
 * - route: se usa en AppNavHost para navegar a esta pantalla.
 * - title: título que mostramos en la TopAppBar (MainScaffold).
 */
object RecoverDestination : NavigationDestination {
    override val route = "recover"
    override val title = "Recuperar contraseña"
}

/**
 * RecoverPasswordPage (UI pura).
 *
 * ✅ Importante:
 * - Esta pantalla NO guarda estado con remember.
 * - Todo el estado entra por parámetros (username, isLoading, isQuestionLoaded, etc.).
 * - La pantalla solo “emite eventos” mediante callbacks:
 *   - onSearchUserClick()
 *   - onResetPasswordClick()
 *
 * Esto facilita integrar MVVM + Room después:
 * - El ViewModel controlará el estado y validará si el usuario/correo existe.
 * - Room aportará la persistencia (usuarios + pregunta/respuesta ficticia).
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecoverPasswordPage(
    username: String,
    securityQuestion: String,
    securityAnswer: String,
    newPassword: String,
    isQuestionLoaded: Boolean,
    isLoading: Boolean,
    onUsernameChange: (String) -> Unit,
    onSecurityAnswerChange: (String) -> Unit,
    onNewPasswordChange: (String) -> Unit,
    onSearchUserClick: () -> Unit = {},
    onResetPasswordClick: () -> Unit = {},
    onBackClick: () -> Unit = {}
) {
    // El botón de reset solo se habilita si ya hay respuesta y contraseña nueva.
    val canReset = securityAnswer.isNotBlank() && newPassword.isNotBlank()

    MainScaffold(
        // ✅ Usamos el title del Destination para que sea consistente con la navegación.
        title = RecoverDestination.title,
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
                text = "Recupera tu contraseña sin email real usando tu pregunta de seguridad.",
                style = MaterialTheme.typography.bodyMedium
            )

            OutlinedTextField(
                value = username,
                onValueChange = onUsernameChange,
                label = { Text("Usuario o correo") },
                leadingIcon = { Icon(Icons.Default.Email, contentDescription = null) },
                singleLine = true,
                shape = RoundedCornerShape(8.dp),
                enabled = !isLoading,
                modifier = Modifier.fillMaxWidth()
            )

            Button(
                onClick = onSearchUserClick,
                enabled = username.isNotBlank() && !isLoading,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                shape = RoundedCornerShape(12.dp)
            ) {
                if (isLoading) {
                    CircularProgressIndicator(
                        strokeWidth = 2.dp,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Text("Buscando...", fontWeight = FontWeight.Bold)
                } else {
                    Text("Buscar cuenta", fontWeight = FontWeight.Bold)
                }
            }

            /**
             * ✅ Este bloque (Paso 2) solo se muestra cuando:
             * isQuestionLoaded == true
             *
             * Eso significa que “el usuario/correo existe” y ya tenemos la pregunta cargada.
             * En el futuro, el ViewModel lo activará tras consultar Room.
             */
            if (isQuestionLoaded) {
                Spacer(modifier = Modifier.height(10.dp))

                OutlinedTextField(
                    value = securityQuestion,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Pregunta de seguridad") },
                    leadingIcon = { Icon(Icons.Default.QuestionMark, contentDescription = null) },
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = securityAnswer,
                    onValueChange = onSecurityAnswerChange,
                    label = { Text("Tu respuesta") },
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = newPassword,
                    onValueChange = onNewPasswordChange,
                    label = { Text("Nueva contraseña") },
                    leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null) },
                    singleLine = true,
                    visualTransformation = PasswordVisualTransformation(),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.fillMaxWidth()
                )

                Button(
                    onClick = onResetPasswordClick,
                    enabled = canReset && !isLoading,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("Cambiar contraseña", fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@Preview(showBackground = true, name = "RecoverPasswordPage - Paso 1 (Light)")
@Composable
fun RecoverPasswordPagePreviewStep1Light() {
    LifeMusicTheme {
        RecoverPasswordPage(
            username = "",
            securityQuestion = "",
            securityAnswer = "",
            newPassword = "",
            isQuestionLoaded = false,
            isLoading = false,
            onUsernameChange = {},
            onSecurityAnswerChange = {},
            onNewPasswordChange = {}
        )
    }
}

@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES, name = "RecoverPasswordPage - Paso 2 (Dark)")
@Composable
fun RecoverPasswordPagePreviewStep2Dark() {
    LifeMusicTheme {
        RecoverPasswordPage(
            username = "luis@lifemusic.com",
            securityQuestion = "¿En qué ciudad naciste?",
            securityAnswer = "",
            newPassword = "",
            isQuestionLoaded = true,
            isLoading = false,
            onUsernameChange = {},
            onSecurityAnswerChange = {},
            onNewPasswordChange = {}
        )
    }
}
