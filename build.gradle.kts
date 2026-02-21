// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.kotlin.compose) apply false

// Plugin KSP (Kotlin Symbol Processing)
// Necesario para el procesamiento de anotaciones de Room (@Entity, @Dao, etc.)
// Genera el código en tiempo de compilación.
    id("com.google.devtools.ksp") version "2.3.2" apply false

}