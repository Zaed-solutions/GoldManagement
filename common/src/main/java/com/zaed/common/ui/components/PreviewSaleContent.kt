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
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.zaed.common.R
import com.zaed.common.data.model.customer.Account
import com.zaed.common.data.model.customer.WholeSaleCustomer
import com.zaed.common.data.model.sale.Discount
import com.zaed.common.data.model.sale.DiscountType
import com.zaed.common.data.model.sale.Product
import com.zaed.common.data.model.sale.Transaction
import com.zaed.common.data.model.sale.WholesaleTransaction
import com.zaed.common.data.model.supplier.Supplier
import com.zaed.common.ui.suppliers.SelectSupplierSheet
import com.zaed.common.ui.util.toMoneyFormat

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PreviewSaleContent(
    modifier: Modifier = Modifier,
    isSelectCustomerEnabled: Boolean = true,
    transaction: Transaction,
    onUpdateProduct: (product: Product) -> Unit = {},
    onDeleteProduct: (product: Product) -> Unit = {},
    deleteAllProducts: () -> Unit = {},
    onAddNewAccount: () -> Unit = {},
    query: String = "",
    onQueryChanged: (String) -> Unit = {},
    selectedAccount: Account,
    onAccountSelected: (Account) -> Unit = {},
    suggestedAccounts: List<Account> = emptyList(),
    onNext: () -> Unit = {},
    isAdmin: Boolean =false,
    isLoading: Boolean  =false,
    supplierSearchQuery: String ="" ,
    onUpdateSupplierSearchQuery: (String) -> Unit ={},
    filteredSuppliers: List<Supplier> = emptyList(),
    onSupplierClicked: (String) -> Unit ={},
    onAddSupplier: (Supplier) -> Unit = {},
    isStoreSale: Boolean = false,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(vertical = 16.dp),
    ) {
        var editProductSheet by remember { mutableStateOf(false) }
        var selectedProduct by remember { mutableStateOf<Product?>(null) }
        var showCustomerSheet by remember { mutableStateOf(false) }
        var showSupplierSheet by remember { mutableStateOf(false) }
        if (selectedAccount.id.isNotBlank()) {
            CustomerInfoSection(
                modifier = Modifier.padding(horizontal = 16.dp),
                customerName = selectedAccount.name,
                customerDebt = selectedAccount.moneyDebtAmount,
                onCustomerClicked = {}
            )
            Spacer(modifier = Modifier.height(8.dp))
        }
        val productsState by rememberUpdatedState(transaction.products)
        LazyColumn(
            modifier = Modifier.weight(1f),
        ) {
            items(
                items = productsState,
            ) { product ->
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
            modifier = Modifier.padding(horizontal = 16.dp)
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
                        text = stringResource(R.string.total),
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = transaction.totalAmount.toMoneyFormat(2),
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
                    if (isSelectCustomerEnabled) {
                        OutlinedButton(
                            onClick = {
                                when(selectedAccount){
                                    is WholeSaleCustomer ->{
                                        showCustomerSheet = true
                                    }
                                    is Supplier ->{
                                        showSupplierSheet = true
                                    }
                                }

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
                                if(selectedAccount is WholeSaleCustomer) {
                                    Text(
                                        text = if (selectedAccount.id.isNotBlank()) stringResource(R.string.edit_customer) else stringResource(
                                            R.string.add_customer
                                        )
                                    )
                                }else{
                                    Text(
                                        text = if (selectedAccount.id.isNotBlank()) stringResource(R.string.edit_supplier) else stringResource(
                                            R.string.add_supplier
                                        )
                                    )
                                }
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
                                text = stringResource(R.string.clear_all),
                                color = MaterialTheme.colorScheme.error
                            )
                        }
                    }
                }
                Button(
                    onClick = onNext,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(4.dp),
                    enabled = transaction.products.isNotEmpty(),

                    ) {
                    Text(
                        stringResource(R.string.confirm),
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
                            isStoreSale= isStoreSale,
                            product1 = it,
                            onValueChange = { updatedProduct ->
                                selectedProduct = updatedProduct
                            }
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
                                text = stringResource(R.string.confirm)
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
                                text = stringResource(R.string.delete)
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
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(vertical = TopAppBarDefaults.TopAppBarExpandedHeight),
                    onAddNewCustomer = onAddNewAccount,
                    query = query,
                    onQueryChanged = onQueryChanged,
                    selectedCustomer = selectedAccount as WholeSaleCustomer,
                    onCustomerSelected = {
                        onAccountSelected(it)
                        showCustomerSheet = false
                    },
                    suggestedCustomers = suggestedAccounts as List<WholeSaleCustomer>
                )
            }
        }
        AnimatedVisibility(showSupplierSheet) {
            SelectSupplierSheet(
                onDismiss = {
                    showSupplierSheet = false
                },
                isAdmin = isAdmin,
                isLoading = isLoading,
                onUpdateSearchQuery = { onUpdateSupplierSearchQuery(it) },
                searchQuery = supplierSearchQuery,
                filteredSuppliers = filteredSuppliers,
                onSupplierClicked = { supplier ->
                    onSupplierClicked(supplier.id)
                    showSupplierSheet = false
                },
                onAddSupplier = {
                    onAddSupplier(it)
                }
            )
        }

    }
}

@Preview(showBackground = true, locale = "ar")
@Composable
private fun PreviewSaleContentPreview() {
    PreviewSaleContent(
        transaction = WholesaleTransaction(
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
        onAddNewAccount = {},
        query = "",
        onQueryChanged = {},
        selectedAccount = WholeSaleCustomer(),
        onAccountSelected = {},
        suggestedAccounts = listOf(),
        isAdmin =  false,
        isLoading = false,
        supplierSearchQuery = "",
        onUpdateSupplierSearchQuery = {},
        filteredSuppliers = emptyList(),
        onSupplierClicked = {},
        onAddSupplier = {}
    )


}