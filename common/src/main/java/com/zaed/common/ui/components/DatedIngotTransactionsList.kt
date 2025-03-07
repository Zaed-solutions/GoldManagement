package com.zaed.common.ui.components

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.zaed.common.data.model.sale.DatedIngotTransactions
import com.zaed.common.data.model.sale.IngotTransaction

@Composable
fun DatedIngotTransactionsList(
    modifier: Modifier = Modifier,
    isLoading: Boolean,
    datedTransactions: List<DatedIngotTransactions>,
    isEditable: Boolean = false,
    isDeletable: Boolean = false,
    onEdit: (IngotTransaction) -> Unit = {},
    onDelete: (IngotTransaction) -> Unit = {}
) {
    ListWithLoading(
        modifier = modifier,
        isLoading = isLoading,
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxWidth(),
            contentPadding = PaddingValues(vertical = 16.dp),
        ) {
            items(
                items = datedTransactions,
                key = { it.formattedDate }
            ) {
                DatedIngotTransactionItem(
                    modifier = Modifier.animateItem(),
                    datedTransaction = it,
                    isEditable = isEditable,
                    isDeletable = isDeletable,
                    onEdit = onEdit,
                    onDelete = onDelete
                )
            }
        }
    }
}


