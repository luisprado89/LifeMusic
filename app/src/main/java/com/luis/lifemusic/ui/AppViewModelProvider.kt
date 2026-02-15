package com.luis.lifemusic.ui

import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.luis.lifemusic.LifeMusicApplication
import com.luis.lifemusic.ui.detail.DetailViewModel
import com.luis.lifemusic.ui.home.HomeViewModel
import com.luis.lifemusic.ui.list.ListViewModel
import com.luis.lifemusic.ui.login.LoginViewModel
import com.luis.lifemusic.ui.recover.RecoverViewModel
import com.luis.lifemusic.ui.register.RegisterViewModel

/**
 * Acceso cómodo a LifeMusicApplication desde cualquier ViewModelFactory.
 *
 * Permite obtener appContainer (repositorios) sin pasar dependencias manualmente
 * por parámetros desde Compose root o desde NavHost.
 */
fun CreationExtras.lifeMusicApp(): LifeMusicApplication =
    (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as LifeMusicApplication)

/**
 * Factory global para ViewModels.
 *
 * ✅ Ventajas:
 * - Centraliza inyección de dependencias aquí (repositorios).
 * - Las Routes solo hacen: viewModel(factory = AppViewModelProvider.Factory)
 * - AppNavHost y Pages se mantienen limpios (sin AppContainer).
 */
object AppViewModelProvider {
    val Factory = viewModelFactory {

        // ------------------------------------
        // AUTH
        // ------------------------------------

        initializer {
            LoginViewModel(
                userRepository = lifeMusicApp().appContainer.userRepository,
                sessionRepository = lifeMusicApp().appContainer.sessionRepository
            )
        }

        initializer {
            RegisterViewModel(
                userRepository = lifeMusicApp().appContainer.userRepository,
                sessionRepository = lifeMusicApp().appContainer.sessionRepository
            )
        }

        initializer {
            /**
             * RecoverViewModel:
             * - Gestiona recuperación de contraseña (pregunta seguridad + reset).
             * - Depende solo de UserRepository (Room).
             */
            RecoverViewModel(
                userRepository = lifeMusicApp().appContainer.userRepository
            )
        }

        // ------------------------------------
        // MAIN
        // ------------------------------------

        initializer {
            /**
             * HomeViewModel:
             * - Controla el estado de Home y el guard de sesión (DataStore).
             */
            HomeViewModel(
                sessionRepository = lifeMusicApp().appContainer.sessionRepository
            )
        }

        initializer {
            /**
             * ListViewModel:
             * - Controla el estado de la lista de canciones.
             * - Incluye guard de sesión (DataStore) para proteger la pantalla.
             */
            ListViewModel(
                sessionRepository = lifeMusicApp().appContainer.sessionRepository
            )
        }

        initializer {
            /**
             * DetailViewModel:
             * - Lee songId desde argumentos (SavedStateHandle).
             * - Gestiona favoritos con Room.
             * - Aplica guard de sesión.
             */
            DetailViewModel(
                savedStateHandle = this.createSavedStateHandle(),
                sessionRepository = lifeMusicApp().appContainer.sessionRepository,
                favoritesRepository = lifeMusicApp().appContainer.favoritesRepository
            )
        }

        initializer {
            // 🔜 Pendiente implementar ProfileViewModel
            TODO("Crear ProfileViewModel")
        }
    }
}
