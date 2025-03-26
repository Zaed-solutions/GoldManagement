package com.zaed.common.ui.util

import android.content.Context
import androidx.annotation.StringRes
import com.zaed.common.R
import java.text.SimpleDateFormat
import java.time.Month
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.concurrent.TimeUnit

fun Date.format(dateFormat: DateFormat): String {
    val formatter = SimpleDateFormat(dateFormat.pattern, Locale.getDefault())
    return formatter.format(this)
}
fun Date.toDateString(): String {
    val dateFormat = SimpleDateFormat("EEE MMM dd HH:mm:ss zzz Z yyyy", Locale.getDefault())
    return dateFormat.format(this)
}

fun String.toDate(): Date {
    val dateFormat = SimpleDateFormat("EEE MMM dd HH:mm:ss zzz Z yyyy", Locale.getDefault())
    return dateFormat.parse(this) ?: Date()
}
fun Date.getPaymentTitle(context: Context): String {
    val currentDate = Calendar.getInstance()
    val targetDate = Calendar.getInstance().apply { time = this@getPaymentTitle }

    // Check if dates are in the same year
    val sameYear = currentDate.get(Calendar.YEAR) == targetDate.get(Calendar.YEAR)

    // Create a date format for showing time
    val timeFormat = SimpleDateFormat("h:mm a", Locale.getDefault())

    // Calculate days difference
    val daysDifference = calculateDaysDifference(currentDate, targetDate)

    return when {
        // Today
        daysDifference == 0 -> context.getString(R.string.today_at, timeFormat.format(this))

        // Yesterday
        daysDifference == 1 -> context.getString(R.string.yesterday_at, timeFormat.format(this))

        // Within last week
        daysDifference in 2..6 -> {
            val dayFormat = SimpleDateFormat("EEEE", Locale.getDefault())
            context.getString(R.string.last_day_at, dayFormat.format(this), timeFormat.format(this))
        }

        // If in the same year, show month and day
        sameYear -> {
            val dateFormat = SimpleDateFormat("MMMM d", Locale.getDefault())
            dateFormat.format(this)
        }

        // Otherwise, show full date
        else -> {
            val fullDateFormat = SimpleDateFormat("MMMM d, yyyy", Locale.getDefault())
            fullDateFormat.format(this)
        }
    }
}

private fun calculateDaysDifference(current: Calendar, target: Calendar): Int {
    val currentCopy = current.clone() as Calendar
    val targetCopy = target.clone() as Calendar

    currentCopy.set(Calendar.HOUR_OF_DAY, 0)
    currentCopy.set(Calendar.MINUTE, 0)
    currentCopy.set(Calendar.SECOND, 0)
    currentCopy.set(Calendar.MILLISECOND, 0)

    targetCopy.set(Calendar.HOUR_OF_DAY, 0)
    targetCopy.set(Calendar.MINUTE, 0)
    targetCopy.set(Calendar.SECOND, 0)
    targetCopy.set(Calendar.MILLISECOND, 0)

    val diffInMillis = currentCopy.timeInMillis - targetCopy.timeInMillis
    return TimeUnit.DAYS.convert(diffInMillis, TimeUnit.MILLISECONDS).toInt()
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

fun getCurrentYear(): Int {
    val calendar = Calendar.getInstance()
    return calendar.get(Calendar.YEAR)
}

 fun getCurrentMonth(): Month {
    val calendar = Calendar.getInstance()
    return Month.of(calendar.get(Calendar.MONTH) + 1) // Calendar months are 0-based
}

 fun getCurrentDay(): Int {
    val calendar = Calendar.getInstance()
    return calendar.get(Calendar.DAY_OF_MONTH)
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

