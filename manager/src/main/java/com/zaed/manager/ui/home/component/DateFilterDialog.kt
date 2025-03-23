package com.zaed.manager.ui.home.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.zaed.manager.R
import com.zaed.manager.ui.home.DashboardUiAction
import java.util.Calendar

@Composable
fun DateFilterDialog(
    dateFilter: DateFilter,
    onDismiss: () -> Unit,
    onAction: (DashboardUiAction) -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
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
                    text = stringResource(R.string.select_date),
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(16.dp))

                FilterChips(
                    selectedFilterType = dateFilter.filterType,
                    onFilterTypeSelected = { filterType ->
                        onAction(DashboardUiAction.SelectDateFilterType(filterType))
                    }
                )

                Spacer(modifier = Modifier.height(16.dp))

                when (dateFilter.filterType) {
                    DateFilterType.DAY -> {
                        YearSelector(
                            selectedYear = dateFilter.selectedYear,
                            onYearSelected = { onAction(DashboardUiAction.SelectYear(it)) }
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        MonthSelector(
                            selectedMonth = dateFilter.selectedMonth,
                            onClick = {
                                onAction(DashboardUiAction.SelectMonth(it))
                            }
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        Text(
                            text = stringResource(R.string.select_day),
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        val calendar = Calendar.getInstance()
                        calendar.set(dateFilter.selectedYear, dateFilter.selectedMonth.value - 1, 1)
                        val daysInMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH)

                        LazyVerticalGrid(
                            columns = GridCells.Fixed(7),
                            modifier = Modifier.height(200.dp)
                        ) {
                            items(daysInMonth) { day ->
                                DayItem(
                                    day = day + 1,
                                    isSelected = day + 1 == dateFilter.selectedDay,
                                    onClick = { onAction(DashboardUiAction.SelectDay(day + 1)) }
                                )
                            }
                        }
                    }

                    DateFilterType.MONTH -> {
                        YearSelector(
                            selectedYear = dateFilter.selectedYear,
                            onYearSelected = { onAction(DashboardUiAction.SelectYear(it)) }
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        MonthSelector(
                            selectedMonth = dateFilter.selectedMonth,
                            onClick = {
                                onAction(DashboardUiAction.SelectMonth(it))
                            }
                        )
                    }

                    DateFilterType.YEAR -> {
                        YearSelector(
                            selectedYear = dateFilter.selectedYear,
                            onYearSelected = { onAction(DashboardUiAction.SelectYear(it)) }
                        )
                    }

                    DateFilterType.RANGE -> {
                        val startDate = dateFilter.startDate ?: Calendar.getInstance().time
                        val endDate = dateFilter.endDate ?: Calendar.getInstance().time

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            DatePickerField(
                                label = stringResource(R.string.start_date),
                                date = startDate,
                                onDateSelected = { newStartDate ->
                                    onAction(
                                        DashboardUiAction.SelectDateRange(
                                            newStartDate,
                                            endDate
                                        )
                                    )
                                },
                                modifier = Modifier.weight(1f)
                            )

                            Spacer(modifier = Modifier.width(16.dp))

                            DatePickerField(
                                label = stringResource(R.string.end_date),
                                date = endDate,
                                onDateSelected = { newEndDate ->
                                    onAction(
                                        DashboardUiAction.SelectDateRange(
                                            startDate,
                                            newEndDate
                                        )
                                    )
                                },
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    TextButton(onClick = onDismiss) {
                        Text(stringResource(R.string.cancel))
                    }

                    Spacer(modifier = Modifier.width(8.dp))

                    Button(onClick = { onAction(DashboardUiAction.ApplyDateFilter) }) {
                        Text(stringResource(R.string.apply))
                    }
                }
            }
        }
    }
}

