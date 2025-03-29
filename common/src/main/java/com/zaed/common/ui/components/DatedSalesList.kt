package com.zaed.common.ui.components

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.zaed.common.data.model.sale.DatedSales
import com.zaed.common.data.model.sale.Transaction
import com.zaed.common.ui.addpurchase.ProductType

@Composable
fun DatedSalesList(
    modifier: Modifier = Modifier,
    isLoading : Boolean,
    datedSales: List<DatedSales>,
    onSaleClicked: (String,ProductType) -> Unit,
    isEditable: Boolean = false,
    onEdit: (Transaction) -> Unit= {},
    isDeletable: Boolean = false,
    onDelete: (Transaction) -> Unit = {},
) {
    ListWithLoading(
        modifier = modifier,
        isLoading = isLoading,
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxWidth(),
            contentPadding = PaddingValues(top = 16.dp, bottom = 54.dp),
        ) {
            items(
                items = datedSales,
                key = { it.formattedDate }
            ) {
                DatedSalesItem(
                    modifier = Modifier.animateItem(),
                    datedSale = it,
                    onSaleClicked = onSaleClicked,
                    isEditable = isEditable,
                    onEdit = onEdit,
                    isDeletable = isDeletable,
                    onDelete = onDelete
                )
            }
        }
    }

}