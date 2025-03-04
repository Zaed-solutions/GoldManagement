package com.zaed.cashier.ui.sales.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.zaed.common.data.model.sale.StoreSale
import com.zaed.common.ui.components.ListWithLoading
import com.zaed.common.ui.components.StoreSaleItem
import com.zaed.common.ui.components.SwipeToEditOrDeleteContainer

@Composable
fun SalesList(
    modifier: Modifier = Modifier,
    isLoading: Boolean,
    sales: List<StoreSale>,
    onSaleClicked: (String) -> Unit,
    onDeleteSale: (StoreSale) -> Unit,
    onEditSale: (StoreSale) -> Unit
) {
    ListWithLoading(
        isLoading = isLoading
    ) {
        LazyColumn(
            modifier = modifier.fillMaxSize(),
            contentPadding = PaddingValues(vertical = 16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(
                items = sales,
                key = { it.id }
            ) { sale ->
                SwipeToEditOrDeleteContainer(
                    modifier = Modifier.animateItem(),
                    onDelete = {
                        onDeleteSale(sale)
                    },
                    isEditEnabled = true,
                    onEdit = {
                        onEditSale(sale)
                    }
                ) {
                    StoreSaleItem(
                        modifier = Modifier.padding(horizontal = 16.dp),
                        sale = sale,
                        onSaleClicked = { onSaleClicked(sale.id) }
                    )
                }
            }
        }
    }
}

