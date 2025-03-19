package com.zaed.common.ui.addGoldSale.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
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
import androidx.compose.ui.unit.dp
import com.zaed.common.R
import com.zaed.common.data.model.sale.Product
import com.zaed.common.data.model.sale.WholesaleTransaction
import com.zaed.common.ui.addpurchase.ProductType
import com.zaed.common.ui.components.ProductsList
import com.zaed.common.ui.util.toMoneyFormat
import java.util.UUID

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SelectGoldContent(
    modifier: Modifier = Modifier,
    sale: WholesaleTransaction,
    onAddGold: (Product) -> Unit,
    onRemoveGold: (id: String) -> Unit,
    onNext: () -> Unit,
) {

    var isAddProductSheetVisible by remember { mutableStateOf(false) }
    val bottomSheetState2 = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    var selectedProduct by remember {
        mutableStateOf(Product())
    }
    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalAlignment = Alignment.Start
    ) {
        Text(
            modifier = Modifier.padding(horizontal = 16.dp),
            text = stringResource(com.zaed.common.R.string.add_gold_amounts),
            style = MaterialTheme.typography.headlineMedium
        )
        ProductsList(
            modifier = Modifier.padding(horizontal = 8.dp),
            products = sale.products,
            onAddProduct = {
                isAddProductSheetVisible = true
                selectedProduct = selectedProduct.copy(
                    categoryId = UUID.randomUUID().toString(),
                )
            },
            onRemoveProduct = onRemoveGold,
            label = stringResource(com.zaed.common.R.string.gold),
            onEditProduct = {
                selectedProduct = it
                isAddProductSheetVisible = true
            },
            productType = ProductType.GOLD
        )
        Spacer(modifier = Modifier.weight(1f))
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
                        sale.totalAmount.toMoneyFormat(2)
                    ),
                    style = MaterialTheme.typography.titleMedium
                )
            }
        }
        AnimatedVisibility(isAddProductSheetVisible) {
            ModalBottomSheet(
                sheetState = bottomSheetState2,
                onDismissRequest = { isAddProductSheetVisible = false },
            ) {
                SaveGoldSheetContent(
                    initialProduct = selectedProduct,
                    onSaveProduct = {
                        onAddGold(it)
                        isAddProductSheetVisible = false
                    }
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SelectIngotsContent(
    modifier: Modifier = Modifier,
    sale: WholesaleTransaction,
    onAddIngot: (Product) -> Unit,
    onRemoveIngot: (id: String) -> Unit,
    onNext: () -> Unit,
) {

    var isAddProductSheetVisible by remember { mutableStateOf(false) }
    val bottomSheetState2 = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    var selectedProduct by remember {
        mutableStateOf(Product())
    }
    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalAlignment = Alignment.Start
    ) {
        Text(
            modifier = Modifier.padding(horizontal = 16.dp),
            text = stringResource(com.zaed.common.R.string.add_ingot_transaction),
            style = MaterialTheme.typography.headlineMedium
        )
        ProductsList(
            modifier = Modifier.padding(horizontal = 8.dp),
            products = sale.products,
            onAddProduct = {
                isAddProductSheetVisible = true
                selectedProduct = selectedProduct.copy(
                    categoryId = UUID.randomUUID().toString(),
                )
            },
            onRemoveProduct = onRemoveIngot,
            label = stringResource(com.zaed.common.R.string.ingots),
            onEditProduct = {
                isAddProductSheetVisible = true
                selectedProduct = selectedProduct.copy(
                    categoryId = UUID.randomUUID().toString(),
                )
            },
            productType = ProductType.INGOT
        )
        Spacer(modifier = Modifier.weight(1f))
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
                    text = stringResource(com.zaed.common.R.string.buy),
                    style = MaterialTheme.typography.titleMedium
                )
                Text(
                    text = stringResource(
                        R.string.total_placeholder,
                        sale.totalAmount.toMoneyFormat(2)
                    ),
                    style = MaterialTheme.typography.titleMedium
                )
            }
        }
        AnimatedVisibility(isAddProductSheetVisible) {
            ModalBottomSheet(
                sheetState = bottomSheetState2,
                onDismissRequest = { isAddProductSheetVisible = false },
            ) {
                SaveIngotSheetContent(
                    initialProduct = selectedProduct,
                    onSaveProduct = {
                        onAddIngot(it)
                        isAddProductSheetVisible = false
                    }
                )
            }
        }
    }
}