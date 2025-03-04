package com.zaed.common.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.zaed.common.R
import com.zaed.common.data.model.Inventory
import com.zaed.common.data.model.InventoryType
import com.zaed.common.data.model.QuantityUnit
import com.zaed.common.data.model.sale.Karat

@Composable
fun InventoryItem(
    modifier: Modifier = Modifier,
    inventory: Inventory
) {
    val context = LocalContext.current
    val title = remember {
        when (inventory.type) {
            InventoryType.PRODUCT -> {
                inventory.productName
            }

            InventoryType.GOLD -> {
                context.getString(R.string.gold)
            }

            else -> {
                context.getString(
                    R.string.ingot_of_karat,
                    Karat.valueOf(inventory.karat).value.toString()
                )
            }
        }
    }
    val quantityColor = when (inventory.quantity) {
        in 0.0..10.0 -> MaterialTheme.colorScheme.error
        in 10.0..25.0 -> MaterialTheme.colorScheme.tertiary
        else -> MaterialTheme.colorScheme.primary
    }

    Column(
        modifier = modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
            )
            Text(
                text = if (inventory.quantityUnit == QuantityUnit.GRAMS)
                    stringResource(R.string.grams_placeholder, inventory.quantity.toString())
                else
                    stringResource(R.string.units_placeholder, inventory.quantity.toString()),
                style = MaterialTheme.typography.bodyMedium,
                color = quantityColor
            )
        }
        HorizontalDivider(thickness = 1.dp)
    }
}

@Preview(showSystemUi = true, showBackground = true, device = "id:pixel_9_pro")
@Composable
private fun InventoryItemPreview() {
    InventoryItem(
        inventory = Inventory(
            id = "1",
            productId = "1",
            productName = "Gold Ring",
            ownerId = "1",
            ownerName = "John Doe",
            quantity = 10.0,
            quantityUnit = QuantityUnit.GRAMS,
            type = InventoryType.GOLD,
            karat = Karat.K24.name
        )
    )
}