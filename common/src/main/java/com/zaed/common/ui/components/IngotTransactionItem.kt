package com.zaed.common.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.zaed.common.R
import com.zaed.common.data.model.sale.IngotTransaction
import com.zaed.common.data.model.sale.Karat
import com.zaed.common.data.model.sale.TransactionType
import com.zaed.common.ui.theme.GoldManagementTheme
import com.zaed.common.ui.util.formatMoney

@Composable
fun IngotTransactionItem(
    modifier: Modifier = Modifier,
    transaction: IngotTransaction,
    isDividerVisible: Boolean = true,
    isEditable: Boolean = false,
    isDeletable: Boolean = false,
    onEdit: () -> Unit = {},
    onDelete: () -> Unit = {}
) {
    val context = LocalContext.current
    val icon = remember(transaction.type) {
        when (transaction.type) {
            TransactionType.PURCHASE -> Icons.Default.ArrowDownward
            TransactionType.SALE -> Icons.Default.ArrowUpward
        }
    }
    val primaryColor = MaterialTheme.colorScheme.primary
    val errorColor = MaterialTheme.colorScheme.error
    val moreOptions = remember(isEditable, isDeletable) {
        val list = mutableListOf<MoreDropdownItem>()
        if (isEditable) {
            list.add(
                MoreDropdownItem(
                    onClick = onEdit,
                    icon = Icons.Default.Edit,
                    title = context.getString(R.string.edit),
                    tint = primaryColor,
                )
            )
        }
        if (isDeletable) {
            list.add(
                MoreDropdownItem(
                    onClick = onDelete,
                    icon = Icons.Default.Delete,
                    title = context.getString(R.string.delete),
                    tint = errorColor,
                )
            )
        }
        list
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
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSecondary,
                    modifier = Modifier
                        .size(40.dp)
                        .background(
                            color = MaterialTheme.colorScheme.secondary,
                            shape = CircleShape
                        )
                        .padding(8.dp)
                )
                Column(
                    modifier = Modifier
                        .padding(start = 16.dp)
                        .weight(1f),
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Text(
                            text = stringResource(
                                R.string.ingot_transaction_title_placeholder,
                                transaction.grams,
                                transaction.karat.value
                            ),
                            style = MaterialTheme.typography.titleMedium.copy(fontSize = 18.sp),
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            modifier = Modifier.weight(1f)
                        )
                        Text(
                                text = transaction.totalEarning.formatMoney(),
                        style = MaterialTheme.typography.titleMedium.copy(
                            color = MaterialTheme.colorScheme.primary,
                            fontWeight = FontWeight.ExtraBold,
                            fontSize = 18.sp
                        ),
                        )
                        if (moreOptions.isNotEmpty()) {
                            MoreDropDownMenu(
                                items = moreOptions,
                            )
                        }
                    }
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Text(
                            text = stringResource(R.string.total_amount) + ":",
                            style = MaterialTheme.typography.bodyMedium.copy(
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            ),
                        )
                        Text(
                            modifier = Modifier.padding(start = 4.dp),
                            text = transaction.totalAmount.formatMoney(),
                            style = MaterialTheme.typography.bodyMedium.copy(
                                color = MaterialTheme.colorScheme.onSurface
                            ),
                        )
                    }
                }



            }
            if (isDividerVisible) {
                HorizontalDivider(thickness = 1.dp)
            }
        }
    }
}


@Preview(showSystemUi = true, showBackground = true, device = "id:pixel_9_pro")
@Composable
private fun Preview() {
    GoldManagementTheme {
        IngotTransactionItem(
            modifier = Modifier.padding(vertical = 24.dp),
            transaction = IngotTransaction(
                id = "1",
                grams = 10.0,
                sellingGramPrice = 300.0,
                buyingGramPrice = 200.0,
                karat = Karat.K24,
                type = TransactionType.SALE
            ),
            isEditable = true,
            isDeletable = true
        )
    }
}