package com.zaed.distributor.ui.customerdetails.component

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.produceState
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.zaed.common.data.model.payment.Payment
import com.zaed.common.ui.components.BalanceSection
import com.zaed.common.ui.components.PaymentItem

@Composable
fun PaymentsList(
    modifier: Modifier = Modifier,
    listState: LazyListState,
    payments: Map<String, List<Payment>>,
    debtAmount: Double,
    onPaymentClicked: (Payment) -> Unit = {},
    onDeletePayment: (Payment) -> Unit = {},
    onEditPayment: (Payment) -> Unit = {}
) {
    LazyColumn (
        state = listState,
        modifier = modifier.fillMaxSize().padding(vertical = 8.dp)
    ){
        item {
            BalanceSection(
                modifier = Modifier.padding(vertical = 8.dp),
                amount = debtAmount,
            )
        }
        payments.forEach { (date, payments) ->
            item {
                Text(text = date)
            }
            items(payments) { payment ->
                PaymentItem(
                    payment = payment,
                    onClick = { onPaymentClicked(payment) },
                    onDelete = {  onDeletePayment(payment) },
                    onEdit = {  onEditPayment(payment) }
                )
            }

        }

    }
}
@Composable
fun LazyListState.isScrollingUp(): State<Boolean> {
    return produceState(initialValue = true) {
        var lastIndex = 0
        var lastScroll = Int.MAX_VALUE
        snapshotFlow {
            firstVisibleItemIndex to firstVisibleItemScrollOffset
        }.collect { (currentIndex, currentScroll) ->
            if (currentIndex != lastIndex || currentScroll != lastScroll) {
                value = currentIndex < lastIndex ||
                        (currentIndex == lastIndex && currentScroll < lastScroll)
                lastIndex = currentIndex
                lastScroll = currentScroll
            }
        }
    }
}

