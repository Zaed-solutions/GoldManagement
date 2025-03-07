package com.zaed.distributor.ui.addGoldSale.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.zaed.common.R
import com.zaed.common.data.model.payment.Payment
import com.zaed.common.ui.components.PaymentItem

@Composable
fun PaymentsList(
    modifier: Modifier = Modifier,
    payments: List<Payment>,
    onAddPayment: () -> Unit,
    onEditPayment: (Payment) -> Unit,
    onRemovePayment: (Payment) -> Unit
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = stringResource(R.string.payments),
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.weight(1f)
            )
            IconButton(onClick = { onAddPayment() }) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add Payment"
                )
            }
        }
        LazyColumn(
            modifier = Modifier.fillMaxWidth(),
            contentPadding = PaddingValues(vertical = 16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            items(
                items = payments,
                key = { it.id }
            ) { payment ->
                PaymentItem(
                    payment = payment,
                    onEdit = {
                        onEditPayment(payment)
                    },
                    onDelete = {
                        onRemovePayment(payment)
                    }
                )
            }
        }
    }

}