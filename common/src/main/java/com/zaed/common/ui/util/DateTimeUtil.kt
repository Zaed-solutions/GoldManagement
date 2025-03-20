package com.zaed.common.ui.util

import androidx.annotation.StringRes
import com.zaed.common.R
import java.text.SimpleDateFormat
import java.time.Month
import java.util.Calendar
import java.util.Date
import java.util.Locale

fun Date.format(dateFormat: DateFormat): String {
    val formatter = SimpleDateFormat(dateFormat.pattern, Locale.getDefault())
    return formatter.format(this)
}

enum class DateFormat(val pattern: String, @StringRes val labelRes: Int){
    DATE("d MMM, yyyy", R.string.daily),
    MONTH_YEAR("MMM, yyyy", R.string.monthly),
    YEAR("yyyy", R.string.yearly),
    CUSTOM_RANGE("", R.string.custom_range),
    TIME("hh:mm a", 0),
    DATE_TIME("d MMM, yyyy, hh:mm a", 0),
    SHORT_DATE_TIME("d MMM, hh:mm a",0)

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

