package com.zaed.common.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.zaed.common.R

@Composable
fun SearchBarWithFilterButton(
    modifier: Modifier = Modifier,
    searchQuery: String,
    onQueryChanged: (String) -> Unit,
    isFiltered: Boolean,
    onFilterClicked: () -> Unit
) {
    Row(
        modifier = modifier
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        SearchBar(
            modifier = Modifier.weight(1f),
            placeHolder = stringResource(R.string.search_by_receipt_number),
            query = searchQuery,
            onQueryChanged = onQueryChanged
        )
        BadgedBox(
            badge = {
                if(isFiltered) {
                    Box(
                        modifier = Modifier
                            .size(8.dp)
                            .background(
                                color = MaterialTheme.colorScheme.primary,
                                shape = CircleShape
                            )
                    )
                }
            }
        ) {
            IconButton(
                onClick = {
                    onFilterClicked()
                }
            ) {
                Icon(
                    imageVector = Icons.Default.FilterList,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primaryContainer,
                    modifier = Modifier
                        .size(40.dp)
                        .background(
                            color = MaterialTheme.colorScheme.onPrimaryContainer,
                            shape = CircleShape
                        )
                        .padding(8.dp)
                )
            }
        }
    }

}