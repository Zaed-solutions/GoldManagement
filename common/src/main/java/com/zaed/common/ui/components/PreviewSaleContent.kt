package com.zaed.common.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.PersonAdd
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.zaed.common.data.model.customer.WholeSaleCustomer
import com.zaed.common.data.model.sale.Discount
import com.zaed.common.data.model.sale.DiscountType
import com.zaed.common.data.model.sale.Product
import com.zaed.common.data.model.sale.Sale
import com.zaed.common.data.model.sale.WholesaleProductSale
import com.zaed.common.ui.util.toMoneyFormat

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PreviewSaleContent(
    modifier: Modifier = Modifier,
    isSelectCustomerEnabled: Boolean = true,
    sale: Sale,
    onUpdateProduct: (product: Product) -> Unit = {},
    onDeleteProduct: (product: Product) -> Unit = {},
    deleteAllProducts: () -> Unit = {},
    onAddNewCustomer: () -> Unit = {},
    query: String = "",
    onQueryChanged: (String) -> Unit = {},
    selectedCustomer: WholeSaleCustomer = WholeSaleCustomer(),
    onCustomerSelected: (WholeSaleCustomer) -> Unit = {},
    suggestedCustomers: List<WholeSaleCustomer> = emptyList(),
    onNext: () -> Unit = {},
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
    ) {
        var editProductSheet by remember { mutableStateOf(false) }
        var selectedProduct by remember { mutableStateOf<Product?>(null) }
        var showCustomerSheet by remember { mutableStateOf(false) }
        if(selectedCustomer.id.isNotBlank()) {
            CustomerInfoSection(
                customerName = selectedCustomer.name,
                customerDebt = selectedCustomer.debtAmount,
                onCustomerClicked = {}
            )
            Spacer(modifier = Modifier.height(8.dp))
        }
        LazyColumn(
            modifier = Modifier.weight(1f),
        ) {
            items(sale.products) { product ->
                PreviewSaleItem(
                    product = product,
                    onShowProductDetails = {
                        selectedProduct = it
                        editProductSheet = true
                    }
                )
                HorizontalDivider()
            }
        }
        Surface(
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "المجموع",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = sale.totalAmount.toMoneyFormat(2),
                        modifier = Modifier.weight(1f),
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.End
                    )
                }
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 4.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    //customer
                    if(isSelectCustomerEnabled) {
                        OutlinedButton(
                            onClick = {
                                showCustomerSheet = true
                            },
                            modifier = Modifier
                                .weight(1f),
                            shape = RoundedCornerShape(4.dp)
                        ) {
                            Column(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Icon(
                                    imageVector = Icons.Outlined.PersonAdd,
                                    contentDescription = null
                                )
                                Text(
                                    text = if (selectedCustomer.id.isNotBlank()) "تعديل عميل" else "اضافة عميل"
                                )
                            }
                        }
                    }
                    //delete all
                    OutlinedButton(
                        onClick = {
                            deleteAllProducts()

                        },
                        colors = ButtonDefaults.outlinedButtonColors(
                            containerColor = MaterialTheme.colorScheme.errorContainer,
                        ),
                        modifier = Modifier
                            .weight(1f),
                        shape = RoundedCornerShape(4.dp)
                    ) {
                        Column(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Icon(
                                imageVector = Icons.Outlined.Delete,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.error
                            )
                            Text(
                                text = "افراغ السلة",
                                color = MaterialTheme.colorScheme.error
                            )
                        }
                    }
                }
                Button(
                    onClick = onNext,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(4.dp),
                    enabled = sale.products.isNotEmpty(),

                    ) {
                    Text(
                        "تأكيد البيع",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )
                }

            }

        }
        AnimatedVisibility(editProductSheet) {
            ModalBottomSheet(
                sheetState = rememberModalBottomSheetState(
                    skipPartiallyExpanded = true,
                ),
                onDismissRequest = {
                    editProductSheet = false
                    selectedProduct = null
                },
                dragHandle = {}
            ) {

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp)
                ) {
                    CenterAlignedTopAppBar(
                        title = {
                            Text(
                                text = selectedProduct?.name ?: ""
                            )
                        },
                        navigationIcon = {
                            IconButton(
                                onClick = {
                                    editProductSheet = false
                                    selectedProduct = null
                                }
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Close,
                                    contentDescription = null
                                )
                            }
                        },
                        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                            containerColor = Color.Transparent
                        )
                    )
                    selectedProduct?.let {
                        ProductFieldsContent(
                            product1 = it,
                            onValueChange = { updatedProduct ->
                                selectedProduct = updatedProduct
                            },

                            )
                    }
                    Spacer(Modifier.weight(1f))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Button(
                            modifier = Modifier
                                .weight(1f)
                                .padding(end = 8.dp),
                            onClick = {
                                selectedProduct?.let {
                                    onUpdateProduct(it)
                                }
                                editProductSheet = false
                                selectedProduct = null
                            },
                            shape = RoundedCornerShape(4.dp)
                        ) {
                            Text(
                                text = "تأكيد"
                            )
                        }
                        FilledTonalButton(
                            modifier = Modifier
                                .weight(1f)
                                .padding(start = 8.dp),
                            onClick = {
                                selectedProduct?.let {
                                    onDeleteProduct(it)
                                }
                                editProductSheet = false
                                selectedProduct = null
                            },
                            shape = RoundedCornerShape(4.dp)
                        ) {
                            Text(
                                text = "حذف"
                            )
                        }
                    }
                }

            }
        }
        AnimatedVisibility(showCustomerSheet) {
            ModalBottomSheet(
                sheetState = rememberModalBottomSheetState(
                    skipPartiallyExpanded = true,
                ),
                onDismissRequest = {
                    showCustomerSheet = false
                },
                dragHandle = {}
            ) {
                SelectCustomerContent(
                    modifier = Modifier.fillMaxSize().padding(vertical = TopAppBarDefaults.TopAppBarExpandedHeight),
                    onAddNewCustomer = onAddNewCustomer,
                    query = query,
                    onQueryChanged = onQueryChanged,
                    selectedCustomer = selectedCustomer,
                    onCustomerSelected = {
                        onCustomerSelected(it)
                        showCustomerSheet = false
                    },
                    suggestedCustomers = suggestedCustomers
                )
            }
        }
    }
}

@Preview(showBackground = true, locale = "ar")
@Composable
private fun PreviewSaleContentPreview() {
    PreviewSaleContent(
        sale = WholesaleProductSale(
            products = listOf(
                Product(
                    id = "1",
                    name = "product 1",
                    gramPrice = 10.0,
                    quantity = 10,
                    grams = 50.0,
                    discount = Discount(
                        type = DiscountType.PERCENTAGE,
                        value = 10.0
                    )
                )
            )
        ),
        onNext = {},
        onAddNewCustomer = {},
        query = "",
        onQueryChanged = {},
        selectedCustomer = WholeSaleCustomer(),
        onCustomerSelected = {},
        suggestedCustomers = listOf()
    )


}