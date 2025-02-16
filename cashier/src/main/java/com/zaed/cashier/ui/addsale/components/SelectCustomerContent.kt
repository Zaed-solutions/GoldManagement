package com.zaed.cashier.ui.addsale.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Mail
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PersonOutline
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.zaed.common.R
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
        modifier = modifier.fillMaxSize().padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalAlignment = Alignment.Start
    ) {
        Text(
            text = stringResource(R.string.customer_information),
            style = MaterialTheme.typography.headlineMedium,
        )
        TextInputTextField(
            modifier = Modifier.fillMaxWidth().padding(top = 24.dp),
            value = sale.customerName,
            containerColor = MaterialTheme.colorScheme.surfaceContainer,
            onValueChange = onUpdateCustomerName,
            placeHolder = stringResource(R.string.customer_name),
            imageVector = Icons.Default.Person,
        )
        TextInputTextField(
            modifier = Modifier.fillMaxWidth(),
            value = sale.customerPhoneNumber,
            containerColor = MaterialTheme.colorScheme.surfaceContainer,
            onValueChange = onUpdateCustomerPhone,
            placeHolder = stringResource(R.string.customer_phone),
            imageVector = Icons.Default.Phone,
        )
        TextInputTextField(
            modifier = Modifier.fillMaxWidth(),
            value = sale.customerEmail,
            containerColor = MaterialTheme.colorScheme.surfaceContainer,
            onValueChange = onUpdateCustomerEmail,
            placeHolder = stringResource(R.string.customer_email),
            imageVector = Icons.Default.Mail,
        )
    }

}