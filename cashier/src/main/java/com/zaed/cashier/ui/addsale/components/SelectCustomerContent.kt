package com.zaed.cashier.ui.addsale.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Mail
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PersonOutline
import androidx.compose.material.icons.filled.Phone
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.zaed.cashier.R
import com.zaed.common.data.model.StoreSale
import com.zaed.common.ui.components.TextInputTextField

@Composable
fun SelectCustomerContent(
    modifier: Modifier = Modifier,
    sale: StoreSale,
    onUpdateCustomerName: (String) -> Unit,
    onUpdateCustomerPhone: (String) -> Unit,
    onUpdateCustomerEmail: (String) -> Unit,
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TextInputTextField(
            modifier = Modifier.fillMaxWidth(),
            value = sale.customerName,
            onValueChange = onUpdateCustomerName,
            label = stringResource(R.string.customer_name),
            imageVector = Icons.Default.Person,
        )
        TextInputTextField(
            modifier = Modifier.fillMaxWidth(),
            value = sale.customerPhoneNumber,
            onValueChange = onUpdateCustomerPhone,
            label = stringResource(R.string.customer_phone),
            imageVector = Icons.Default.Phone,
        )
        TextInputTextField(
            modifier = Modifier.fillMaxWidth(),
            value = sale.customerEmail,
            onValueChange = onUpdateCustomerEmail,
            label = stringResource(R.string.customer_email),
            imageVector = Icons.Default.Mail,
        )
    }

}