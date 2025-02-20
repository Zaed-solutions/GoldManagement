package com.zaed.distributor.ui.customerdetails.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.zaed.common.R
import com.zaed.common.data.model.customer.WholeSaleCustomer
import com.zaed.common.ui.components.DetailRow

@Composable
fun CustomerDetailsHeader(
    modifier: Modifier = Modifier,
    customer : WholeSaleCustomer,

    ) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Text(
            text = customer.name,
            style = MaterialTheme.typography.titleLarge,
        )

        DetailRow(
            label = stringResource(R.string.phone_number),
            value = customer.phone,
        )
        DetailRow(
            label = stringResource(R.string.city),
            value = customer.city,
        )
        DetailRow(
            label = stringResource(R.string.zone),
            value = customer.zone.name,
        )
    }
}