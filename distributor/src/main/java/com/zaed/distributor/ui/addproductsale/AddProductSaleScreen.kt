package com.zaed.distributor.ui.addproductsale

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.ProgressIndicatorDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.zaed.common.ui.components.ProgressIndicatorTopAppBar
import kotlinx.coroutines.CoroutineScope
import org.koin.androidx.compose.koinViewModel

@Composable
fun AddProductSaleScreen(
    modifier: Modifier = Modifier,
    viewModel: AddProductSaleViewModel = koinViewModel(),
    saleId: String = "",
    onBackClicked: () -> Unit,
    onNavigateToProductSaleDetails: (saleId: String) -> Unit
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    LaunchedEffect (true) {
        viewModel.init(saleId)
    }
    LaunchedEffect (state.isFinished){
        if(state.isFinished){
            onNavigateToProductSaleDetails(state.sale.id)
        }
    }
}

@Composable
private fun AddProductSaleScreenContent(
    modifier: Modifier = Modifier,
    state: AddProductSaleUiState,
    onAction: (AddProductSaleUiAction) -> Unit,
    scope: CoroutineScope = rememberCoroutineScope()
) {
    val pagerState = rememberPagerState { 4 }
    val progress by animateFloatAsState(
        targetValue = (pagerState.currentPage + 1).toFloat() /(pagerState.pageCount + 1),
        animationSpec = ProgressIndicatorDefaults.ProgressAnimationSpec,
        label = "linear progress inicator"
    )
    Scaffold (
        topBar = {
            ProgressIndicatorTopAppBar(
                progress = progress
            ) {
                onAction(AddProductSaleUiAction.OnBackClicked)
            }
        },
        bottomBar = {
            TODO()
        }
    ){ innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            HorizontalPager(
                modifier = Modifier.padding(top = 16.dp),
                state = pagerState,
                userScrollEnabled = false,
            ) { page ->
                when(page){
                    0 -> {
                        //select customer
                    }
                    1 -> {
                        //add products
                    }
                    2 -> {
                        //add payments
                    }
                    3 -> {
                        //order summary
                    }
                }
            }
        }
    }

}