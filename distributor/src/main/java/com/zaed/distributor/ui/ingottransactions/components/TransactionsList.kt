package com.zaed.distributor.ui.ingottransactions.components

import android.view.SurfaceControl
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.zaed.common.data.model.sale.IngotTransaction
import com.zaed.common.ui.components.SwipeToEditOrDeleteContainer

@Composable
fun TransactionsList(
    modifier: Modifier = Modifier,
    isLoading: Boolean,
    transactions: List<IngotTransaction>,
    onEditTransaction: (IngotTransaction) -> Unit,
    onDeleteTransaction: (IngotTransaction) -> Unit
){
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.TopCenter,
    ){
        AnimatedVisibility(isLoading) {
            CircularProgressIndicator(modifier = Modifier.size(24.dp).padding(top = 64.dp))
        }
        LazyColumn (
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(vertical = 16.dp)
        ){
            items(transactions){ transaction ->
                SwipeToEditOrDeleteContainer(
                    onDelete = { onDeleteTransaction(transaction) },
                    onEdit = { onEditTransaction(transaction) },
                    isEditEnabled = true
                ) {
                    Column(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        TransactionItem(
                            transaction = transaction,
                        )
                        HorizontalDivider()
                    }
                }
            }
        }
    }

}