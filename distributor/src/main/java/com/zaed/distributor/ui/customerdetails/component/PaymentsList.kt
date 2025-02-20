package com.zaed.distributor.ui.customerdetails.component

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.produceState
import androidx.compose.runtime.snapshotFlow
import com.zaed.common.data.model.payment.Payment

@Composable
fun PaymentsList(
    listState: LazyListState,
    payments: Map<String, List<Payment>>
) {
    LazyColumn (
        state = listState
    ){
        payments.forEach { (date, payments) ->
            item {
                Text(text = date)
            }
            items(payments) { payment ->
                PaymentItem(payment = payment)
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