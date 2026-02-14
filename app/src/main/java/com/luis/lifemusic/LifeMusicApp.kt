package com.luis.lifemusic

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.luis.lifemusic.navigation.AppNavHost

/**
 * Punto raíz de la app (Compose).
 *
 * ✅ Responsabilidad:
 * - Crear el NavController
 * - Obtener el AppContainer desde LifeMusicApplication
 * - Pasarlo al AppNavHost
 *
 * ✅ Regla:
 * - Aquí NO se crea Room, ni DataStore, ni Retrofit.
 * - Eso vive en AppContainer dentro de LifeMusicApplication.
 */
@Composable
fun LifeMusicApp(
    navController: NavHostController = rememberNavController()
) {
    /**
     * Recuperamos el Application real.
     *
     * - LocalContext.current -> context actual (Activity)
     * - applicationContext -> contexto de aplicación (seguro y global)
     * - Lo casteamos a LifeMusicApplication para acceder a appContainer
     */
    val lifeMusicApp = LocalContext.current.applicationContext as LifeMusicApplication

    /**
     * AppContainer ya está creado una sola vez en LifeMusicApplication.onCreate().
     * No hace falta remember aquí.
     */
    val appContainer = lifeMusicApp.appContainer

    /**
     * AppNavHost recibe el contenedor con Room/DataStore/Retrofit (cuando toque).
     */
    AppNavHost(
        navController = navController,
        appContainer = appContainer
    )
}
