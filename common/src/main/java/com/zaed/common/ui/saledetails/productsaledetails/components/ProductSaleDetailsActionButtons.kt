package com.zaed.common.ui.saledetails.productsaledetails.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.zaed.common.R

@Composable
fun ProductSaleDetailsActionButtons(
    modifier: Modifier = Modifier,
    onShareClicked: () -> Unit,
    enabled: Boolean,
    onPrintClicked: () -> Unit,
) {
    Column (
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            OutlinedButton(
                modifier = Modifier.weight(1f).heightIn(min = 48.dp),
                enabled = enabled,
                onClick = {
                    onShareClicked()
                }
            ) {
                Text(
                    text = stringResource(R.string.share)
                )
            }
            Button(
                modifier = Modifier.weight(1f).heightIn(min = 48.dp),
                enabled = enabled,
                onClick = {
                    onPrintClicked()
                }
            ) {
                Text(
                    text = stringResource(R.string.print)
                )
            }
        }
        if(!enabled) {
            Text(
                text = stringResource(R.string.sale_must_be_paid_to_print_or_share_receipt),
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}