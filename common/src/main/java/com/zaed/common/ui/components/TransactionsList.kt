package com.zaed.common.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.zaed.common.data.model.sale.WholesaleTransaction
import com.zaed.common.ui.addpurchase.ProductType

@Composable
fun SalesList(
    modifier: Modifier = Modifier,
    listState: LazyListState = LazyListState(),
    isLoading: Boolean,
    sales: List<WholesaleTransaction>,
    onSaleClicked: (id: String, isProduct: Boolean) -> Unit,
    onDeleteSale: (id: String, isProduct: Boolean) -> Unit,
    onEditSale: (id: String, isProduct: Boolean) -> Unit
) {
    ListWithLoading(
        isLoading = isLoading
    ) {
        LazyColumn(
            state = listState,
            modifier = modifier.fillMaxSize(),
            contentPadding = PaddingValues(vertical = 16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(
                items = sales,
                key = { it.id }
            ) { sale ->
                SaleItem(
                    modifier = Modifier.animateItem(),
                    transaction = sale,
                    onSaleClicked = {
                        onSaleClicked(sale.id, sale.productType == ProductType.PRODUCT)
                    },
                    onDelete = {
                        onDeleteSale(sale.id, sale.productType == ProductType.PRODUCT)
                    },
                    onEdit = {
                        onEditSale(sale.id, sale.productType == ProductType.PRODUCT)
                    },
                    isDeletable = true,
                    isEditable = true,
                    isDividerVisible = true
                )
            }
        }
    }
}

