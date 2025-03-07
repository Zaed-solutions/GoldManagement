package com.zaed.manager.ui.distributors

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.zaed.common.R
import com.zaed.common.ui.components.SearchBar
import com.zaed.manager.ui.distributors.components.DistributorsList
import org.koin.androidx.compose.koinViewModel

@Composable
fun DistributorsScreen(
    modifier: Modifier = Modifier,
    viewModel: DistributorsViewModel = koinViewModel(),
    onShowNavDrawer: () -> Unit,
    onNavigateToDistributorDetails: (String) -> Unit
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    DistributorsScreenContent(
        state = state,
        onAction = { action ->
            when(action){
                is DistributorsUiAction.OnDistributorClicked -> onNavigateToDistributorDetails(action.distributor.id)
                DistributorsUiAction.OnShowNavDrawer -> onShowNavDrawer()
                else -> viewModel.handleAction(action)
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DistributorsScreenContent(
    modifier: Modifier = Modifier,
    state: DistributorsUiState,
    onAction: (DistributorsUiAction) -> Unit
) {
    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                title = { Text(text = stringResource(R.string.distributors)) },
                navigationIcon = {
                    IconButton(
                        onClick = {
                            onAction(DistributorsUiAction.OnShowNavDrawer)
                        }
                    ) {
                        Icon(Icons.Filled.Menu, contentDescription = null)
                    }
                }
            )
        },
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            SearchBar(
                modifier = Modifier.fillMaxWidth().padding(16.dp),
                query = state.searchQuery,
                onQueryChanged = { query ->
                    onAction(DistributorsUiAction.UpdateSearchQuery(query))
                }
            )
            DistributorsList(
                modifier = Modifier.weight(1f),
                isLoading = state.isLoading,
                distributors = state.displayedDistributors,
                onClick = { distributor ->
                    onAction(DistributorsUiAction.OnDistributorClicked(distributor))
                }
            )
        }
    }
}