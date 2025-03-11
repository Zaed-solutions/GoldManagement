package com.zaed.common.ui.displaycustomers.component

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddCustomerFab(onClick: () -> Unit) {
    FloatingActionButton(
        modifier = Modifier.rotate(45f),
        onClick = onClick
    ) {
        Icon(
            modifier = Modifier.rotate(-45f),
            imageVector = Icons.Filled.Add,
            contentDescription = "Add Customer"
        )
    }
}