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
import com.zaed.common.data.model.sale.DatedSales
import com.zaed.common.data.model.sale.Transaction
import com.zaed.common.data.model.sale.StoreTransaction
import com.zaed.common.data.model.sale.WholesaleGoldTransaction
import com.zaed.common.data.model.sale.WholesaleProductTransaction
import com.zaed.common.ui.util.formatMoney

@Composable
fun DatedSalesItem(
    modifier: Modifier = Modifier,
    datedSale: DatedSales,
    onSaleClicked: (String,String) -> Unit,
    isEditable: Boolean = false,
    onEdit: (Transaction) -> Unit= {},
    isDeletable: Boolean = false,
    onDelete: (Transaction) -> Unit = {},
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
                        text = datedSale.formattedDate,
                        style = MaterialTheme.typography.titleLarge,
                        modifier = Modifier.weight(1f)
                    )
                    Text(
                        text = datedSale.totalAmount.formatMoney(),
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
                        datedSale.transactions.forEach { sale ->
                            TransactionItem(
                                transaction = sale,
                                onTransactionClicked = {
                                    when(sale){
                                        is WholesaleProductTransaction -> onSaleClicked(sale.id,sale::class.qualifiedName?:"")
                                        is WholesaleGoldTransaction -> onSaleClicked(sale.id,sale::class.qualifiedName?:"")
                                        is StoreTransaction -> onSaleClicked(sale.id,sale::class.qualifiedName?:"")
                                    }
                                },
                                isDividerVisible = false,
                                isEditable = isEditable,
                                onEdit = {
                                    onEdit(sale)
                                },
                                isDeletable = isDeletable,
                                onDelete = {
                                    onDelete(sale)
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