package com.zaed.accountant.ui.purchases

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.zaed.common.R
import com.zaed.common.ui.components.SearchBar
import com.zaed.common.ui.components.TransactionsList
import org.koin.androidx.compose.koinViewModel

@Composable
fun PurchasesScreen(
    modifier: Modifier = Modifier,
    viewModel: PurchasesViewModel = koinViewModel(),
    onShowNavDrawer: () -> Unit,
    onNavigateToPurchaseDetails: (purchaseId: String) -> Unit
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    PurchasesScreenContent(
        state = state,
        onAction = {action ->
            when(action){
                PurchasesUiAction.OnShowNavDrawer -> onShowNavDrawer()
                is PurchasesUiAction.OnPurchaseClicked -> onNavigateToPurchaseDetails(action.purchaseId)
                else -> viewModel.handleAction(action)
            }
        }
    )

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PurchasesScreenContent(
    modifier: Modifier = Modifier,
    state: PurchasesUiState,
    onAction: (PurchasesUiAction) -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(R.string.purchases),
                        style = MaterialTheme.typography.titleLarge
                    )
                },
                navigationIcon = {
                    IconButton(
                        onClick = {
                            onAction(PurchasesUiAction.OnShowNavDrawer)
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Menu,
                            contentDescription = null
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            SearchBar(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                query = state.searchQuery,
                onQueryChanged = {
                    onAction(PurchasesUiAction.OnSearchQueryChanged(it))
                }
            )

            TransactionsList(
                isLoading = state.isLoading,
                transactions = state.displayedPurchases,
                onTransactionClicked = { purchase, _ ->
                    onAction(PurchasesUiAction.OnPurchaseClicked(purchase.id))
                },
                isDeletable = false,
                isEditable = false
            )
        }
    }
}