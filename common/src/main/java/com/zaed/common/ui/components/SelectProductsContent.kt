package com.zaed.common.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
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
import com.zaed.common.data.model.category.Category
import com.zaed.common.data.model.sale.Product
import com.zaed.common.data.model.sale.Transaction
import com.zaed.common.data.model.sale.WholesaleTransaction
import com.zaed.common.ui.util.toMoneyFormat

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun SelectProductsContent(
    modifier: Modifier = Modifier,
    categories: List<Category>,
    transaction: Transaction,
    onNext: () -> Unit,
    onAddProduct: (Product) -> Unit,
    onDeleteProduct: (Product) -> Unit,
    onAddNewCategory: (Category) -> Unit = {},
    isStoreSale: Boolean = false,
    isPurchase: Boolean = false
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
    var selectedProduct by remember { mutableStateOf<Product?>(null) }
    var addCategorySheetVisible by remember { mutableStateOf(false) }
    var selectedCategory by remember { mutableStateOf<Category?>(null) }
    Scaffold(
        floatingActionButton = {
            if (isPurchase) {
                FloatingActionButton(
                    onClick = {
                        addCategorySheetVisible = true
                    },
                ) {
                    Icon(
                        imageVector = androidx.compose.material.icons.Icons.Default.Add,
                        contentDescription = null
                    )
                }
            }
        },
        bottomBar = {
            Button(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                onClick = onNext,
                enabled = transaction.products.isNotEmpty(),
                shape = RoundedCornerShape(4.dp)
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = stringResource(R.string.sell),
                        style = MaterialTheme.typography.titleMedium
                    )
                    Text(
                        text = stringResource(
                            R.string.total_placeholder,
                            transaction.totalAmount.toMoneyFormat(2)
                        ),
                        style = MaterialTheme.typography.titleMedium
                    )
                }
            }
        }
    ) { paddingValues ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(paddingValues),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalAlignment = Alignment.Start
        ) {
            Text(
                modifier = Modifier.padding(horizontal = 16.dp),
                text = stringResource(R.string.select_products),
                style = MaterialTheme.typography.headlineMedium
            )
            SearchBar(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                query = searchQuery,
                onQueryChanged = { searchQuery = it }
            )
            LazyColumn(
                modifier = Modifier.weight(1f),
            ) {
                items(displayedCategory, key = { it.id }) { category ->
                    CategoryItem(
                        modifier = Modifier.animateItem(fadeInSpec = null, fadeOutSpec = null),
                        count = transaction.products.firstOrNull { it.categoryId == category.id }?.quantity
                            ?: 0,
                        price = transaction.products.filter { it.categoryId == category.id }
                            .sumOf { it.totalPriceAfterDiscount },
                        category = category,
                        onClick = {
                            selectedProduct =
                                transaction.products.firstOrNull { it.categoryId == category.id }
                                    ?: Product(
                                        categoryId = category.id,
                                        name = if (isStoreSale) "" else category.name,
                                        buyingPrice = category.buyingPrice
                                    )
                            enterProductSheetVisible = true
                        }
                    )
                    HorizontalDivider(Modifier.padding(horizontal = 16.dp))
                }
            }

            AnimatedVisibility(enterProductSheetVisible) {
                ModalBottomSheet(
                    sheetState = rememberModalBottomSheetState(
                        skipPartiallyExpanded = true,
                    ),
                    onDismissRequest = {
                        enterProductSheetVisible = false
                        selectedProduct = null
                    }
                ) {
                    selectedProduct?.let {
                        SaveProductSheetContent(
                            isStoreSale = isStoreSale,
                            initialProduct = it,
                            onSaveProduct = { newProduct ->
                                onAddProduct(newProduct)
                                enterProductSheetVisible = false
                                selectedProduct = null
                            },
                            deleteProduct = { deletedProduct ->
                                onDeleteProduct(deletedProduct)
                                enterProductSheetVisible = false
                                selectedProduct = null
                            }
                        )
                    }
                }
            }
            AnimatedVisibility(addCategorySheetVisible) {
                ModalBottomSheet(
                    sheetState = rememberModalBottomSheetState(
                        skipPartiallyExpanded = true,
                    ),
                    onDismissRequest = { addCategorySheetVisible = false }
                ) {
                    AddCategorySheetContent(
                        onDismiss = { addCategorySheetVisible = false },
                        onAddNewCategory = onAddNewCategory,
                        category = selectedCategory
                    )
                }
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
        transaction = WholesaleTransaction(
            products = listOf(
                Product(name = "Gold", quantity = 1, gramPrice = 100.0),
                Product(name = "Silver", quantity = 2, gramPrice = 50.0),
            )
        ),
        onAddProduct = {},
        onNext = {},
        onDeleteProduct = {}
    )
}

