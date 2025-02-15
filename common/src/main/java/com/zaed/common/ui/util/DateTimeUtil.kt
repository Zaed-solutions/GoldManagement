package com.zaed.common.ui.util

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

fun Date.format(dateFormat: DateFormat): String {
    val formatter = SimpleDateFormat(dateFormat.pattern, Locale.getDefault())
    return formatter.format(this)
}

enum class DateFormat(val pattern: String){
    DATE("d MMM, yyyy"),
    TIME("hh:mm a"),
    DATE_TIME("d MMM, yyyy, hh:mm a"),
}

