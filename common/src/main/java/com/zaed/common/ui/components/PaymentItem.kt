package com.zaed.common.ui.components

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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.zaed.common.R
import com.zaed.common.data.model.payment.GoldPayment
import com.zaed.common.data.model.payment.Payment
import com.zaed.common.ui.util.DateFormat
import com.zaed.common.ui.util.format
import com.zaed.common.ui.util.toMoneyFormat

@Composable
fun PaymentItem(
    modifier: Modifier = Modifier,
    payment: Payment,
    onClick: () -> Unit = {},
    onDelete: () -> Unit = {},
    onEdit: () -> Unit = {},
    isEditable: Boolean = true,
    isDeletable: Boolean = true,
) {
    val context = LocalContext.current
    val primaryColor = MaterialTheme.colorScheme.primary
    val errorColor = MaterialTheme.colorScheme.error
    val tag1 = remember(payment) {
        if (!payment.given) context.getString(R.string.taken) else context.getString(R.string.given)
    }
    val (color, title, price) =
        when (payment) {
            is GoldPayment -> {
                Triple(
                    if (payment.given) errorColor else primaryColor,
                    "$tag1 " + if (payment.pricePerGram == 0.0) context.getString(R.string.not_specified_yet) else "",
                    context.getString(
                        R.string.grams_placeholder,
                        payment.givenGoldAmount.toString()
                    )
                )
            }

            else -> {
                Triple(
                    if (payment.given) errorColor else primaryColor,
                    tag1,
                    payment.amount.toMoneyFormat(2)
                )
            }
        }

    val moreOptions =
        if (isEditable && !isDeletable) {
             listOf(
                MoreDropdownItem(
                    onClick = { onEdit() },
                    icon = Icons.Default.Edit,
                    title = context.getString(R.string.edit),
                    tint = primaryColor,
                )
            )
        }  else if(isDeletable && !isEditable) {
            listOf(
                MoreDropdownItem(
                    onClick = { onDelete() },
                    icon = Icons.Default.Delete,
                    title = context.getString(R.string.delete),
                    tint = errorColor,
                )
            )
        }else if (isEditable && isDeletable) {
            listOf(
                MoreDropdownItem(
                    onClick = { onEdit() },
                    icon = Icons.Default.Edit,
                    title = context.getString(R.string.edit),
                    tint = primaryColor,
                ),
                MoreDropdownItem(
                    onClick = { onDelete() },
                    icon = Icons.Default.Delete,
                    title = context.getString(R.string.delete),
                    tint = errorColor,
                )
            )
        }else{
            emptyList()
        }
    Surface(
        modifier = modifier.fillMaxWidth(),
        onClick = onClick,
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 12.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                FilterChip(
                    modifier = Modifier.height(FilterChipDefaults.Height - 8.dp),
                    selected = true,
                    onClick = {},
                    label = { Text(text = stringResource(payment.type.titleRes)) },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = color,
                        selectedLabelColor = contentColorFor(color)
                    )
                )
                Spacer(modifier = Modifier.weight(1f))
                Text(
                    text = title, style = MaterialTheme.typography.titleMedium
                )
                if (moreOptions.isNotEmpty()) {
                    MoreDropDownMenu(
                        modifier = Modifier.padding(start = 8.dp), items = moreOptions
                    )
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = payment.createdAt.format(DateFormat.DATE_TIME),
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.weight(1f)
                )
                Text(
                    text = price,
                    style = MaterialTheme.typography.titleLarge,
                    color = color,
                    fontWeight = FontWeight.Bold
                )

            }
        }
    }
}








