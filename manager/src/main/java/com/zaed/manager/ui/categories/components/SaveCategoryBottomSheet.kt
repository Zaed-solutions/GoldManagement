package com.zaed.manager.ui.categories.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
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
import com.zaed.common.ui.components.NumberInputTextField
import com.zaed.common.ui.components.TextInputTextField
import com.zaed.common.data.model.category.CategoryWithInventory

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SaveCategoryBottomSheet(
    modifier: Modifier = Modifier,
    visible: Boolean,
    initialCategory: CategoryWithInventory,
    onDismiss: () -> Unit,
    onSave: (CategoryWithInventory) -> Unit
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    AnimatedVisibility(
        modifier = modifier,
        visible = visible
    ) {
        var selectedCategory by remember {
            mutableStateOf(initialCategory)
        }
        var isNameError by remember {
            mutableStateOf(false)
        }
        var isQuantityError by remember {
            mutableStateOf(false)
        }
        ModalBottomSheet(
            sheetState = sheetState,
            onDismissRequest = onDismiss
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp)
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = stringResource(id = if(selectedCategory.category.id.isBlank()) R.string.add_product else R.string.update_product),
                    style = MaterialTheme.typography.headlineMedium
                )
                TextInputTextField(
                    modifier = Modifier.fillMaxWidth().padding(top = 16.dp),
                    label = stringResource(id = R.string.product_name),
                    value = selectedCategory.category.name,
                    isError = isNameError,
                    withBorder = true,
                    errorMessage = R.string.name_is_required,
                    onValueChange = {
                        isNameError = false
                        selectedCategory = selectedCategory.copy(
                            category = selectedCategory.category.copy(
                                name = it
                            )
                        )
                    }
                )
                NumberInputTextField(
                    modifier = Modifier.fillMaxWidth(),
                    label = stringResource(id = R.string.quantity),
                    value = selectedCategory.inventory.quantity,
                    isError = isQuantityError,
                    withBorder = true,
                    errorMessage = R.string.quantity_cannot_be_zero,
                    onValueChange = {
                        isQuantityError = false
                        selectedCategory = selectedCategory.copy(
                            inventory =  selectedCategory.inventory.copy(
                                quantity = it
                            )
                        )
                    }
                )
                Button(
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(min = 48.dp),
                    onClick = {
                        if(selectedCategory.category.name.isBlank()){
                            isNameError = true
                        } else if (selectedCategory.inventory.quantity == 0.0) {
                            isQuantityError = true
                        } else {
                            onSave(selectedCategory)
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