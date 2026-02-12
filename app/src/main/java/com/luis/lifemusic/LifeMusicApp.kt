package com.luis.lifemusic

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.luis.lifemusic.data.AppContainer
import com.luis.lifemusic.navigation.AppNavHost

/**
 * Punto raíz de la app (Compose).
 *
 * ✅ Responsabilidad:
 * - Crear el NavController
 * - Crear el AppContainer (dependencias globales)
 * - Pasar AppContainer al AppNavHost
 *
 * ✅ Regla:
 * - Aquí NO creamos Room ni DataStore directamente.
 * - Eso lo hace AppContainer.
 */
@Composable
fun LifeMusicApp(
    navController: NavHostController = rememberNavController()
) {
    /**
     * appContext:
     * - applicationContext es el más seguro para dependencias globales.
     * - Evitamos llamar a esta variable "context" por claridad.
     */
    val appContext: Context = LocalContext.current.applicationContext

    /**
     * AppContainer se crea una sola vez.
     * remember evita recrearlo en recomposiciones.
     */
    val appContainer: AppContainer = remember {
        AppContainer(appContext)
    }

    /**
     * AppNavHost recibe el contenedor.
     *
     * Más adelante:
     * - AppNavHost creará ViewModels por pantalla usando repositorios del container.
     * - Así evitamos que cada pantalla "invente" dependencias.
     */
    AppNavHost(
        navController = navController,
        appContainer = appContainer
    )
}
