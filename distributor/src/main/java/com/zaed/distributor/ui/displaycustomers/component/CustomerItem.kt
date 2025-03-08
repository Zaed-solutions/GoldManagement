package com.zaed.distributor.ui.displaycustomers.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.zaed.common.data.model.customer.WholeSaleCustomer
import com.zaed.common.ui.components.MoreDropDownMenu
import com.zaed.common.ui.components.MoreDropdownItem
import com.zaed.common.ui.components.getContainerColor
import com.zaed.common.ui.components.getContentColor
import com.zaed.common.ui.components.getDebtTitle
import com.zaed.common.ui.util.toMoneyFormat
import com.zaed.common.R
import com.zaed.distributor.ui.theme.DistributorAppTheme
import kotlin.math.absoluteValue

@Composable
fun CustomerItem(
    customer: WholeSaleCustomer,
    onClick: () -> Unit = {},
    onEdit: () -> Unit,
    onDelete: () -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth(),
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
                MoreDropDownMenu(
                    items = listOf(
                        MoreDropdownItem(
                            onClick = { onEdit()},
                            title = stringResource(R.string.edit),
                            tint = MaterialTheme.colorScheme.primary,
                            icon = Icons.Default.Edit
                        ),
                        MoreDropdownItem(
                            onClick = { onDelete()},
                            title = stringResource(R.string.delete),
                            tint = MaterialTheme.colorScheme.error,
                            icon = Icons.Default.Delete
                        )
                    )
                )
            }
            HorizontalDivider(Modifier.padding(vertical = 8.dp))
            Row {
                val chipColor =
                    if (customer.inDebt) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.primary
                Text(
                    text = customer.debtAmount.absoluteValue.toMoneyFormat(2),
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = customer.debtAmount.getContainerColor()
                )
                Spacer(modifier = Modifier.weight(1f))
                FilterChip(
                    modifier = Modifier.height(FilterChipDefaults.Height - 4.dp),
                    selected = true,
                    onClick = {},
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = customer.debtAmount.getContainerColor(),
                        selectedLabelColor = customer.debtAmount.getContentColor()
                    ),
                    label = {
                        Text(
                            text = customer.debtAmount.getDebtTitle()
                        )
                    }
                )
            }
        }
    }
}

@Preview
@Composable
private fun CustomerItemPreview() {
    DistributorAppTheme {
        CustomerItem(
            customer = WholeSaleCustomer(
                id = "1",
                name = "John Doe",
                phone = "1234567890",
            ),
            onClick = {},
            onEdit = {},
            onDelete = {}
        )
    }

}