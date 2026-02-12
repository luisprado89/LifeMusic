package com.luis.lifemusic.data.local.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

/**
 * Tabla de usuarios.
 *
 * ✅ Clave primaria REAL: id autogenerado (estable).
 * - No usamos username como PK porque puede cambiarse o repetirse si no lo validas.
 *
 * ✅ username/email los trataremos como "credenciales" (con validación en el repo/VM).
 */
@Entity(
    tableName = "users",
    indices = [
        Index(value = ["username"], unique = true), // si quieres permitir repetidos, quita unique=true
    ]
)
data class UserEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,

    @ColumnInfo(name = "username")
    val username: String,

    // En un proyecto real: nunca guardar password plano.
    // Aquí dejamos el campo listo para migrar a hash más adelante.
    @ColumnInfo(name = "password")
    val password: String,

    // Para recuperación ficticia (sin email real).
    @ColumnInfo(name = "security_question")
    val securityQuestion: String,

    @ColumnInfo(name = "security_answer")
    val securityAnswer: String,

    // Foto del perfil:
    // Guardamos una Uri como String (ej: "content://...") o null si no hay foto.
    @ColumnInfo(name = "photo_uri")
    val photoUri: String? = null,

    // Datos extra de perfil (pueden editarse)
    @ColumnInfo(name = "display_name")
    val displayName: String = username,

    @ColumnInfo(name = "email")
    val email: String = ""
)
