package com.zaed.manager.ui.storessales.components

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.zaed.common.data.model.sale.StoreTransaction
import com.zaed.common.ui.components.ListWithLoading

@Composable
fun StoresSalesList(
    modifier: Modifier = Modifier,
    isLoading: Boolean,
    sales: List<StoreTransaction>,
    onSaleClicked: (StoreTransaction) -> Unit
) {
    ListWithLoading(
        modifier = modifier,
        isLoading = isLoading
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(vertical = 16.dp)
        ) {
            items(
                items = sales,
                key = { it.id }
            ) { sale ->
                StoreSaleItem(
                    modifier = Modifier.animateItem(),
                    sale = sale,
                    onClick = {
                        onSaleClicked(sale)
                    }
                )
            }
        }
    }
}

