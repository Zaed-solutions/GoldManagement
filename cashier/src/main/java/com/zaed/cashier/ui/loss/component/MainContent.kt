package com.zaed.cashier.ui.loss.component

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.zaed.common.data.model.loss.StoreLoss

@Composable
fun MainContent(
    date: String,
    onShowDetails: () -> Unit,
    losses: List<StoreLoss> = emptyList()
) {
    Surface(
        shadowElevation = 5.dp,
        tonalElevation = 5.dp,
        modifier = Modifier.padding(4.dp),
        shape = MaterialTheme.shapes.medium,
        onClick = onShowDetails,
    ) {
        LossItemTopRow(date, losses)
    }
}