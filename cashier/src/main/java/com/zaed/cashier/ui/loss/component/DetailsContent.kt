package com.zaed.cashier.ui.loss.component

import LossItem
import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.zaed.common.data.model.loss.StoreLoss

@Composable
fun DetailsContent(
    onBack: () -> Unit,
    date: String,
    losses: List<StoreLoss>,
    onEdit: (loss: StoreLoss) -> Unit = {},
    onDelete: (loss: StoreLoss) -> Unit = {}
) {
    Surface(
        modifier = Modifier.padding(4.dp),
        onClick = onBack,
        shape = MaterialTheme.shapes.medium
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 8.dp)
        ) {
            Log.d("LossItem", "losses: ${losses.map { it.value }}")
            LossItemTopRow(date, losses)
            losses.forEachIndexed { index, _ ->
                val loss = losses[index]
                LossItem(
                    loss = loss,
                    onClickEdit = {
                        Log.d("LossItem", "onClickEdit: ${loss.value}")
                        onEdit(loss)
                    },
                    onClickDelete = {
                        Log.d("LossItem", "onClickDelete: ${loss.value}")
                        onDelete(loss)
                    }
                )
            }
        }
    }
}