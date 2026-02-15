package com.luis.lifemusic.ui

import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.luis.lifemusic.LifeMusicApplication
import com.luis.lifemusic.ui.login.LoginViewModel
import com.luis.lifemusic.ui.recover.RecoverViewModel
import com.luis.lifemusic.ui.register.RegisterViewModel

/**
 * Acceso cómodo a LifeMusicApplication desde cualquier ViewModelFactory.
 */
fun CreationExtras.lifeMusicApp(): LifeMusicApplication =
    (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as LifeMusicApplication)

/**
 * Factory global para ViewModels (como en DiscosFavoritos2).
 *
 * ✅ Ventajas:
 * - Centralizas dependencias (repositorios) aquí.
 * - Las Screens solo hacen: viewModel(factory = AppViewModelProvider.Factory)
 * - AppNavHost no se llena de factories.
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
            // RecoverPasswordViewModel(...)
            RecoverViewModel(
                userRepository = lifeMusicApp().appContainer.userRepository
            )

        }

        // ------------------------------------
        // MAIN
        // ------------------------------------
        initializer {
            // HomeViewModel(...)
            TODO("Crear HomeViewModel")
        }

        initializer {
            // ListViewModel(...)
            TODO("Crear ListViewModel")
        }

        initializer {
            // DetailViewModel(savedStateHandle, ...)
            // this.createSavedStateHandle()
            TODO("Crear DetailViewModel")
        }

        initializer {
            // ProfileViewModel(...)
            TODO("Crear ProfileViewModel")
        }
    }
}
