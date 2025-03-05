package com.zaed.common.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.zaed.common.data.model.loss.DatedLoss
import com.zaed.common.data.model.loss.Loss

@Composable
fun DatedLossesList(
    modifier: Modifier = Modifier,
    isLoading: Boolean,
    datedLosses: List<DatedLoss>,
    isDeleteEnabled: Boolean = false,
    onDeleteLoss: (Loss) -> Unit = {},
    isEditEnabled: Boolean = false,
    onUpdateLoss: (Loss) -> Unit = {}
) {
    ListWithLoading(isLoading = isLoading) {
        LazyColumn(
            modifier = Modifier.fillMaxWidth(),
            contentPadding = PaddingValues(vertical = 16.dp),
        ) {
            items(
                items = datedLosses,
                key = { it.formattedDate }
            ) {
                DatedLossItem(
                    modifier = Modifier.animateItem(),
                    datedLoss = it,
                    isDeleteEnabled = isDeleteEnabled,
                    onDeleteLoss = onDeleteLoss,
                    isEditEnabled = isEditEnabled,
                    onUpdateLoss = onUpdateLoss
                )
            }
        }
    }
}


