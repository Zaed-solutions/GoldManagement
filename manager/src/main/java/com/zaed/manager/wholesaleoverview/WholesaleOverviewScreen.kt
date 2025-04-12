package com.zaed.manager.wholesaleoverview

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Replay
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.zaed.common.ui.addpurchase.ProductType
import com.zaed.common.ui.util.toMoneyFormat
import com.zaed.manager.R
import com.zaed.manager.ui.home.DashboardUiAction
import com.zaed.manager.ui.home.component.DateFilterDialog
import com.zaed.manager.ui.home.component.EarningsAndLossesHeader
import com.zaed.manager.ui.home.component.getDateFilterDisplayText
import com.zaed.manager.ui.theme.ManagerTheme
import org.koin.androidx.compose.koinViewModel


// Screens
@Composable
fun WholesaleOverviewScreen(
    viewModel: WholesaleOverviewViewModel = koinViewModel(),
    type: ProductType = ProductType.PRODUCT,
    navigateToDistributorDetails: (String) -> Unit,
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    LaunchedEffect(Unit) {
        viewModel.init(type)
    }
    WholesaleOverviewScreenContent(
        uiState = uiState,
        onAction = { action ->
            when (action) {

                is DashboardUiAction.OnDistributorsSalesClicked ->{
                    navigateToDistributorDetails(action.distributorId)
                }
                else -> viewModel.handleAction(action)
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WholesaleOverviewScreenContent(
    uiState: WholesaleOverviewUiState,
    onAction: (DashboardUiAction) -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
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
                .padding(it),
            verticalArrangement = Arrangement.Top
        ) {
            val title = when(uiState.type){
                ProductType.GOLD -> stringResource(R.string.gold_distributors)
                ProductType.PRODUCT -> stringResource(R.string.product_distributors)
                ProductType.SILVER -> stringResource(R.string.silver_distributors)
                ProductType.INGOT -> stringResource(R.string.ingot_distributors)
            }
            Text(
                text = title,
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            EarningsAndLossesHeader(
                totalEarnings = uiState.wholesaleSummary.sumOf { it.profit },
                totalLosses = uiState.wholesaleSummary.sumOf { it.loss },
            )

            Spacer(modifier = Modifier.height(24.dp))


            uiState.wholesaleSummary.forEach { summary ->
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp),
                    shadowElevation = 4.dp,
                    color = MaterialTheme.colorScheme.surface,
                    contentColor = MaterialTheme.colorScheme.onSurface,
                    shape = MaterialTheme.shapes.large,
                    onClick = { onAction(DashboardUiAction.OnDistributorsSalesClicked(summary.distributorId)) }
                ) {
                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .padding(12.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Row {
                            Text(
                                text = stringResource(com.zaed.common.R.string.distributor),
                                style = MaterialTheme.typography.titleLarge,
                                modifier = Modifier.weight(1f),
                            )
                            Text(
                                text = summary.distributorName,
                                style = MaterialTheme.typography.titleLarge,
                                modifier = Modifier.weight(1f),
                            )
                        }
                        HorizontalDivider()
                        Row {
                            Text(
                                text = stringResource(R.string.profit),
                                style = MaterialTheme.typography.titleLarge,
                                modifier = Modifier.weight(1f),
                            )
                            Text(
                                text = summary.profit.toMoneyFormat(2),
                                style = MaterialTheme.typography.titleLarge,
                                modifier = Modifier.weight(1f),
                            )
                        }
                        HorizontalDivider()
                        Row {
                            Text(
                                text = stringResource(R.string.sales),
                                style = MaterialTheme.typography.titleLarge,
                                modifier = Modifier.weight(1f),
                            )
                            Text(
                                text = summary.sales.toMoneyFormat(2),
                                style = MaterialTheme.typography.titleLarge,
                                modifier = Modifier.weight(1f),
                            )
                        }
                        HorizontalDivider()
                        Row {
                            Text(
                                text = stringResource(R.string.losses),
                                style = MaterialTheme.typography.titleLarge,
                                modifier = Modifier.weight(1f),
                            )
                            Text(
                                text = summary.loss.toMoneyFormat(2),
                                style = MaterialTheme.typography.titleLarge,
                                modifier = Modifier.weight(1f),
                            )
                        }
                    }
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
fun WholesaleOverviewScreenPreview() {
    ManagerTheme {
        WholesaleOverviewScreenContent(
            uiState = WholesaleOverviewUiState(

            ),
            onAction = {}
        )
    }
}
