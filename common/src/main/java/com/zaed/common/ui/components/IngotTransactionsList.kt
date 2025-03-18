package com.zaed.common.ui.components

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.zaed.common.data.model.sale.IngotTransaction

@Composable
fun IngotTransactionsList(
    modifier: Modifier = Modifier,
    isLoading: Boolean,
    transactions: List<IngotTransaction>,
    isEditable: Boolean,
    onEdit: (IngotTransaction) -> Unit,
    isDeletable: Boolean,
    onDelete: (IngotTransaction) -> Unit
) {
    ListWithLoading(
        modifier = modifier,
        isLoading = isLoading
    ) {
        LazyColumn (
            modifier = Modifier.fillMaxWidth(),
            contentPadding = PaddingValues(vertical = 16.dp)
        ){
            items(
                items= transactions,
                key = {it.id}
            ){transaction ->
                IngotTransactionItem(
                    transaction = transaction,
                    isDividerVisible = true,
                    isEditable = isEditable,
                    isDeletable = isDeletable,
                    onEdit = {
                        onEdit(transaction)
                    },
                    onDelete = {
                        onDelete(transaction)
                    }
                )
            }
        }
    }

}