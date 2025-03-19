package com.zaed.common.ui.util

import androidx.annotation.StringRes
import com.zaed.common.R
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

fun Date.format(dateFormat: DateFormat): String {
    val formatter = SimpleDateFormat(dateFormat.pattern, Locale.getDefault())
    return formatter.format(this)
}

enum class DateFormat(val pattern: String, @StringRes val labelRes: Int) {
    DATE("d MMM, yyyy", R.string.daily),
    MONTH_YEAR("MMM, yyyy", R.string.monthly),
    YEAR("yyyy", R.string.yearly),
    CUSTOM_RANGE("", R.string.custom_range),
    TIME("hh:mm a", 0),
    DATE_TIME("d MMM, yyyy, hh:mm a", 0),
    SHORT_DATE_TIME("d MMM, hh:mm a", 0)

}


fun Date.isAfter(date: Date): Boolean {
    // Extract just the date part (year, month, day) from both dates
    val originalCalendar = Calendar.getInstance()
    originalCalendar.time = this

    val startCalendar = Calendar.getInstance()
    startCalendar.time = date

    // Compare only year, month, and day (ignore time)
    val originalYear = originalCalendar.get(Calendar.YEAR)
    val originalMonth = originalCalendar.get(Calendar.MONTH)
    val originalDay = originalCalendar.get(Calendar.DAY_OF_MONTH)

    val startYear = startCalendar.get(Calendar.YEAR)
    val startMonth = startCalendar.get(Calendar.MONTH)
    val startDay = startCalendar.get(Calendar.DAY_OF_MONTH)

    // Compare the dates without time components
    return if (originalYear > startYear) true
    else if (originalYear < startYear) false
    else if (originalMonth > startMonth) true
    else if (originalMonth < startMonth) false
    else originalDay >= startDay
}

fun Date.isBefore(date: Date): Boolean {
    val originalCalendar = Calendar.getInstance()
    originalCalendar.time = this

    val endCalendar = Calendar.getInstance()
    endCalendar.time = date

    val originalYear = originalCalendar.get(Calendar.YEAR)
    val originalMonth = originalCalendar.get(Calendar.MONTH)
    val originalDay = originalCalendar.get(Calendar.DAY_OF_MONTH)

    val endYear = endCalendar.get(Calendar.YEAR)
    val endMonth = endCalendar.get(Calendar.MONTH)
    val endDay = endCalendar.get(Calendar.DAY_OF_MONTH)
    return if (originalYear < endYear) true
    else if (originalYear > endYear) false
    else if (originalMonth < endMonth) true
    else if (originalMonth > endMonth) false
    else endDay >= originalDay
}

