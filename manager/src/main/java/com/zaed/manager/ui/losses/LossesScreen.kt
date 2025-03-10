package com.zaed.manager.ui.losses

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Menu
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
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.zaed.common.R
import com.zaed.common.data.model.loss.ManagerLoss
import com.zaed.common.data.model.loss.ManagerLossType
import com.zaed.common.ui.components.ConfirmDeleteBottomSheet
import com.zaed.common.ui.components.DatedListWithFilter
import com.zaed.common.ui.components.DatedLossesList
import com.zaed.manager.ui.losses.components.SaveLossBottomSheet
import com.zaed.manager.ui.storedetails.StoreDetailsUiAction
import kotlinx.coroutines.launch
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
            when(action){
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
    onAction: (LossesUiAction) -> Unit,
) {
    val pagerState = rememberPagerState { 2 }
    val scope = rememberCoroutineScope()
    var selectedLoss by remember{
        mutableStateOf(ManagerLoss())
    }
    var isSaveLossSheetVisible by remember {
        mutableStateOf(false)
    }
    var isConfirmDeleteSheetVisible by remember{
        mutableStateOf(false)
    }
    Scaffold(
        modifier = modifier,
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
                        onClick = {
                            onAction(LossesUiAction.OnShowNavDrawer)
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
            FloatingActionButton(
                modifier = Modifier.padding(bottom = 8.dp, end = 8.dp).rotate(45f),
                shape = RoundedCornerShape(16.dp),
                onClick = {
                    selectedLoss = ManagerLoss(
                        type = if(pagerState.currentPage == 0) ManagerLossType.NORMAL else ManagerLossType.PERSONAL_EXPENSE
                    )
                    isSaveLossSheetVisible = true
                },
            ) {
                Icon(
                    modifier = Modifier.rotate(-45f),
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add Loss"
                )
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),

            ) {
            PrimaryTabRow(
                selectedTabIndex = pagerState.currentPage,
                indicator = {
                    TabRowDefaults.PrimaryIndicator(
                        modifier = Modifier
                            .run {
                                if (LocalLayoutDirection.current == LayoutDirection.Rtl)
                                    scale(-1f, 1f)
                                else
                                    this
                            }
                            .tabIndicatorOffset(pagerState.currentPage, true),
                        width = Dp.Unspecified,
                    )
                }
            ) {
                Tab(
                    selected = pagerState.currentPage == 0,
                    onClick = {
                        scope.launch {
                            pagerState.animateScrollToPage(0)
                        }
                    },
                    text = {
                        Text(text = stringResource(R.string.work_losses))
                    }
                )
                Tab(
                    selected = pagerState.currentPage == 1,
                    onClick = {
                        scope.launch {
                            pagerState.animateScrollToPage(1)
                        }
                    },
                    text = {
                        Text(text = stringResource(R.string.personal_expenses))
                    }
                )
            }
            HorizontalPager(
                state = pagerState
            ) { pageNum ->
                when (pageNum) {
                    0 -> {
                        DatedListWithFilter(
                            selectedFilter = state.selectedLossesFilter,
                            onFilterClicked = {
                                onAction(
                                    LossesUiAction.UpdateLossesDateFilter(
                                        it
                                    )
                                )
                            },
                            content = {
                                DatedLossesList(
                                    isLoading = state.isLoading,
                                    datedLosses = state.datedLosses,
                                    isEditEnabled = true,
                                    isDeleteEnabled = true,
                                    onUpdateLoss = {
                                        selectedLoss = it as ManagerLoss
                                        isSaveLossSheetVisible = true
                                    },
                                    onDeleteLoss = {
                                        selectedLoss = it as ManagerLoss
                                        isConfirmDeleteSheetVisible = true
                                    }
                                )
                            }
                        )
                    }

                    1 -> {
                        DatedListWithFilter(
                            selectedFilter = state.selectedPersonalExpensesFilter,
                            onFilterClicked = {
                                onAction(
                                    LossesUiAction.UpdatePersonalExpensesDateFilter(
                                        it
                                    )
                                )
                            },
                            content = {
                                DatedLossesList(
                                    isLoading = state.isLoading,
                                    datedLosses = state.datedPersonalExpenses,
                                    isEditEnabled = true,
                                    isDeleteEnabled = true,
                                    onUpdateLoss = {
                                        selectedLoss = it as ManagerLoss
                                        isSaveLossSheetVisible = true
                                    },
                                    onDeleteLoss = {
                                        selectedLoss = it as ManagerLoss
                                        isConfirmDeleteSheetVisible = true
                                    }
                                )
                            }
                        )
                    }
                }
            }
            SaveLossBottomSheet(
                isVisible = isSaveLossSheetVisible,
                onDismiss = {
                    isSaveLossSheetVisible = false
                },
                initialLoss = selectedLoss,
                onSaveLoss = {
                    isSaveLossSheetVisible = false
                    onAction(LossesUiAction.SaveLoss(it))
                }
            )
            ConfirmDeleteBottomSheet(
                visible = isConfirmDeleteSheetVisible,
                onDismiss = {
                    isConfirmDeleteSheetVisible = false
                },
                onConfirm = {
                    isConfirmDeleteSheetVisible = false
                    onAction(LossesUiAction.DeleteLoss(selectedLoss))
                },
                label = stringResource(
                    if(selectedLoss.type == ManagerLossType.PERSONAL_EXPENSE) R.string.personal_expense else R.string.loss
                )
            )
        }
    }
}