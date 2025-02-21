package com.zaed.distributor.ui.addproductsale.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
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
import com.zaed.common.data.model.payment.Payment
import com.zaed.common.data.model.payment.PaymentType
import com.zaed.common.ui.components.DetailRow
import com.zaed.common.ui.components.NumberInputTextField
import com.zaed.common.ui.components.TitledDropDownTextField
import com.zaed.common.ui.util.formatMoney
import java.util.UUID

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SelectPaymentsContent(
    modifier: Modifier = Modifier,
    totalAmount: Double,
    totalPaid: Double,
    payments: List<Payment>,
    onAddPayment: (Payment) -> Unit = {},
    onRemovePayment: (Payment) -> Unit = {},
    onEditPayment: (Payment) -> Unit = {},
) {
    var isBottomSheetVisible by remember { mutableStateOf(false) }
    var selectedPayment by remember { mutableStateOf(Payment()) }
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
            value = totalAmount.formatMoney(),
        )
        DetailRow(
            label = stringResource(R.string.total_paid),
            value = totalPaid.formatMoney(),
            isDividerVisible = false
        )
        Text(
            text = stringResource(R.string.all_unpaid_amount_will_be_considered_as_future_payment),
            style = MaterialTheme.typography.bodySmall,
        )
        //payments list
        PaymentsList(
            payments = payments,
            onAddPayment = {
                selectedPayment = Payment()
                isBottomSheetVisible = true
            },
            onEditPayment = {
                selectedPayment = it
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
                SavePaymentBottomSheetContent(
                    initialPayment = selectedPayment,
                    onSave = {
                        if(selectedPayment.id.isBlank()){
                            onAddPayment(it.copy(id = "distributor-"+ UUID.randomUUID().toString()))
                        } else {
                            onEditPayment(it)
                        }
                        isBottomSheetVisible = false
                    }
                )
            }
        }
    }
}


