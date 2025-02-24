package com.zaed.distributor.ui.addproductsale.components

import android.util.Log
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
import com.zaed.common.data.model.payment.MoneyPayment
import com.zaed.distributor.ui.customerdetails.component.PaymentItem

@Composable
fun PaymentsList(
    modifier: Modifier = Modifier,
    moneyPayments: List<MoneyPayment>,
    onAddPayment: () -> Unit,
    onEditPayment: (MoneyPayment) -> Unit,
    onRemovePayment: (MoneyPayment) -> Unit
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
                items = moneyPayments,
                key = { it.id }
            ) { payment ->
                PaymentItem(
                    payment = payment,
                    onEdit = {
                        onEditPayment(payment)
                    },
                    onDelete = {
                        Log.d("find the issue", "paymentSent: $payment")
                        onRemovePayment(payment)
                    }
                )
            }
        }
    }

}