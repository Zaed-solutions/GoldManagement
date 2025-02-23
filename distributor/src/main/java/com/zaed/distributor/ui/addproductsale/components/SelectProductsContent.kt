package com.zaed.distributor.ui.addproductsale.components

import android.util.Log
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
import com.zaed.common.R
import com.zaed.common.data.model.Category
import com.zaed.common.data.model.sale.Product
import com.zaed.common.data.model.sale.WholesaleProductSale
import com.zaed.common.ui.components.ProductsList
import com.zaed.common.ui.components.SaveProductSheetContent
import com.zaed.common.ui.components.SelectCategorySheetContent

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SelectProductsContent(
    modifier: Modifier = Modifier,
    categories: List<Category>,
    sale: WholesaleProductSale,
    onAddProduct: (Product) -> Unit,
    onEditProduct: (Product) -> Unit,
    onRemoveProduct: (id: String) -> Unit,
) {

    var isAddProductSheetVisible by remember { mutableStateOf(false) }
    var isSaveProductBottomSheetVisible by remember { mutableStateOf(false) }
    val bottomSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
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
            text = stringResource(R.string.select_products),
            style = MaterialTheme.typography.headlineMedium
        )
        ProductsList(
            modifier = Modifier.padding(horizontal = 8.dp),
            products = sale.products,
            onAddProduct = { isAddProductSheetVisible = true },
            onRemoveProduct = onRemoveProduct,
            onEditProduct = {
                selectedProduct = it
                isSaveProductBottomSheetVisible = true
            }
        )
        AnimatedVisibility(isAddProductSheetVisible) {
            ModalBottomSheet(
                sheetState = bottomSheetState,
                onDismissRequest = { isAddProductSheetVisible = false },
            ) {
                SelectCategorySheetContent(
                    categories = categories,
                    onAddProduct = {
                        onAddProduct(it)
                        selectedProduct = it
                        isAddProductSheetVisible = false
                        isSaveProductBottomSheetVisible = true
                    }
                )
            }
        }
        AnimatedVisibility(isSaveProductBottomSheetVisible) {
            ModalBottomSheet(
                sheetState = bottomSheetState2,
                onDismissRequest = { isSaveProductBottomSheetVisible = false },
            ) {

                SaveProductSheetContent(
                    initialProduct = selectedProduct,
                    onSaveProduct = {
                        onEditProduct(it)
                        isSaveProductBottomSheetVisible = false
                    }
                )
            }
        }
    }
}