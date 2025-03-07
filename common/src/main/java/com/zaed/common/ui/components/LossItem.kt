package com.zaed.common.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.zaed.common.data.model.loss.DistributorLoss
import com.zaed.common.data.model.loss.Loss
import com.zaed.common.ui.util.formatMoney

@Composable
fun LossItem(
    modifier: Modifier = Modifier,
    loss: Loss,
    isDeleteEnabled: Boolean,
    onDeleteLoss: () -> Unit,
    isEditEnabled: Boolean,
    onUpdateLoss: () -> Unit
) {
    SwipeToEditOrDeleteContainer(
        modifier = modifier,
        onDelete = onDeleteLoss,
        isEditEnabled = isEditEnabled,
        isDeleteEnabled = isDeleteEnabled,
        onEdit = onUpdateLoss
    ) {
        Surface (
            color = MaterialTheme.colorScheme.background
        ){
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    modifier = Modifier.weight(1f),
                    maxLines = 1,
                    text = if (loss is DistributorLoss && loss.allowance) stringResource(com.zaed.common.R.string.daily_allowance) else loss.reason,
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontSize = 20.sp,
                    )
                )
                Text(
                    text = loss.value.formatMoney(),
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontSize = 20.sp,
                        fontWeight = FontWeight.ExtraBold
                    )
                )
            }
        }
    }
}

@Preview(showSystemUi = true, showBackground = true, device = "id:pixel_9_pro")
@Composable
private fun Preview() {
    LossItem(
        modifier = Modifier.padding(vertical = 48.dp),
        loss = DistributorLoss(
            id = "1",
            value = 100.0,
            allowance = true,
            reason = "Fuel"
        ),
        onDeleteLoss = {},
        isDeleteEnabled = true,
        isEditEnabled = true,
        onUpdateLoss = {}
    )
}