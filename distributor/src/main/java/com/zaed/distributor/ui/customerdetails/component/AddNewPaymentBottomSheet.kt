package com.zaed.distributor.ui.customerdetails.component

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.zaed.common.R
import com.zaed.common.data.model.payment.PaymentType
import com.zaed.common.ui.components.NumberInputTextField
import com.zaed.common.ui.components.TitledDropDownTextField2
import com.zaed.distributor.ui.customerdetails.CustomerDetailsUiAction
import com.zaed.distributor.ui.customerdetails.CustomerDetailsUiState
import kotlin.math.absoluteValue

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun AddOrEditNewPaymentBottomSheet(
    visible: Boolean,
    isEditMode: Boolean,
    uiState: CustomerDetailsUiState,
    onAction: (CustomerDetailsUiAction) -> Unit,
    onDismiss: () -> Unit = {}
) {
    AnimatedVisibility(
        visible = visible
    ) {
        ModalBottomSheet(
            onDismissRequest = onDismiss ,
            sheetState = rememberModalBottomSheetState(
                skipPartiallyExpanded = true
            )
        ) {
            Column(
                verticalArrangement = Arrangement.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = if(isEditMode) stringResource(R.string.edit_payment) else  stringResource(R.string.add_payment),
                    style = MaterialTheme.typography.titleLarge
                )
                Spacer(modifier = Modifier.height(16.dp))
                NumberInputTextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    label = stringResource(R.string.amount),
                    value = uiState.currentMoneyPayment.amount.absoluteValue,
                    onValueChange = {
                        onAction(CustomerDetailsUiAction.OnAmountChanged(it))
                    },
                )
                TitledDropDownTextField2(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    label = stringResource(R.string.type),
                    options = PaymentType.entries,
                    selectedValue = uiState.currentMoneyPayment.type,
                    onValueChanged = {
                        onAction(CustomerDetailsUiAction.OnTypeChanged(it))
                    },
                    placeHolder = stringResource(R.string.select_type)
                )
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(stringResource(R.string.given))
                    Switch(
                        modifier = Modifier.padding(horizontal = 8.dp),
                        checked = uiState.paymentDirection,
                        onCheckedChange = {
                            onAction(CustomerDetailsUiAction.OnChangeValueDirection(it))
                        },
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = Color.Green,
                            checkedTrackColor = Color.Green.copy(alpha = 0.2f),
                            checkedBorderColor = MaterialTheme.colorScheme.outline,
                            uncheckedThumbColor = Color.Red,
                            uncheckedTrackColor = Color.Red.copy(alpha = 0.2f),
                        )
                    )
                    Text(stringResource(R.string.taken))
                }
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Button(
                        onClick = {
                            if(isEditMode){
                                onAction(CustomerDetailsUiAction.OnConfirmEditPayment)
                            }else {
                                onAction(CustomerDetailsUiAction.OnSaveClicked)
                            }
                            onDismiss()
                        },
                        modifier = Modifier
                            .weight(1f)
                            .padding(horizontal = 8.dp),
                        shape = MaterialTheme.shapes.medium
                    ) {
                        Text(stringResource(R.string.save))
                    }
                    OutlinedButton(
                        onClick = onDismiss,
                        modifier = Modifier
                            .weight(1f)
                            .padding(horizontal = 8.dp),
                        shape = MaterialTheme.shapes.medium
                    ) {
                        Text(stringResource(R.string.cancel))
                    }
                }

            }
        }
    }
}