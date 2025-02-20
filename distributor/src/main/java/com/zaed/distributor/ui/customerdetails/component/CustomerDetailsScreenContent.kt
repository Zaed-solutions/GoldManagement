package com.zaed.distributor.ui.customerdetails.component

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.PrimaryTabRow
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow.Companion.Ellipsis
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import com.zaed.common.data.model.payment.Payment
import com.zaed.common.ui.components.BackIcon
import com.zaed.common.ui.components.ConfirmDeleteBottomSheet
import com.zaed.common.ui.util.toMoneyFormat
import com.zaed.distributor.ui.customerdetails.CustomerDetailsUiAction
import com.zaed.distributor.ui.customerdetails.CustomerDetailsUiState
import com.zaed.distributor.ui.sales.components.SalesList

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomerDetailsScreenContent(
    modifier: Modifier = Modifier,
    uiState: CustomerDetailsUiState,
    onAction: (CustomerDetailsUiAction) -> Unit = {}
) {
    var addPaymentBottomSheetVisible by remember { mutableStateOf(false) }
    val listState = remember { LazyListState() }
    var selectedPayment by remember { mutableStateOf<Payment?>(null) }
    var confirmDeletePayment by remember { mutableStateOf(false) }
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = uiState.customer.name,
                        style = MaterialTheme.typography.titleLarge,
                        maxLines = 1,
                        overflow = Ellipsis
                    )
                },
                navigationIcon = {
                    BackIcon {
                        onAction(CustomerDetailsUiAction.OnBackClicked)
                    }
                },
                actions = {
                    IconButton(
                        onClick = {}
                    ) {
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = ""
                        )
                    }
                }
            )
        },
        floatingActionButton = {
            AnimatedVisibility(
                visible = listState.isScrollingUp().value,
                enter = slideInVertically(initialOffsetY = { it }) + fadeIn(),
                exit = slideOutVertically(targetOffsetY = { it }) + fadeOut()
            ) {
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
            }
        },
        modifier = modifier
    ) {
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp)
                .padding(it)

        ) {
            var selectedTab by remember { mutableIntStateOf(0) }
            val tabs = listOf(
                stringResource(com.zaed.common.R.string.payments),
                stringResource(com.zaed.common.R.string.sales)
            )
            PrimaryTabRow(
                selectedTabIndex = selectedTab,
                indicator = {
                    TabRowDefaults.PrimaryIndicator(
                        modifier = Modifier
                            .run {
                                if (LocalLayoutDirection.current == LayoutDirection.Rtl)
                                    scale(-1f, 1f)
                                else
                                    this
                            }
                            .tabIndicatorOffset(selectedTab, true),
                        width = Dp.Unspecified,
                    )
                }
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
                            listState = listState,
                            payments = uiState.payments,
                            onDeletePayment = { payment ->
                                selectedPayment = payment
                                confirmDeletePayment = true
                            },
                            onEditPayment = { payment ->
                                onAction(CustomerDetailsUiAction.EditPayment(payment))
                            }
                        )
                    }

                    1 -> {
                        SalesList(
                            listState = listState,
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
        AddNewPaymentBottomSheet(
            addPaymentBottomSheetVisible,
            uiState,
            onAction,
            onDismiss = { addPaymentBottomSheetVisible = false }
        )
        ConfirmDeleteBottomSheet(
            visible = confirmDeletePayment,
            label = selectedPayment?.amount?.toMoneyFormat(2) ?:"",
            onDismiss = {
                confirmDeletePayment = false
                selectedPayment = null
            },
            onConfirm = {
                selectedPayment?.let { onAction(CustomerDetailsUiAction.DeletePayment(it)) }
                confirmDeletePayment = false
                selectedPayment = null
            }
        )
    }
}