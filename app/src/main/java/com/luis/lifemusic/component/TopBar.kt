package com.luis.lifemusic.component

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(title: String, onBackClick: (() -> Unit)? = null) {
    CenterAlignedTopAppBar(
        title = {
            Text(
                text = title,
                color = MaterialTheme.colorScheme.onPrimary
            )
        },
        navigationIcon = {
            onBackClick?.let {
                IconButton(onClick = it) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Volver",
                        tint = MaterialTheme.colorScheme.onPrimary
                    )
                }
            }
        },
        colors = androidx.compose.material3.TopAppBarDefaults.centerAlignedTopAppBarColors(
            containerColor = MaterialTheme.colorScheme.primary
        )
    )
}
