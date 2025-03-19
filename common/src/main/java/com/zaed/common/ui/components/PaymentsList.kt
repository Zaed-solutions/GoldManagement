package com.zaed.common.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.zaed.common.data.model.payment.Payment

@Composable
fun PaymentsList(
    modifier: Modifier = Modifier,
    payments: List<Payment>,
    onEditPayment: (Payment) -> Unit,
    onRemovePayment: (Payment) -> Unit
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxWidth(),
            contentPadding = PaddingValues(vertical = 16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            items(
                items = payments,
                key = { it.id }
            ) { payment ->
                Column (
                    modifier = Modifier.animateItem(),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ){
                    PaymentItem(
                        payment = payment,
                        onEdit = { onEditPayment(payment) },
                        onDelete = { onRemovePayment(payment) }
                    )
                    HorizontalDivider()
                }
            }
        }
    }

}
