package com.luis.lifemusic.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.luis.lifemusic.data.localsed.LocalSeedSong
import com.luis.lifemusic.data.localsed.localSeedSongs
import com.luis.lifemusic.data.repository.FavoritesRepository
import com.luis.lifemusic.data.repository.SessionRepository
import com.luis.lifemusic.data.repository.SpotifyRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/**
 * ============================================================
 * HOME VIEWMODEL
 * ============================================================
 *
 * 🎯 RESPONSABILIDAD:
 * - Orquestar la carga de contenido para Home:
 *   - Discovery songs (local + Spotify) con fallback offline.
 *   - Secciones: Recomendadas / Nuevos lanzamientos / Más populares.
 * - Observar sesión activa (DataStore).
 * - Observar favoritos del usuario (Room).
 * - Exponer un HomeUiState inmutable para la UI.
 *
 * ✅ REGLAS DE ARQUITECTURA (MVVM):
 * - UI no tiene lógica de negocio.
 * - ViewModel usa repositorios.
 * - Repository decide la estrategia online/offline.
 *
 * ✅ OPTIMIZACIÓN:
 * - Carga catálogo remoto/local solo UNA vez por sesión activa.
 * - Si cambian favoritos, recalcula secciones sin volver a llamar a la red.
 */
@OptIn(ExperimentalCoroutinesApi::class)
class HomeViewModel(
    private val sessionRepository: SessionRepository,
    private val favoritesRepository: FavoritesRepository,
    private val spotifyRepository: SpotifyRepository
) : ViewModel() {

    companion object {
        /**
         * Spotify Search (q) suele tener límites prácticos de longitud.
         * Para ir seguros, limitamos a 144 chars.
         */
        private const val MAX_SPOTIFY_QUERY_LENGTH = 144

        /**
         * Si no tenemos artistas suficientes, usamos una query genérica.
         */
        private const val QUERY_FALLBACK = "music"
    }

    private val _uiState = MutableStateFlow(HomeUiState(isLoading = true))
    val uiState: StateFlow<HomeUiState> = _uiState

    /**
     * Cache por sesión: evitamos pedir Spotify cada vez que cambian favoritos,
     * pero recalculamos secciones con el catálogo ya cargado.
     */
    private var loadedUserId: Long? = null
    private var catalogSongs: List<LocalSeedSong> = emptyList()
    private var usedOfflineFallbackLastLoad: Boolean = false

    init {
        observeHomeContent()
    }

    private fun observeHomeContent() {
        viewModelScope.launch {
            sessionRepository.sessionUserId
                .flatMapLatest { userId ->
                    if (userId == null) {
                        flowOf(null to emptySet<String>())
                    } else {
                        favoritesRepository.observeFavoriteSongIds(userId)
                            .map { ids -> userId to ids.toSet() }
                    }
                }
                .collect { (userId, favoriteIds) ->

                    // ------------------------------------------------------------
                    // 1) Sin sesión -> estado vacío + mensaje
                    // ------------------------------------------------------------
                    if (userId == null) {
                        loadedUserId = null
                        catalogSongs = emptyList()
                        usedOfflineFallbackLastLoad = false

                        _uiState.update {
                            it.copy(
                                isLoading = false,
                                hasActiveSession = false,
                                recommendedSongs = emptyList(),
                                newReleaseSongs = emptyList(),
                                popularSongs = emptyList(),
                                errorMessage = "Inicia sesión para descubrir música",
                                offlineNoticeMessage = null,
                                recommendedInfoMessage = null
                            )
                        }
                        return@collect
                    }

                    // ------------------------------------------------------------
                    // 2) Con sesión -> cargando
                    // ------------------------------------------------------------
                    _uiState.update {
                        it.copy(
                            hasActiveSession = true,
                            isLoading = true,
                            errorMessage = null,
                            offlineNoticeMessage = null
                        )
                    }

                    // ------------------------------------------------------------
                    // 3) Carga remota/local solo una vez por sesión
                    //    (o si catalogSongs está vacío)
                    // ------------------------------------------------------------
                    if (loadedUserId != userId || catalogSongs.isEmpty()) {

                        // Query basada en artistas (sin género)
                        val query = buildSpotifySearchQuery(favoriteIds)

                        val discovery = spotifyRepository.getDiscoverySongs(query)

                        catalogSongs = discovery.songs
                        usedOfflineFallbackLastLoad = discovery.usedOfflineFallback
                        loadedUserId = userId
                    }

                    // ------------------------------------------------------------
                    // 4) Recalcular secciones (cada vez que cambian favoritos)
                    // ------------------------------------------------------------
                    val sections = calculateSections(catalogSongs, favoriteIds)

                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            recommendedSongs = sections.recommendedSongs,
                            newReleaseSongs = sections.newReleaseSongs,
                            popularSongs = sections.popularSongs,
                            recommendedInfoMessage = sections.recommendedInfoMessage,
                            errorMessage = null,
                            offlineNoticeMessage = if (usedOfflineFallbackLastLoad) {
                                "Hay más canciones disponibles online, pero no se pueden mostrar sin conexión."
                            } else {
                                null
                            }
                        )
                    }
                }
        }
    }

    /**
     * ============================================================
     * Construcción de query Spotify SIN géneros:
     * - Basada en los nombres de artistas de favoritos.
     * - Importante: funciona tanto con favoritos locales como con favoritos online,
     *   porque usa `catalogSongs` (local + Spotify) cuando esté disponible.
     *
     * Formato recomendado:
     *   artist:"Queen" OR artist:"Adele"
     * ============================================================
     */
    private fun buildSpotifySearchQuery(favoriteIds: Set<String>): String {
        // Si ya tenemos catálogo cargado, úsalo (incluye canciones online)
        // Si no, usa localSeedSongs para no quedarte sin base en primer arranque
        val baseCatalog = if (catalogSongs.isNotEmpty()) catalogSongs else localSeedSongs

        val favoriteArtists = baseCatalog
            .filter { it.spotifyId in favoriteIds }
            .flatMap { it.artists }
            .map { it.trim() }
            .filter { it.isNotEmpty() }
            .distinct()
            .take(3)

        // Convertimos a tokens artist:"..."
        val artistTokens = favoriteArtists.map { artist ->
            val sanitized = artist.replace('"', ' ').trim()
            "artist:\"$sanitized\""
        }

        // Construimos query sin pasarnos de longitud
        val query = buildString {
            artistTokens.forEach { token ->
                val separator = if (isEmpty()) "" else " OR "
                val candidate = this.toString() + separator + token
                if (candidate.length <= MAX_SPOTIFY_QUERY_LENGTH) {
                    append(separator)
                    append(token)
                }
            }
        }

        return if (query.length < 5) QUERY_FALLBACK else query
    }

    /**
     * Resultado calculado para Home
     */
    private data class ComputedSections(
        val recommendedSongs: List<LocalSeedSong>,
        val newReleaseSongs: List<LocalSeedSong>,
        val popularSongs: List<LocalSeedSong>,
        val recommendedInfoMessage: String?
    )

    /**
     * ============================================================
     * Secciones:
     * - recommendedSongs: canciones de artistas parecidos a favoritos
     * - newReleaseSongs: por releaseDate desc
     * - popularSongs: por popularidad desc
     *
     * Además, evitamos mostrar canciones que ya son favoritas en estas secciones.
     * ============================================================
     */
    private fun calculateSections(
        allSongs: List<LocalSeedSong>,
        favoriteIds: Set<String>
    ): ComputedSections {

        val deduped = allSongs.distinctBy { it.spotifyId }

        // No queremos sugerir como descubrimiento lo ya favorito
        val discoverableSongs = deduped.filter { it.spotifyId !in favoriteIds }
        val favoriteSongs = deduped.filter { it.spotifyId in favoriteIds }

        val favoriteArtistIds = favoriteSongs.flatMap { it.artistIds }.toSet()

        val recommended = if (favoriteArtistIds.isEmpty()) {
            emptyList()
        } else {
            discoverableSongs
                .filter { song -> song.artistIds.any { it in favoriteArtistIds } }
                .take(12)
        }

        return ComputedSections(
            recommendedSongs = recommended,
            newReleaseSongs = discoverableSongs.sortedByDescending { it.releaseDate }.take(12),
            popularSongs = discoverableSongs.sortedByDescending { it.popularity }.take(12),
            recommendedInfoMessage = if (favoriteArtistIds.isEmpty()) {
                "No tenemos recomendaciones porque aún no tienes canciones en tu lista de favoritos."
            } else {
                null
            }
        )
    }

    /**
     * Añadir favorito para el usuario logueado.
     */
    fun addFavorite(songSpotifyId: String) {
        viewModelScope.launch {
            val userId = loadedUserId ?: return@launch
            favoritesRepository.addFavorite(userId, songSpotifyId)
        }
    }

    /**
     * Fuerza refresco:
     * - vacía cache
     * - vuelve a pedir discovery al repositorio con query de artistas
     * - recalcula secciones
     */
    fun refreshContent() {
        viewModelScope.launch {
            val userId = sessionRepository.sessionUserId.first() ?: return@launch

            loadedUserId = null
            catalogSongs = emptyList()

            _uiState.update { it.copy(isLoading = true) }

            val favoriteIds = favoritesRepository.observeFavoriteSongIds(userId).first().toSet()

            val query = buildSpotifySearchQuery(favoriteIds)
            val discovery = spotifyRepository.getDiscoverySongs(query)

            catalogSongs = discovery.songs
            usedOfflineFallbackLastLoad = discovery.usedOfflineFallback
            loadedUserId = userId

            val sections = calculateSections(catalogSongs, favoriteIds)

            _uiState.update {
                it.copy(
                    isLoading = false,
                    recommendedSongs = sections.recommendedSongs,
                    newReleaseSongs = sections.newReleaseSongs,
                    popularSongs = sections.popularSongs,
                    recommendedInfoMessage = sections.recommendedInfoMessage,
                    errorMessage = null,
                    offlineNoticeMessage = if (usedOfflineFallbackLastLoad) {
                        "Hay más canciones disponibles online, pero no se pueden mostrar sin conexión."
                    } else {
                        null
                    }
                )
            }
        }
    }
}