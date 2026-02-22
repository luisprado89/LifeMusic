package com.luis.lifemusic.camera

import android.net.Uri
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
/**
 * ViewModel encargado de gestionar el estado de la pantalla de cámara.
 */
data class CameraUiState(
    val isCapturing: Boolean = false,
    val lastCapturedUri: Uri? = null,
    val errorMessage: String? = null
)

class CameraViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(CameraUiState())
    val uiState: StateFlow<CameraUiState> = _uiState

    // Indica que comienza la captura
    fun onCaptureStart() {
        _uiState.update { it.copy(isCapturing = true, errorMessage = null) }
    }

    // Guarda el URI cuando la captura es exitosa
    fun onCaptureSuccess(uri: Uri) {
        _uiState.update { it.copy(isCapturing = false, lastCapturedUri = uri) }
    }

    // Muestra error en caso de fallo
    fun onCaptureError(message: String) {
        _uiState.update { it.copy(isCapturing = false, errorMessage = message) }
    }

    fun clearError() {
        _uiState.update { it.copy(errorMessage = null) }
    }
}