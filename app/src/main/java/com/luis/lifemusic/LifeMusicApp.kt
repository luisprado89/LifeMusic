package com.luis.lifemusic

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.luis.lifemusic.navigation.AppNavHost

/**
 * Punto raíz de la app (Compose).
 *
 * ✅ Responsabilidad:
 * - Crear el NavController
 * - Delegar navegación al AppNavHost
 *
 * ✅ Regla:
 * - Aquí NO se crea Room, ni DataStore, ni Retrofit.
 * - Aquí tampoco se pasa appContainer manualmente.
 *
 * ✅ ¿Por qué está así?
 * - Las dependencias (Room/DataStore/Retrofit) ya se resuelven desde
 *   LifeMusicApplication -> AppContainer -> AppViewModelProvider.
 * - De este modo, la capa Compose raíz queda centrada en navegación y
 *   evitamos duplicar responsabilidades o acoplar de más el App root.
 */
@Composable
fun LifeMusicApp(
    navController: NavHostController = rememberNavController()
) {
    /**
     * AppNavHost define rutas y flujo de pantallas.
     *
     * Los ViewModels obtienen sus repositorios usando la Factory global,
     * por eso aquí no hace falta recuperar LocalContext ni castear Application.
     */
    AppNavHost(navController = navController)
}
