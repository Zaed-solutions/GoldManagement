package com.zaed.manager.ui.home.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.zaed.common.ui.util.getCurrentDay
import com.zaed.common.ui.util.getCurrentMonth
import com.zaed.common.ui.util.getCurrentYear
import java.time.Month
import java.util.Calendar
import java.util.Date

@Composable
fun DatePickerDialog(
    onDismissRequest: () -> Unit,
    onDateSelected: (Date) -> Unit
) {
    var selectedYear by remember { mutableStateOf(getCurrentYear()) }
    var selectedMonth by remember { mutableStateOf(getCurrentMonth()) }
    var selectedDay by remember { mutableStateOf(getCurrentDay()) }

    Dialog(onDismissRequest = onDismissRequest) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Text(
                    text = "Select Date",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Year selection
                YearSelector(
                    selectedYear = selectedYear,
                    onYearSelected = { selectedYear = it }
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Month selection
                Text(
                    text = "Select Month",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(8.dp))

                LazyRow {
                    items(Month.entries) { month ->
                        MonthItem(
                            month = month,
                            isSelected = month == selectedMonth,
                            onClick = { selectedMonth = month }
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Day selection
                Text(
                    text = "Select Day",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Generate days for the selected month
                val calendar = Calendar.getInstance()
                calendar.set(selectedYear, selectedMonth.value - 1, 1)
                val daysInMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH)

                LazyVerticalGrid(
                    columns = GridCells.Fixed(7),
                    modifier = Modifier.height(200.dp)
                ) {
                    items(daysInMonth) { day ->
                        DayItem(
                            day = day + 1,
                            isSelected = day + 1 == selectedDay,
                            onClick = { selectedDay = day + 1 }
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    TextButton(onClick = onDismissRequest) {
                        Text("Cancel")
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(
                        onClick = {
                            calendar.set(selectedYear, selectedMonth.value - 1, selectedDay)
                            onDateSelected(calendar.time)
                        }
                    ) {
                        Text("Select")
                    }
                }
            }
        }
    }
}