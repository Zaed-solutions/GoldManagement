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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.zaed.common.data.model.sale.DatedIngotTransactions
import com.zaed.common.data.model.sale.IngotTransaction
import com.zaed.common.ui.util.formatMoney

@Composable
fun DatedIngotTransactionItem(
    modifier: Modifier = Modifier,
    datedTransaction: DatedIngotTransactions,
    isEditable: Boolean = false,
    isDeletable: Boolean = false,
    onEdit: (IngotTransaction) -> Unit = {},
    onDelete: (IngotTransaction) -> Unit = {}
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
    Column(
        modifier = modifier.fillMaxWidth()
    ) {
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
                        text = datedTransaction.formattedDate,
                        style = MaterialTheme.typography.titleLarge,
                        modifier = Modifier.weight(1f)
                    )
                    Text(
                        text = datedTransaction.totalEarnings.formatMoney(),
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
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 8.dp),
                    ) {
                        datedTransaction.transactions.forEach { transaction ->
                            IngotTransactionItem(
                                transaction = transaction,
                                isDividerVisible = false,
                                isEditable = isEditable,
                                isDeletable = isDeletable,
                                onEdit = {
                                    onEdit(transaction)
                                },
                                onDelete = {
                                    onDelete(transaction)
                                }
                            )
                        }
                    }
                }
            }
        }
        HorizontalDivider(thickness = 1.dp)
    }
}