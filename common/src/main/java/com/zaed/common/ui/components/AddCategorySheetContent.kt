package com.zaed.common.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
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
import com.zaed.common.data.model.category.Category

@Composable
fun AddCategorySheetContent(
    onDismiss: () -> Unit,
    category: Category?,
    onAddNewCategory: (Category) -> Unit
) {
    var initialCategory by remember { mutableStateOf(category ?: Category()) }
    var isNameError by remember {
        mutableStateOf(false)
    }
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = if (initialCategory.id.isBlank()) stringResource(R.string.add_category) else stringResource(
                R.string.update_category
            ),
            style = MaterialTheme.typography.headlineMedium
        )
        TextInputTextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp),
            value = initialCategory.name,
            onValueChange = {
                initialCategory = initialCategory.copy(name = it)
            },
            label = stringResource(R.string.name),
            isError = isNameError,
            errorMessage = R.string.name_is_required,
            withBorder = true
        )
        NumberInputTextField(
            modifier = Modifier.fillMaxWidth(),
            value = initialCategory.availableGrams,
            onValueChange = {
                initialCategory = initialCategory.copy(availableGrams = it)
            },
            label = stringResource(R.string.grams),
            withBorder = true
        )
        Button(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 24.dp)
                .heightIn(min = 48.dp),
            shape = MaterialTheme.shapes.medium,
            onClick = {
                if (initialCategory.name.isBlank()) {
                    isNameError = true
                } else {
                    onAddNewCategory(initialCategory)
                    onDismiss()
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