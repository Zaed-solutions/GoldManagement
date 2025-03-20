package com.zaed.manager.ui.home.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.zaed.manager.R
import java.time.Month

@Composable
fun MonthSelector(
    selectedMonth: Month,
    onClick :(Month) -> Unit
) {
    Text(
        text = stringResource(R.string.select_month),
        style = MaterialTheme.typography.titleMedium,
        fontWeight = FontWeight.Bold
    )

    Spacer(modifier = Modifier.height(8.dp))

    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(Month.entries) { month ->
            FilterChip(
                isSelected = month == selectedMonth,
                onClick = { onClick(month) },
                text = month.getMonthName(),
            )

        }
    }
}