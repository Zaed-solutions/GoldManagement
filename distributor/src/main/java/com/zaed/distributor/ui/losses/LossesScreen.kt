package com.zaed.distributor.ui.losses

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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
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
import com.zaed.common.data.model.loss.DistributorLoss
import com.zaed.common.ui.components.ConfirmDeleteBottomSheet
import com.zaed.common.ui.components.DatedLossesList
import com.zaed.distributor.ui.losses.components.SaveLossBottomSheet
import org.koin.androidx.compose.koinViewModel

@Composable
fun LossesScreen(
    modifier: Modifier = Modifier,
    onShowNavDrawer: () -> Unit,
    viewModel: LossesViewModel = koinViewModel()
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    LossesScreenContent(
        state = state,
        onAction = { action ->
            when (action) {
                LossesUiAction.OnShowNavDrawer -> onShowNavDrawer()
                else -> viewModel.handleAction(action)
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LossesScreenContent(
    modifier: Modifier = Modifier,
    state: LossesUiState,
    onAction: (LossesUiAction) -> Unit
) {
    var isSaveLossSheetVisible by remember { mutableStateOf(false) }
    var isConfirmDeleteSheetVisible by remember { mutableStateOf(false) }
    var selectedLoss by remember { mutableStateOf(DistributorLoss()) }
    Scaffold(
        containerColor = MaterialTheme.colorScheme.surface,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(R.string.losses),
                        style = MaterialTheme.typography.titleLarge
                    )
                },
                navigationIcon = {
                    IconButton(
                        onClick = { onAction(LossesUiAction.OnShowNavDrawer) }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Menu,
                            contentDescription = "Menu icon"
                        )
                    }
                },
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                modifier = Modifier
                    .padding(bottom = 8.dp, end = 8.dp)
                    .rotate(45f),
                shape = RoundedCornerShape(16.dp),
                onClick = {
                    selectedLoss = DistributorLoss()
                    isSaveLossSheetVisible = true
                },
            ) {
                Icon(
                    modifier = Modifier.rotate(-45f),
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add Sale"
                )
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            DatedLossesList(
                isLoading = state.isLoading,
                datedLosses = state.datedLosses,
                onDeleteLoss = {
                    selectedLoss = it as DistributorLoss
                    isConfirmDeleteSheetVisible = true
                },
                onUpdateLoss = {
                    selectedLoss = it as DistributorLoss
                    isSaveLossSheetVisible = true
                }
            )
            SaveLossBottomSheet(
                isVisible = isSaveLossSheetVisible,
                initialLoss = selectedLoss,
                onSave = {
                    onAction(
                        if (selectedLoss.id.isNotBlank()) {
                            LossesUiAction.OnUpdateLoss(it)
                        } else {
                            LossesUiAction.OnAddLoss(it)
                        }
                    )
                    isSaveLossSheetVisible = false
                },
                onDismiss = {
                    isSaveLossSheetVisible = false
                }
            )
            ConfirmDeleteBottomSheet(
                visible = isConfirmDeleteSheetVisible,
                onDismiss = {
                    isConfirmDeleteSheetVisible = false
                },
                onConfirm = {
                    onAction(LossesUiAction.OnDeleteLoss(selectedLoss))
                    isConfirmDeleteSheetVisible = false
                },
                label = stringResource(R.string.loss)
            )
        }
    }

}