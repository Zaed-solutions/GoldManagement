package com.zaed.manager.ui.home.component

import com.zaed.common.ui.util.getCurrentDay
import com.zaed.common.ui.util.getCurrentMonth
import com.zaed.common.ui.util.getCurrentYear
import kotlinx.datetime.Month
import java.util.Date

data class DateFilter(
    val startDate: Date? = null,
    val endDate: Date? = null,
    val filterType: DateFilterType = DateFilterType.MONTH,
    val selectedYear: Int = getCurrentYear(),
    val selectedMonth: Month = getCurrentMonth(),
    val selectedDay: Int = getCurrentDay(),
    val isFilterVisible: Boolean = false
)