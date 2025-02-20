package com.zaed.distributor.ui.customerdetails

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.PrimaryTabRow
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.material3.contentColorFor
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.zaed.common.data.model.Payment
import com.zaed.common.data.model.PaymentType
import com.zaed.common.data.model.WholeSaleCustomer
import com.zaed.common.ui.components.DetailRow
import com.zaed.common.ui.components.NumberInputTextField
import com.zaed.common.ui.components.SwipeToEditOrDeleteContainer
import com.zaed.common.ui.components.TitledDropDownTextField2
import com.zaed.common.ui.util.toMoneyFormat
import com.zaed.distributor.ui.sales.components.SalesList
import org.koin.androidx.compose.koinViewModel

@Composable
fun CustomerDetailsScreen(
    viewModel: CustomerDetailsViewModel = koinViewModel(),
    customerId: String
) {
    LaunchedEffect(Unit) {
        viewModel.init(customerId)
    }
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    CustomerDetailsScreenContent(
        uiState = uiState,
        onAction = { action ->
            when (action) {
                CustomerDetailsUiAction.OnBackClicked -> {}
                else -> {
                    viewModel.handleUiAction(action)
                }
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomerDetailsScreenContent(
    modifier: Modifier = Modifier,
    uiState: CustomerDetailsUiState,
    onAction: (CustomerDetailsUiAction) -> Unit = {}
) {
    var addPaymentBottomSheetVisible by remember { mutableStateOf(false) }
    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                modifier = Modifier.rotate(45f),
                onClick = {
                    addPaymentBottomSheetVisible = true
                }
            ) {
                Icon(
                    modifier = Modifier.rotate(-45f),
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add Payment"
                )
            }
        },
        modifier = modifier
    ) {
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(it)
                .padding(16.dp)
        ) {
            var selectedTab by remember { mutableIntStateOf(0) }
            val tabs = listOf("Payments", "Transactions")
            CustomerDetailsHeader(
                modifier = Modifier.padding(horizontal = 4.dp, vertical = 8.dp),
                customer = uiState.customer
            )
            BalanceSection(
                modifier = Modifier.padding(horizontal = 4.dp, vertical = 12.dp),
                amount = uiState.customer.debtAmount
            )
            PrimaryTabRow(
                selectedTabIndex = selectedTab,
            ) {
                tabs.forEachIndexed { index, title ->
                    Tab(
                        selected = selectedTab == index,
                        onClick = { selectedTab = index },
                        text = { Text(text = title) }
                    )
                }
            }
            AnimatedContent(selectedTab) { value ->
                when (value) {
                    0 -> {
                        PaymentsList(
                            payments = uiState.payments
                        )
                    }

                    1 -> {
                        SalesList(
                            isLoading = uiState.loading,
                            sales = uiState.sales,
                            onSaleClicked = { id, isProduct -> },
                            onDeleteSale = { id, isProduct -> },
                            onEditSale = { id, isProduct -> }
                        )
                    }
                }

            }

        }
        AnimatedVisibility(
            visible = addPaymentBottomSheetVisible
        ) {
            ModalBottomSheet(
                onDismissRequest = { addPaymentBottomSheetVisible = false },
                sheetState = rememberModalBottomSheetState(
                    skipPartiallyExpanded = true
                )
            ) {
                Column(
                    verticalArrangement = Arrangement.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalAlignment = androidx.compose.ui.Alignment.CenterHorizontally
                ) {
                    Text(
                        text = stringResource(com.zaed.common.R.string.add_payment),
                        style = MaterialTheme.typography.titleLarge
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    NumberInputTextField(
                        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
                        label = stringResource(com.zaed.common.R.string.amount),
                        value = uiState.currentPayment.amount,
                        onValueChange = {
                            onAction(CustomerDetailsUiAction.OnAmountChanged(it))
                        },
                    )
                    TitledDropDownTextField2(
                        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
                        label = stringResource(com.zaed.common.R.string.type),
                        options = PaymentType.entries,
                        selectedValue = uiState.currentPayment.type,
                        onValueChanged = {
                            onAction(CustomerDetailsUiAction.OnTypeChanged(it))
                        },
                        placeHolder = stringResource(com.zaed.common.R.string.select_type)
                    )
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = androidx.compose.ui.Alignment.CenterVertically
                    ) {
                        Text("Given")
                        Switch(
                            modifier = Modifier.padding(horizontal = 8.dp),
                            checked = uiState.currentPayment.amount > 0,
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
                        Text("Taken")
                    }
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = androidx.compose.ui.Alignment.CenterVertically
                    ) {
                        Button(
                            onClick = {
                                onAction(CustomerDetailsUiAction.OnSaveClicked)
                                addPaymentBottomSheetVisible = false
                            },
                            modifier = Modifier.weight(1f).padding(horizontal = 8.dp),
                            shape = MaterialTheme.shapes.medium
                        ) {
                            Text("Save")
                        }
                        OutlinedButton(
                            onClick = {
                                addPaymentBottomSheetVisible = false
                            },
                            modifier = Modifier.weight(1f).padding(horizontal = 8.dp),
                            shape = MaterialTheme.shapes.medium
                        ) {
                            Text("Cancel")
                        }
                    }

                }
            }
        }
    }
}

sealed interface CustomerDetailsUiAction {
    data object OnEditPaymentClicked : CustomerDetailsUiAction
    data object OnDeletePaymentClicked : CustomerDetailsUiAction
    data object OnBackClicked : CustomerDetailsUiAction
    data object OnSaveClicked : CustomerDetailsUiAction
    data class OnAmountChanged(val amount: Double) : CustomerDetailsUiAction
    data class OnTypeChanged(val type: PaymentType) : CustomerDetailsUiAction
    data class OnChangeValueDirection(val isGiven: Boolean) : CustomerDetailsUiAction
}

@Composable
fun PaymentsList(
    payments: Map<String, List<Payment>>
) {
    LazyColumn {
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
fun PaymentItem(
    payment: Payment,
    onClick: () -> Unit = {},
    onDelete: () -> Unit = {},
    onEdit: () -> Unit = {}
) {
    SwipeToEditOrDeleteContainer(
        onDelete = onDelete,
        onEdit = onEdit,
        isEditEnabled = true,
    ) {
        Surface(
            modifier = Modifier.padding(8.dp),
            onClick = onClick,
            border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.5f)),
            shape = MaterialTheme.shapes.medium
        ) {
            val chipColor = if (payment.amount >= 0) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.errorContainer
            Column(
                modifier = Modifier.padding(12.dp)
            ) {
                Row(
                    verticalAlignment = androidx.compose.ui.Alignment.CenterVertically
                ) {
                    Text(text = payment.amount.toMoneyFormat(2))
                    Spacer(modifier = Modifier.weight(1f))
                    FilterChip(
                        modifier = Modifier.height(FilterChipDefaults.Height - 8.dp),
                        selected = true,
                        onClick = {},
                        label = { Text(text = payment.type.name) },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor =  chipColor,
                            selectedLabelColor = contentColorFor(chipColor)
                        )
                    )
                }
            }
        }
    }
}

@Composable
fun CustomerDetailsHeader(
    modifier: Modifier = Modifier,
    customer : WholeSaleCustomer,

    ) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Text(
            text = customer.name,
            style = MaterialTheme.typography.titleLarge,
        )

        DetailRow(
            label = stringResource(com.zaed.common.R.string.phone_number),
            value = customer.phone,
        )
        DetailRow(
            label = stringResource(com.zaed.common.R.string.city),
            value = customer.city,
        )
        DetailRow(
            label = stringResource(com.zaed.common.R.string.zone),
            value = customer.zone.name,
        )
    }
}
@Composable
fun BalanceSection(
    modifier: Modifier = Modifier,
    amount: Double,
) {
    Column(
        modifier = modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = if (amount >= 0) "اعطيت" else "اخذت",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.weight(1f),
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                text = amount.toMoneyFormat(2),
                style = MaterialTheme.typography.titleLarge,
                color = if (amount < 0) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.error
            )
        }
    }
}
