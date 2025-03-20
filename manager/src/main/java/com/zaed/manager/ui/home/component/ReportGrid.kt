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
    totalPurchases: Double,
    totalLosses: Double,
    totalWholesaleSales: Double,
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
                title = "Purchases",
                value = totalPurchases,
                iconRes = com.zaed.manager.R.drawable.ic_add_cart,
                cardColor = MaterialTheme.colorScheme.secondaryContainer,
                onClick = { onReportClick(ReportType.PURCHASES) }
            )
        }

        item {
            ReportCard(
                title = "Wholesale Sales",
                value = totalWholesaleSales,
                iconRes = R.drawable.ic_person,
                cardColor = MaterialTheme.colorScheme.tertiaryContainer,
                onClick = { onReportClick(ReportType.WHOLESALE_SALES) }
            )
        }

        item {
            ReportCard(
                title = "Losses",
                value = totalLosses,
                iconRes = R.drawable.ic_money_minus,
                cardColor = MaterialTheme.colorScheme.errorContainer,
                onClick = { onReportClick(ReportType.STORE_LOSS) }
            )
        }
    }
}