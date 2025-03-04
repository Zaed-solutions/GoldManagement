package com.zaed.common.ui.components

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.zaed.common.data.model.sale.DatedWholesaleProductSale

@Composable
fun DatedProductSalesList(
    modifier: Modifier = Modifier,
    isLoading : Boolean ,
    datedProductSales: List<DatedWholesaleProductSale>,
    onSaleClicked: (String) -> Unit
) {
    ListWithLoading(
        isLoading = isLoading,
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxWidth(),
            contentPadding = PaddingValues(vertical = 16.dp),
        ) {
            items(
                items = datedProductSales,
                key = { it.formattedDate }
            ) {
                DatedProductSalesItem(
                    modifier = Modifier.animateItem(),
                    datedProductSale = it,
                    onSaleClicked = onSaleClicked
                )
            }
        }
    }

}