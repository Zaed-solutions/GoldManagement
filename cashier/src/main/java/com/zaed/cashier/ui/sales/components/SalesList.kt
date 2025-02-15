package com.zaed.cashier.ui.sales.components

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.zaed.common.data.model.StoreSale
import com.zaed.common.ui.components.ListWithLoading
import com.zaed.common.ui.components.SwipeToEditOrDeleteContainer
import com.zaed.common.ui.util.DateFormat
import com.zaed.common.ui.util.format
import com.zaed.common.ui.util.formatMoney

@Composable
fun SalesList(
    modifier: Modifier = Modifier,
    isLoading: Boolean,
    sales: List<StoreSale>,
    onSaleClicked: (String) -> Unit,
    onDeleteSale: (StoreSale) -> Unit
) {
    ListWithLoading(
        isLoading = isLoading
    ) {
        LazyColumn(
            modifier = modifier.fillMaxSize(),
            contentPadding = PaddingValues(vertical = 16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(
                items = sales,
                key = { it.id }
            ) { sale ->
                SwipeToEditOrDeleteContainer(
                    modifier = Modifier.animateItem(),
                    onDelete = {
                        onDeleteSale(sale)
                    },
                    isEditEnabled = false
                ) {
                    SaleItem(
                        modifier = Modifier.padding(horizontal = 16.dp),
                        sale = sale,
                        onSaleClicked = { onSaleClicked(sale.id) }
                    )
                }
            }
        }
    }
}

@Composable
fun SaleItem(
    modifier: Modifier = Modifier,
    sale: StoreSale,
    onSaleClicked: () -> Unit
) {
    Surface(
        modifier = modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.medium,
        onClick = { onSaleClicked() },
        tonalElevation = 2.dp
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(
                    text = sale.customerName,
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Normal)
                )
                Text(
                    text = sale.createdAt.format(DateFormat.DATE_TIME),
                    style = MaterialTheme.typography.bodySmall.copy(color = MaterialTheme.colorScheme.onSurfaceVariant),
                )
            }
            Log.d("SalesList", "SaleItem: ${sale.totalPrice},,,,${sale.products}")
            Text(
                text = sale.totalPrice.formatMoney(),
                style = MaterialTheme.typography.titleMedium.copy(
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Bold
                )
            )
        }
    }
}