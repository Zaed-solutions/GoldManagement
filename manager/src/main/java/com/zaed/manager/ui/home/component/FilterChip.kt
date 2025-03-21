package com.zaed.manager.ui.home.component

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

@Composable
fun FilterChip(
    text: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    androidx.compose.material3.FilterChip(
        selected = isSelected,
        onClick = onClick,
        label = { Text(text = text) },
        colors = androidx.compose.material3.FilterChipDefaults.filterChipColors(
            selectedContainerColor = androidx.compose.material3.MaterialTheme.colorScheme.primary,
            selectedLabelColor = androidx.compose.material3.MaterialTheme.colorScheme.onPrimary
        )
    )
}