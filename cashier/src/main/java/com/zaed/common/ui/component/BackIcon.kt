package com.zaed.common.ui.component

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBackIos
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable

@Composable
fun BackIcon(onBack: () -> Unit) {
    IconButton(onBack) {
        Icon(
            imageVector = Icons.AutoMirrored.Default.ArrowBackIos,
            contentDescription = "Back"
        )
    }
}