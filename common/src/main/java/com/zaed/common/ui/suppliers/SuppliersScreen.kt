package com.zaed.common.ui.suppliers

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.zaed.common.R
import com.zaed.common.data.model.authentication.UserRole
import com.zaed.common.data.model.supplier.Supplier
import com.zaed.common.ui.components.ConfirmDeleteBottomSheet
import com.zaed.common.ui.components.SearchBar
import com.zaed.common.ui.suppliers.components.SaveSupplierBottomSheet
import com.zaed.common.ui.suppliers.components.SuppliersList
import org.koin.androidx.compose.koinViewModel

@Composable
fun SuppliersScreen(
    modifier: Modifier = Modifier,
    role: UserRole,
    viewModel: SuppliersViewModel = koinViewModel(),
    onShowNavDrawer: () -> Unit,
    onNavigateToSupplierDetails: (String) -> Unit
) {
    LaunchedEffect(true) {
        viewModel.init(role)
    }
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    SuppliersScreenContent(
        state = state,
        onAction = { action ->
            when (action) {
                SuppliersUiAction.OnShowNavDrawer -> onShowNavDrawer()
                is SuppliersUiAction.OnSupplierClicked -> onNavigateToSupplierDetails(action.supplierId)
                else -> viewModel.handleAction(action)
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SuppliersScreenContent(
    modifier: Modifier = Modifier,
    state: SuppliersUiState,
    onAction: (SuppliersUiAction) -> Unit
) {
    var selectedSupplier by remember { mutableStateOf(Supplier()) }
    var isSaveSupplierSheetVisible by remember { mutableStateOf(false) }
    var isConfirmDeleteSheetVisible by remember { mutableStateOf(false) }
    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(R.string.suppliers),
                        style = MaterialTheme.typography.titleLarge
                    )
                },
                navigationIcon = {
                    IconButton(
                        onClick = {
                            onAction(SuppliersUiAction.OnShowNavDrawer)
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Menu,
                            contentDescription = null
                        )
                    }
                }
            )
        },
        floatingActionButton = {
            if (state.isAdmin) {
                FloatingActionButton(
                    modifier = Modifier
                        .padding(bottom = 8.dp, end = 8.dp)
                        .rotate(45f),
                    shape = RoundedCornerShape(16.dp),
                    onClick = {
                        selectedSupplier = Supplier()
                        isSaveSupplierSheetVisible = true
                    },
                ) {
                    Icon(
                        modifier = Modifier.rotate(-45f),
                        imageVector = Icons.Default.Add,
                        contentDescription = "Add Supplier"
                    )
                }
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            SearchBar(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                query = state.searchQuery,
                placeHolder = stringResource(R.string.search_by_name_or_phone),
                onQueryChanged = { query ->
                    onAction(SuppliersUiAction.UpdateSearchQuery(query))
                }
            )
            //list
            SuppliersList(
                isLoading = state.isLoading,
                suppliers = state.filteredSuppliers,
                onSupplierClicked = {
                    onAction(SuppliersUiAction.OnSupplierClicked(it))
                },
                isEditable = state.isAdmin,
                onEditSupplier = {
                    selectedSupplier = it
                    isSaveSupplierSheetVisible = true
                },
                isDeletable = state.isAdmin,
                onDeleteSupplier = {
                    selectedSupplier = it
                    isConfirmDeleteSheetVisible = true
                }
            )
            //save bottom sheet
            SaveSupplierBottomSheet(
                isVisible = isSaveSupplierSheetVisible,
                onDismiss = {
                    isSaveSupplierSheetVisible = false
                },
                onSave = {
                    isSaveSupplierSheetVisible = false
                    onAction(
                        if (it.id.isBlank()) {
                            SuppliersUiAction.AddSupplier(it)
                        } else {
                            SuppliersUiAction.UpdateSupplier(it)
                        }
                    )
                },
                initialSupplier = selectedSupplier
            )
            ConfirmDeleteBottomSheet(
                visible = isConfirmDeleteSheetVisible,
                label = stringResource(R.string.supplier),
                onDismiss = {
                    isConfirmDeleteSheetVisible = false
                },
                onConfirm = {
                    isConfirmDeleteSheetVisible = false
                    onAction(SuppliersUiAction.DeleteSupplier(selectedSupplier))
                }
            )
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SelectSupplierSheet(
    modifier: Modifier = Modifier,
    onDismiss: () -> Unit,
    isAdmin: Boolean,
    searchQuery: String,
    onUpdateSearchQuery: (String) -> Unit,
    filteredSuppliers: List<Supplier>,
    isLoading: Boolean,
    onAddSupplier: (Supplier) -> Unit,
    onSupplierClicked: (String) -> Unit
) {
    var selectedSupplier by remember { mutableStateOf(Supplier()) }
    var isSaveSupplierSheetVisible by remember { mutableStateOf(false) }
    var isConfirmDeleteSheetVisible by remember { mutableStateOf(false) }
    ModalBottomSheet(
        onDismissRequest = onDismiss,
        dragHandle = {},
        sheetState = rememberModalBottomSheetState(
            skipPartiallyExpanded = true,
        ),
    ) {
        Scaffold(
            modifier = modifier,
            topBar = {
                TopAppBar(
                    title = {
                        Text(
                            text = stringResource(R.string.suppliers),
                            style = MaterialTheme.typography.titleLarge
                        )
                    },
                    navigationIcon = {
                        IconButton(
                            onClick = onDismiss
                        ) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = null
                            )
                        }
                    }
                )
            },
            floatingActionButton = {
                if (isAdmin) {
                    FloatingActionButton(
                        modifier = Modifier
                            .padding(bottom = 8.dp, end = 8.dp)
                            .rotate(45f),
                        shape = RoundedCornerShape(16.dp),
                        onClick = {
                            selectedSupplier = Supplier()
                            isSaveSupplierSheetVisible = true
                        },
                    ) {
                        Icon(
                            modifier = Modifier.rotate(-45f),
                            imageVector = Icons.Default.Add,
                            contentDescription = "Add Supplier"
                        )
                    }
                }
            }
        ) { innerPadding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                SearchBar(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    query = searchQuery,
                    placeHolder = stringResource(R.string.search_by_name_or_phone),
                    onQueryChanged = { query ->
                        onUpdateSearchQuery(query)
                    }
                )
                //list
                SuppliersList(
                    isLoading = isLoading,
                    suppliers = filteredSuppliers,
                    onSupplierClicked = {
                        onSupplierClicked(it)
                    },
                    isEditable = isAdmin,
                    onEditSupplier = {
                        selectedSupplier = it
                        isSaveSupplierSheetVisible = true
                    },
                    isDeletable = isAdmin,
                    onDeleteSupplier = {
                        selectedSupplier = it
                        isConfirmDeleteSheetVisible = true
                    }
                )
                //save bottom sheet
                SaveSupplierBottomSheet(
                    isVisible = isSaveSupplierSheetVisible,
                    onDismiss = {
                        isSaveSupplierSheetVisible = false
                    },
                    onSave = {
                        isSaveSupplierSheetVisible = false
                        if (it.id.isBlank()) {
                            onAddSupplier(it)
                        } else {
                        }
                    },
                    initialSupplier = selectedSupplier
                )
                ConfirmDeleteBottomSheet(
                    visible = isConfirmDeleteSheetVisible,
                    label = stringResource(R.string.supplier),
                    onDismiss = {
                        isConfirmDeleteSheetVisible = false
                    },
                    onConfirm = {
                        isConfirmDeleteSheetVisible = false
                    }
                )
            }
        }
    }
}