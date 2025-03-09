package com.zaed.manager.ui.distributorssales.components

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.zaed.common.data.model.sale.StoreSale
import com.zaed.common.data.model.sale.WholesaleSale
import com.zaed.common.ui.components.ListWithLoading
import com.zaed.manager.ui.storessales.components.StoreSaleItem

@Composable
fun DistributorsSalesList(
    modifier: Modifier = Modifier,
    isLoading: Boolean,
    sales: List<WholesaleSale>,
    onSaleClicked: (WholesaleSale) -> Unit
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
                DistributorSaleItem(
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

