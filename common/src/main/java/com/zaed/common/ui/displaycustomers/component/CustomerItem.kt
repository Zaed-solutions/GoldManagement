package com.zaed.common.ui.displaycustomers.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.zaed.common.R
import com.zaed.common.data.model.customer.WholeSaleCustomer
import com.zaed.common.ui.components.MoreDropDownMenu
import com.zaed.common.ui.components.MoreDropdownItem
import com.zaed.common.ui.components.getContainerColor
import com.zaed.common.ui.components.getContentColor
import com.zaed.common.ui.components.getDebtTitle
import com.zaed.common.ui.util.toMoneyFormat
import kotlin.math.absoluteValue

@Composable
fun CustomerItem(
    modifier: Modifier = Modifier,
    customer: WholeSaleCustomer,
    onClick: () -> Unit = {},
    onEdit: () -> Unit,
    onDelete: () -> Unit
) {
    Surface(
        modifier = modifier
            .fillMaxWidth(),
        onClick = onClick,
    ) {
        Column {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 16.dp, top = 8.dp, bottom = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    Modifier
                        .padding(end = 4.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f))

                ) {
                    Text(
                        text = customer.name.first().toString(),
                        style = MaterialTheme.typography.titleLarge,
                        modifier = Modifier.padding(vertical = 8.dp, horizontal = 16.dp),
                        color = MaterialTheme.colorScheme.primary
                    )
                }
                Column(
                    modifier = Modifier.weight(1f),
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = customer.name,
                            style = MaterialTheme.typography.titleLarge
                        )
                        Spacer(modifier = Modifier.weight(1f))
                        Text(
                            text = customer.phone,
                        )
                        MoreDropDownMenu(
                            items = listOf(
                                MoreDropdownItem(
                                    onClick = { onEdit() },
                                    title = stringResource(R.string.edit),
                                    tint = MaterialTheme.colorScheme.primary,
                                    icon = Icons.Default.Edit
                                ),
                                MoreDropdownItem(
                                    onClick = { onDelete() },
                                    title = stringResource(R.string.delete),
                                    tint = MaterialTheme.colorScheme.error,
                                    icon = Icons.Default.Delete
                                )
                            )
                        )
                    }
                    Row {
                        Text(
                            text = customer.moneyDebtAmount.absoluteValue.toMoneyFormat(2),
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = customer.moneyDebtAmount.getContainerColor()
                        )
                        Spacer(modifier = Modifier.weight(1f))
                        FilterChip(
                            modifier = Modifier.height(FilterChipDefaults.Height - 4.dp)
                                .padding(end = 16.dp),
                            selected = true,
                            onClick = {},
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = customer.moneyDebtAmount.getContainerColor(),
                                selectedLabelColor = customer.moneyDebtAmount.getContentColor()
                            ),
                            label = {
                                Text(
                                    text = customer.moneyDebtAmount.getDebtTitle()
                                )
                            }
                        )
                    }
                }
            }
            HorizontalDivider(thickness = 1.dp)
        }
    }
}

@Preview
@Composable
private fun CustomerItemPreview() {
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