package com.luis.lifemusic.data.local.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

/**
 * Tabla de usuarios.
 *
 * Arquitectura realista:
 * - Login SOLO con email.
 * - Email es único en la base de datos.
 * - username ya NO es credencial.
 * - El nombre mostrado debajo del perfil se deriva del email (lo anterior al @).
 */
@Entity(
    tableName = "users",
    indices = [
        Index(value = ["email"], unique = true) // 🔥 Email único
    ]
)
data class UserEntity(

    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,

    // Credencial de acceso
    @ColumnInfo(name = "email")
    val email: String,

    // En un proyecto real debería estar hasheada
    @ColumnInfo(name = "password")
    val password: String,

    // Datos personales
    @ColumnInfo(name = "display_name")
    val displayName: String,

    @ColumnInfo(name = "birth_date")
    val birthDate: Long, // Obligatoria

    // Recuperación de contraseña
    @ColumnInfo(name = "security_question")
    val securityQuestion: String,

    @ColumnInfo(name = "security_answer")
    val securityAnswer: String,

    // Foto de perfil (opcional)
    @ColumnInfo(name = "photo_uri")
    val photoUri: String? = null
)
