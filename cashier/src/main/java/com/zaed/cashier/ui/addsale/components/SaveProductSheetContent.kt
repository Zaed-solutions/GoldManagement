package com.zaed.cashier.ui.addsale.components

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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.zaed.cashier.R
import com.zaed.common.data.model.Product
import com.zaed.common.ui.components.TextInputTextField
import com.zaed.common.ui.util.formatMoney

@Composable
fun SaveProductSheetContent(
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
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = stringResource(R.string.add_product),
            style = MaterialTheme.typography.headlineMedium
        )
        TextInputTextField(
            label = stringResource(R.string.name),
            value = product.name,
            onValueChange = {
                product = product.copy(name = it)
            },
            containerColor = MaterialTheme.colorScheme.surfaceContainer,
            modifier = Modifier.fillMaxWidth()
        )
        TextInputTextField(
            modifier = Modifier.fillMaxWidth(),
            value = if (product.grams == 0.0) "" else product.grams.toString(),
            onValueChange = {
                if (it.matches(Regex("^\\d+\\.?\\d*\$"))) { // Accepts digits and an optional decimal point
                    product = product.copy(grams = it.toDouble())
                } else {
                    product = product.copy(grams = 0.0)
                }
            },
            label = stringResource(R.string.grams),
            containerColor = MaterialTheme.colorScheme.surfaceContainer,
            keyboardType = KeyboardType.Decimal,
        )
        TextInputTextField(
            modifier = Modifier.fillMaxWidth(),
            value = if (product.gramPrice == 0.0) "" else product.gramPrice.toString(),
            onValueChange = {
                if (it.matches(Regex("^\\d+\\.?\\d*\$"))) { // Accepts digits and an optional decimal point
                    product = product.copy(gramPrice = it.toDouble())
                } else {
                    product = product.copy(gramPrice = 0.0)
                }
            },
            label = stringResource(R.string.gram_price),
            supportingText = stringResource(
                R.string.minimum_price_placeholder,
                product.minPrice.formatMoney()
            ),
            containerColor = MaterialTheme.colorScheme.surfaceContainer,
            keyboardType = KeyboardType.Decimal
        )
        Button(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(min = 64.dp),
            shape = MaterialTheme.shapes.medium,
            onClick = {
                onSaveProduct(product)
            },
            enabled = product.name.isNotBlank() && product.grams > 0.0 && product.gramPrice > 0.0
        ) {
            Text(
                text = stringResource(R.string.save)
            )
        }
    }
}