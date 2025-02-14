package com.zaed.cashier.ui.addsale.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.input.InputTransformation.Companion.keyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AttachMoney
import androidx.compose.material.icons.filled.Percent
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
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
import com.zaed.common.data.model.DiscountType
import com.zaed.common.data.model.Product
import com.zaed.common.data.model.StoreSale
import com.zaed.common.ui.components.TitledDropDownTextField

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SelectProductsContent(
    modifier: Modifier = Modifier,
    allProducts: List<Product>,
    sale: StoreSale,
    onAddProduct: (Product) -> Unit,
    onRemoveProduct: (id: String) -> Unit,
    onUpdateDiscountType: (DiscountType) -> Unit,
    onUpdateDiscountValue: (Double) -> Unit,
    onUpdateProductCount: (id: String, count: Int) -> Unit,
    onUpdateProductPrice: (id: String, price: Double) -> Unit,
) {
    var isAddProductSheetVisible by remember { mutableStateOf(false) }
    val bottomSheetState = rememberModalBottomSheetState()
    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        //discount
        TitledDropDownTextField(
            modifier = Modifier.padding(horizontal = 16.dp),
            selectedValue = stringResource(sale.discount.type.titleRes),
            options = DiscountType.entries.map { stringResource(it.titleRes) },
            onValueChanged = { index ->
                onUpdateDiscountType(DiscountType.entries[index])
            }
        )
        AnimatedVisibility(visible = sale.discount.type != DiscountType.NONE) {
            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .padding(top = 8.dp),
                value = if(sale.discount.value == 0.0) "" else sale.discount.value.toInt().toString(),
                shape = MaterialTheme.shapes.large,
                onValueChange = {
                    if (it.matches(Regex("^\\d+\\.?\\d*\$"))) { // Accepts digits and an optional decimal point
                        onUpdateDiscountValue(it.toDouble())
                    } else {
                        onUpdateDiscountValue(0.0)
                    }
                },
                label = {
                    Text(
                        text = stringResource(R.string.discount_value)
                    )
                },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                leadingIcon = {
                    Icon(
                        imageVector = if (sale.discount.type == DiscountType.PERCENTAGE) {
                            Icons.Default.Percent
                        } else {
                            Icons.Default.AttachMoney
                        },
                        contentDescription = "discount type"
                    )
                }
            )
        }
        //products
        ProductsList(
            products = sale.products,
            onUpdateProductPrice = onUpdateProductPrice,
            onUpdateProductCount = onUpdateProductCount,
            onAddProduct = { isAddProductSheetVisible = true },
            onRemoveProduct = onRemoveProduct
        )
        AnimatedVisibility(isAddProductSheetVisible) {
            ModalBottomSheet(
                sheetState = bottomSheetState,
                onDismissRequest = { isAddProductSheetVisible = false },
            ) {
                AddProductSheetContent(
                    allProducts = allProducts,
                    onAddProduct = {
                        onAddProduct(it)
                        isAddProductSheetVisible = false
                    }
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddProductSheetContent(
    modifier: Modifier = Modifier,
    allProducts: List<Product>,
    onAddProduct: (Product) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    var query by remember { mutableStateOf("") }
    val filteredProducts by remember(query) {
        mutableStateOf(allProducts.filter { it.name.contains(query, ignoreCase = true) })
    }
    Column(
        modifier = modifier
            .fillMaxWidth()
            .heightIn(min = 400.dp)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = stringResource(R.string.add_product),
            style = MaterialTheme.typography.headlineMedium
        )
        ExposedDropdownMenuBox(
            modifier = Modifier.fillMaxWidth(),
            expanded = expanded,
            onExpandedChange = {
                expanded = !expanded
            },
        ) {
            OutlinedTextField(
                value = query,
                onValueChange = {
                    query = it
                },
                label = {
                    Text(
                        text = stringResource(R.string.search)
                    )
                },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "search"
                    )
                },
                shape = MaterialTheme.shapes.small,
                colors = OutlinedTextFieldDefaults.colors(
                    unfocusedBorderColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.5f),
                ),
                modifier = Modifier
                    .menuAnchor(MenuAnchorType.PrimaryEditable)
                    .fillMaxWidth()
            )

            ExposedDropdownMenu(
                expanded = expanded, onDismissRequest = { expanded = false }) {
                filteredProducts.take(5).forEach{ product ->
                    DropdownMenuItem(
                        text = { Text(text = product.name) },
                        onClick = {
                            onAddProduct(product)
                        }
                    )
                }
            }
        }
    }
}
