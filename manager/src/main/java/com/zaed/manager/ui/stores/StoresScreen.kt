package com.zaed.manager.ui.stores

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.zaed.common.data.model.store.Store
import com.zaed.common.ui.components.ConfirmDeleteBottomSheet
import com.zaed.manager.R
import com.zaed.manager.ui.stores.components.SaveStoreBottomSheet
import com.zaed.manager.ui.stores.components.StoresList
import org.koin.androidx.compose.koinViewModel

@Composable
fun StoresScreen(
    modifier: Modifier = Modifier,
    viewModel: StoresViewModel = koinViewModel(),
    onShowNavDrawer: () -> Unit,
    onNavigateToStoreDetails: (String) -> Unit
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    StoresScreenContent(
        state = state,
        onAction = { action ->
            when(action){
                is StoresUiAction.OnShowNavDrawer -> onShowNavDrawer()
                is StoresUiAction.OnStoreClicked -> onNavigateToStoreDetails(action.storeId)
                else -> viewModel.handleAction(action)
            }
        }
    )
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StoresScreenContent(
    modifier: Modifier = Modifier,
    state: StoresUiState,
    onAction: (StoresUiAction) -> Unit
){
    var isSaveStoreBottomSheetVisible by remember { mutableStateOf(false) }
    var isConfirmDeleteStoreSheetVisible by remember { mutableStateOf(false) }
    var selectedStore by remember {
        mutableStateOf(Store())
    }
    Scaffold (
        modifier = modifier,
        topBar = {
            TopAppBar(
                title = { Text(text = stringResource(R.string.stores)) },
                navigationIcon = {
                    IconButton(
                        onClick = {
                            onAction(StoresUiAction.OnShowNavDrawer)
                        }
                    ) {
                        Icon(Icons.Filled.Menu, contentDescription = null)
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                modifier = Modifier
                    .padding(bottom = 8.dp, end = 8.dp)
                    .rotate(45f),
                shape = RoundedCornerShape(16.dp),
                onClick = {
                    selectedStore = Store()
                    isSaveStoreBottomSheetVisible = true
                },
            ) {
                Icon(
                    modifier = Modifier.rotate(-45f),
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add Store"
                )
            }
        }
    ){ innerPadding ->
        Column (
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
        ){
            StoresList(
                modifier = Modifier.fillMaxSize(),
                isLoading = state.isLoading,
                stores = state.stores,
                onDeleteStore = {
                    selectedStore = it
                    isConfirmDeleteStoreSheetVisible = true
                },
                onEditStore = {
                    selectedStore = it
                    isSaveStoreBottomSheetVisible = true
                },
                onStoreClicked = {
                    onAction(StoresUiAction.OnStoreClicked(it.id))
                }
            )
            SaveStoreBottomSheet(
                isVisible = isSaveStoreBottomSheetVisible,
                initialStore = selectedStore,
                onDismiss = {
                    isSaveStoreBottomSheetVisible = false
                },
                onSave = {
                    onAction(
                        if(it.id.isBlank()){
                            StoresUiAction.OnAddStore(it)
                        } else {
                            StoresUiAction.OnUpdateStore(it)
                        }
                    )
                    isSaveStoreBottomSheetVisible = false
                }
            )
            ConfirmDeleteBottomSheet(
                visible = isConfirmDeleteStoreSheetVisible,
                onDismiss = {
                    isConfirmDeleteStoreSheetVisible = false
                },
                onConfirm = {
                    onAction(StoresUiAction.OnDeleteStore(selectedStore))
                    isConfirmDeleteStoreSheetVisible = false
                },
                label = stringResource(com.zaed.common.R.string.store)
            )
        }
    }

}