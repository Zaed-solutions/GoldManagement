package com.zaed.common.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.zaed.common.R
import com.zaed.common.data.model.inventory.Inventory
import com.zaed.common.data.model.inventory.InventoryType
import com.zaed.common.data.model.inventory.QuantityUnit
import com.zaed.common.data.model.sale.Karat


@Composable
fun InventoryItem(
    modifier: Modifier = Modifier,
    inventory: Inventory,
    onClick: () -> Unit = {},
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
    Surface(
        modifier = modifier,
        color = Color.Transparent,
        onClick = { onClick() }
    ) {
        Column {
            Row(
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    Modifier
                        .padding(end = 4.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f))

                ) {
                    Text(
                        text = title.first().toString(),
                        style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier.padding(vertical = 8.dp, horizontal = 16.dp),
                        color = MaterialTheme.colorScheme.primary
                    )
                }
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium
                )
                Spacer(modifier = Modifier.weight(1f))
                Text(
                    text = if (inventory.quantityUnit == QuantityUnit.GRAMS)
                        stringResource(R.string.grams_placeholder, inventory.quantity.toString())
                    else
                        stringResource(R.string.units_placeholder, inventory.quantity.toString()),
                    style = MaterialTheme.typography.titleMedium,
                    color = quantityColor
                )

            }
            HorizontalDivider(thickness = 1.dp)
        }
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