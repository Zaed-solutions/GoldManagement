package com.zaed.manager.ui.home.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import kotlinx.datetime.Month
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

@Composable
fun MonthItem(month: Month, isSelected: Boolean, onClick: () -> Unit) {
    val monthFormatter = SimpleDateFormat("MMM", Locale.getDefault())
    val calendar = Calendar.getInstance()
    calendar.set(Calendar.MONTH, month.value - 1)
    val monthName = monthFormatter.format(calendar.time)

    Box(
        modifier = Modifier
            .padding(4.dp)
            .background(
                color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surface,
                shape = RoundedCornerShape(8.dp)
            )
            .clickable(onClick = onClick)
            .padding(horizontal = 12.dp, vertical = 8.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = monthName,
            color = if (isSelected) Color.White else MaterialTheme.colorScheme.onSurface
        )
    }
}
fun Month.getMonthName(): String {
    val monthFormatter = SimpleDateFormat("MMM", Locale.getDefault())
    val calendar = Calendar.getInstance()
    calendar.set(Calendar.MONTH, value - 1)
    return monthFormatter.format(calendar.time)
}