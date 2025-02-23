package com.zaed.common.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp

@Composable
fun MultiOptionSwitch(
    modifier: Modifier = Modifier,
    selectedIndex: Int = 0,
    options: List<String>,
    onOptionSelected: (Int) -> Unit
) {
    TabRow(
        selectedTabIndex = selectedIndex,
        containerColor = MaterialTheme.colorScheme.surfaceVariant,
        contentColor = MaterialTheme.colorScheme.onSurface,
        modifier = Modifier
            .padding(vertical = 4.dp, horizontal = 8.dp)
            .clip(MaterialTheme.shapes.extraLarge)
            .padding(1.dp),
        indicator = {},
        divider = {},
    ) {
        options.forEachIndexed { index, text ->
            val selected = selectedIndex == index
            Tab(
                modifier = if (selected) Modifier
                    .padding(4.dp)
                    .clip(MaterialTheme.shapes.extraLarge)
                    .background(
                        MaterialTheme.colorScheme.surface
                    )
                else Modifier
                    .clip(MaterialTheme.shapes.extraLarge)
                    .background(
                        MaterialTheme.colorScheme.surfaceVariant
                    ),
                selected = selected,
                onClick = {
                    onOptionSelected(index)
                },
                selectedContentColor = MaterialTheme.colorScheme.surface,
                unselectedContentColor = MaterialTheme.colorScheme.surfaceVariant,
                text = {
                    Text(
                        text = text,
                        style = MaterialTheme.typography.titleMedium,
                        color = if (selected) MaterialTheme.colorScheme.onSurface else MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            )
        }
    }
}