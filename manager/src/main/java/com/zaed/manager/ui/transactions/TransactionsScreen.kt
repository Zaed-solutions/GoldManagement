package com.zaed.manager.ui.transactions

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.koin.androidx.compose.koinViewModel

@Composable
fun TransactionsScreen(
    modifier: Modifier = Modifier,
    onShowNavDrawer: () -> Unit,
    viewModel: TransactionsViewModel = koinViewModel()
){
    val state by viewModel.uiState.collectAsStateWithLifecycle()
}


@Composable
private fun TransactionsScreenContent(
    modifier: Modifier = Modifier,
    state: TransactionsUiState,
    onAction: (TransactionsUiAction) -> Unit
){

}