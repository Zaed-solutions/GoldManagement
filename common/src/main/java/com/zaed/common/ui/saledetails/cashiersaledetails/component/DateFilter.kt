package com.zaed.cashier.ui.saledetails.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FilterAlt
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.zaed.common.ui.util.DateFormat
import com.zaed.common.ui.util.format
import java.util.Date

@Composable
fun DateFilter(
    modifier: Modifier = Modifier,
    date: Date,
    onChangeDate: () -> Unit
) {
    Row(
        modifier = modifier.fillMaxWidth().padding(horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = stringResource(com.zaed.common.R.string.sales_of, date.format(DateFormat.DATE)),
            style = MaterialTheme.typography.titleMedium
        )
        IconButton(
            onClick = onChangeDate
        ) {
            Icon(
                imageVector = Icons.Default.FilterAlt,
                contentDescription = "Filter icon"
            )
        }
    }
}