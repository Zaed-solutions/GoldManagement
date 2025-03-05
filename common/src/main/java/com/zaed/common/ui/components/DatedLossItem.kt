package com.zaed.common.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Animatable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.zaed.common.data.model.loss.DatedLoss
import com.zaed.common.data.model.loss.DistributorLoss
import com.zaed.common.data.model.loss.Loss
import com.zaed.common.ui.util.DateFormat
import com.zaed.common.ui.util.format
import com.zaed.common.ui.util.formatMoney
import java.util.Date

@Composable
fun DatedLossItem(
    modifier: Modifier = Modifier,
    datedLoss: DatedLoss,
    isDeleteEnabled: Boolean,
    onDeleteLoss: (Loss) -> Unit,
    isEditEnabled: Boolean,
    onUpdateLoss: (Loss) -> Unit
) {
    var isExpanded by remember {
        mutableStateOf(false)
    }
    val anim = remember {
        Animatable(0f)
    }
    LaunchedEffect(isExpanded) {
        anim.animateTo(
            targetValue = if (isExpanded) 180f else 0f
        )
    }
    Column (
        modifier = modifier.fillMaxWidth()
    ){
        Surface(
            modifier = Modifier.fillMaxWidth(),
            color = MaterialTheme.colorScheme.background,
            onClick = { isExpanded = !isExpanded },
        ) {
            Column(
                modifier = Modifier.padding(vertical = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = datedLoss.formattedDate,
                        style = MaterialTheme.typography.titleLarge,
                        modifier = Modifier.weight(1f)
                    )
                    Text(
                        text = datedLoss.totalLosses.formatMoney(),
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontSize = 20.sp,
                            fontWeight = FontWeight.ExtraBold
                        ),
                    )
                    Icon(
                        imageVector = Icons.Default.KeyboardArrowDown,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier
                            .rotate(anim.value)
                    )
                }
                AnimatedVisibility(isExpanded) {
                    Column(
                        modifier = Modifier.fillMaxWidth().padding(top = 8.dp),
                    ) {
                        datedLoss.losses.forEach { loss ->
                            LossItem(
                                loss = loss ,
                                isDeleteEnabled = isDeleteEnabled,
                                onDeleteLoss = { onDeleteLoss(loss) },
                                isEditEnabled = isEditEnabled,
                                onUpdateLoss = { onUpdateLoss(loss) }
                            )
                        }
                    }
                }
            }
        }
        HorizontalDivider(thickness = 1.dp)
    }
}

@Preview(showSystemUi = true, showBackground = true, device = "id:pixel_9_pro")
@Composable
private fun Preview() {
    val loss = DatedLoss(
        formattedDate = Date().format(DateFormat.DATE),
        totalLosses = 300.0,
        losses = listOf(
            DistributorLoss(
                id = "1",
                value = 100.0,
                allowance = false,
                reason = "Fuel"
            ),
            DistributorLoss(
                id = "2",
                value = 200.0,
                allowance = true,
                reason = "Food"
            ),
        )
    )
    DatedLossItem(
        modifier = Modifier.padding(vertical = 48.dp),
        datedLoss = loss,
        onDeleteLoss = { },
        isEditEnabled = true,
        isDeleteEnabled = true,
    ) { }
}