package com.zaed.common.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.zaed.common.R
import com.zaed.common.data.model.sale.DatedSales
import com.zaed.common.data.model.sale.Transaction
import com.zaed.common.ui.util.DateFormat
import java.util.Date

@Composable
fun DatedSalesWithSearchSection(
    modifier: Modifier = Modifier,
    isLoading: Boolean,
    query: String,
    onQueryChanged: (String) -> Unit,
    selectedFilter: DateFormat,
    onFilterClicked: (DateFormat) -> Unit,
    onCustomRangeSelected: (Pair<Date?, Date?>) -> Unit,
    datedSales: List<DatedSales>,
    onSaleClicked: (String,String) -> Unit,
    isEditable: Boolean = false,
    onEdit: (Transaction) -> Unit= {},
    isDeletable: Boolean = false,
    onDelete: (Transaction) -> Unit = {},
){
    Column (
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        SearchBar(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
            placeHolder = stringResource(R.string.search_by_receipt_number),
            query = query,
            onQueryChanged = onQueryChanged
        )
        DatedListWithFilter(
            modifier = modifier,
            selectedFilter = selectedFilter,
            onFilterClicked = onFilterClicked,
            onCustomRangeSelected = onCustomRangeSelected
        ) {
            DatedSalesList(
                isLoading = isLoading,
                datedSales = datedSales,
                onSaleClicked = onSaleClicked,
                isEditable = isEditable,
                onEdit = onEdit,
                isDeletable = isDeletable,
                onDelete = onDelete
            )
        }
    }

}