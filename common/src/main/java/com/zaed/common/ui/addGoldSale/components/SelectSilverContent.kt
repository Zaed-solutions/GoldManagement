package com.zaed.common.ui.addGoldSale.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
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
import com.zaed.common.ui.components.NumberInputTextField
import com.zaed.common.ui.components.ProductsList
import com.zaed.common.ui.components.TextInputTextField
import com.zaed.common.ui.util.toMoneyFormat
import java.util.UUID

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SelectSilverContent(
    modifier: Modifier = Modifier,
    sale: WholesaleTransaction,
    onAddSilver: (Product) -> Unit,
    onRemoveSilver: (id: String) -> Unit,
    onNext: () -> Unit,
    isPurchase: Boolean = false,
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
            text = stringResource(com.zaed.common.R.string.add_silver),
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
            onRemoveProduct = onRemoveSilver,
            label = stringResource(com.zaed.common.R.string.gold),
            onEditProduct = {
                selectedProduct = it
                isAddProductSheetVisible = true
            },
            isPurchase = true,
            productType = ProductType.SILVER
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
                SaveSilverSheetContent(
                    isPurchase = isPurchase,
                    initialProduct = selectedProduct,
                    onSaveProduct = {
                        onAddSilver(it)
                        isAddProductSheetVisible = false
                    }
                )
            }
        }
    }
}

@Composable
fun SaveSilverSheetContent(
    modifier: Modifier = Modifier,
    isPurchase: Boolean = false,
    initialProduct: Product,
    onSaveProduct: (Product) -> Unit
) {
    var product by remember { mutableStateOf(initialProduct) }
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = stringResource(if(isPurchase) R.string.add_amount else R.string.add_product),
            style = MaterialTheme.typography.headlineMedium
        )
        if(!isPurchase){
            TextInputTextField(
                value = product.name,
                onValueChange = { value ->
                    product = product.copy(name = value)
                },
                label = stringResource(R.string.product_name),
                containerColor = MaterialTheme.colorScheme.surfaceContainer,
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(4.dp),
            )
        }
        NumberInputTextField(
            modifier = Modifier.fillMaxWidth(),
            value = product.grams,
            onValueChange = { value ->
                product = product.copy(grams = value)
            },
            label = stringResource(R.string.grams),
            containerColor = MaterialTheme.colorScheme.surfaceContainer,
        )
        NumberInputTextField(
            modifier = Modifier.fillMaxWidth(),
            value = product.gramPrice,
            onValueChange = { value ->
                product = product.copy(gramPrice = value)
            },
            label = stringResource(R.string.gram_price),

            containerColor = MaterialTheme.colorScheme.surfaceContainer,
        )

        Button(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp)
                .heightIn(min = 48.dp),
            shape = MaterialTheme.shapes.medium,
            onClick = {
                onSaveProduct(product)
            },
            enabled = product.grams > 0.0 && product.gramPrice > 0.0
        ) {
            Text(
                text = stringResource(R.string.save)
            )
        }
    }
}