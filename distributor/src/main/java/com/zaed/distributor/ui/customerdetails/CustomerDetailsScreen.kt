package com.zaed.distributor.ui.customerdetails

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.PrimaryTabRow
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.zaed.common.data.model.Payment
import com.zaed.common.ui.components.SwipeToEditOrDeleteContainer
import org.koin.androidx.compose.koinViewModel

@Composable
fun CustomerDetailsScreen(
    viewModel: CustomerDetailsViewModel = koinViewModel(),
    customerId: String
) {
    LaunchedEffect(Unit) {
        viewModel.getCustomerTransactions(customerId)
    }
    val uiState = viewModel.uiState.collectAsStateWithLifecycle()
    CustomerDetailsScreenContent(
        uiState = uiState.value
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomerDetailsScreenContent(
    modifier: Modifier = Modifier,
    uiState: CustomerDetailsUiState
) {
    Scaffold (
        modifier = modifier
    ){
        Column (
            modifier = modifier.fillMaxSize().padding(it).padding(16.dp)
        ){
            var selectedTab by remember { mutableIntStateOf(0) }
            val tabs = listOf("Payments","Transactions")
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
            AnimatedContent (selectedTab){value->
                when(value){
                    0 -> {
                        PaymentsList(
                            payments = uiState.payments
                        )
                    }
                    1 -> {
                        //TransactionsList()
                    }
                }

            }

        }
    }
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
            items(payments){payment->
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
    ) {
        Surface(
            modifier = Modifier.padding(8.dp),
            onClick = onClick
        ) {
            Column(
                modifier = Modifier.padding(12.dp)
            ) {
                Row(
                    verticalAlignment = androidx.compose.ui.Alignment.CenterVertically
                ) {
                    Text(text = payment.amount.toString())
                    Spacer(modifier = Modifier.weight(1f))
                    FilterChip(
                        modifier = Modifier.height(FilterChipDefaults.Height - 8.dp),
                        selected = true,
                        onClick = {},
                        label = { Text(text = payment.type.name) },
                        colors = FilterChipDefaults.filterChipColors(
                            containerColor = if (payment.amount >= 0) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.errorContainer,
                        )
                    )
                }
            }
        }
    }
}
