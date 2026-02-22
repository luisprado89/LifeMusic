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
 * ============================================================
 * APP VIEWMODEL PROVIDER
 * ============================================================
 *
 * 🎯 RESPONSABILIDAD:
 * - Crear todos los ViewModels de la app.
 * - Inyectar dependencias manualmente desde AppContainer.
 *
 * ✅ ARQUITECTURA:
 * - Sin Hilt.
 * - Sin Koin.
 * - Inyección manual vía LifeMusicApplication.
 *
 * 👉 Cada ViewModel recibe SOLO repositorios,
 *    nunca DAOs ni Retrofit directamente.
 */

/**
 * Extensión para obtener la instancia de LifeMusicApplication
 * desde CreationExtras.
 */
fun CreationExtras.lifeMusicApp(): LifeMusicApplication =
    this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as LifeMusicApplication

/**
 * Factory centralizada de ViewModels.
 */
object AppViewModelProvider {

    val Factory = viewModelFactory {

        // ============================================================
        // AUTH VIEWMODELS
        // ============================================================

        initializer {
            LoginViewModel(
                userRepository = lifeMusicApp().appContainer.userRepository,
                sessionRepository = lifeMusicApp().appContainer.sessionRepository
            )
        }

        initializer {
            RegisterViewModel(
                userRepository = lifeMusicApp().appContainer.userRepository,
                sessionRepository = lifeMusicApp().appContainer.sessionRepository,
                favoritesRepository = lifeMusicApp().appContainer.favoritesRepository
            )
        }

        initializer {
            RecoverViewModel(
                userRepository = lifeMusicApp().appContainer.userRepository
            )
        }

        // ============================================================
        // MAIN VIEWMODELS
        // ============================================================

        initializer {
            HomeViewModel(
                sessionRepository = lifeMusicApp().appContainer.sessionRepository,
                favoritesRepository = lifeMusicApp().appContainer.favoritesRepository,
                // 🔥 Ahora usamos el SpotifyRepository del AppContainer
                spotifyRepository = lifeMusicApp().appContainer.spotifyRepository
            )
        }

        initializer {
            ListViewModel(
                sessionRepository = lifeMusicApp().appContainer.sessionRepository,
                favoritesRepository = lifeMusicApp().appContainer.favoritesRepository,
                spotifyRepository = lifeMusicApp().appContainer.spotifyRepository
            )
        }

        initializer {
            DetailViewModel(
                savedStateHandle = this.createSavedStateHandle(),
                sessionRepository = lifeMusicApp().appContainer.sessionRepository,
                favoritesRepository = lifeMusicApp().appContainer.favoritesRepository,
                spotifyRepository = lifeMusicApp().appContainer.spotifyRepository
            )
        }

        initializer {
            ProfileViewModel(
                userRepository = lifeMusicApp().appContainer.userRepository,
                sessionRepository = lifeMusicApp().appContainer.sessionRepository
            )
        }
    }
}