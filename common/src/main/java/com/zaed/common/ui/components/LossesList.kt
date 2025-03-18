package com.zaed.common.ui.components

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.zaed.common.data.model.loss.Loss

@Composable
fun LossesList(
    modifier: Modifier = Modifier,
    isLoading: Boolean,
    losses: List<Loss>,
    isEditable: Boolean,
    onEdit: (Loss) -> Unit,
    isDeletable: Boolean,
    onDelete: (Loss) -> Unit
){
    ListWithLoading(
        modifier = modifier,
        isLoading = isLoading
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxWidth(),
            contentPadding = PaddingValues(vertical = 16.dp)
        ) {
            items(
                items = losses,
                key = {it.id}
            ) { loss ->
                LossItem(
                    loss = loss ,
                    isDeleteEnabled = isDeletable,
                    onDeleteLoss = { onDelete(loss) },
                    isEditEnabled = isEditable,
                    onUpdateLoss = { onEdit(loss) }
                )
            }
        }
    }

}