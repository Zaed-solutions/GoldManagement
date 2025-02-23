package com.zaed.distributor.ui.losses.components

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
import com.zaed.common.data.model.loss.DistributorLoss

@Composable
fun DatedLossesList(
    modifier: Modifier = Modifier,
    isLoading: Boolean,
    datedLosses: List<DatedLoss>,
    onDeleteLoss: (DistributorLoss) -> Unit,
    onUpdateLoss: (DistributorLoss) -> Unit
) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.TopCenter
    ) {
        AnimatedVisibility(isLoading) {
            CircularProgressIndicator(
                modifier = Modifier
                    .size(24.dp)
                    .padding(top = 48.dp)
            )
        }
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
                    onDeleteLoss = onDeleteLoss,
                    onUpdateLoss = onUpdateLoss
                )
            }
        }
    }
}


