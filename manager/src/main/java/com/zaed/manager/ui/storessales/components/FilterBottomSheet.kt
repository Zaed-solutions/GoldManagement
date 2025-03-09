package com.zaed.manager.ui.storessales.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.FlowRowOverflow
import androidx.compose.foundation.layout.FlowRowOverflowScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.ModalBottomSheetProperties
import androidx.compose.material3.SheetValue
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.layout
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.zaed.common.R
import com.zaed.common.data.model.Category
import com.zaed.common.data.model.authentication.User
import com.zaed.common.ui.components.TitledSection

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun FilterBottomSheet(
    modifier: Modifier = Modifier,
    isVisible: Boolean,
    onDismiss: () -> Unit,
    onSubmitFilter: (StoreSalesFilter) -> Unit,
    initialFilter: StoreSalesFilter,
    locations: Set<String>,
    employees: List<User>,
    customers: Set<String>,
    categories: List<Category>
) {
    val bottomSheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true,
        confirmValueChange = { newValue ->
            newValue != SheetValue.Hidden || !isVisible
        }
    )
    AnimatedVisibility(
        modifier = modifier,
        visible = isVisible
    ) {
        var filter by remember {
            mutableStateOf(initialFilter)
        }
        ModalBottomSheet(
            sheetState = bottomSheetState,
            shape = RectangleShape,
            dragHandle = {},
            onDismissRequest = {},
            properties = ModalBottomSheetProperties(shouldDismissOnBackPress = false)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                ) {
                    Text(
                        modifier = Modifier.align(Alignment.Center),
                        text = stringResource(R.string.filter),
                        style = MaterialTheme.typography.headlineMedium
                    )
                    IconButton(
                        modifier = Modifier.align(Alignment.CenterStart),
                        onClick = {
                            onDismiss()
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Clear,
                            contentDescription = null
                        )
                    }
                }
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .padding(top = 16.dp)
                        .verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    TitledSection(
                        modifier = Modifier.padding(horizontal = 16.dp),
                        title = stringResource(R.string.date),
                        style = MaterialTheme.typography.titleLarge
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            DatePickerFieldToModal(
                                modifier = Modifier.weight(1f),
                                initialValue = filter.startDate,
                                label = stringResource(R.string.start_date),
                                onDateSelected = {
                                    filter = filter.copy(startDate = it)
                                }
                            )
                            DatePickerFieldToModal(
                                modifier = Modifier.weight(1f),
                                initialValue = filter.endDate,
                                label = stringResource(R.string.end_date),
                                onDateSelected = {
                                    filter = filter.copy(endDate = it)
                                }
                            )
                        }
                    }
                    HorizontalDivider(thickness = 1.dp)
                    TitledSection(
                        modifier = Modifier.padding(horizontal = 16.dp),
                        title = stringResource(R.string.location),
                        style = MaterialTheme.typography.titleLarge
                    ) {
                        FlowRow(
                            modifier = Modifier.fillMaxWidth(),
                            verticalArrangement = Arrangement.spacedBy(8.dp),
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                        ) {
                            locations.forEach { location ->
                                val selected = filter.locations.contains(location)
                                FilterChip(
                                    selected = selected,
                                    label = {
                                        Text(text = location)
                                    },
                                    leadingIcon = {
                                        if (selected) {
                                            Icon(
                                                imageVector = Icons.Default.Check,
                                                contentDescription = null,
                                                modifier = Modifier.size(16.dp)
                                            )
                                        }
                                    },
                                    onClick = {
                                        filter = if (selected) {
                                            filter.copy(locations = filter.locations - location)
                                        } else {
                                            filter.copy(locations = filter.locations + location)
                                        }
                                    }
                                )
                            }
                        }
                    }
                    HorizontalDivider(thickness = 1.dp)
                    TitledSection(
                        modifier = Modifier.padding(horizontal = 16.dp),
                        title = stringResource(R.string.employees),
                        style = MaterialTheme.typography.titleLarge
                    ) {
                        FlowRow(
                            modifier = Modifier.fillMaxWidth(),
                            verticalArrangement = Arrangement.spacedBy(8.dp),
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                        ) {
                            employees.forEach { employee ->
                                val selected = filter.employees.contains(employee)
                                FilterChip(
                                    selected = selected,
                                    label = {
                                        Text(text = employee.fullName)
                                    },
                                    leadingIcon = {
                                        if (selected) {
                                            Icon(
                                                imageVector = Icons.Default.Check,
                                                contentDescription = null,
                                                modifier = Modifier.size(16.dp)
                                            )
                                        }
                                    },
                                    onClick = {
                                        filter = if (selected) {
                                            filter.copy(employees = filter.employees - employee)
                                        } else {
                                            filter.copy(employees = filter.employees + employee)
                                        }
                                    }
                                )
                            }
                        }
                    }
                    HorizontalDivider(thickness = 1.dp)
                    TitledSection(
                        modifier = Modifier.padding(horizontal = 16.dp),
                        title = stringResource(R.string.customers),
                        style = MaterialTheme.typography.titleLarge
                    ) {
                        var maxLines by remember { mutableStateOf(3) }

                        FlowRow(
                            modifier = Modifier.fillMaxWidth(),
                            verticalArrangement = Arrangement.spacedBy(8.dp),
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            maxLines = maxLines,
                            overflow = FlowRowOverflow.expandIndicator {
                                TextButton(
                                    onClick = {
                                        maxLines = Int.MAX_VALUE
                                    }
                                ) {
                                    Text(
                                        text = "... ${stringResource(R.string.show_more)}",
                                    )
                                }
                            }
                        ) {
                            customers.forEach { customer ->
                                val selected = filter.customers.contains(customer)
                                FilterChip(
                                    selected = selected,
                                    label = {
                                        Text(text = customer)
                                    },
                                    leadingIcon = {
                                        if (selected) {
                                            Icon(
                                                imageVector = Icons.Default.Check,
                                                contentDescription = null,
                                                modifier = Modifier.size(16.dp)
                                            )
                                        }
                                    },
                                    onClick = {
                                        filter = if (selected) {
                                            filter.copy(customers = filter.customers - customer)
                                        } else {
                                            filter.copy(customers = filter.customers + customer)
                                        }
                                    }
                                )
                            }
                        }
                    }
                    HorizontalDivider(thickness = 1.dp)
                    TitledSection(
                        modifier = Modifier.padding(horizontal = 16.dp),
                        title = stringResource(R.string.categories),
                        style = MaterialTheme.typography.titleLarge
                    ) {
                        var maxLines by remember { mutableIntStateOf(3) }
                        FlowRow(
                            modifier = Modifier.fillMaxWidth(),
                            verticalArrangement = Arrangement.spacedBy(8.dp),
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            maxLines = maxLines,
                            overflow = FlowRowOverflow.expandIndicator {
                                TextButton(
                                    onClick = {
                                        maxLines = Int.MAX_VALUE
                                    }
                                ) {
                                    Text(
                                        text = "... ${stringResource(R.string.show_more)}",
                                    )
                                }
                            }
                        ) {
                            categories.forEach { category ->
                                val selected = filter.categories.contains(category)
                                FilterChip(
                                    selected = selected,
                                    label = {
                                        Text(text = category.name)
                                    },
                                    leadingIcon = {
                                        if (selected) {
                                            Icon(
                                                imageVector = Icons.Default.Check,
                                                contentDescription = null,
                                                modifier = Modifier.size(16.dp)
                                            )
                                        }
                                    },
                                    onClick = {
                                        filter = if (selected) {
                                            filter.copy(categories = filter.categories - category)
                                        } else {
                                            filter.copy(categories = filter.categories + category)
                                        }
                                    }
                                )
                            }
                        }
                    }
                    HorizontalDivider(thickness = 1.dp)
                }
                Button(
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(min = 48.dp)
                        .padding(16.dp),
                    onClick = {
                        onSubmitFilter(filter)
                    },
                    shape = MaterialTheme.shapes.medium
                ) {
                    Text(
                        text = stringResource(R.string.confirm),
                        style = MaterialTheme.typography.titleLarge
                    )
                }
            }
        }
    }
}

