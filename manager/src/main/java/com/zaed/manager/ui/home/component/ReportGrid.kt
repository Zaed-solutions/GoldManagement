package ui.home.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.zaed.common.R
import com.zaed.manager.ui.home.component.ReportCard
import com.zaed.manager.ui.home.component.ReportType

@Composable
fun ReportGrid(
    totalStoreSales: Double,
    totalStoreSalesLoading: Boolean,
    totalStoreLoss: Double,
    totalStoreLossLoading: Boolean,
    totalStoreProfit: Double,
    totalStoreProfitLoading: Boolean,
    totalWholesaleSales: Double,
    totalWholesaleSalesLoading: Boolean,
    totalWholesaleLoss: Double,
    totalWholesaleLossLoading: Boolean,
    totalWholesaleProfit: Double,
    totalWholesaleProfitLoading: Boolean,
    totalManagerSales: Double,
    totalManagerSalesLoading: Boolean,
    totalManagerLoss: Double,
    totalManagerLossLoading: Boolean,
    totalManagerProfit: Double,
    totalManagerProfitLoading: Boolean,
    onReportClick: (ReportType) -> Unit
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        contentPadding = PaddingValues(vertical = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            ReportCard(
                title = stringResource(R.string.store_sales),
                value = totalStoreSales,
                loading = totalStoreSalesLoading,
                iconRes = R.drawable.ic_store,
                cardColor = MaterialTheme.colorScheme.primaryContainer,
                onClick = { onReportClick(ReportType.STORE_SALES) }
            )
        }
        item {
            ReportCard(
                title = stringResource(R.string.store_profit),
                value = totalStoreProfit,
                loading = totalStoreProfitLoading,
                iconRes = R.drawable.ic_store,
                cardColor = MaterialTheme.colorScheme.secondaryContainer,
                onClick = { onReportClick(ReportType.STORE_PROFIT) }
            )
        }


        item {
            ReportCard(
                title = stringResource(R.string.store_loss),
                value = totalStoreLoss,
                loading = totalStoreLossLoading,
                iconRes = R.drawable.ic_money_minus,
                cardColor = MaterialTheme.colorScheme.errorContainer,
                onClick = { onReportClick(ReportType.STORE_LOSS) }
            )
        }
        item {
            ReportCard(
                title = stringResource(R.string.wholesale_sales),
                value = totalWholesaleSales,
                loading = totalWholesaleSalesLoading,
                iconRes = R.drawable.ic_customers,
                cardColor = MaterialTheme.colorScheme.primaryContainer,
                onClick = { onReportClick(ReportType.WHOLESALE_SALES) }
            )
        }
        item {
            ReportCard(
                title = stringResource(R.string.wholesale_profit),
                value = totalWholesaleProfit,
                loading = totalWholesaleProfitLoading,
                iconRes = R.drawable.ic_customers,
                cardColor = MaterialTheme.colorScheme.secondaryContainer,
                onClick = { onReportClick(ReportType.WHOLESALE_PROFIT) }
            )
        }


        item {
            ReportCard(
                title = stringResource(R.string.wholesale_loss),
                value = totalWholesaleLoss,
                loading = totalWholesaleLossLoading,
                iconRes = R.drawable.ic_money_minus,
                cardColor = MaterialTheme.colorScheme.errorContainer,
                onClick = { onReportClick(ReportType.WHOLESALE_LOSS) }
            )
        }
        item {
            ReportCard(
                title = stringResource(R.string.manager_sales),
                value = totalManagerSales,
                iconRes = R.drawable.ic_person,
                loading = totalManagerSalesLoading,
                cardColor = MaterialTheme.colorScheme.primaryContainer,
                onClick = { onReportClick(ReportType.MANAGER_SALES) }
            )
        }
        item {
            ReportCard(
                title = stringResource(R.string.manager_profit),
                value = totalManagerProfit,
                iconRes = R.drawable.ic_person,
                loading = totalManagerProfitLoading,
                cardColor = MaterialTheme.colorScheme.secondaryContainer,
                onClick = { onReportClick(ReportType.MANAGER_PROFIT) }
            )
        }


        item {
            ReportCard(
                title = stringResource(R.string.manager_loss),
                value = totalManagerLoss,
                loading = totalManagerLossLoading,
                iconRes = R.drawable.ic_money_minus,
                cardColor = MaterialTheme.colorScheme.errorContainer,
                onClick = { onReportClick(ReportType.MANAGER_LOSS) }
            )
        }
    }
}