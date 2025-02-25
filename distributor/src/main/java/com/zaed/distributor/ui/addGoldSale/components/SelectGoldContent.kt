package com.zaed.distributor.ui.addGoldSale.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
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
import com.zaed.common.data.model.sale.Product
import com.zaed.common.ui.components.ProductsList

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SelectGoldContent(
    modifier: Modifier = Modifier,
    sale: List<Product>,
    onAddGold: (Product) -> Unit,
    onEditGold: (Product) -> Unit,
    onRemoveGold: (id: String) -> Unit,
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
            products = sale,
            onAddProduct = { isAddProductSheetVisible = true },
            onRemoveProduct = onRemoveGold,
            label = stringResource(com.zaed.common.R.string.gold),
            onEditProduct = {
                selectedProduct = it
                isAddProductSheetVisible = true
            }
        )
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