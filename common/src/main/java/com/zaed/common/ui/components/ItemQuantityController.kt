package com.zaed.common.ui.components

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun ItemQuantityController(
    modifier: Modifier = Modifier,
    quantity: Int,
    onIncrementQuantity: () -> Unit,
    onDecrementQuantity: () -> Unit,
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Button(
            onClick = { onDecrementQuantity() },
            enabled = quantity != 1,
            contentPadding = PaddingValues(4.dp),
            shape = MaterialTheme.shapes.small,
            modifier = Modifier
                .widthIn(24.dp)
                .heightIn(24.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Remove,
                contentDescription = "decrement quantity",
                modifier = Modifier.size(16.dp)
            )
        }
        Text(text = "$quantity")
        Button(
            onClick = { onIncrementQuantity() },
            contentPadding = PaddingValues(4.dp),
            shape = MaterialTheme.shapes.small,
            modifier = Modifier
                .widthIn(24.dp)
                .heightIn(24.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = "increment quantity",
                modifier = Modifier.size(16.dp)
            )
        }
    }
}