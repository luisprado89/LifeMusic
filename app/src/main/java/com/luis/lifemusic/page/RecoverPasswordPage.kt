package com.luis.lifemusic.page

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.QuestionMark
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
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
 * - Todo el estado entra por parámetros (email, isLoading, isQuestionLoaded, etc.).
 * - La pantalla solo “emite eventos” mediante callbacks:
 *   - onSearchUserClick()
 *   - onResetPasswordClick()
 *
 * Esto facilita integrar MVVM + Room:
 * - El ViewModel controlará el estado y validará si el correo existe.
 * - Room aportará la persistencia (users + pregunta/respuesta).
 *
 * Flujo (2 pasos):
 * 1) Introduces email y pulsas "Buscar cuenta"
 * 2) Si existe, se muestra la pregunta + campos para resetear contraseña
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecoverPasswordPage(
    email: String,
    securityQuestion: String,
    securityAnswer: String,
    newPassword: String,
    isQuestionLoaded: Boolean,
    isLoading: Boolean,
    errorMessage: String? = null,
    successMessage: String? = null,
    onEmailChange: (String) -> Unit,
    onSecurityAnswerChange: (String) -> Unit,
    onNewPasswordChange: (String) -> Unit,
    onSearchUserClick: () -> Unit = {},
    onResetPasswordClick: () -> Unit = {},
    onBackClick: () -> Unit = {}
) {
    // El botón de reset solo se habilita si ya hay respuesta y contraseña nueva.
    val canReset = securityAnswer.isNotBlank() && newPassword.isNotBlank()

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
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
                    text = "Recupera tu contraseña usando tu pregunta de seguridad.",
                    style = MaterialTheme.typography.bodyMedium
                )

                OutlinedTextField(
                    value = email,
                    onValueChange = onEmailChange,
                    label = { Text("Correo electrónico") },
                    leadingIcon = { Icon(Icons.Default.Email, contentDescription = null) },
                    singleLine = true,
                    shape = RoundedCornerShape(8.dp),
                    enabled = !isLoading,
                    modifier = Modifier.fillMaxWidth()
                )

                Button(
                    onClick = onSearchUserClick,
                    enabled = email.isNotBlank() && !isLoading,
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

                // Feedback de error del ViewModel (validación o credenciales de recuperación).
                if (!errorMessage.isNullOrBlank()) {
                    Text(
                        text = errorMessage,
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodySmall
                    )
                }

                // Feedback de éxito tras cambiar la contraseña.
                if (!successMessage.isNullOrBlank()) {
                    Text(
                        text = successMessage,
                        color = MaterialTheme.colorScheme.primary,
                        style = MaterialTheme.typography.bodySmall,
                        fontWeight = FontWeight.SemiBold
                    )
                }

                /**
                 * ✅ Paso 2:
                 * Este bloque solo se muestra cuando isQuestionLoaded == true
                 *
                 * Eso significa que “el correo existe” y ya tenemos la pregunta cargada.
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
                        enabled = !isLoading,
                        modifier = Modifier.fillMaxWidth()
                    )

                    OutlinedTextField(
                        value = newPassword,
                        onValueChange = onNewPasswordChange,
                        label = { Text("Nueva contraseña") },
                        leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null) },
                        singleLine = true,
                        enabled = !isLoading,
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
}

@Preview(showBackground = true, name = "RecoverPasswordPage - Paso 1 (Light)")
@Composable
fun RecoverPasswordPagePreviewStep1Light() {
    LifeMusicTheme {
        RecoverPasswordPage(
            email = "",
            securityQuestion = "",
            securityAnswer = "",
            newPassword = "",
            isQuestionLoaded = false,
            isLoading = false,
            errorMessage = null,
            successMessage = null,
            onEmailChange = {},
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
            email = "luis@lifemusic.com",
            securityQuestion = "¿En qué ciudad naciste?",
            securityAnswer = "",
            newPassword = "",
            isQuestionLoaded = true,
            isLoading = false,
            errorMessage = null,
            successMessage = null,
            onEmailChange = {},
            onSecurityAnswerChange = {},
            onNewPasswordChange = {}
        )
    }
}
