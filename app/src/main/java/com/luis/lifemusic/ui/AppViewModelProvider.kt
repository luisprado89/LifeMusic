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
import com.luis.lifemusic.ui.profile.ProfileViewModel
import com.luis.lifemusic.ui.recover.RecoverViewModel
import com.luis.lifemusic.ui.register.RegisterViewModel

/**
 * Acceso cómodo a LifeMusicApplication desde cualquier ViewModelFactory.
 *
 * ✅ Por qué:
 * - Nos permite recuperar el AppContainer y sus repositorios (Room/DataStore/etc.)
 *   sin pasar dependencias manualmente por Compose.
 */
fun CreationExtras.lifeMusicApp(): LifeMusicApplication =
    (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as LifeMusicApplication)

/**
 * Factory global para ViewModels.
 *
 * ✅ Ventajas:
 * - Centraliza dependencias (repositorios) aquí.
 * - Las Routes solo hacen: viewModel(factory = AppViewModelProvider.Factory)
 * - AppNavHost se mantiene limpio (no se llena de factories).
 */
object AppViewModelProvider {

    val Factory = viewModelFactory {

        // ------------------------------------
        // AUTH
        // ------------------------------------

        initializer {
            /**
             * LoginViewModel:
             * - Autentica usuario (Room) y guarda sesión (DataStore).
             */
            LoginViewModel(
                userRepository = lifeMusicApp().appContainer.userRepository,
                sessionRepository = lifeMusicApp().appContainer.sessionRepository
            )
        }

        initializer {
            /**
             * RegisterViewModel:
             * - Registra usuario (Room) y crea sesión (DataStore) tras éxito.
             */
            RegisterViewModel(
                userRepository = lifeMusicApp().appContainer.userRepository,
                sessionRepository = lifeMusicApp().appContainer.sessionRepository
            )
        }

        initializer {
            /**
             * RecoverViewModel:
             * - Recupera contraseña usando pregunta/respuesta de seguridad (Room).
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
             * - Controla el estado de List y el guard de sesión (DataStore).
             */
            ListViewModel(
                sessionRepository = lifeMusicApp().appContainer.sessionRepository
            )
        }

        initializer {
            /**
             * DetailViewModel:
             * - Lee argumentos de navegación con SavedStateHandle (songId).
             * - Controla guard de sesión (DataStore).
             * - Gestiona favoritos reales por usuario (Room).
             */
            DetailViewModel(
                savedStateHandle = this.createSavedStateHandle(),
                sessionRepository = lifeMusicApp().appContainer.sessionRepository,
                favoritesRepository = lifeMusicApp().appContainer.favoritesRepository
            )
        }

        initializer {
            /**
             * ProfileViewModel:
             * - Observa sesión activa (DataStore).
             * - Observa usuario actual (Room) y permite actualizar perfil.
             * - Logout limpiando sesión (DataStore).
             */
            ProfileViewModel(
                userRepository = lifeMusicApp().appContainer.userRepository,
                sessionRepository = lifeMusicApp().appContainer.sessionRepository
            )
        }
    }
}
