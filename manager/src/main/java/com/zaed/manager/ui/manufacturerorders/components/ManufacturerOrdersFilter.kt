package com.zaed.manager.ui.manufacturerorders.components

import androidx.annotation.StringRes
import com.zaed.common.R
import java.util.Date

data class ManufacturerOrdersFilter(
    val startDate: Date? = null,
    val endDate: Date? = null,
    val orderStatus: OrderStatusFilter = OrderStatusFilter.ALL
){
    val isFiltered: Boolean
        get() = startDate != null || endDate != null || orderStatus != OrderStatusFilter.ALL
}

enum class OrderStatusFilter(@StringRes val title: Int){
    ALL(R.string.all),
    OPEN(R.string.open),
    CLOSED(R.string.closed)
}
