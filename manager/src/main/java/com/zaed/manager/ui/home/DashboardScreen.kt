package com.zaed.manager.ui.home

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.zaed.common.data.model.dashboard.Dashboard
import com.zaed.manager.ui.home.component.DateFilterDialog
import com.zaed.manager.ui.home.component.ReportGrid
import com.zaed.manager.ui.home.component.SummaryCards
import com.zaed.manager.ui.home.component.getDateFilterDisplayText
import com.zaed.manager.ui.theme.ManagerTheme
import org.koin.androidx.compose.koinViewModel


// Screens
@Composable
fun DashboardScreen(
    viewModel: DashboardViewModel = koinViewModel(),
    onShowNavDrawer: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    DashboardScreenContent(
        uiState = uiState,
        onAction = { action ->
            when (action) {
                DashboardUiAction.OnShowNavDrawer -> {onShowNavDrawer()}
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

            SummaryCards(
                totalProfit = uiState.dashboardData.totalNetProfit,
                totalLoss = uiState.dashboardData.totalLoss,
                changePercentage = 20
            )

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "Other Report",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(8.dp))

            ReportGrid(
                totalStoreSales = uiState.dashboardData.totalStoreSales,
                totalPurchases = uiState.dashboardData.totalPurchases,
                totalLosses = uiState.dashboardData.totalLoss,
                totalWholesaleSales = uiState.dashboardData.totalWholesaleSales,
                onReportClick = { reportType ->
                    onAction(DashboardUiAction.NavigateToDetail(reportType))
                }
            )

            if (uiState.isLoading) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }

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
                dashboardData = Dashboard(
                    totalProfit = 15000.0,
                    totalLoss = 6219.19,
                    totalNetProfit = 8780.81,
                    totalStoreSales = 24805.0,
                    totalPurchases = 10580.0,
                    totalWholesaleSales = 11250.0
                )
            ),
            onAction = {}
        )
    }
}
