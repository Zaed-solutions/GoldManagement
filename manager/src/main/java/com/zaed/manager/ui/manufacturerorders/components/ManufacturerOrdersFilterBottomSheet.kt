package com.zaed.manager.ui.manufacturerorders.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.ModalBottomSheetDefaults.properties
import androidx.compose.material3.ModalBottomSheetProperties
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.zaed.common.R
import com.zaed.common.ui.components.TitledSection
import com.zaed.manager.ui.distributorssales.components.DistributorsSalesFilter
import com.zaed.manager.ui.storessales.components.DatePickerFieldToModal

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ManufacturerOrdersFilterBottomSheet(
    modifier: Modifier = Modifier,
    visible: Boolean,
    initialFilter: ManufacturerOrdersFilter,
    onDismiss: () -> Unit,
    onApply: (ManufacturerOrdersFilter) -> Unit
) {
    val bottomSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    AnimatedVisibility(visible) {
        var filter by remember {
            mutableStateOf(initialFilter)
        }
        ModalBottomSheet(
            sheetState = bottomSheetState,
            onDismissRequest = onDismiss,
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(
                    text = stringResource(R.string.filter),
                    style = MaterialTheme.typography.headlineMedium
                )
                TitledSection(
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
                TitledSection(
                    title = stringResource(R.string.order_status),
                    style = MaterialTheme.typography.titleLarge
                ) {
                    Row (
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ){
                        OrderStatusFilter.entries.forEach { status ->
                            FilterChip(
                                selected = filter.orderStatus == status,
                                label = {
                                    Text(text = stringResource(status.title))
                                },
                                leadingIcon = {
                                    if (filter.orderStatus == status) {
                                        Icon(
                                            imageVector = Icons.Default.Check,
                                            contentDescription = null,
                                            modifier = Modifier.size(16.dp)
                                        )
                                    }
                                },
                                onClick = {
                                    filter = filter.copy(orderStatus = status)
                                }
                            )
                        }
                    }
                }
                Row (
                    modifier = Modifier.fillMaxWidth().padding(vertical = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Button(
                        modifier = Modifier
                            .weight(1f)
                            .heightIn(min = 48.dp),
                        onClick = {
                            onApply(filter)
                        },
                        shape = MaterialTheme.shapes.medium
                    ) {
                        Text(
                            text = stringResource(R.string.confirm),
                            style = MaterialTheme.typography.titleMedium
                        )
                    }
                    FilledTonalButton(
                        modifier = Modifier
                            .weight(1f)
                            .heightIn(min = 48.dp),
                        onClick = {
                            onApply(ManufacturerOrdersFilter())
                        },
                        shape = MaterialTheme.shapes.medium
                    ) {
                        Text(
                            text = stringResource(R.string.clear_all),
                            style = MaterialTheme.typography.titleMedium
                        )
                    }
                }
            }
        }
    }

}