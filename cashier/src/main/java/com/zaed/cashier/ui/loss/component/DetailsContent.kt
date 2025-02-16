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
import com.zaed.common.data.model.Loss

@Composable
fun DetailsContent(
    onBack: () -> Unit,
    date: String,
    losses: List<Loss>,
    onEdit: (loss: Loss) -> Unit = {},
    onDelete: (loss:Loss) -> Unit = {}
) {
    Surface(
        modifier = Modifier.padding(4.dp),
        onClick = onBack,
        shape = MaterialTheme.shapes.medium
    ) {
        Column(
            modifier = Modifier.padding(8.dp)
        ) {
            LossItemTopRow(date, losses)
            losses.forEachIndexed { index, _ ->
                val loss = losses[index]
                LossItem(
                    loss = loss,
                    onClickEdit = { onEdit(loss) },
                    onClickDelete = {
                        Log.d("LossItem", "onClickDelete: $loss")
                        Log.d("LossItem", "losses: ${losses.map { it.id }}")
                        onDelete(loss)
                    }
                )
            }
        }
    }
}