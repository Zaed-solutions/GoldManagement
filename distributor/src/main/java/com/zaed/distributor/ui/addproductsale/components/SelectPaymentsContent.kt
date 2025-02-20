package com.zaed.distributor.ui.addproductsale.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.zaed.common.R
import com.zaed.common.data.model.payment.Payment
import com.zaed.common.ui.components.DetailRow
import com.zaed.common.ui.util.formatMoney

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SelectPaymentsContent(
    modifier: Modifier = Modifier,
    totalAmount: Double,
    payments: List<Payment>,
    onAddPayment: (Payment) -> Unit ={},
    onRemovePayment: (Payment) -> Unit={},
    onEditPayment: (Payment) -> Unit={},
) {
    val totalPaid = remember(payments) { payments.sumOf { it.amount }.formatMoney() }
    var isBottomSheetVisible by remember { mutableStateOf(false) }
    val bottomSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalAlignment = Alignment.Start
    ) {
        Text(
            modifier = Modifier.padding(horizontal = 16.dp),
            text = stringResource(R.string.add_payments),
            style = MaterialTheme.typography.headlineMedium
        )
        DetailRow(
            modifier = Modifier.padding(top = 16.dp),
            label = stringResource(R.string.total_amount),
            value = totalAmount.formatMoney(),
        )
        DetailRow(
            label = stringResource(R.string.total_paid),
            value = totalPaid,
            isDividerVisible = false
        )
        Text(
            text = stringResource(R.string.all_unpaid_amount_will_be_considered_as_future_payment),
            style = MaterialTheme.typography.bodySmall,
        )
        //payments list
        //add payment bottom sheet
        AnimatedVisibility(isBottomSheetVisible) {
            ModalBottomSheet(
                onDismissRequest = { isBottomSheetVisible = false },
            ) {
//                SavePaymentBottomSheetContent()
            }
        }
    }
}


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
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
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
            ) {
                TODO()
            }
        }
    }

}