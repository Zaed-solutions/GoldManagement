package com.zaed.manager.ui.storedetails.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.zaed.common.data.model.sale.DatedStoreSale
import com.zaed.common.data.model.sale.DatedWholesaleProductSale
import com.zaed.common.ui.components.DatedListWithFilter
import com.zaed.common.ui.components.DatedProductSalesList
import com.zaed.common.ui.util.DateFormat

@Composable
fun StoreSalesSection(
    modifier: Modifier = Modifier,
    isLoading: Boolean,
    selectedFilter: DateFormat,
    onFilterClicked: (DateFormat) -> Unit,
    datedProductSales: List<DatedStoreSale>,
    onSaleClicked: (String) -> Unit
){
    DatedListWithFilter(
        modifier = modifier,
        isLoading = isLoading,
        selectedFilter = selectedFilter,
        onFilterClicked = onFilterClicked
    ){
        DatedProductSalesList(
            isLoading = isLoading,
            datedProductSales = datedProductSales,
            onSaleClicked = {}
        )
    }

}