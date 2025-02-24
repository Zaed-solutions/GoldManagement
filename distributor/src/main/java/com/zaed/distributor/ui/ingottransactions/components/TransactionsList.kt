package com.zaed.distributor.ui.ingottransactions.components

import android.view.SurfaceControl
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.zaed.common.data.model.sale.IngotTransaction

@Composable
fun TransactionsList(
    modifier: Modifier = Modifier,
    isLoading: Boolean,
    transactions: List<IngotTransaction>,
    onEditTransaction: (IngotTransaction) -> Unit,
    onDeleteTransaction: (IngotTransaction) -> Unit
){

}