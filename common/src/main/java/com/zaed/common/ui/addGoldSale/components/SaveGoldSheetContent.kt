package com.zaed.common.ui.addGoldSale.components
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.zaed.common.R
import com.zaed.common.data.model.sale.Product
import com.zaed.common.ui.components.NumberInputTextField

@Composable
fun SaveGoldSheetContent(
    modifier: Modifier = Modifier,
    initialProduct: Product,
    onSaveProduct: (Product) -> Unit
) {
    var product by remember { mutableStateOf(initialProduct) }
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = stringResource(com.zaed.common.R.string.add_amount),
            style = MaterialTheme.typography.headlineMedium
        )

        NumberInputTextField(
            modifier = Modifier.fillMaxWidth(),
            value = product.grams,
            onValueChange = { value ->
                product = product.copy(grams = value)
            },
            label = stringResource(R.string.grams),
            containerColor = MaterialTheme.colorScheme.surfaceContainer,
        )
        NumberInputTextField(
            modifier = Modifier.fillMaxWidth(),
            value = product.gramPrice,
            onValueChange = { value ->
                product = product.copy(gramPrice = value)
            },
            label = stringResource(R.string.gram_price),

            containerColor = MaterialTheme.colorScheme.surfaceContainer,
        )
        NumberInputTextField(
            modifier = Modifier.fillMaxWidth(),
            value = product.laborCost,
            onValueChange = { value ->
                product = product.copy(laborCost = value)
            },
            label = stringResource(R.string.labor_cost),
            containerColor = MaterialTheme.colorScheme.surfaceContainer,
        )

        Button(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp)
                .heightIn(min = 48.dp),
            shape = MaterialTheme.shapes.medium,
            onClick = {
                onSaveProduct(product)
            },
            enabled = product.grams > 0.0 && product.gramPrice > 0.0
        ) {
            Text(
                text = stringResource(R.string.save)
            )
        }
    }
}