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
import com.zaed.common.data.model.sale.Transaction
import com.zaed.common.data.model.sale.WholesaleProductTransaction

@Composable
fun TransactionsList(
    modifier: Modifier = Modifier,
    listState: LazyListState = LazyListState(),
    isLoading: Boolean,
    transactions: List<Transaction>,
    onTransactionClicked: (id: String, isProduct: Boolean) -> Unit,
    onDeleteTransaction: (id: String, isProduct: Boolean) -> Unit,
    onEditTransaction: (id: String, isProduct: Boolean) -> Unit
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
                items = transactions,
                key = { it.id }
            ) { transaction ->
                TransactionItem(
                    modifier = Modifier.animateItem(),
                    transaction = transaction,
                    onTransactionClicked = {
                        onTransactionClicked(transaction.id, transaction is WholesaleProductTransaction)
                    },
                    onDelete = {
                        onDeleteTransaction(transaction.id, transaction is WholesaleProductTransaction)
                    },
                    onEdit = {
                        onEditTransaction(transaction.id, transaction is WholesaleProductTransaction)
                    },
                    isDeletable = true,
                    isEditable = true,
                    isDividerVisible = true
                )
            }
        }
    }
}

