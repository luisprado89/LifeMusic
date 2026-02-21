plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)

    // KSP necesario para que Room genere las implementaciones de DAOs y Database
    id("com.google.devtools.ksp")

}

android {
    namespace = "com.luis.lifemusic"
    compileSdk {
        version = release(36)
    }

    defaultConfig {
        applicationId = "com.luis.lifemusic"
        minSdk = 26
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        compose = true
    }
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.compose.material.icons.extended)
    implementation(libs.androidx.navigation.runtime.ktx)
    implementation(libs.androidx.navigation.compose)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)
    debugImplementation(libs.androidx.compose.ui.tooling)
    debugImplementation(libs.androidx.compose.ui.test.manifest)

    // ----------------------------
    // Room Database (persistencia local)
    // ----------------------------
    implementation(libs.androidx.room.runtime)
    implementation(libs.androidx.room.ktx)
    ksp(libs.androidx.room.compiler)

    // ------------------------------------
    // DataStore (persistencia ligera)
    // ------------------------------------
    // Se usará para guardar:
    // - userId de sesión activa
    // - posibles preferencias simples (ej: tema oscuro)
    // No requiere KSP ni procesador de anotaciones.
    implementation(libs.androidx.datastore.preferences)


// ============================================================
// DEPENDENCIAS DE RED (RETROFIT + OKHTTP)
// ============================================================

// 🎯 RETROFIT: Cliente HTTP para comunicarnos con Spotify API
// - Convierte automáticamente JSON a objetos Kotlin
// - Maneja peticiones asíncronas con corrutinas (suspend functions)
    implementation(libs.retrofit)

// 🎯 CONVERTER GSON: Traduce JSON ↔ objetos Kotlin
// - Usamos Gson porque es simple y Spotify devuelve JSON complejo
// - Alternativa: kotlinx.serialization (más moderno pero más complejo)
    implementation(libs.converter.gson)

// 🎯 OKHTTP: Cliente HTTP subyacente que Retrofit usa
// - Necesario para añadir INTERCEPTORES (logs, autenticación)
    implementation(libs.okhttp)

// 🎯 LOGGING INTERCEPTOR: VER TODAS LAS LLAMADAS API EN LOGCAT
// - IMPRESCINDIBLE en desarrollo para depurar
// - Muestra URL, headers, cuerpo de petición y respuesta
    implementation(libs.logging.interceptor)
}