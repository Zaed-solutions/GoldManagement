package com.zaed.common.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.zaed.common.R
import com.zaed.common.ui.util.DateFormat
import com.zaed.common.ui.util.format
import kotlinx.serialization.json.JsonNull.content
import java.util.Date

@Composable
fun DatedListWithFilter(
    modifier: Modifier = Modifier,
    selectedFilter: DateFormat,
    onFilterClicked: (DateFormat) -> Unit,
    selectedRange: Pair<Date?, Date?> = null to null,
    onCustomRangeSelected: (Pair<Date?, Date?>) -> Unit,
    content: @Composable () -> Unit
) {
    val dateFilterItems = remember {
        listOf(DateFormat.DATE, DateFormat.MONTH_YEAR, DateFormat.YEAR, DateFormat.CUSTOM_RANGE)
    }
    var isDateRangePickerVisible by remember { mutableStateOf(false) }
    Column(
        modifier = modifier
            .fillMaxSize()
    ) {
        LazyRow(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp),
            contentPadding = PaddingValues(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(dateFilterItems) { filter ->
                val selected = filter == selectedFilter
                FilterChip(
                    selected = selected,
                    onClick = {
                        if(filter == DateFormat.CUSTOM_RANGE) {
                            isDateRangePickerVisible = true
                        } else {
                            onFilterClicked(filter)
                        }
                    },
                    leadingIcon = if (selected) {
                        {
                            Icon(
                                imageVector = Icons.Default.Check,
                                contentDescription = null,
                                modifier = Modifier.size(16.dp)
                            )
                        }
                    } else null,
                    label = {
                        Text(
                            text = stringResource(filter.labelRes)
                        )
                    }
                )
            }
        }
        if(selectedFilter == DateFormat.CUSTOM_RANGE){
            Column {
                selectedRange.first?.let {
                    Text(
                        modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 8.dp),
                        text = stringResource(
                            R.string.from_template,
                            it.format(DateFormat.DATE),
                        ),
                        maxLines = 1
                    )
                }
                selectedRange.second?.let {
                    Text(
                        modifier = Modifier.padding(start = 16.dp, end = 16.dp, bottom = 8.dp),
                        maxLines = 1,
                        text = stringResource(
                            R.string.to_template,
                            it.format(DateFormat.DATE),
                        )
                    )
                }
            }
        }
        content()
        AnimatedVisibility(isDateRangePickerVisible) {
            ModalDateRangePicker(
                onDateRangeSelected = { dateRange ->
                    onCustomRangeSelected(dateRange)
                    isDateRangePickerVisible = false
                },
                onDismiss = {
                    isDateRangePickerVisible = false
                }
            )
        }
    }

}