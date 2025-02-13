package com.zaed.cashier.ui.addsale

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.zaed.cashier.ui.addsale.components.AddSaleBottomBar
import com.zaed.cashier.ui.addsale.components.AddSaleTopBar
import com.zaed.cashier.ui.addsale.components.SelectCustomerContent
import com.zaed.cashier.ui.theme.CashierAppTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel

@Composable
fun AddSaleScreen(
    modifier: Modifier = Modifier,
    viewModel: AddSaleViewModel = koinViewModel()
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    AddSaleScreenContent(
        state = state,
        onAction = { action ->
            when(action){
                AddSaleUiAction.OnBackClicked ->{TODO()}
                else -> viewModel.handleAction(action)
            }
        }
    )

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AddSaleScreenContent(
    modifier: Modifier = Modifier,
    state: AddSaleUiState,
    onAction: (AddSaleUiAction) -> Unit,
    scope: CoroutineScope = rememberCoroutineScope()
) {
    val pagerState = rememberPagerState { 2 }
    Scaffold (
        modifier = modifier,
        topBar = {
            AddSaleTopBar { onAction(AddSaleUiAction.OnBackClicked) }
        },
        bottomBar = {
            AddSaleBottomBar(
                currentPage = pagerState.currentPage,
                customerName = state.sale.customerName,
                onPreviousClicked = {
                    scope.launch {
                        pagerState.animateScrollToPage(0)
                    }
                },
                onContinueClicked = {
                    scope.launch {
                        pagerState.animateScrollToPage(1)
                    }
                },
                onAddClicked = {
                    onAction(AddSaleUiAction.OnAddClicked)
                }
            )
        }
    ){ innerPadding ->
        Column (
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ){
            LinearProgressIndicator(
                progress = { pagerState.currentPage.toFloat() / pagerState.pageCount },
                modifier = Modifier.fillMaxWidth().height(8.dp)
            )
            HorizontalPager(
                modifier = Modifier.padding(top = 16.dp),
                state = pagerState,
                userScrollEnabled = false,
            ) { page ->
                when(page){
                    0 -> {
                        SelectCustomerContent(
                            sale = state.sale,
                            onUpdateCustomerName = { onAction(AddSaleUiAction.OnUpdateCustomerName(it)) },
                            onUpdateCustomerPhone = { onAction(AddSaleUiAction.OnUpdateCustomerPhone(it)) },
                            onUpdateCustomerEmail = { onAction(AddSaleUiAction.OnUpdateCustomerEmail(it)) }
                        )
                    }
                    1 -> {
//                        SelectProductsContent()
                    }
                }
            }
        }
    }
}

@Preview(showSystemUi = true, showBackground = true, device = "id:pixel_9_pro")
@Composable
private fun Preview() {
    CashierAppTheme {
        AddSaleScreenContent(
            state = AddSaleUiState(),
            onAction = {}
        )
    }
}