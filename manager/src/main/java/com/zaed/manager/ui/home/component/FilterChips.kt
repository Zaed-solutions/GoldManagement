package com.zaed.manager.ui.home.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.zaed.manager.R

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun FilterChips(
    selectedFilterType: DateFilterType,
    onFilterTypeSelected: (DateFilterType) -> Unit
) {
    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        item {
            FilterChip(
                text = stringResource(R.string.day),
                isSelected = selectedFilterType == DateFilterType.DAY,
                onClick = { onFilterTypeSelected(DateFilterType.DAY) }
            )
        }
        item {

            FilterChip(
                text = stringResource(R.string.month),
                isSelected = selectedFilterType == DateFilterType.MONTH,
                onClick = { onFilterTypeSelected(DateFilterType.MONTH) }
            )
        }
        item {

            FilterChip(
                text = stringResource(R.string.year),
                isSelected = selectedFilterType == DateFilterType.YEAR,
                onClick = { onFilterTypeSelected(DateFilterType.YEAR) }
            )
        }
        item {

            FilterChip(
                text = stringResource(R.string.range),
                isSelected = selectedFilterType == DateFilterType.RANGE,
                onClick = { onFilterTypeSelected(DateFilterType.RANGE) }
            )
        }
    }
}