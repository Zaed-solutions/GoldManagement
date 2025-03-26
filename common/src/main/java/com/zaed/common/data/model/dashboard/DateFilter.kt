package com.zaed.common.data.model.dashboard

import com.zaed.common.ui.util.getCurrentDay
import com.zaed.common.ui.util.getCurrentMonth
import com.zaed.common.ui.util.getCurrentYear
import kotlinx.datetime.Month
import java.util.Calendar
import java.util.Date

data class DateFilter(
    val startDate: Date = getStartOfToday(),
    val endDate: Date = getEndOfToday(),
    val filterType: DateFilterType = DateFilterType.DAY,
    val selectedYear: Int = getCurrentYear(),
    val selectedMonth: Month = getCurrentMonth(),
    val selectedDay: Int = getCurrentDay(),
    val isFilterVisible: Boolean = false
)

fun getStartOfToday(): Date {
    val calendar = Calendar.getInstance().apply {
        set(Calendar.HOUR_OF_DAY, 0)
        set(Calendar.MINUTE, 0)
        set(Calendar.SECOND, 0)
        set(Calendar.MILLISECOND, 0)
    }
    return calendar.time
}

fun getEndOfToday(): Date {
    val calendar = Calendar.getInstance().apply {
        set(Calendar.HOUR_OF_DAY, 23)
        set(Calendar.MINUTE, 59)
        set(Calendar.SECOND, 59)
        set(Calendar.MILLISECOND, 999)
    }
    return calendar.time
}
