package com.zaed.cashier.ui.addsale.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.zaed.cashier.R

@Composable
fun AddSaleBottomBar(
    modifier: Modifier = Modifier,
    currentPage: Int,
    customerName: String,
    onPreviousClicked: () -> Unit,
    onContinueClicked: () -> Unit,
    onAddClicked: () -> Unit,
) {
    Surface(
        modifier = modifier,
        shadowElevation = 8.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp, end = 16.dp, top = 8.dp, bottom = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            FilledTonalButton(
                modifier = Modifier.weight(1f).padding(end = 8.dp),
                onClick = onPreviousClicked,
                enabled = currentPage > 0
            ) {
                Text(
                    text = stringResource(R.string.previous),
                )
            }
            Button(
                modifier = Modifier.weight(1f).padding(start = 8.dp),
                enabled = customerName.isNotBlank(),
                onClick = {
                    if (currentPage != 2) {
                        onContinueClicked()
                    } else {
                        onAddClicked()
                    }
                }
            ) {
                Text(
                    text = if (currentPage != 2) {
                        stringResource(R.string.continue_)
                    } else {
                        stringResource(R.string.add)
                    }
                )
            }
        }
    }

}