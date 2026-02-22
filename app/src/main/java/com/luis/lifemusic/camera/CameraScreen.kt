package com.luis.lifemusic.camera

import android.content.ContentValues
import android.content.Context
import android.media.AudioManager
import android.media.ToneGenerator
import android.provider.MediaStore
import androidx.camera.core.Camera
import androidx.camera.core.CameraSelector
import androidx.camera.core.FocusMeteringAction
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionStatus
import com.google.accompanist.permissions.rememberPermissionState
import com.luis.lifemusic.utils.CameraUtils
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.concurrent.TimeUnit

/**
 * Pantalla de cámara (CameraX) para sacar foto y devolver el URI.
 */
@OptIn(ExperimentalPermissionsApi::class, ExperimentalMaterial3Api::class)
@Composable
fun CameraScreen(
    onBackClick: () -> Unit,
    onPhotoCaptured: (String) -> Unit,
    viewModel: CameraViewModel = viewModel()
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val uiState by viewModel.uiState.collectAsState()

    val snackbarHostState = remember { SnackbarHostState() }
    val cameraPermission = rememberPermissionState(android.Manifest.permission.CAMERA)

    // 0 = trasera | 1 = frontal
    var lensFacing by rememberSaveable { mutableStateOf(0) }

    // Flash continuo (torch)
    var torchEnabled by rememberSaveable { mutableStateOf(false) }

    // Zoom
    var zoomRatio by rememberSaveable { mutableFloatStateOf(1f) }
    var maxZoomRatio by remember { mutableFloatStateOf(1f) }

    // Referencia a la cámara ya “binded”
    var boundCamera by remember { mutableStateOf<Camera?>(null) }

    // Si no hay hardware de cámara, mostramos mensaje
    if (!CameraUtils.hasAnyCameraHardware(context)) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("Cámara") },
                    navigationIcon = {
                        IconButton(onClick = onBackClick) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "Volver"
                            )
                        }
                    }
                )
            }
        ) { padding ->
            Box(
                modifier = Modifier.fillMaxSize().padding(padding),
                contentAlignment = Alignment.Center
            ) {
                Text("Este dispositivo no tiene cámara disponible.")
            }
        }
        return
    }

    // Errores -> Snackbar
    LaunchedEffect(uiState.errorMessage) {
        uiState.errorMessage?.let {
            snackbarHostState.showSnackbar(it)
            viewModel.clearError()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Cámara") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Volver"
                        )
                    }
                }
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { padding ->

        when (val status = cameraPermission.status) {

            is PermissionStatus.Granted -> {
                CameraContent(
                    modifier = Modifier.fillMaxSize().padding(padding),
                    context = context,
                    lifecycleOwner = lifecycleOwner,
                    lensFacing = lensFacing,

                    // Torch
                    torchEnabled = torchEnabled,
                    onTorchChange = { enabled ->
                        torchEnabled = enabled
                        boundCamera?.cameraControl?.enableTorch(enabled)
                    },

                    // Zoom
                    zoomRatio = zoomRatio,
                    maxZoomRatio = maxZoomRatio,
                    onZoomChange = { newRatio ->
                        zoomRatio = newRatio
                        boundCamera?.cameraControl?.setZoomRatio(newRatio)
                    },

                    isCapturing = uiState.isCapturing,

                    // Cambiar cámara
                    onSwitchCamera = {
                        lensFacing = if (lensFacing == 0) 1 else 0
                        torchEnabled = false
                        zoomRatio = 1f
                    },

                    // Guardamos referencia a la cámara y aplicamos límites
                    onCameraBound = { camera ->
                        boundCamera = camera
                        val max = camera.cameraInfo.zoomState.value?.maxZoomRatio ?: 1f
                        maxZoomRatio = max
                        camera.cameraControl.enableTorch(torchEnabled)
                        camera.cameraControl.setZoomRatio(zoomRatio.coerceIn(1f, maxZoomRatio))
                    },

                    // Tap-to-focus usando cameraControl real
                    onTapToFocus = { x, y, previewView ->
                        val camera = boundCamera ?: return@CameraContent
                        val point = previewView.meteringPointFactory.createPoint(x, y)
                        val action = FocusMeteringAction.Builder(point)
                            .setAutoCancelDuration(3, TimeUnit.SECONDS)
                            .build()
                        camera.cameraControl.startFocusAndMetering(action)
                    },

                    // Captura
                    onCapture = { imageCapture ->
                        capturePhotoToMediaStore(
                            context = context,
                            imageCapture = imageCapture,
                            onStart = viewModel::onCaptureStart,
                            onSuccess = { uriString ->
                                playCaptureSound()
                                viewModel.onCaptureSuccess(android.net.Uri.parse(uriString))
                                onPhotoCaptured(uriString)
                            },
                            onError = viewModel::onCaptureError
                        )
                    }
                )
            }

            is PermissionStatus.Denied -> {
                val permanentlyDenied = !status.shouldShowRationale
                PermissionDeniedContent(
                    modifier = Modifier.fillMaxSize().padding(padding),
                    permanentlyDenied = permanentlyDenied,
                    onRequestPermission = { cameraPermission.launchPermissionRequest() }
                )
            }
        }
    }
}

@Composable
private fun PermissionDeniedContent(
    modifier: Modifier,
    permanentlyDenied: Boolean,
    onRequestPermission: () -> Unit
) {
    Box(modifier, contentAlignment = Alignment.Center) {
        Column(
            modifier = Modifier.padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = if (permanentlyDenied)
                    "Permiso denegado. Actívalo en Ajustes para usar la cámara."
                else
                    "Necesitamos permiso de cámara para capturar fotos.",
                style = MaterialTheme.typography.bodyLarge
            )
            Button(onClick = onRequestPermission) {
                Text("Conceder permiso")
            }
        }
    }
}

@Composable
private fun CameraContent(
    modifier: Modifier,
    context: Context,
    lifecycleOwner: LifecycleOwner,
    lensFacing: Int,

    torchEnabled: Boolean,
    onTorchChange: (Boolean) -> Unit,

    zoomRatio: Float,
    maxZoomRatio: Float,
    onZoomChange: (Float) -> Unit,

    isCapturing: Boolean,
    onSwitchCamera: () -> Unit,
    onCameraBound: (Camera) -> Unit,

    onTapToFocus: (x: Float, y: Float, previewView: PreviewView) -> Unit,
    onCapture: (ImageCapture) -> Unit
) {
    val imageCapture = remember {
        ImageCapture.Builder()
            .setCaptureMode(ImageCapture.CAPTURE_MODE_MINIMIZE_LATENCY)
            .build()
    }

    val previewView = remember { PreviewView(context) }

    // Re-bindea la cámara cuando cambia lensFacing
    DisposableEffect(lensFacing) {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(context)
        val executor = ContextCompat.getMainExecutor(context)

        val runnable = Runnable {
            val cameraProvider = cameraProviderFuture.get()

            val selector = if (lensFacing == 0)
                CameraSelector.DEFAULT_BACK_CAMERA
            else
                CameraSelector.DEFAULT_FRONT_CAMERA

            val preview = Preview.Builder().build().also {
                it.setSurfaceProvider(previewView.surfaceProvider)
            }

            try {
                cameraProvider.unbindAll()
                val camera = cameraProvider.bindToLifecycle(
                    lifecycleOwner,
                    selector,
                    preview,
                    imageCapture
                )
                onCameraBound(camera)
            } catch (_: Exception) {
                // Si falla el bind, evitamos crash
            }
        }

        cameraProviderFuture.addListener(runnable, executor)

        onDispose {
            runCatching { cameraProviderFuture.get().unbindAll() }
        }
    }

    Box(modifier) {

        // Preview + tap-to-focus
        AndroidView(
            factory = { previewView },
            modifier = Modifier
                .fillMaxSize()
                .pointerInput(Unit) {
                    detectTapGestures { offset ->
                        onTapToFocus(offset.x, offset.y, previewView)
                    }
                }
        )

        // Controles arriba
        Row(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(12.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Button(
                onClick = onSwitchCamera,
                enabled = !isCapturing
            ) {
                Text(if (lensFacing == 0) "Frontal" else "Trasera")
            }

            Button(
                onClick = { onTorchChange(!torchEnabled) },
                enabled = !isCapturing && lensFacing == 0
            ) {
                Text(if (torchEnabled) "Flash ON" else "Flash OFF")
            }
        }

        // Controles abajo (zoom + capturar)
        Column(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (maxZoomRatio > 1f) {
                Text("Zoom: ${String.format(Locale.getDefault(), "%.1f", zoomRatio)}x")
                Slider(
                    value = zoomRatio.coerceIn(1f, maxZoomRatio),
                    onValueChange = { onZoomChange(it.coerceIn(1f, maxZoomRatio)) },
                    valueRange = 1f..maxZoomRatio
                )
            }

            if (isCapturing) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    CircularProgressIndicator()
                    Text("Capturando...", modifier = Modifier.padding(start = 12.dp))
                }
            }

            Button(
                onClick = { onCapture(imageCapture) },
                enabled = !isCapturing
            ) {
                Text("Capturar foto")
            }
        }
    }
}

private fun capturePhotoToMediaStore(
    context: Context,
    imageCapture: ImageCapture,
    onStart: () -> Unit,
    onSuccess: (String) -> Unit,
    onError: (String) -> Unit
) {
    onStart()

    val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
    val fileName = "LifeMusic_$timeStamp.jpg"

    val contentValues = ContentValues().apply {
        put(MediaStore.MediaColumns.DISPLAY_NAME, fileName)
        put(MediaStore.MediaColumns.MIME_TYPE, "image/jpeg")
        put(MediaStore.Images.Media.RELATIVE_PATH, "Pictures/LifeMusic")
    }

    val outputOptions = ImageCapture.OutputFileOptions
        .Builder(
            context.contentResolver,
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            contentValues
        )
        .build()

    imageCapture.takePicture(
        outputOptions,
        ContextCompat.getMainExecutor(context),
        object : ImageCapture.OnImageSavedCallback {
            override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
                val uri = outputFileResults.savedUri
                if (uri == null) onError("No se pudo obtener el URI de la imagen.")
                else onSuccess(uri.toString())
            }

            override fun onError(exception: ImageCaptureException) {
                onError("Error al capturar: ${exception.message ?: "desconocido"}")
            }
        }
    )
}

// Sonido simple al capturar
private fun playCaptureSound() {
    runCatching {
        ToneGenerator(AudioManager.STREAM_NOTIFICATION, 80)
            .startTone(ToneGenerator.TONE_PROP_BEEP, 120)
    }
}