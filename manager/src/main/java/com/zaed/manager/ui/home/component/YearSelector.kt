package com.zaed.manager.ui.home.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
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
import java.util.Calendar

@Composable
fun YearSelector(
    selectedYear: Int,
    onYearSelected: (Int) -> Unit
) {
    val calendar = Calendar.getInstance()
    val currentYear = calendar.get(Calendar.YEAR)
    val years = (currentYear - 5..currentYear + 1).toList().reversed()

    Column {
        Text(
            text = stringResource(com.zaed.common.R.string.select_year),
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(8.dp))

        LazyRow (
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ){
            items(years) { year ->
                FilterChip (
                    isSelected = year == selectedYear,
                    onClick = { onYearSelected(year) },
                    text = year.toString(),
                )
            }
        }
    }
}