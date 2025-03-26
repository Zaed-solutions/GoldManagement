package com.zaed.manager.ui.home.component

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.zaed.common.data.model.dashboard.DateFilter
import com.zaed.common.data.model.dashboard.DateFilterType
import com.zaed.common.ui.util.DateFormat
import com.zaed.common.ui.util.format
import java.util.Calendar
import java.util.Date

@Composable
fun getDateFilterDisplayText(dateFilter: DateFilter): String {
    return when (dateFilter.filterType) {
        DateFilterType.DAY -> {
            val calendar = Calendar.getInstance()
            calendar.set(
                dateFilter.selectedYear,
                dateFilter.selectedMonth.value - 1,
                dateFilter.selectedDay
            )
            calendar.time.format(DateFormat.DATE)
        }

        DateFilterType.MONTH -> {
            val calendar = Calendar.getInstance()
            calendar.set(dateFilter.selectedYear, dateFilter.selectedMonth.value - 1, 1)
            calendar.time.format(DateFormat.MONTH_YEAR)
        }

        DateFilterType.YEAR -> {
            Date(dateFilter.selectedYear - 1900, 0, 1).format(DateFormat.YEAR)
        }

        DateFilterType.RANGE -> {
            if (dateFilter.startDate != null && dateFilter.endDate != null) {
                "${dateFilter.startDate!!.format(DateFormat.DATE)} - ${
                    dateFilter.endDate!!.format(
                        DateFormat.DATE
                    )
                }"
            } else {
                stringResource(id = DateFormat.CUSTOM_RANGE.labelRes)
            }
        }
    }
}