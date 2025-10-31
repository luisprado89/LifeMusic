package com.luis.lifemusic.component

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.font.FontWeight

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScaffold(
    title: String,
    isHome: Boolean = false, // 🔹 controla si es la Home o una subpantalla
    onBackClick: () -> Unit = {},
    onListClick: () -> Unit = {},
    onProfileClick: () -> Unit = {},
    content: @Composable (PaddingValues) -> Unit
) {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = title,
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    // 🔹 Solo mostrar flecha atrás si NO estamos en Home
                    if (!isHome) {
                        IconButton(onClick = onBackClick) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "Volver"
                            )
                        }
                    }
                },
                actions = {
                    // 🔹 Solo mostrar lista y perfil si ES Home
                    if (isHome) {
                        IconButton(onClick = onListClick) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.List,
                                contentDescription = "Lista"
                            )
                        }
                        IconButton(onClick = onProfileClick) {
                            Icon(
                                imageVector = Icons.Default.Person,
                                contentDescription = "Perfil"
                            )
                        }
                    }
                }
            )
        }
    ) { innerPadding ->
        content(innerPadding)
    }
}
