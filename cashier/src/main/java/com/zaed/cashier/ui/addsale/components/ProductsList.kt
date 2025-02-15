package com.zaed.cashier.ui.addsale.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.zaed.cashier.R
import com.zaed.common.data.model.Product
import com.zaed.common.ui.components.ExpandableItem
import com.zaed.common.ui.components.ItemQuantityController
import com.zaed.common.ui.components.SwipeToEditOrDeleteContainer
import com.zaed.common.ui.util.formatMoney

@Composable
fun ProductsList(
    modifier: Modifier = Modifier,
    products: List<Product>,
    onAddProduct: () -> Unit,
    onEditProduct: (Product) -> Unit,
    onRemoveProduct: (id: String) -> Unit
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = stringResource(R.string.products),
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
        LazyColumn (
            modifier = Modifier.fillMaxWidth(),
            contentPadding = PaddingValues(vertical = 16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            items(
                items = products,
                key = {it.id}
            ) {
                SwipeToEditOrDeleteContainer(
                    onDelete = {
                        onRemoveProduct(it.id)
                    },
                    isEditEnabled = true,
                    onEdit = {
                        onEditProduct(it)
                    }
                ) {
                    ProductItem(
                        modifier = Modifier.animateItem().padding(horizontal = 16.dp),
                        product = it,
                    )
                }
            }
        }
    }
}

@Composable
fun ProductItem(
    modifier: Modifier = Modifier,
    product: Product,
) {
    Surface(
        modifier = modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.medium,
        tonalElevation = 2.dp
    ) {
        Row (
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ){
            Text(
                text = product.name,
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.weight(0.5f)
            )
            Text(
                text = stringResource(R.string.grams_placeholder, product.grams),
                style = MaterialTheme.typography.titleMedium,
                textAlign = TextAlign.Center,
                modifier = Modifier.weight(0.2f)
            )
            Text(
                text = (product.grams * product.gramPrice * product.quantity).formatMoney(),
                style = MaterialTheme.typography.titleMedium,
                textAlign = TextAlign.Center,
                maxLines = 1,
                modifier = Modifier.weight(0.3f)
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
