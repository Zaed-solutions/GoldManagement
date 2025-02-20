package com.zaed.distributor.ui.displaycustomers.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.zaed.common.data.model.customer.WholeSaleCustomer
import com.zaed.common.ui.components.SwipeToEditOrDeleteContainer
import com.zaed.common.ui.util.toMoneyFormat

@Composable
fun CustomerItem(
    customer: WholeSaleCustomer,
    onClick: () -> Unit = {},
    onEdit: () -> Unit,
    onDelete: () -> Unit
) {
    SwipeToEditOrDeleteContainer(
        onDelete = onDelete,
        isEditEnabled = true,
        onEdit = onEdit
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .padding(4.dp),
            shadowElevation = 4.dp,
            onClick = onClick,
            border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.5f)),
            shape = MaterialTheme.shapes.medium
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
            ) {
                Row {
                    Text(
                        text = customer.name,
                    )
                    Spacer(modifier = Modifier.weight(1f))
                    Text(
                        text = customer.phone,
                    )
                }
                HorizontalDivider(Modifier.padding(vertical = 8.dp))
                Row {
                    val chipColor = if (customer.inDebt) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.primary
                    Text(
                        text = customer.debtAmount.toMoneyFormat(2),
                    )
                    Spacer(modifier = Modifier.weight(1f))
                    FilterChip(
                        modifier = Modifier.height(FilterChipDefaults.Height - 4.dp),
                        selected = true,
                        onClick = { /*TODO*/ },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = chipColor,
                            selectedLabelColor = contentColorFor(chipColor)
                        ),
                        label = {
                            Text(
                                text = if (customer.inDebt) "In Debt" else "Not In Debt"
                            )
                        }
                    )
                }
            }
        }
    }
}