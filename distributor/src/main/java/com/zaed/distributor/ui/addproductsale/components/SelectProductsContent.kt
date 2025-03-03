package com.zaed.distributor.ui.addproductsale.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.zaed.common.R
import com.zaed.common.data.model.Category
import com.zaed.common.data.model.sale.Product
import com.zaed.common.data.model.sale.WholesaleProductSale
import com.zaed.common.ui.components.SaveProductSheetContent
import com.zaed.common.ui.components.SearchBar
import com.zaed.common.ui.util.toMoneyFormat

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SelectProductsContent(
    modifier: Modifier = Modifier,
    categories: List<Category>,
    sale: WholesaleProductSale,
    onNext: () -> Unit,
    onAddProduct: (Product) -> Unit,

) {
    val categories1 by rememberUpdatedState(categories)
    var searchQuery by remember { mutableStateOf("") }
    val displayedCategory by remember {
        derivedStateOf {
            if (searchQuery.isBlank()) {
                categories1
            } else {
                categories1.filter { it.name.contains(searchQuery, ignoreCase = true) }
            }
        }
    }
    var enterProductSheetVisible by remember { mutableStateOf(false) }
    var selectedCategory by remember { mutableStateOf<Category?>(null) }
    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalAlignment = Alignment.Start
    ) {
        Text(
            modifier = Modifier.padding(horizontal = 16.dp),
            text = stringResource(R.string.select_products),
            style = MaterialTheme.typography.headlineMedium
        )
        SearchBar(
            modifier = Modifier.padding(16.dp),
            query = searchQuery,
            onQueryChanged = { searchQuery = it }
        )
        LazyColumn(
            modifier = Modifier.weight(1f),
        ) {
            items(displayedCategory) { category ->
                CategoryItem(
                    count = sale.products.count { it.name == category.name },
                    price = sale.products.filter { it.name == category.name }.sumOf { it.grams * it.gramPrice },
                    category = category,
                    onClick = {
                        selectedCategory = category
                        enterProductSheetVisible = true
                    }
                )
                HorizontalDivider(Modifier.padding(horizontal = 16.dp))
            }
        }
        Button(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            onClick = onNext,
            enabled = sale.products.isNotEmpty(),
            shape = RoundedCornerShape(4.dp)
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = stringResource(com.zaed.common.R.string.sell),
                    style = MaterialTheme.typography.titleMedium
                )
                Text(
                    text = stringResource(
                        R.string.total_placeholder,
                        sale.totalPrice.toMoneyFormat(2)
                    ),
                    style = MaterialTheme.typography.titleMedium
                )
            }
        }
        AnimatedVisibility(enterProductSheetVisible) {
            ModalBottomSheet(
                onDismissRequest = {
                    enterProductSheetVisible = false
                    selectedCategory = null
                }
            ) {
                SaveProductSheetContent(
                    initialProduct = Product(name = selectedCategory?.name?:"", categoryId = selectedCategory?.id?:""),
                    onSaveProduct = {
                        onAddProduct(it)
                        enterProductSheetVisible = false
                        selectedCategory = null
                    }
                )
            }
        }

    }

}

@Preview(showBackground = true)
@Composable
private fun SelectProductsContentPreview() {
    SelectProductsContent(
        categories = listOf(
            Category(name = "Gold"),
            Category(name = "Silver"),
            Category(name = "Platinum"),
            Category(name = "Diamond"),
        ),
        sale = WholesaleProductSale(
            products = listOf(
                Product(name = "Gold", quantity = 1, gramPrice = 100.0),
                Product(name = "Silver", quantity = 2, gramPrice = 50.0),
            )
        ),
        onAddProduct = {},
        onNext = {}
    )
}

