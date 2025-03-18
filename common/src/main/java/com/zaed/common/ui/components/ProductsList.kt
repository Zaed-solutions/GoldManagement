package com.zaed.common.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.zaed.common.R
import com.zaed.common.data.model.sale.Product
import com.zaed.common.ui.addpurchase.ProductType
import com.zaed.common.ui.util.formatMoney
import com.zaed.common.ui.util.toMoneyFormat

@Composable
fun ProductsList(
    modifier: Modifier = Modifier,
    products: List<Product>,
    onAddProduct: () -> Unit,
    onEditProduct: (Product) -> Unit,
    label: String = stringResource(R.string.products),
    onRemoveProduct: (id: String) -> Unit,
    productType: ProductType
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = label,
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.weight(1f)
            )
            IconButton(onClick = { onAddProduct() }) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add Reservation"
                )
            }
        }
        ProductsTable(
            products = products,
            productType = productType,
        )

    }
}

@Composable
fun ProductItem(
    modifier: Modifier = Modifier,
    product: Product,
) {
    Surface(
        modifier = modifier.fillMaxWidth(),
        tonalElevation = 2.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = product.name,
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.weight(5f)
            )

            Text(
                text = product.grams.toString(),
                style = MaterialTheme.typography.titleMedium,
                textAlign = TextAlign.Center,
                modifier = Modifier.weight(3f)
            )
            Text(
                text = product.totalPriceAfterDiscount.formatMoney(),
                style = MaterialTheme.typography.titleMedium,
                textAlign = TextAlign.End,
                maxLines = 1,
                modifier = Modifier.weight(3f)
            )
        }
    }
}

@Composable
fun GoldItem(
    modifier: Modifier = Modifier,
    product: Product,
) {
    Surface(
        modifier = modifier.fillMaxWidth(),
        tonalElevation = 2.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = product.grams.toString(),
                style = MaterialTheme.typography.titleMedium,
                textAlign = TextAlign.Center,
                modifier = Modifier.weight(3f)
            )

            Text(
                text = product.totalPriceAfterDiscount.formatMoney(),
                style = MaterialTheme.typography.titleMedium,
                textAlign = TextAlign.End,
                maxLines = 1,
                modifier = Modifier.weight(5f)
            )
            Text(
                text = product.laborCost.toMoneyFormat(2),
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.weight(3f),
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
fun IngotItem(
    modifier: Modifier = Modifier,
    product: Product,
) {
    Surface(
        modifier = modifier.fillMaxWidth(),
        tonalElevation = 2.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = product.grams.toString(),
                style = MaterialTheme.typography.titleMedium,
                textAlign = TextAlign.Center,
                modifier = Modifier.weight(3f)
            )
            Text(
                text = product.totalPriceAfterDiscount.formatMoney(),
                style = MaterialTheme.typography.titleMedium,
                textAlign = TextAlign.End,
                maxLines = 1,
                modifier = Modifier.weight(5f)
            )
            Text(
                text = product.karat.name,
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.weight(3f),
                textAlign = TextAlign.Center
            )

        }
    }
}

@Preview(showSystemUi = true, showBackground = true, device = "id:pixel_9_pro")
@Composable
private fun Preview() {
    ProductItem(
        modifier = Modifier.padding(horizontal = 16.dp, vertical = 80.dp),
        product = Product(
            id = "1",
            name = "Product 1",
            gramPrice = 100.0,
            quantity = 1,
            minPrice = 50.0
        ),
    )
}
