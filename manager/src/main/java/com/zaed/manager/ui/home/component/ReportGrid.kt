package com.zaed.manager.ui.home.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import com.zaed.common.R

@Composable
fun ReportGrid(
    totalStoreSales: Double,
    totalStoreLoss: Double,
    totalStoreProfit: Double,
    totalWholesaleSales: Double,
    totalWholesaleLoss: Double,
    totalWholesaleProfit: Double,
    totalManagerSales: Double,
    totalManagerLoss: Double,
    totalManagerProfit: Double,
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
                title = "Store Sales",
                value = totalStoreSales,
                iconRes = R.drawable.ic_store,
                cardColor = MaterialTheme.colorScheme.primaryContainer,
                onClick = { onReportClick(ReportType.STORE_SALES) }
            )
        }
        item {
            ReportCard(
                title = "Store Profit",
                value = totalStoreProfit,
                iconRes = R.drawable.ic_store,
                cardColor = MaterialTheme.colorScheme.secondaryContainer,
                onClick = { onReportClick(ReportType.STORE_PROFIT) }
            )
        }


        item {
            ReportCard(
                title = "Store Loss",
                value = totalStoreLoss,
                iconRes = R.drawable.ic_money_minus,
                cardColor = MaterialTheme.colorScheme.errorContainer,
                onClick = { onReportClick(ReportType.STORE_LOSS) }
            )
        }
        item {
            ReportCard(
                title = "Wholesale Sales",
                value = totalWholesaleSales,
                iconRes = R.drawable.ic_customers,
                cardColor = MaterialTheme.colorScheme.primaryContainer,
                onClick = { onReportClick(ReportType.WHOLESALE_SALES) }
            )
        }
        item {
            ReportCard(
                title = "Wholesale Profit",
                value = totalWholesaleProfit,
                iconRes = R.drawable.ic_customers,
                cardColor = MaterialTheme.colorScheme.secondaryContainer,
                onClick = { onReportClick(ReportType.WHOLESALE_PROFIT) }
            )
        }


        item {
            ReportCard(
                title = "Wholesale Loss",
                value = totalWholesaleLoss,
                iconRes = R.drawable.ic_money_minus,
                cardColor = MaterialTheme.colorScheme.errorContainer,
                onClick = { onReportClick(ReportType.WHOLESALE_LOSS) }
            )
        }
        item {
            ReportCard(
                title = "Manager Sales",
                value = totalManagerSales,
                iconRes = R.drawable.ic_person,
                cardColor = MaterialTheme.colorScheme.primaryContainer,
                onClick = { onReportClick(ReportType.MANAGER_SALES) }
            )
        }
        item {
            ReportCard(
                title = "Manager Profit",
                value = totalManagerProfit,
                iconRes = R.drawable.ic_person,
                cardColor = MaterialTheme.colorScheme.secondaryContainer,
                onClick = { onReportClick(ReportType.MANAGER_PROFIT) }
            )
        }


        item {
            ReportCard(
                title = "Manager Loss",
                value = totalManagerLoss,
                iconRes = R.drawable.ic_money_minus,
                cardColor = MaterialTheme.colorScheme.errorContainer,
                onClick = { onReportClick(ReportType.MANAGER_LOSS) }
            )
        }
    }
}