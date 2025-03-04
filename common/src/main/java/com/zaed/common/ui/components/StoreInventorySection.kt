package com.zaed.common.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.zaed.common.data.model.Inventory

@Composable
fun StoreInventorySection(
    modifier: Modifier = Modifier,
    isLoading: Boolean,
    query: String,
    onQueryChanged: (String) -> Unit,
    inventories: List<Inventory>,
    onAddInventory: () -> Unit
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth()
        ) {
            SearchBar(
                modifier = Modifier.weight(1f),
                query = query,
                onQueryChanged = onQueryChanged
            )
            IconButton(
                onClick = {
                    onAddInventory()
                }
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = null
                )
            }
        }
        InventoryList(
            isLoading = isLoading,
            inventories = inventories
        )
    }
}

