package com.zaed.manager.ui.home

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Replay
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.zaed.common.R
import com.zaed.common.ui.addpurchase.ProductType
import com.zaed.common.ui.util.toDateString
import com.zaed.manager.ui.home.component.DateFilterDialog
import com.zaed.manager.ui.home.component.EarningsAndLossesHeader
import com.zaed.manager.ui.home.component.HomeSummary
import com.zaed.manager.ui.home.component.HomeSummaryList
import com.zaed.manager.ui.home.component.ReportType
import com.zaed.manager.ui.home.component.getDateFilterDisplayText
import com.zaed.manager.ui.theme.ManagerTheme
import org.koin.androidx.compose.koinViewModel


// Screens
@Composable
fun DashboardScreen(
    viewModel: DashboardViewModel = koinViewModel(),
    navigateToStoresSales: (String, String) -> Unit,
    navigateToDistributorsSales: (String, String) -> Unit,
    navigateToWholesaleOverview : (ProductType) -> Unit,
    onShowNavDrawer: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    DashboardScreenContent(
        uiState = uiState,
        onAction = { action ->
            when (action) {
                DashboardUiAction.OnShowNavDrawer -> {
                    onShowNavDrawer()
                }

                is DashboardUiAction.NavigateToDetail -> {
                    when (action.reportType) {
                        ReportType.STORE_SALES -> {
                            navigateToStoresSales(
                                uiState.dateFilter.startDate.toDateString(),
                                uiState.dateFilter.endDate.toDateString()
                            )
                        }

                        ReportType.WHOLESALE_SALES -> {
                            navigateToDistributorsSales(
                                uiState.dateFilter.startDate.toDateString(),
                                uiState.dateFilter.endDate.toDateString()
                            )
                        }

                        else -> {}

                    }
                }

                is DashboardUiAction.OnStoresClicked -> { /*todo*/
                }

                is DashboardUiAction.OnGoldSalesClicked -> { /*todo*/
                    navigateToWholesaleOverview(ProductType.GOLD)

                }

                is DashboardUiAction.OnSilverSalesClicked -> { /*todo*/
                    navigateToWholesaleOverview(ProductType.SILVER)

                }

                is DashboardUiAction.OnIngotTransactionsClicked -> { /*todo*/
                    navigateToWholesaleOverview(ProductType.INGOT)
                }

                else -> viewModel.handleAction(action)
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreenContent(
    uiState: DashboardUiState,
    onAction: (DashboardUiAction) -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                navigationIcon = {
                    IconButton(
                        onClick = {
                            onAction(DashboardUiAction.OnShowNavDrawer)
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Menu,
                            contentDescription = null
                        )
                    }
                },
                actions = {
                    IconButton(
                        onClick = { onAction(DashboardUiAction.ReloadAllData) }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Replay,
                            contentDescription = "Filter"
                        )
                    }
                    IconButton(
                        onClick = { onAction(DashboardUiAction.ToggleDateFilterVisibility) }
                    ) {
                        Icon(
                            imageVector = Icons.Default.CalendarMonth,
                            contentDescription = "Filter"
                        )
                    }
                },
                title = {
                    Text(
                        text = getDateFilterDisplayText(uiState.dateFilter),
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold
                    )


                    if (uiState.dateFilter.isFilterVisible) {
                        DateFilterDialog(
                            dateFilter = uiState.dateFilter,
                            onDismiss = { onAction(DashboardUiAction.ToggleDateFilterVisibility) },
                            onAction = onAction
                        )
                    }
                }
            )
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .padding(it)
        ) {

            EarningsAndLossesHeader(
                totalEarnings = uiState.totalProfit,
                totalLosses = uiState.totalLoss,
            )

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = stringResource(com.zaed.common.R.string.other_report),
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(8.dp))

            HomeSummaryList(
                summaries = listOf(
                    HomeSummary(
                        title = stringResource(R.string.stores),
                        iconRes = R.drawable.ic_store,
                        totalSales = uiState.storesSales,
                        totalLosses = uiState.storesLoss,
                        onClick = {
                            onAction(DashboardUiAction.OnStoresClicked)
                        }
                    ),
                    HomeSummary(
                        title = stringResource(R.string.gold_sales),
                        iconRes = R.drawable.ic_gold,
                        totalSales = uiState.goldSales,
                        onClick = {
                            onAction(DashboardUiAction.OnGoldSalesClicked)
                        }
                    ),
                    HomeSummary(
                        title = stringResource(R.string.silver_sales),
                        iconRes = R.drawable.ic_coins,
                        totalSales = uiState.silverSales,
                        onClick = {
                            onAction(DashboardUiAction.OnSilverSalesClicked)
                        }
                    ),
                    HomeSummary(
                        title = stringResource(R.string.ingots_transactions),
                        iconRes = R.drawable.ic_ingot,
                        totalSales = uiState.ingotTransactions,
                        onClick = {
                            onAction(DashboardUiAction.OnIngotTransactionsClicked)
                        }
                    ),
                )
            )
            uiState.error?.let { error ->
                Text(
                    text = error,
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.padding(top = 16.dp)
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DashboardScreenPreview() {
    ManagerTheme {
        DashboardScreenContent(
            uiState = DashboardUiState(

            ),
            onAction = {}
        )
    }
}
