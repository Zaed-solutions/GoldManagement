package com.zaed.manager.ui.manufacturerorders.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.zaed.common.R
import com.zaed.common.data.model.manufacturerorder.ManufacturerOrder
import com.zaed.common.ui.components.MoreDropDownMenu
import com.zaed.common.ui.components.MoreDropdownItem
import com.zaed.common.ui.util.DateFormat
import com.zaed.common.ui.util.format

@Composable
fun ManufacturerOrderItem(
    modifier: Modifier = Modifier,
    order: ManufacturerOrder,
    onEdit: () -> Unit,
    onDelete: () -> Unit
) {
    val context = LocalContext.current
    val primaryColor = MaterialTheme.colorScheme.primary
    val errorColor = MaterialTheme.colorScheme.error
    val moreOptions = remember {
        listOf(
            MoreDropdownItem(
                onClick = onEdit,
                icon = Icons.Default.Edit,
                title = context.getString(R.string.edit),
                tint = primaryColor,
            ),
            MoreDropdownItem(
                onClick = onDelete,
                icon = Icons.Default.Delete,
                title = context.getString(R.string.delete),
                tint = errorColor,
            )
        )
    }
    Surface(
        modifier = modifier.fillMaxWidth(),
        color = MaterialTheme.colorScheme.background,
        contentColor = MaterialTheme.colorScheme.onBackground
    ) {
        Column {
            Row(
                modifier = Modifier
                    .padding(
                        vertical = 16.dp
                    )
                    .padding(start = 16.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = order.createdAt.format(DateFormat.SHORT_DATE_TIME),
                        style = MaterialTheme.typography.bodySmall.copy(color = MaterialTheme.colorScheme.onSurfaceVariant),
                    )
                    Text(
                        text = stringResource(
                            R.string.order_title_template,
                            order.orderNumber,
                            order.rawAmount,
                            if(order.receivedAmount == 0.0){
                                stringResource(R.string.none)
                            } else {
                                stringResource(
                                    R.string.grams_placeholder,
                                    order.receivedAmount.toString()
                                )
                            }
                        ),
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                    )
                    Text(
                        text = stringResource(
                            R.string.processing_fee_template,
                            order.totalProcessingFee,
                            order.paidProcessingFee
                        ),
                        style = MaterialTheme.typography.bodyMedium.copy(color = MaterialTheme.colorScheme.onSurfaceVariant),
                    )
                }
                MoreDropDownMenu(
                    items = moreOptions,
                )
            }
            HorizontalDivider(thickness = 1.dp)
        }
    }
}

@Preview(showSystemUi = true, showBackground = true, device = "id:pixel_9_pro")
@Composable
private fun Preview() {
    ManufacturerOrderItem(
        order = ManufacturerOrder(
            orderNumber = "123",
            rawAmount = 100.0,
            receivedAmount = 50.0,
            totalProcessingFee = 10.0,
            paidProcessingFee = 5.0
        ),
        onEdit = {}
    ) { }
}