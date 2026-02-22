package com.luis.lifemusic.ui

import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.luis.lifemusic.LifeMusicApplication
import com.luis.lifemusic.data.repository.SpotifyRepository
import com.luis.lifemusic.ui.detail.DetailViewModel
import com.luis.lifemusic.ui.home.HomeViewModel
import com.luis.lifemusic.ui.list.ListViewModel
import com.luis.lifemusic.ui.login.LoginViewModel
import com.luis.lifemusic.ui.profile.ProfileViewModel
import com.luis.lifemusic.ui.recover.RecoverViewModel
import com.luis.lifemusic.ui.register.RegisterViewModel

fun CreationExtras.lifeMusicApp(): LifeMusicApplication =
    (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as LifeMusicApplication)

object AppViewModelProvider {

    val Factory = viewModelFactory {

        // AUTH
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

        // MAIN
        initializer {
            HomeViewModel(
                sessionRepository = lifeMusicApp().appContainer.sessionRepository,
                favoritesRepository = lifeMusicApp().appContainer.favoritesRepository,
                spotifyRepository = SpotifyRepository() // Usamos el nuevo repositorio
            )
        }

        initializer {
            ListViewModel(
                sessionRepository = lifeMusicApp().appContainer.sessionRepository,
                favoritesRepository = lifeMusicApp().appContainer.favoritesRepository
            )
        }

        initializer {
            DetailViewModel(
                savedStateHandle = this.createSavedStateHandle(),
                sessionRepository = lifeMusicApp().appContainer.sessionRepository,
                favoritesRepository = lifeMusicApp().appContainer.favoritesRepository
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
