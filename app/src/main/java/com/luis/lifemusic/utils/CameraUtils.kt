package com.luis.lifemusic.utils

import android.content.Context
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat

/**
 * Utilidades relacionadas con cámara y permisos.
 */
object CameraUtils {

    // Comprueba si el permiso CAMERA está concedido
    fun hasCameraPermission(context: Context): Boolean {
        return ContextCompat.checkSelfPermission(
            context,
            android.Manifest.permission.CAMERA
        ) == PackageManager.PERMISSION_GRANTED
    }

    // Verifica si el dispositivo tiene al menos una cámara
    fun hasAnyCameraHardware(context: Context): Boolean {
        return context.packageManager
            .hasSystemFeature(PackageManager.FEATURE_CAMERA_ANY)
    }
}