package com.zaed.manager.ui.storesoverview.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.zaed.common.R
import com.zaed.common.data.model.authentication.User
import com.zaed.common.data.model.category.Category
import com.zaed.common.data.model.sale.StoreTransaction
import com.zaed.common.ui.components.PriceCalculationItem
import com.zaed.common.ui.components.SearchBarWithFilterButton
import com.zaed.manager.ui.storessales.components.StoreSalesFilter
import com.zaed.manager.ui.storessales.components.StoreSalesFilterBottomSheet
import com.zaed.manager.ui.storessales.components.StoresSalesList

@Composable
fun StoreSalesContent(
    modifier: Modifier = Modifier,
    searchQuery: String,
    onUpdateSearchQuery: (String) -> Unit,
    filter: StoreSalesFilter,
    totalAmount: Double,
    isLoading: Boolean, filteredSales: List<StoreTransaction>,
    onSaleClicked: (String) -> Unit,
    onUpdateFilter: (StoreSalesFilter) -> Unit,
    categories: List<Category>,
    locations: Set<String>,
    employees: List<User>,
    customers: Set<String>,
) {
    var isFilterSheetVisible by remember {
        mutableStateOf(false)
    }
    Column(
        modifier = modifier
            .fillMaxSize()
    ) {
        //search bar
        SearchBarWithFilterButton(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
            searchQuery = searchQuery,
            onQueryChanged = {
                onUpdateSearchQuery(it)
            },
            isFiltered = filter.isFiltered,
            onFilterClicked = {
                isFilterSheetVisible = true
            }
        )
        PriceCalculationItem(
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .padding(bottom = 8.dp),
            title = stringResource(R.string.total_amount),
            price = totalAmount,
            style = MaterialTheme.typography.titleMedium
        )
        //stores sales
        StoresSalesList(
            isLoading = isLoading,
            sales = filteredSales,
            onSaleClicked = {
                onSaleClicked(it.id)
            }
        )
        //filter bottom sheet
        StoreSalesFilterBottomSheet(
            isVisible = isFilterSheetVisible,
            onDismiss = {
                isFilterSheetVisible = false
            },
            onSubmitFilter = {
                isFilterSheetVisible = false
                onUpdateFilter(it)
            },
            initialFilter = filter,
            categories = categories,
            locations = locations,
            employees = employees,
            customers = customers
        )
    }
}