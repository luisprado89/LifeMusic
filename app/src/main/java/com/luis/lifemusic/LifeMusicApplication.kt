package com.luis.lifemusic

import android.app.Application
import com.luis.lifemusic.data.AppContainer

/**
 * Application de LifeMusic.
 *
 * ✅ Aquí vive el AppContainer para toda la app.
 * - Se crea UNA vez al arrancar la aplicación.
 * - Así evitamos crear Room/DataStore en composables o activities.
 *
 * ⚠️ Recuerda:
 * - Debes declarar esta clase en el AndroidManifest.xml:
 *   android:name=".LifeMusicApplication"
 */
class LifeMusicApplication : Application() {

    /** Contenedor global de dependencias (Room/DataStore/Retrofit...) */
    lateinit var appContainer: AppContainer
        private set

    override fun onCreate() {
        super.onCreate()

        // applicationContext = contexto seguro para dependencias globales
        appContainer = AppContainer(applicationContext)
    }
}
