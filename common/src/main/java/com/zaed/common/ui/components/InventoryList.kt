package com.zaed.common.ui.components

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.zaed.common.data.model.inventory.Inventory

@Composable
fun InventoryList(
    modifier: Modifier = Modifier,
    isLoading: Boolean,
    inventories: List<Inventory>,
    onInventoryClicked: (Inventory) -> Unit
) {
    ListWithLoading(
        modifier = modifier,
        isLoading = isLoading
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxWidth(),
            contentPadding = PaddingValues(vertical = 16.dp)
        ) {
            items(
                items = inventories,
                key ={it.id}
            ) { inventory ->
                InventoryItem(
                    modifier = Modifier.animateItem(),
                    inventory = inventory,
                    onClick = {
                        onInventoryClicked(inventory)
                    }
                )
            }
        }
    }

}