package com.zaed.cashier.ui.addsale.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Mail
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.zaed.common.R
import com.zaed.common.data.model.sale.StoreTransaction
import com.zaed.common.ui.components.PhoneNumberTextField
import com.zaed.common.ui.components.TextInputTextField

@Composable
fun SelectCustomerContent(
    modifier: Modifier = Modifier,
    sale: StoreTransaction,
    onUpdateCustomerName: (String) -> Unit,
    onUpdateCustomerPhone: (String) -> Unit,
    onUpdateCustomerEmail: (String) -> Unit,
    onContinue: () -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
            .verticalScroll(
                rememberScrollState()
            ),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.Start
    ) {
        Text(
            text = stringResource(R.string.customer_information),
            style = MaterialTheme.typography.headlineMedium,
        )
        TextInputTextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 24.dp),
            value = sale.customerName,
            containerColor = MaterialTheme.colorScheme.surfaceContainer,
            onValueChange = onUpdateCustomerName,
            placeHolder = stringResource(R.string.customer_name),
            imageVector = Icons.Default.Person,
        )
        PhoneNumberTextField(
            modifier = Modifier.fillMaxWidth(),
            value = sale.customerPhone,
            containerColor = MaterialTheme.colorScheme.surfaceContainer,
            onValueChange = onUpdateCustomerPhone,
        )
        TextInputTextField(
            modifier = Modifier.fillMaxWidth(),
            value = sale.customerEmail,
            containerColor = MaterialTheme.colorScheme.surfaceContainer,
            onValueChange = onUpdateCustomerEmail,
            placeHolder = stringResource(R.string.customer_email),
            imageVector = Icons.Default.Mail,
        )
        Spacer(modifier = Modifier.weight(1f))
        Button(
            onClick = onContinue,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 24.dp),
            shape = RoundedCornerShape(4.dp),
        ) {
            Text(
                text = stringResource(R.string.continue_),
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
        }
    }

}