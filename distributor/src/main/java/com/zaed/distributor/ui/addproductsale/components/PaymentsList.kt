package com.zaed.distributor.ui.addproductsale.components

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.zaed.common.data.model.payment.BankTransferPayment
import com.zaed.common.data.model.payment.CashPayment
import com.zaed.common.data.model.payment.ChequePayment
import com.zaed.common.data.model.payment.FuturePayment
import com.zaed.common.data.model.payment.LossPayment
import com.zaed.common.data.model.payment.Payment
import com.zaed.distributor.ui.customerdetails.component.PaymentItem

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
                Log.d("find the issue", "paymentSent: $payment")
                when (payment) {
                    is CashPayment-> {
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
                    is LossPayment ->{
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
                    is FuturePayment ->{
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
                    is ChequePayment ->{
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
                    is BankTransferPayment ->{
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
    }

}