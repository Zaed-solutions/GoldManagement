package com.zaed.common.ui.addGoldSale.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
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
import com.zaed.common.data.model.payment.GoldPayment
import com.zaed.common.data.model.payment.CashPayment
import com.zaed.common.data.model.payment.Payment
import com.zaed.common.ui.components.DetailRow
import com.zaed.common.ui.util.formatMoney
import com.zaed.common.ui.util.toMoneyFormat
import java.util.UUID

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SelectGoldPaymentsContent(
    modifier: Modifier = Modifier,
    goldCost: Double,
    laborCost: Double,
    moneyPayments: Double,
    goldPayments: Double,
    totalPaid: Double,
    payments: List<Payment>,
    onAddPayment: (Payment) -> Unit = {},
    onRemovePayment: (Payment) -> Unit = {},
    onEditPayment: (Payment) -> Unit = {},
    totalGoldPaid: Double,
) {
    var isBottomSheetVisible by remember { mutableStateOf(false) }
    var selectedPayment by remember { mutableStateOf<Payment>(CashPayment()) }
    val bottomSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalAlignment = Alignment.Start
    ) {
        Text(
            text = stringResource(R.string.add_payments),
            style = MaterialTheme.typography.headlineMedium
        )
        DetailRow(
            modifier = Modifier.padding(top = 16.dp),
            label = stringResource(R.string.total_amount),
            value = goldCost.toMoneyFormat(2),
            isDividerVisible = false
        )
        DetailRow(
            label = stringResource(R.string.labor_cost),
            value = laborCost.formatMoney(),
            isDividerVisible = false
        )
        DetailRow(
            label = stringResource(R.string.total),
            value = (goldCost + laborCost).formatMoney(),
        )
        DetailRow(
            label = stringResource(R.string.money_paid),
            value = moneyPayments.formatMoney(),
            isDividerVisible = false
        )
        if(goldPayments >0 && totalGoldPaid>0.0) {
            DetailRow(
                label = stringResource(R.string.gold_paid),
                value = stringResource(R.string.grams_placeholder, goldPayments),
                isDividerVisible = false
            )
            DetailRow(
                label = stringResource(R.string.total_paid),
                value = totalPaid.formatMoney(),
                isDividerVisible = false
            )
        }
        if(goldPayments >0 && totalGoldPaid==0.0) {
            Text(
                "Gold Karat not specified"
            )
        }


        Text(
            text = stringResource(R.string.all_unpaid_amount_will_be_considered_as_future_payment),
            style = MaterialTheme.typography.bodySmall,
        )
        //payments list
        PaymentsList(
            payments = payments,
            onAddPayment = {
                selectedPayment = CashPayment()
                isBottomSheetVisible = true
            },
            onEditPayment = {
                when (it) {
                    is CashPayment -> selectedPayment = it
                    is GoldPayment -> selectedPayment = it
                }
                isBottomSheetVisible = true
            },
            onRemovePayment = onRemovePayment
        )
        //add payment bottom sheet
        AnimatedVisibility(isBottomSheetVisible) {
            ModalBottomSheet(
                sheetState = bottomSheetState,
                onDismissRequest = { isBottomSheetVisible = false },
            ) {

                SaveGoldPaymentBottomSheetContent(
                    initialMoneyPayment = selectedPayment,
                    onSave = {
                        if (selectedPayment.id.isBlank()) {
                            when (it) {
                                is CashPayment -> {
                                    onAddPayment(
                                        it.copy(
                                            id = "distributor-" + UUID.randomUUID().toString()
                                        )
                                    )
                                }

                                is GoldPayment -> {
                                    onAddPayment(
                                        it.copy(
                                            id = "distributor-" + UUID.randomUUID().toString()
                                        )
                                    )
                                }
                            }
                        } else {
                            when (it) {
                                is CashPayment -> {
                                    onEditPayment(it)
                                }

                                is GoldPayment -> {
                                    onEditPayment(it)
                                }

                            }
                        }
                        isBottomSheetVisible = false
                    }
                )
            }
        }
    }
}


