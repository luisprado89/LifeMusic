# 🎓 Proyecto Android – LifeMusic

**Autor:** Luis Darwin Prado Cisneros  
📅 *Trabajo individual por motivos laborales (horarios imprevisibles)*  
💻 *Desarrollado en Android Studio 2025 con Jetpack Compose y Material 3*

---

## 🧩 Parte 1 – Diseño de la App

**Tema elegido:** 🎵 Aplicación musical *LifeMusic*

El proyecto busca recrear una app moderna de música donde el usuario pueda iniciar sesión, explorar canciones recomendadas, ver detalles y gestionar su perfil personal.

**Páginas diseñadas:**
- **Página de login** – Donde el usuario introduce sus credenciales de acceso.  
- **Página inicial** – Pantalla principal que se muestra tras iniciar sesión.  
- **Página de listado** – Muestra un listado de canciones scrollable.  
- **Página de detalle** – Presenta información más completa de una canción.  
- **Página de perfil** – Permite al usuario modificar su nombre o correo.

En el diseño inicial se consideró la presencia de una **toolbar (TopAppBar)** integrada en todas las pantallas mediante el componente `MainScaffold`.

---

### 📱 Ejemplos visuales del diseño

#### 🟦 Página de Login
Pantalla de inicio de sesión con campos de texto, íconos y botón de acceso.

![LoginPage](docs/login.png)

---

#### 🟩 Página Inicial (HomePage)
Pantalla principal con secciones de “Recomendadas para ti” y “Nuevos lanzamientos”.  
Se emplea `LazyRow` para mostrar canciones en tarjetas horizontales (`SongCard`).

![HomePage](docs/home.png)

---

#### 🟧 Página de Listado (ListPage)
Listado vertical de canciones usando `LazyColumn` y el componente `SongListItem`.

![ListPage](docs/list.png)

---

#### 🟥 Página de Detalle (DetailPage)
Muestra información detallada de una canción, con opciones de “Me gusta ❤️” y “Volver al listado”.

![DetailPage](docs/detail.png)

---

#### 🟨 Página de Perfil (ProfilePage)
Pantalla donde el usuario puede ver y editar su información, con campos editables y botones de acción.

![ProfilePage](docs/profile.png)

---

## 🧱 Parte 2 – Identificación de Componentes

| Componente | Archivo | Parámetros | Descripción |
|-------------|----------|------------|--------------|
| `MainScaffold` | `MainScaffold.kt` | title, isHome, onBackClick, onListClick, onProfileClick | Estructura base con barra superior adaptable (inicio/subpantalla) |
| `SongCard` | `SongCard.kt` | imageRes, title, artist, duration, isFavorite | Tarjeta visual para canciones recomendadas |
| `SongListItem` | `SongListItem.kt` | imageRes, title, artist, album, duration, isFavorite | Elemento compacto de la lista vertical |
| `SongData` | `SongData.kt` | — | Lista compartida con 20 canciones de ejemplo |
| `ProfileInfoItem` | `ProfilePage.kt` | icon, label, value | Elemento informativo del perfil del usuario |

📘 Todos los componentes fueron definidos como `@Composable` y se implementaron **previews en modo claro y oscuro**.

---

## ⚙️ Parte 3 – Implementación

Se desarrollaron los composables definidos anteriormente, siguiendo las buenas prácticas de **Material Design 3**.

- Se usaron `remember` y `mutableStateOf` para los estados interactivos.  
- `LifeMusicTheme` gestiona automáticamente el modo claro/oscuro.  
- Cada componente incluye `@Preview` doble (Light y Dark).  
- Se crearon **data classes** para las canciones (`Song`) y una lista `sampleSongs`.  

Ejemplo de lista dinámica:

```kotlin
LazyColumn {
    items(sampleSongs) { song ->
        SongListItem(
            imageRes = song.imageRes,
            title = song.title,
            artist = song.artist,
            album = song.album,
            duration = song.duration,
            isFavorite = song.isFavorite
        )
    }
}
```

---

## 🖥️ Parte 4 – Páginas Implementadas

### 🟦 LoginPage
Campos de usuario y contraseña con validación visual.  
Incluye botón “Iniciar sesión” y texto clicable para recuperación de contraseña.

### 🟩 HomePage
Secciones horizontales con `SongCard`.  
Los íconos de lista y perfil aparecen en la barra superior.

### 🟧 ListPage
Lista completa con desplazamiento vertical (`LazyColumn`) y canciones provenientes de `SongData.kt`.

### 🟥 DetailPage
Muestra información detallada de la canción seleccionada.  
Incluye botones interactivos “❤️ Me gusta” y “Volver al listado”.

### 🟨 ProfilePage
Permite ver, editar y guardar información del perfil (nombre y correo electrónico).  
Contiene botones **Guardar cambios**, **Cancelar** y **Cerrar sesión**.

---

## 🌗 Tema y Apariencia

El tema `LifeMusicTheme` adapta todos los colores, textos y superficies a **modo claro y oscuro**, cumpliendo con las pautas de **Material Design 3**.

Cada pantalla posee un `@Preview` doble para validar ambos modos.

---

## 🧩 Estructura del Proyecto

```
com.luis.lifemusic/
│
├── component/
│   ├── MainScaffold.kt
│   ├── SongCard.kt
│   └── SongListItem.kt
│
├── page/
│   ├── LoginPage.kt
│   ├── HomePage.kt
│   ├── ListPage.kt
│   ├── DetailPage.kt
│   └── ProfilePage.kt
│
├── data/
│   └── SongData.kt
│
├── ui/theme/
│   ├── Color.kt
│   ├── Theme.kt
│   ├── Type.kt
│   └── Shape.kt
│
└── MainActivity.kt
```

---

## 🚀 Conclusión

El proyecto **LifeMusic** cumple con todas las fases del enunciado académico:

✔ Diseño visual con imágenes y barra superior.  
✔ Identificación clara de componentes y parámetros.  
✔ Implementación modular y reutilización eficiente del código.  
✔ Uso correcto de `Scaffold`, `LazyColumn`, `LazyRow` y `MaterialTheme`.  
✔ Previews en modo claro y oscuro.  

✨ *LifeMusic es una aplicación base funcional, visualmente coherente y preparada para integrar navegación y persistencia en futuras versiones.*

---

# Segunda entrega del proyecto




---

## 1) Contexto de evolución del proyecto

- Commit base de referencia de la primera entrega (README inicial):
    - `ad9ef5aeb488222adfcf9a3143e4665ff21bd940` (Nov 9, 2025).
- Desde ese commit hasta el estado actual se incorporó:
    - navegación completa con `NavHost` y rutas tipadas,
    - arquitectura MVVM por pantalla,
    - persistencia local con Room + DataStore,
    - integración de API con Retrofit/OkHttp (Spotify),
    - módulo de cámara con CameraX,
    - nuevas pantallas de autenticación y cámara.

### Pantallas añadidas después del commit base

Tras `ad9ef5a...` se añadieron **3 pantallas nuevas** al flujo funcional:

1. `RegisterPage` (registro)
2. `RecoverPasswordPage` (recuperación)
3. `CameraScreen` (captura foto de perfil)

Además de las pantallas ya existentes (Login, Home, List, Detail, Profile), que fueron refactorizadas para MVVM y navegación desacoplada.

---

## 2) Enunciado de la segunda entrega (texto de requisitos)

### Proyecto

El proyecto es seleccionado por los alumnos pero deben cumplir las siguientes condiciones:

### Condiciones

#### Interfaz gráfica - 30% (Minimo 15%)
La aplicación debe contar con una interfaz de usuario implementada utilizando Jetpack Compose, con
componentes visuales bien estructurados y una experiencia de usuario intuitiva.

Requisitos específicos:
- Componentes personalizados: Crear componentes Composable reutilizables y mantenerlos en
  archivos separados organizados por funcionalidad (ej: `components/`, `ui/components/`)
- Sistema de temas: Implementar un tema personalizado con `MaterialTheme`, definiendo colores,
  tipografía y formas en `ui/theme/`
- Separación de responsabilidades:
    - Componentes de UI puros (`@Composable`) sin lógica de negocio
    - Uso de estados (`State`, `MutableState`) gestionados por ViewModel
    - Separar componentes de presentación de componentes contenedores
- Modularización: Organizar la UI en módulos lógicos:
    - `screens/`: Pantallas completas de la aplicación
    - `components/`: Componentes reutilizables
    - `theme/`: Configuración de temas (`Color.kt`, `Type.kt`, `Theme.kt`)
- Buenas prácticas:
    - Uso de previews (`@Preview`) para cada componente
    - Parametrización de componentes para máxima reutilización
    - Aplicación consistente del Material Design 3

#### ViewModel - 20% (Minimo 15%)
Implementar la arquitectura MVVM (Model-View-ViewModel) utilizando ViewModels de Android para
separar la lógica de negocio de la interfaz de usuario y gestionar el estado de la aplicación de manera
eficiente.

Requisitos específicos:
- Separación de responsabilidades:
    - Cada pantalla debe tener su propio ViewModel
    - El ViewModel no debe tener referencias a componentes de UI (Views, Context, etc.)
    - Organizar ViewModels en `viewmodels/` o dentro de cada módulo de feature
- Gestión de estado:
    - Usar `StateFlow` o `MutableStateFlow` para estados observables
    - Implementar un UIState (`data class`) por cada pantalla para encapsular el estado
    - Exponer estados como inmutables (`StateFlow`) y modificarlos solo internamente
- Gestión de eventos:
    - Implementar funciones públicas para manejar eventos de UI (ej: `onLoginClick()`,
      `onTextChange()`)
    - Usar sealed classes para eventos complejos o de un solo uso (ej: navegación, mensajes)
- Estructura organizativa:
    - `viewmodels/`: ViewModels de la aplicación
    - `uistate/`: Clases de estado de UI
    - `events/`: Eventos y acciones de usuario

#### Scaffold - 10%
Utilizar el componente Scaffold de Jetpack Compose para estructurar las pantallas de la aplicación,
incluyendo TopAppBar, BottomNavigation, FloatingActionButton u otros elementos de diseño Material.

Requisitos específicos:
- Componentes del Scaffold:
    - Implementar TopAppBar con título dinámico y acciones contextuales
    - BottomNavigation o NavigationRail para navegación principal (si aplica)
    - FloatingActionButton para acciones primarias de la pantalla
    - SnackbarHost para mensajes y notificaciones al usuario
- Estructura consistente:
    - Mantener un Scaffold base reutilizable para toda la aplicación
    - Parametrizar elementos según necesidades de cada pantalla
    - Gestionar visibilidad de elementos según contexto de navegación
- Organización:
    - Crear archivo `MainScaffold.kt` o similar para el scaffold principal
    - Componentes de barra separados en `components/appbar/`
    - Configuración de navegación inferior en `components/navigation/`
- Buenas prácticas:
    - Uso de Material Design 3 components (`TopAppBar`, `NavigationBar`)
    - Estados elevados para control de scaffold desde ViewModel
    - Adaptabilidad a diferentes tamaños de pantalla

#### Navegación - 10%
Implementar un sistema de navegación entre pantallas utilizando Navigation Component de Jetpack
Compose, definiendo rutas y gestión del backstack.

Requisitos específicos:
- Sistema de rutas:
    - Definir rutas como objetos o sealed classes (ej: `sealed class Screen(val route: String)`)
    - Usar argumentos de navegación para pasar datos entre pantallas
- Estructura NavHost:
    - Configurar NavHost con todas las pantallas de la aplicación
    - Definir start destination apropiado
    - Gestión correcta del backstack (`popUpTo`, `inclusive`, `saveState`)
- Organización:
    - `navigation/`: Configuración de navegación
    - `NavGraph.kt`: Definición del grafo de navegación
    - `Screen.kt` o `Routes.kt`: Definición de rutas
    - Separar navegación por módulos si la app es grande
- Navegación y ViewModel:
    - Pasar NavController desde composables, no almacenar en ViewModel
    - Usar eventos de un solo uso para acciones de navegación
    - ViewModels con scope apropiado (`navGraphViewModel` si se comparten)
- Buenas prácticas:
    - Type-safe navigation con argumentos tipados
    - Prevenir navegación duplicada con `launchSingleTop`
    - Restauración de estado al navegar hacia atrás

#### Retrofit - 15% (Minimo 10%)
Integrar Retrofit para realizar peticiones HTTP a una API REST, incluyendo la deserialización de datos
JSON y manejo de respuestas asíncronas.

Requisitos específicos:
- Configuración de Retrofit:
    - Crear instancia de Retrofit con base URL
    - Configurar converters (Gson, Moshi o Kotlinx Serialization)
    - Implementar interceptores para logging, autenticación, headers
    - Timeout configuration y manejo de errores de red
- Definición de API:
    - Interfaces separadas por dominio o funcionalidad
    - Uso correcto de anotaciones (`@GET`, `@POST`, `@PUT`, `@DELETE`, `@Query`, `@Path`,
      `@Body`)
    - DTOs (Data Transfer Objects) en `data/models/` o `data/dto/`
- Arquitectura de capas:
    - `data/api/`: Interfaces de servicios Retrofit
    - `data/repository/`: Repositorios que encapsulan llamadas API
    - `domain/`: Modelos de dominio separados de DTOs
    - Mappers para convertir DTOs a modelos de dominio
- Manejo de respuestas:
    - Implementar sealed classes para estados (Loading, Success, Error)
    - Try-catch en repositorios para manejar excepciones
    - Transformar errores HTTP a mensajes user-friendly
- Buenas prácticas:
    - Inyección de dependencias para servicios API
    - Uso de suspend functions y Flow para llamadas asíncronas

#### Cámara - 5%
Incorporar funcionalidad de cámara utilizando CameraX o la API de cámara de Android para capturar
fotos o videos dentro de la aplicación.

Requisitos específicos:
- Implementación CameraX:
    - Configurar Preview, ImageCapture y/o VideoCapture use cases
    - Manejo de lifecycle para iniciar/detener cámara correctamente
    - Configuración de selector de cámara (frontal/trasera)
    - Controles de flash, zoom y enfoque (opcional)
- Permisos y seguridad:
    - Solicitud de permisos CAMERA en runtime
    - Manejo de denegación de permisos con UI explicativa
    - Verificación de disponibilidad de cámara en el dispositivo
- Captura y almacenamiento:
    - Guardar imágenes en almacenamiento interno o external storage
    - Nombrado de archivos con timestamps o identificadores únicos
    - Generación de URIs para compartir o mostrar imágenes
    - Compresión de imágenes si es necesario
- Organización:
    - `camera/`: Componentes relacionados con cámara
    - `CameraScreen.kt`: Pantalla de captura
    - `CameraViewModel.kt`: Lógica de cámara
    - `utils/CameraUtils.kt`: Utilidades para permisos y configuración
- Buenas prácticas:
    - Preview en tiempo real antes de capturar
    - Feedback visual al capturar (animación, sonido)
    - Manejo de errores de cámara con mensajes claros
    - Liberación de recursos al salir de la pantalla

#### DataStore o BBDD - 10%
Implementar persistencia de datos local mediante DataStore para datos simples o Room Database
para estructuras de datos más complejas que requieran almacenamiento estructurado.

Requisitos específicos:

##### Opción A: DataStore
- Uso apropiado:
    - Configuraciones de usuario, preferencias, flags booleanos
    - Datos simples clave-valor (String, Int, Boolean, Float)
    - Token de sesión, último usuario logueado
- Implementación:
    - Usar DataStore Preferences (recomendado para preferencias)
    - Crear una clase wrapper o repository para acceso centralizado
    - Operaciones asíncronas con Flow/suspend functions
- Organización:
    - `data/datastore/`: Clases de DataStore
    - `data/repository/PreferencesRepository.kt`: Repositorio de preferencias

##### Opción B: Room Database
- Configuración de Room:
    - Definir entities con anotaciones (`@Entity`, `@PrimaryKey`, `@ColumnInfo`)
    - DAOs (Data Access Objects) con queries (`@Query`, `@Insert`, `@Update`, `@Delete`)
    - Database class abstracta con `@Database`
    - Migrations para cambios de esquema
- Estructura de datos:
    - Relaciones entre entidades (`@Relation`, `@Embedded`)
    - Indices para mejorar rendimiento (`@Index`)
    - TypeConverters para tipos complejos (Date, Lists, objetos)
- Organización:
    - `data/database/`: Configuración de Room
    - `entities/`: Entidades de la base de datos
    - `dao/`: Data Access Objects
    - `AppDatabase.kt`: Clase de base de datos principal
    - `data/repository/`: Repositorios que usan DAOs
- Buenas prácticas:
    - Operaciones de DB en background (suspend functions, Flow)
    - Inyección de dependencias para Database y DAOs
    - Testing con in-memory database
    - Uso de Flow para observar cambios reactivamente
    - Separación entre entidades de Room y modelos de dominio

---

## 3) Cumplimiento del enunciado en el estado actual del repositorio

> Estado: **cumplimiento alto a nivel funcional y arquitectónico para entrega académica**, con mejoras pendientes en seguridad, tests y formalización de capas de dominio.

### 3.1 Interfaz gráfica (Compose + Material 3)

**Cumple ampliamente**:
- UI en Jetpack Compose.
- Componentes reutilizables separados (`MainScaffold`, `SongCard`, `SongListItem`).
- Tema Material 3 en `ui/theme/`.
- Previews en pantallas/componentes clave.
- Diferenciación entre páginas UI (`page/*`) y contenedores (`ui/*Route`).

### 3.2 ViewModel (MVVM)

**Cumple**:
- Un ViewModel por pantalla/feature principal (`Login`, `Register`, `Recover`, `Home`, `List`, `Detail`, `Profile`, `Camera`).
- Estados con `StateFlow/MutableStateFlow` y `UiState` por pantalla.
- Eventos públicos (`onEmailChange`, `tryLogin`, `toggleFavorite`, etc.).
- ViewModel sin referencias directas a composables.

**Observación**:
- El proyecto usa estructura por feature (`ui/<feature>/`) en lugar de carpetas globales `viewmodels/`, `uistate/`, `events/`.
    - Esto **no incumple**; es una variante de organización modular aceptada.

### 3.3 Scaffold

**Cumple**:
- Scaffold base reutilizable (`MainScaffold.kt`).
- TopAppBar dinámico por pantalla.
- Parametrización de acciones contextuales.
- Uso de `SnackbarHost` en pantalla de cámara.

**Parcial/no aplicable explícito**:
- No hay `BottomNavigation` global fija en toda la app (la navegación principal usa acciones en barra superior y rutas dedicadas).

### 3.4 Navegación

**Cumple**:
- `AppNavHost` centraliza rutas.
- `startDestination` definido (`login`).
- `popUpTo`, `inclusive`, `launchSingleTop` para backstack y evitar duplicados.
- Argumento tipado en detalle (`spotifyId: String`).
- `NavController` no se guarda en ViewModel.

### 3.5 Retrofit

**Cumple**:
- Cliente Retrofit con base URL de Spotify.
- Gson converter.
- Interceptores OkHttp (auth bearer + logging).
- Repositorio de red (`SpotifyRepository`) con `suspend` y manejo de excepciones.
- DTOs separados y mapeo a modelo usado en UI.

**Pendientes recomendados**:
- Endurecer gestión de secretos (credenciales en código).
- Añadir capa de resultado tipado (Loading/Success/Error) más uniforme.

### 3.6 Cámara (CameraX)

**Cumple**:
- `CameraScreen` + `CameraViewModel` + `CameraUtils`.
- Preview, captura y guardado en `MediaStore`.
- Permisos runtime y manejo de denegación.
- Selector frontal/trasera, soporte foco táctil, zoom, torch.

### 3.7 DataStore / BBDD

**Cumple muy bien (ambas opciones)**:
- **DataStore** para sesión (userId activo).
- **Room** para usuarios y favoritos con DAOs, entidades e índices.
- Repositorios para desacoplar acceso a datos.
- Flujo reactivo con `Flow`.

---

## 4) Descripción por pantalla (Page/Screen) + navegación actual

> Sección pensada para memoria de entrega. Incluye hueco para imagen por pantalla.

### 4.1 LoginPage (`route = "login"`)

**Qué hace**:
- Entrada principal de autenticación por correo y contraseña.
- Acciones a registro y recuperación.

**Cómo navega**:
- Login correcto → `home` (con `popUpTo(login) { inclusive = true }` y `launchSingleTop`).
- Enlace "Regístrate" → `register`.
- Enlace "¿Olvidaste tu contraseña?" → `recover`.

**Espacio para imagen**:

![Login - segunda entrega](docs/pendiente_login_segunda_entrega.png)

---

### 4.2 RegisterPage (`route = "register"`) **[añadida tras commit base]**

**Qué hace**:
- Registro de nuevo usuario con datos obligatorios (incluida fecha de nacimiento y pregunta/respuesta de seguridad).

**Cómo navega**:
- Back → vuelve al `login`.
- Registro exitoso → `home` limpiando backstack de login.

**Espacio para imagen**:

![Register - segunda entrega](docs/pendiente_register_segunda_entrega.png)

---

### 4.3 RecoverPasswordPage (`route = "recover"`) **[añadida tras commit base]**

**Qué hace**:
- Flujo de recuperación en pasos:
    1) buscar cuenta por email,
    2) validar respuesta de seguridad,
    3) definir nueva contraseña.

**Cómo navega**:
- Back → vuelve a la pantalla anterior (normalmente `login`).

**Espacio para imagen**:

![Recover - segunda entrega](docs/pendiente_recover_segunda_entrega.png)

---

### 4.4 HomePage (`route = "home"`)

**Qué hace**:
- Pantalla principal con secciones de descubrimiento musical.
- Combina catálogo local + remoto (Spotify) con fallback offline.

**Cómo navega**:
- Botón/listado → `list`.
- Perfil → `profile`.
- Click canción → `detail/{spotifyId}`.
- Si no hay sesión activa, redirige a `login` limpiando stack.

**Espacio para imagen**:

![Home - segunda entrega](docs/pendiente_home_segunda_entrega.png)

---

### 4.5 ListPage (`route = "list"`)

**Qué hace**:
- Muestra favoritos del usuario.
- Hidrata canciones por IDs, priorizando local y resolviendo remoto cuando aplica.

**Cómo navega**:
- Back → regresa a `home`.
- Click canción → `detail/{spotifyId}`.
- Sesión inválida → `login`.

**Espacio para imagen**:

![List - segunda entrega](docs/pendiente_list_segunda_entrega.png)

---

### 4.6 DetailPage (`route = "detail/{spotifyId}"`)

**Qué hace**:
- Muestra detalle de canción por `spotifyId`.
- Prioriza datos locales y enriquece con remoto cuando disponible.
- Permite alternar favorito.

**Cómo navega**:
- Back → vuelve a pantalla anterior (`home` o `list`).
- Sesión inválida → `login`.

**Espacio para imagen**:

![Detail - segunda entrega](docs/pendiente_detail_segunda_entrega.png)

---

### 4.7 ProfilePage (`route = "profile"`)

**Qué hace**:
- Visualización/edición de datos de usuario.
- Guarda cambios de nombre/correo.
- Gestiona cierre de sesión.
- Acceso a cámara para foto de perfil.

**Cómo navega**:
- Back → vuelve a `home`.
- Abrir cámara → `camera`.
- Logout/sesión inválida → `login`.

**Espacio para imagen**:

![Profile - segunda entrega](docs/pendiente_profile_segunda_entrega.png)

---

### 4.8 CameraScreen (`route = "camera"`) **[añadida tras commit base]**

**Qué hace**:
- Preview de cámara en vivo.
- Captura y guardado de foto en MediaStore.
- Devuelve URI para actualizar foto en perfil.

**Cómo navega**:
- Back → regresa a `profile`.
- Tras captura correcta → regresa a `profile` y persiste foto.

**Espacio para imagen**:

![Camera - segunda entrega](docs/pendiente_camera_segunda_entrega.png)

---

## 5) Flujo de navegación global (resumen)

```text
login
 ├── register
 ├── recover
 └── home
      ├── list
      │    └── detail/{spotifyId}
      ├── detail/{spotifyId}
      └── profile
           └── camera
```

Reglas de sesión:
- Si expira o no existe sesión activa, se fuerza navegación a `login` limpiando backstack principal.

---

## 6) API usada en la segunda entrega

### API externa

- **Spotify Web API**
- Autenticación: Client Credentials
- Endpoints usados:
    - `GET /search` (búsqueda de tracks)
    - `GET /tracks/{id}` (detalle de track)

### Evidencias que conviene incluir en la memoria

1. Captura de respuesta JSON de `/search`.
2. Captura de respuesta JSON de `/tracks/{id}`.
3. Captura en Logcat/inspector mostrando request con Bearer token.
4. Captura de pantalla de Home mostrando datos cargados desde API.

### Espacios para imágenes de la API

![API search response](docs/pendiente_api_search_response.png)

![API track detail response](docs/pendiente_api_track_response.png)

![API request logcat/interceptor](docs/pendiente_api_logcat.png)

---

## 7) Estado final del repositorio para la entrega

### Síntesis

El proyecto, respecto a la primera entrega, evolucionó de una base centrada en UI estática a una aplicación con:

- arquitectura MVVM operativa,
- navegación completa y controlada por rutas,
- persistencia híbrida (DataStore + Room),
- consumo real de API REST con Retrofit,
- módulo de cámara integrado,
- fallback offline para robustez funcional.

### Valoración de cumplimiento académico

- **Interfaz**: Alto.
- **ViewModel/MVVM**: Alto.
- **Scaffold**: Medio-Alto.
- **Navegación**: Alto.
- **Retrofit**: Alto.
- **Cámara**: Alto.
- **DataStore/BBDD**: Alto.
