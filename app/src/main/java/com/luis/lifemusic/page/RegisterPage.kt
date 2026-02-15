package com.luis.lifemusic.page

import android.app.DatePickerDialog
import android.content.res.Configuration
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.QuestionMark
import androidx.compose.material.icons.filled.Security
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.luis.lifemusic.component.MainScaffold
import com.luis.lifemusic.navigation.NavigationDestination
import com.luis.lifemusic.ui.theme.LifeMusicTheme
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

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
 * ✅ Regla MVVM:
 * - No guardamos estado con remember para datos de negocio.
 * - Todo el estado entra por parámetros y se actualiza con callbacks.
 *
 * ✅ Campos obligatorios:
 * - Nombre completo (displayName)
 * - Correo (email) -> será el login y es único en Room
 * - Contraseña + confirmar (confirm solo UI)
 * - Fecha de nacimiento (birthDate) obligatoria
 * - Pregunta de seguridad + respuesta (para recuperación sin email real)
 *
 * 🗓️ Selector de fecha:
 * - Por defecto usamos DatePickerDialog (Android).
 *   (librería o custom) y devolver igualmente un Long (epoch millis).
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterPage(
    displayName: String,
    email: String,
    birthDate: Long?,
    password: String,
    confirmPassword: String,
    securityQuestion: String,
    securityAnswer: String,
    isLoading: Boolean,
    errorMessage: String?,
    onDisplayNameChange: (String) -> Unit,
    onEmailChange: (String) -> Unit,
    onBirthDateChange: (Long?) -> Unit,
    onPasswordChange: (String) -> Unit,
    onConfirmPasswordChange: (String) -> Unit,
    onSecurityQuestionChange: (String) -> Unit,
    onSecurityAnswerChange: (String) -> Unit,
    onRegisterClick: () -> Unit = {},
    onBackClick: () -> Unit = {}
) {
    val context = LocalContext.current

    // Formateo simple para mostrar la fecha elegida
    val dateFormatter = remember { SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()) }
    val birthDateText = birthDate?.let { dateFormatter.format(Date(it)) } ?: ""

    // ✅ Regla simple para UX:
    // activamos botón si hay datos obligatorios + passwords coinciden + fecha marcada.
    val canRegister =
        displayName.isNotBlank() &&
                email.isNotBlank() &&
                birthDate != null &&
                password.isNotBlank() &&
                confirmPassword.isNotBlank() &&
                password == confirmPassword &&
                securityQuestion.isNotBlank() &&
                securityAnswer.isNotBlank() &&
                !isLoading

    MainScaffold(
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

            // Error (si lo hay)
            if (!errorMessage.isNullOrBlank()) {
                Text(
                    text = errorMessage,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall
                )
            }

            // -------------------------
            // Datos obligatorios
            // -------------------------

            OutlinedTextField(
                value = displayName,
                onValueChange = onDisplayNameChange,
                label = { Text("Nombre completo") },
                leadingIcon = { Icon(Icons.Default.Person, contentDescription = null) },
                singleLine = true,
                enabled = !isLoading,
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = email,
                onValueChange = onEmailChange,
                label = { Text("Correo electrónico") },
                leadingIcon = { Icon(Icons.Default.Email, contentDescription = null) },
                singleLine = true,
                enabled = !isLoading,
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier.fillMaxWidth()
            )

            // Fecha nacimiento (campo readonly + abre DatePicker)
            OutlinedTextField(
                value = birthDateText,
                onValueChange = {},
                label = { Text("Fecha de nacimiento") },
                leadingIcon = { Icon(Icons.Default.CalendarMonth, contentDescription = null) },
                readOnly = true,
                enabled = !isLoading,
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier.fillMaxWidth(),
                trailingIcon = {
                    TextButton(
                        enabled = !isLoading,
                        onClick = {
                            val cal = Calendar.getInstance()
                            if (birthDate != null) cal.timeInMillis = birthDate

                            val year = cal.get(Calendar.YEAR)
                            val month = cal.get(Calendar.MONTH)
                            val day = cal.get(Calendar.DAY_OF_MONTH)

                            DatePickerDialog(
                                context,
                                { _, y, m, d ->
                                    val picked = Calendar.getInstance().apply {
                                        set(Calendar.YEAR, y)
                                        set(Calendar.MONTH, m)
                                        set(Calendar.DAY_OF_MONTH, d)
                                        set(Calendar.HOUR_OF_DAY, 0)
                                        set(Calendar.MINUTE, 0)
                                        set(Calendar.SECOND, 0)
                                        set(Calendar.MILLISECOND, 0)
                                    }
                                    onBirthDateChange(picked.timeInMillis)
                                },
                                year,
                                month,
                                day
                            ).show()
                        }
                    ) {
                        Text("Elegir")
                    }
                }
            )

            OutlinedTextField(
                value = password,
                onValueChange = onPasswordChange,
                label = { Text("Contraseña") },
                leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null) },
                singleLine = true,
                enabled = !isLoading,
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
                enabled = !isLoading,
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
                enabled = !isLoading,
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = securityAnswer,
                onValueChange = onSecurityAnswerChange,
                label = { Text("Respuesta") },
                leadingIcon = { Icon(Icons.Default.Security, contentDescription = null) },
                enabled = !isLoading,
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(10.dp))

            Button(
                onClick = onRegisterClick,
                enabled = canRegister,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                shape = RoundedCornerShape(12.dp)
            ) {
                if (isLoading) {
                    CircularProgressIndicator(
                        strokeWidth = 2.dp,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Text("Creando cuenta…", fontWeight = FontWeight.Bold)
                } else {
                    Text("Registrarme", fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@Preview(showBackground = true, name = "RegisterPage - Light")
@Composable
fun RegisterPagePreviewLight() {
    LifeMusicTheme {
        RegisterPage(
            displayName = "",
            email = "",
            birthDate = null,
            password = "",
            confirmPassword = "",
            securityQuestion = "¿Cómo se llamaba tu primera mascota?",
            securityAnswer = "",
            isLoading = false,
            errorMessage = null,
            onDisplayNameChange = {},
            onEmailChange = {},
            onBirthDateChange = {},
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
            displayName = "Luis Prado",
            email = "luisdpc@gmail.com",
            birthDate = System.currentTimeMillis(),
            password = "123456",
            confirmPassword = "123456",
            securityQuestion = "¿En qué ciudad naciste?",
            securityAnswer = "Vigo",
            isLoading = false,
            errorMessage = null,
            onDisplayNameChange = {},
            onEmailChange = {},
            onBirthDateChange = {},
            onPasswordChange = {},
            onConfirmPasswordChange = {},
            onSecurityQuestionChange = {},
            onSecurityAnswerChange = {}
        )
    }
}
