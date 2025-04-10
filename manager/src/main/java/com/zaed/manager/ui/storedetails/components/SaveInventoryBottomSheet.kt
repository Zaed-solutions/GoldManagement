package com.zaed.manager.ui.storedetails.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.zaed.common.R
import com.zaed.common.data.model.inventory.Inventory
import com.zaed.common.ui.components.InventoryItem
import com.zaed.common.ui.components.NumberInputTextField

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SaveInventoryBottomSheet(
    modifier: Modifier = Modifier,
    isVisible: Boolean,
    onDismiss: () -> Unit,
    mainInventories: List<Inventory>,
    initialInventory: Inventory,
    inventories: List<Inventory>,
    onSaveInventory: (Inventory) -> Unit,
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    AnimatedVisibility(
        modifier = modifier,
        visible = isVisible
    ) {
        var selectedInventory by remember {
            mutableStateOf(initialInventory)
        }
        ModalBottomSheet(
            sheetState = sheetState,
            onDismissRequest = onDismiss
        ) {
            AnimatedContent(
                targetState = selectedInventory.id.isBlank()
            ) { state ->
                when {
                    state -> {
                        Column (
                            modifier = Modifier.fillMaxWidth().padding(top = 16.dp),
                            verticalArrangement = Arrangement.spacedBy(16.dp),
                        ) {
                            Text(
                                text = stringResource(id = R.string.select_inventory),
                                modifier = Modifier.padding(horizontal = 16.dp),
                                style = MaterialTheme.typography.headlineMedium
                            )
                            LazyColumn(
                                modifier = Modifier.fillMaxWidth(),
                                contentPadding = PaddingValues(vertical = 16.dp)
                            ) {
                                items(
                                    items = inventories,
                                    key = { it.id }
                                ) {
                                    InventoryItem(
                                        inventory = it,
                                        onClick = {
                                            selectedInventory = it.copy(quantity = 0.0)
                                        }
                                    )
                                }
                            }
                        }
                    }

                    else -> {
                        var isQuantityError by remember {
                            mutableStateOf(false)
                        }
                        Column(
                            modifier = Modifier.fillMaxWidth().padding(top = 16.dp).padding(horizontal = 16.dp),
                            verticalArrangement = Arrangement.spacedBy(16.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            val availableQuantity = mainInventories.find { it.productName == selectedInventory.productName }?.quantity ?: 0.0
                            Text(
                                text = stringResource(id = R.string.enter_added_quantity),
                                style = MaterialTheme.typography.headlineMedium
                            )
                            InventoryItem(
                                inventory = selectedInventory.copy(quantity = availableQuantity)
                            )
                            NumberInputTextField(
                                modifier = Modifier.fillMaxWidth(),
                                label = stringResource(id = R.string.quantity),
                                value = selectedInventory.quantity,
                                isError = isQuantityError,
                                withBorder = true,
                                errorMessage = R.string.quantity_cannot_be_zero,
                                onValueChange = {
                                    isQuantityError = false
                                    selectedInventory = selectedInventory.copy(
                                        quantity = it
                                    )
                                }
                            )
                            Button(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .heightIn(min = 48.dp),
                                onClick = {
                                    if (selectedInventory.quantity == 0.0) {
                                        isQuantityError = true
                                    }else if (selectedInventory.quantity > availableQuantity) {
                                        isQuantityError = true
                                    } else {
                                        onSaveInventory(selectedInventory)
                                    }
                                }
                            ) {
                                Text(
                                    text = stringResource(R.string.save),
                                    style = MaterialTheme.typography.titleLarge
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}