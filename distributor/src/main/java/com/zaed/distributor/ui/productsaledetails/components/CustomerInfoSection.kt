package com.zaed.distributor.ui.productsaledetails.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.zaed.common.ui.components.DetailRow
import com.zaed.common.ui.components.TitledSection
import com.zaed.common.R

@Composable
fun CustomerInfoSection(
    modifier: Modifier = Modifier,
    customerName: String,
    customerPhone: String,
) {
    TitledSection(
        title = stringResource(R.string.customer_information)
    ) {
        Column (
            modifier = modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ){
            DetailRow(
                label = stringResource(R.string.name),
                value = customerName,
                isDividerVisible = customerPhone.isNotBlank()
            )
            DetailRow(
                label = stringResource(R.string.phone_number),
                value = customerPhone,
                isDividerVisible = false
            )
        }
    }

}