package com.zaed.cashier.ui.addsale.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.zaed.common.R
import com.zaed.cashier.ui.theme.CashierAppTheme
import com.zaed.common.data.model.Product
import com.zaed.common.ui.components.NumberInputTextField
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
        verticalArrangement = Arrangement.spacedBy(8.dp)
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
            modifier = Modifier.fillMaxWidth().padding(top = 16.dp)
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
            supportingText = stringResource(
                R.string.minimum_price_placeholder,
                product.minPrice.formatMoney()
            ),
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
            enabled = product.name.isNotBlank() && product.grams > 0.0 && product.gramPrice > 0.0
        ) {
            Text(
                text = stringResource(R.string.save)
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview(showSystemUi = true, showBackground = true, device = "id:pixel_9_pro")
@Composable
private fun Preview() {
    CashierAppTheme {
        ModalBottomSheet(
            onDismissRequest = {},
//            sheetState = rememberModalBottomSheetState()
        ) {
            SaveProductSheetContent(
                initialProduct = Product(),
            ) { }
        }
    }
}