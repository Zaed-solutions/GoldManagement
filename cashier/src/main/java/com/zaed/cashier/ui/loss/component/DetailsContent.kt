package com.zaed.cashier.ui.loss.component

import LossItem
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
    onEdit: (id: String) -> Unit = {},
    onDelete: (id: String) -> Unit = {}
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
            losses.forEach {
                LossItem(
                    loss = it,
                    onClickEdit = { onEdit(it.id) },
                    onClickDelete = { onDelete(it.id) }
                )
            }
        }
    }
}