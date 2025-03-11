package com.zaed.manager.ui.manufacturerorders.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.zaed.common.data.model.manufacturerorder.ManufacturerOrder
import com.zaed.common.ui.components.NumberInputTextField
import com.zaed.common.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SaveManufacturerOrderBottomSheet(
    modifier: Modifier = Modifier,
    visible: Boolean,
    initialOrder: ManufacturerOrder,
    onDismiss: () -> Unit,
    onSave: (ManufacturerOrder) -> Unit
) {
    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true
    )
    AnimatedVisibility(
        modifier = modifier,
        visible = visible
    ) {
        var order by remember {
            mutableStateOf(initialOrder)
        }
        ModalBottomSheet(
            onDismissRequest = onDismiss,
            sheetState = sheetState
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = stringResource(
                        if (order.id.isBlank()) {
                            R.string.add_manufacturer_order
                        } else {
                            R.string.edit_manufacturer_order
                        }
                    ),
                    style = MaterialTheme.typography.titleLarge
                )
                NumberInputTextField(
                    modifier = Modifier.fillMaxWidth().padding(top = 16.dp),
                    label = stringResource(R.string.raw_gold_amount),
                    value = order.rawAmount,
                    onValueChange = {
                        order = order.copy(rawAmount = it)
                    },
                    withBorder = true,
                    shape = MaterialTheme.shapes.medium,
                )
                NumberInputTextField(
                    modifier = Modifier.fillMaxWidth(),
                    label = stringResource(R.string.received_gold_amount),
                    value = order.receivedAmount,
                    onValueChange = {
                        order = order.copy(receivedAmount = it)
                    },
                    withBorder = true,
                    shape = MaterialTheme.shapes.medium,
                )
                NumberInputTextField(
                    modifier = Modifier.fillMaxWidth(),
                    label = stringResource(R.string.processing_fee),
                    value = order.totalProcessingFee,
                    onValueChange = {
                        order = order.copy(totalProcessingFee = it)
                    },
                    withBorder = true,
                    shape = MaterialTheme.shapes.medium,
                )
                NumberInputTextField(
                    modifier = Modifier.fillMaxWidth(),
                    label = stringResource(R.string.paid_amount),
                    value = order.paidProcessingFee,
                    onValueChange = {
                        order = order.copy(paidProcessingFee = it)
                    },
                    withBorder = true,
                    shape = MaterialTheme.shapes.medium,
                )
                AnimatedVisibility(
                    visible = order.receivedAmount != 0.0 && order.receivedAmount != order.rawAmount
                ) {
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(
                            text = stringResource(R.string.difference_recorded),
                            style = MaterialTheme.typography.titleMedium,
                        )
                        CustomSegmentedButton(
                            modifier = Modifier.fillMaxWidth(),
                            isFirstSelected = order.differenceOnManufacturer,
                            options = stringResource(R.string.on_manufacturer) to stringResource(R.string.for_manufacturer),
                            onOptionSelected = {
                                order = order.copy(differenceOnManufacturer = it)
                            }
                        )
                    }
                }
                AnimatedVisibility(
                    visible = order.id.isNotBlank()
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = stringResource(R.string.close_order),
                            style = MaterialTheme.typography.titleMedium
                        )
                        Checkbox(
                            checked = order.closed,
                            onCheckedChange = {
                                order = order.copy(closed = it)
                            }
                        )
                    }
                }
                Button(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp, bottom = 24.dp)
                        .heightIn(min = 48.dp),
                    onClick = {
                        onSave(order)
                    }
                ) {
                    Text(
                        text = stringResource(R.string.save),
                        style = MaterialTheme.typography.titleMedium
                    )
                }
            }
        }
    }
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
private fun CustomSegmentedButton(
    modifier: Modifier = Modifier,
    isFirstSelected: Boolean,
    options: Pair<String, String>,
    onOptionSelected: (Boolean) -> Unit
) {
    SingleChoiceSegmentedButtonRow(
        modifier = modifier
    ) {
        SegmentedButton(
            selected = isFirstSelected,
            onClick = {
                onOptionSelected(true)
            },
            shape = RoundedCornerShape(topStart = 16.dp, bottomStart = 16.dp),
            icon = {},
            colors = SegmentedButtonDefaults.colors(
                activeContainerColor = MaterialTheme.colorScheme.secondary,
                activeContentColor = MaterialTheme.colorScheme.onSecondary,
                activeBorderColor = Color.Transparent,
                inactiveContainerColor = MaterialTheme.colorScheme.secondaryContainer,
                inactiveContentColor = MaterialTheme.colorScheme.onSecondaryContainer,
                inactiveBorderColor = Color.Transparent
            )
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                AnimatedVisibility(isFirstSelected) {
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp)
                    )
                }
                Text(
                    text = options.first,
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontSize = 16.sp,
                        lineHeight = 19.sp
                    )
                )
            }
        }
        SegmentedButton(
            modifier = Modifier.weight(1f),
            selected = !isFirstSelected,
            onClick = {
                onOptionSelected(false)
            },
            shape = RoundedCornerShape(topEnd = 16.dp, bottomEnd = 16.dp),
            icon = {},
            colors = SegmentedButtonDefaults.colors(
                activeContainerColor = MaterialTheme.colorScheme.secondary,
                activeContentColor = MaterialTheme.colorScheme.onSecondary,
                activeBorderColor = Color.Transparent,
                inactiveContainerColor = MaterialTheme.colorScheme.secondaryContainer,
                inactiveContentColor = MaterialTheme.colorScheme.onSecondaryContainer,
                inactiveBorderColor = Color.Transparent
            )
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                AnimatedVisibility(!isFirstSelected) {
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp)
                    )
                }
                Text(
                    text = options.second,
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontSize = 16.sp,
                        lineHeight = 19.sp
                    )
                )
            }
        }
    }
}

@Preview(showSystemUi = true, showBackground = true, device = "id:pixel_9_pro")
@Composable
private fun Preview() {
    CustomSegmentedButton(
        isFirstSelected = true,
        options = stringResource(R.string.on_manufacturer) to stringResource(R.string.for_manufacturer),
        onOptionSelected = {}
    )
}